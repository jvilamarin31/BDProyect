#En el from se debe especificar la imagen base que se utilizará para construir la imagen Docker. En este caso, se está utilizando una imagen de Eclipse Temurin con JDK 21 basada en Alpine Linux, sacado de Docker hub.
FROM eclipse-temurin:21-jdk-alpine
# En el workdir se establece el directorio de trabajo dentro del contenedor Docker. Aquí, se está configurando el directorio /app como el directorio de trabajo.
WORKDIR /app
# La instrucción copy se utiliza para copiar archivos desde el sistema de archivos del host al sistema de archivos del contenedor Docker. En este caso, se está copiando el archivo JAR generado por la aplicación Spring Boot (target/backend-0.0.1-SNAPSHOT.jar) al contenedor y renombrándolo como app.jar.
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
# La instrucción expose se utiliza para documentar el puerto en el que la aplicación dentro del contenedor escuchará las solicitudes. Aquí, se está exponiendo el puerto 8080, que es el puerto predeterminado para aplicaciones Spring Boot.
EXPOSE 8080
# La instrucción entrypoint define el comando que se ejecutará cuando se inicie un contenedor a partir de esta imagen Docker. En este caso, se está configurando para ejecutar la aplicación Java utilizando el archivo app.jar.
ENTRYPOINT ["java","-jar","app.jar"]
