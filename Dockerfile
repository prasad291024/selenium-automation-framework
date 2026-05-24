FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

# Install Chrome
RUN apt-get update && apt-get install -y \
    ca-certificates \
    wget \
    gnupg \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-linux-signing-keyring.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-linux-signing-keyring.gpg] https://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src
COPY testng_vwo_pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Run tests
CMD ["mvn", "clean", "test", "-Dsurefire.suiteXmlFiles=testng_vwo_pom.xml"]
