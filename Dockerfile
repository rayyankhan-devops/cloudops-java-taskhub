FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /usr/src/app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM gcr.io/distroless/java17-debian12:nonroot

WORKDIR /usr/src/app

COPY --from=builder /usr/src/app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
