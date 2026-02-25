# ─── Stage 1: Test Runner (Java 21 + Chrome) ──────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Install dependencies for Google Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ca-certificates \
    apt-transport-https \
    curl \
    unzip \
    libnss3 \
    libgconf-2-4 \
    libfontconfig1 \
    --no-install-recommends

# Install Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor > /usr/share/keyrings/google-chrome.gpg \
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
# The CMD uses system properties which can be overridden by docker-compose environment variables
CMD ["mvn", "clean", "test", "-Dheadless=true", "-Dbrowser=chrome"]

# ─── Stage 2: Allure Report Generator (Standalone) ───────────────────────────
# This stage is used if you want to build a self-contained report image. 
# Better to use Allure Docker Service in docker-compose for real-time updates.
FROM eclipse-temurin:21-jre-jammy AS report

RUN apt-get update && apt-get install -y wget unzip python3 \
    && wget -q https://github.com/allure-framework/allure2/releases/download/2.25.0/allure-2.25.0.tgz \
    && tar -xzf allure-2.25.0.tgz -C /opt \
    && ln -s /opt/allure-2.25.0/bin/allure /usr/bin/allure \
    && rm allure-2.25.0.tgz \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# In standalone mode, we assume results are copied or mounted
# COPY --from=builder /app/target/allure-results ./allure-results

EXPOSE 8080

# Script to generate and serve
CMD ["sh", "-c", "allure generate allure-results -o allure-report --clean && python3 -m http.server 8080 -d allure-report"]
