# ─── Stage 1: Test Runner (Java 21 + Chrome) ──────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Install Google Chrome dependencies and Chrome itself
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ca-certificates \
    apt-transport-https \
    curl \
    unzip \
    --no-install-recommends \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor > /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" \
        > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Cache dependencies by copying pom.xml first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy project source
COPY src ./src

# Set default command to run tests in headless mode
CMD ["mvn", "clean", "test", "-Dheadless=true", "-Dbrowser=chrome"]

# ─── Stage 2: Allure Report Generator ─────────────────────────────────────────
# This stage is optional if using the Allure Docker Service in docker-compose
FROM eclipse-temurin:21-jre-jammy AS report

RUN apt-get update && apt-get install -y wget unzip python3 \
    && wget -q https://github.com/allure-framework/allure2/releases/download/2.25.0/allure-2.25.0.tgz \
    && tar -xzf allure-2.25.0.tgz -C /opt \
    && ln -s /opt/allure-2.25.0/bin/allure /usr/bin/allure \
    && rm allure-2.25.0.tgz \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy allure results from a previous RUN in the builder stage (if executed)
# Note: In a standard CI/CD, you might skip this and use volume mounts
# COPY --from=builder /app/target/allure-results ./allure-results

# Command to serve the report
EXPOSE 8080
CMD ["python3", "-m", "http.server", "8080"]

