# Obraz bazowy z Mavenem i JDK
FROM maven:3.8.4-openjdk-11-slim AS build

# Ustaw katalog roboczy
WORKDIR /usr/src/app

# Skopiuj pliki projektu do kontenera
COPY . .

# Zbuduj projekt Maven
RUN mvn clean package -DskipTests

# Obraz docelowy z Selenium, Chrome i testami
FROM selenium/standalone-chrome:latest

# Ustaw katalog roboczy
WORKDIR /usr/src/app

# Skopiuj plik JAR z obrazu budowania do obrazu docelowego
COPY --from=build /usr/src/app/target/g2a-1.0-SNAPSHOT.jar .

# Instalacja dodatkowych narzędzi, jeśli potrzebne
# Na przykład, instalacja ChromeDrivera
RUN apt-get update && \
    apt-get install -y wget unzip && \
    wget -q -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$(curl -sS https://chromedriver.storage.googleapis.com/LATEST_RELEASE)/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip && \
    chmod +x /usr/local/bin/chromedriver

# Polecenie uruchamiane po uruchomieniu kontenera
CMD ["java", "-jar", "g2a-1.0-SNAPSHOT.jar"]
