# Fase 1: Compilación usando imagen oficial de Maven con Java 25
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copiamos el pom y descargamos dependencias en caché
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y construimos el archivo JAR
COPY src src
RUN mvn clean package -DskipTests

# Fase 2: Imagen final ultra-ligera de ejecución (JRE)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
