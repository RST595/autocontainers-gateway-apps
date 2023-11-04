FROM openjdk:22-slim-bullseye
MAINTAINER rst.com
COPY ./build/libs/autocontainers-gateway-1.0.0.jar gateway.jar
ENTRYPOINT ["java","-jar","/gateway.jar"]