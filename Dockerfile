FROM openjdk:8-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ["./mvnw", "package", "-Dmaven.test.skip=true"]

FROM openjdk:8-jre-alpine
EXPOSE 8080
COPY --from=build /app/target/accounting-system-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]