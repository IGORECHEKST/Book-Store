FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /application

COPY pom.xml .

COPY checkstyle.xml checkstyle.xml

RUN mvn dependency:go-offline

COPY src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /application
COPY --from=build /application/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
