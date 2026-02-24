# ─── Stage 1: Build & Test ────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-11 AS builder

# Install Chrome dependencies
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    curl \
    unzip \
    --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# Install Google Chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
        >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Run tests with headless Chrome and generate Allure results
RUN mvn clean test -Dheadless=true -Dbrowser=chrome || true

# ─── Stage 2: Allure Report ───────────────────────────────────────────────────
FROM openjdk:11-jre-slim AS report

# Install Allure CLI
RUN apt-get update && apt-get install -y wget unzip \
    && wget -q https://github.com/allure-framework/allure2/releases/download/2.25.0/allure-2.25.0.tgz \
    && tar -xzf allure-2.25.0.tgz -C /opt \
    && ln -s /opt/allure-2.25.0/bin/allure /usr/bin/allure \
    && rm allure-2.25.0.tgz \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy allure results from builder stage
COPY --from=builder /app/target/allure-results ./allure-results

# Generate the Allure HTML report
RUN allure generate allure-results -o allure-report --clean

# Expose port for serving the report
EXPOSE 8080

# Serve the report using Python's HTTP server
RUN apt-get update && apt-get install -y python3 && rm -rf /var/lib/apt/lists/*
CMD ["python3", "-m", "http.server", "8080", "--directory", "/app/allure-report"]
