
# Prueba técnica 4 - Agencia de turismo
GoTrip es una plataforma de turismo que permite a los usuarios buscar y reservar vuelos y hoteles de manera fácil y rápida. La aplicación ofrece una API REST desarrollada con Spring Boot para gestionar la información de vuelos, hoteles y reservas.

## **Tabla de Contenidos**
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [API Endpoints](#api-endpoints)
- [Carga de Datos de Prueba](#carga-de-datos-de-prueba)
- [Modelo Entidad-Relación](#modelo-entidad-relación)
- [Versión en Vivo](#versión-en-vivo)

## **Requisitos Previos**
Para ejecutar GoTrip en tu entorno local, es necesario que tengas instalados los siguientes servicios:

- Docker: Para crear y ejecutar el contenedor del proyecto Spring Boot.
- MySQL: Para gestionar la base de datos. Si la base de datos no existe, se creará automáticamente al iniciar la aplicación.  

El proyecto está desplegado, pero para ejecutarlo en tu máquina local necesitarás Docker para construir la imagen del proyecto y MySQL para establecer la conexión con la base de datos.
## **Instalación y Configuración**
### Clona el repositorio:
```bash
git clone https://github.com/jose-016al/AlmironJose_pruebatec4.git
```
### Configura el archivo docker-compose.yml:   

Modifica las variables de entorno en el archivo docker-compose.yml según los parámetros de tu sistema. En particular, ajusta la ruta de la base de datos, el nombre de usuario y la contraseña para que coincidan con los de tu SGBD.  

Ejemplo de configuración:
```yml
services:

  gotrip:
    container_name: gotrip
    build: ./gotrip
    ports:
      - "8080:8080"
    environment:
      PORT: 8080
      DB_URL: jdbc:mysql://host.docker.internal:3306/go_trip?createDatabaseIfNotExist=true&serverTimezone=UTC
      DB_USER_NAME: user
      DB_PASSWORD: user
      SPRING_SECURITY_NAME: empleado
      SPRING_SECURITY_PASSWORD: empleado123
    restart: always
```
- En principio, host.docker.internal debería funcionar para conectar el contenedor con la base de datos en tu máquina host. Solo necesitarás modificar las credenciales de MySQL.
- Si la conexión no funciona, también puedes ajustar la ruta de la base de datos en la variable DB_URL.  

### Construir y ejecutar con Docker:  
En el directorio raíz del proyecto, ejecuta los siguientes comandos para construir y ejecutar la aplicación en Docker:
```bash
docker-compose up --build
```
### Acceso a la API:  

Una vez que Docker haya iniciado los contenedores, puedes acceder a la API en `http://localhost:80`.
## **API Endpoints**
La documentación completa de la API está disponible a través de Swagger. Para acceder a ella, abre el siguiente enlace después de haber iniciado el proyecto en Docker:

http://localhost:8080/doc/swagger-ui/index.html

Si prefieres no desplegar el proyecto localmente, también puedes acceder a la versión desplegada en línea:

https://almironjose-pruebatec4.onrender.com/doc/swagger-ui/index.html

Además, en el repositorio encontrarás una colección de Postman que facilita la prueba de los endpoints. Solo necesitas importar el archivo `GoTrip.postman_collection.json` en Postman para comenzar
## **Carga de Datos de Prueba**
Si deseas realizar pruebas en tu entorno local, necesitarás importar manualmente el archivo go_trip.sql a tu sistema de gestión de bases de datos. A continuación, se describen los pasos para hacerlo:

### Crear la base de datos (si no existe)

Antes de importar los datos, asegúrate de que la base de datos go_trip exista en tu sistema. Si no es así, créala con el siguiente comando en MySQL:
```bash
CREATE DATABASE go_trip;
```
### Importar el archivo SQL

Para cargar el contenido del archivo `go_trip.sql` en la base de datos, utiliza el siguiente comando en la terminal:
```bash
mysql -u user -p go_trip < go_trip.sql
```
Asegúrate de reemplazar `user` por tu nombre de usuario de MySQL. Se te pedirá que ingreses la contraseña de dicho usuario.

Una vez completado este proceso, la base de datos se poblará con los datos de prueba proporcionados.
## **Modelo Entidad-Relación**
A continuación, se muestra el modelo entidad-relación que representa la estructura de la base de datos del proyecto:  
 
![Modelo entidad-relación](./Modelo-entidad-relación.jpg)

## **Versión en Vivo**
Puedes probar la aplicación en vivo a través del siguiente enlace:

[Enlace a la aplicación en vivo](https://almironjose-pruebatec4.onrender.com)

Esto te permitirá interactuar con los endpoints directamente y probar las funcionalidades de la plataforma GoTrip.