# microservicios-reservas

Sistema de reservas desarrollado con arquitectura de **microservicios** usando **Spring Boot**, **Eureka Server**, **API Gateway** y **JWT** para autenticación.  
Incluye gestión de usuarios con eliminación lógica y control de acceso basado en roles.

---

## 🧩 Servicios Incluidos

- **Eureka Server**  
  Registro y descubrimiento de servicios.

- **API Gateway**  
  Punto de entrada único, responsable del enrutamiento hacia los microservicios.

- **User Service**  
  Gestión de usuarios, login, roles y autorización vía JWT.

- **Reservation Service** *(en desarrollo)*  
  Módulo para gestionar reservas.

---

## 🚀 Tecnologías Utilizadas

- Java 21  
- Spring Boot 3.x  
- Spring Cloud  
- Spring Security + JWT  
- Spring Data JPA / Hibernate  
- MySQL 8  
- Maven  

---

## ⚙️ Variables de Entorno

Configura estas variables en tu entorno o archivo `application.properties`:

### 🔸 Base de Datos (MySQL)
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER_NAME}
spring.datasource.password=${DB_PASS}

spring.mail.host=${SERVIDOR_SMTP}
spring.mail.port=${PUERTO_SMTP}
spring.mail.username=${CORREO_APP}
spring.mail.password=${CORREO_PASS}

jwt.secret-key=${SECRET_KEY}
jwt.expiration-time=${EXPIRATION_TIME}
