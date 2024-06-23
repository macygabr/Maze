FROM maven:3.8.5-openjdk-11 AS builder
WORKDIR /build
COPY . .
RUN sudo mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /
COPY target/Server-1.0.jar /
RUN mkdir /download
COPY src/main/resources/static/download/* /download/.

CMD ["java", "-jar", "Server-1.0.jar"]