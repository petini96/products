# Etapa de build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests --no-transfer-progress

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract || true
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]