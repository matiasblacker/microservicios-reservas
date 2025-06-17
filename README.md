# microservicios-reservas
Microservicios para Sistema de Reservas con Spring Boot, Eureka Server, API Gateway y autenticación JWT. Incluye gestión de usuarios con eliminación lógica y control de acceso basado en roles.


Servicios Incluidos por ahora

Eureka-Server: Registro y descubrimiento de servicios
API-Gateway: Punto de entrada único y enrutamiento
User-Service: Gestión de usuarios, autenticación y autorización
reservation-service (por completar)

Tecnologías

Java 21
Spring Boot 3.x
Spring Cloud
Spring Security + JWT
JPA/Hibernate
MySQL8
Maven

Variables de entorno a modificar

Mysql8
#database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER_NAME}
spring.datasource.password=${DB_PASS}

#configuracion del servidor de correo
spring.mail.host=${SERVIDOR_SMTP}
spring.mail.port=${PUERTO_SMTP}
spring.mail.username=${CORREO_APP}
spring.mail.password=${CORREO_PASS}

#jwt (opcional)
jwt.secret-key=${SECRET_KEY}
jwt.expiration-time=${EXPIRATION_TIME}

![Screenshot from 2025-06-16 22-22-53](https://github.com/user-attachments/assets/473095f0-c8e2-4c9e-845d-4c886ef3b343)

Al registrarte, recibirás una contraseña en tu correo que luego podrá cambiar
