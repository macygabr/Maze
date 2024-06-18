FROM maven:3.8.5-openjdk-11 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /
COPY target/Server-1.0.jar /
RUN mkdir /home/resources
EXPOSE 332
CMD ["java", "-jar", "Server-1.0.jar"]