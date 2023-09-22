# Utilizza un'immagine base con Maven e JDK 20
FROM maven:3.9.4-eclipse-temurin-20 as build

# Copia il pom.xml e i file sorgente nel container
COPY pom.xml /app/
COPY src /app/src

# Imposta la directory di lavoro
WORKDIR /app

# Compila e pacchettizza l'applicazione
RUN mvn clean package

# Utilizza un'immagine base con solo JDK 20 per eseguire l'applicazione
FROM openjdk:20-jdk

# Copia il jar compilato dall'immagine di build
COPY --from=build /app/target/WordleServer-1.0-SNAPSHOT.jar /app/WordleServer.jar
COPY config.properties /app/
COPY files /app/files

# Imposta la directory di lavoro
WORKDIR /app

# Comando per eseguire l'applicazione
CMD ["java", "-jar", "WordleServer.jar"]
