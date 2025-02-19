
# Prueba técnica 4 - Agencia de turismo
GoTrip es una plataforma de turismo que permite a los usuarios buscar y reservar vuelos y hoteles de manera fácil y rápida. La aplicación ofrece una API REST desarrollada con Spring Boot para gestionar la información de vuelos, hoteles y reservas.

## **Tabla de Contenidos**
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [API Endpoints](#api-endpoints)
- [Carga de Datos de Prueba](#carga-de-datos-de-prueba)
- [Modelo Entidad-Relación](#modelo-entidad-relación)

### **Requisitos Previos**
Para ejecutar GoTrip en tu entorno local, necesitas tener instalado:
- **Docker**
Con Docker instalado, puedes construir y ejecutar el proyecto sin necesidad de configurar nada más.
### **Instalación y Configuración**
1. **Clona el repositorio**:
```bash
git clone https://github.com/jose-016al/AlmironJose_pruebatec4.git
```
2. **Construir y ejecutar con Docker**:  
En el directorio raíz del proyecto, ejecuta los siguientes comandos para construir y ejecutar la aplicación en Docker:
```bash
docker-compose up --build
```
3. **Acceso a la API**:  
Una vez que Docker haya iniciado los contenedores, puedes acceder a la API en `http://localhost:80`.
### **API Endpoints**
La documentación completa de la API está disponible en Swagger. Para acceder, abre el siguiente enlace después de haber iniciado el proyecto en Docker:

http://localhost/doc/swagger-ui/index.html

Además, en el repositorio encontrarás una colección de Postman para probar los endpoints fácilmente. Solo tienes que importar el archivo `GoTrip.postman_collection.json` a Postman.
### **Carga de Datos de Prueba**
Si deseas realizar pruebas en local, deberás importar manualmente el archivo go_trip.sql a tu sistema gestor de base de datos. A continuación, se detallan los pasos:

1. Crear la base de datos (si no existe)
Antes de importar los datos, asegúrate de que la base de datos go_trip existe en tu sistema. Si no, créala con el siguiente comando en MySQL:
```bash
CREATE DATABASE go_trip;
```
2. Importar el archivo SQL
Para volcar el contenido del archivo go_trip.sql en la base de datos, usa el siguiente comando en la terminal:
```bash
mysql -u user -p go_trip < go_trip.sql
```
Reemplaza user por tu usuario de MySQL. Se te pedirá la contraseña.

Con esto, la base de datos se poblará con los datos de prueba proporcionados.
### **Modelo Entidad-Relación**
A continuación, se muestra el modelo entidad-relación que representa la estructura de la base de datos del proyecto:  
 
![Modelo entidad-relación](./Modelo-entidad-relación.jpg)