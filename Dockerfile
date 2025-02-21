# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR generado (asegúrate de que el nombre coincida con el que genera Gradle)
COPY build/libs/branch-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto (ajusta según la configuración de tu aplicación)
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
