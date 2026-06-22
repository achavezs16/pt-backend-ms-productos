FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="Caltias - PymeTrack"
WORKDIR /app
# Copiamos el JAR que generaremos con Maven
COPY target/*.jar app.jar
# Exponemos el puerto definido en tu docker-compose para productos
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]