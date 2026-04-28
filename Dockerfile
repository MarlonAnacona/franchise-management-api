FROM gradle:8.14.4-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

RUN gradle build -x test --no-daemon || true

COPY . .

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]