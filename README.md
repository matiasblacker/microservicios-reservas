# microservicios-reservas

Sistema de reservas desarrollado con arquitectura de **microservicios** usando **Spring Boot**, **Eureka Server**, **API Gateway** y **JWT** para autenticación.  
Incluye gestión de usuarios con eliminación lógica y control de acceso basado en roles.

---

## 🧩 Servicios Incluidos y orden de arranque

- **Eureka Server**  | port 8761 |
  Registro y descubrimiento de servicios.

- **API Gateway** | port 8080 |
  Punto de entrada único, responsable del enrutamiento hacia los microservicios.

- **User Service** | port 8081 |
  Gestión de usuarios, login, roles y autorización vía JWT.

- **Reservation Service** *(en desarrollo)* | port 8082 |
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
## 🛠️ Registro Manual de Usuario ADMIN

Podremos registrar por ahora un usuario de tipo **ADMIN** directamente en la base de datos.  
La **contraseña se encriptará automáticamente** después de 1 hora gracias a un servicio `@Scheduled (Cron)` del `user-service`,  
o también puedes forzar la encriptación **reiniciando** el microservicio.

Puedes ejecutar esta consulta SQL desde un gestor de base de datos como **DBeaver**, **MySQL Workbench**, o desde consola.

### 📄 Consulta SQL para crear un usuario ADMIN:

```sql
INSERT INTO user (
  name,
  last_name,
  email,
  password,
  rol,
  enabled
) VALUES (
  'AdminNombre',
  'AdminApellido',
  'admin@correo.com',
  'admin123', -- esta contraseña se encriptará automáticamente después
  'ADMIN',
  true
);

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
---
