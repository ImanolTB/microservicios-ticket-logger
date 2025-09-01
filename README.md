# 🧾 Microservicios Ticket Logger

Este proyecto implementa una **arquitectura de microservicios** para la gestión de tickets de incidencias en un supermercado.  
Está desarrollado en **Java + Spring Boot**, siguiendo principios de escalabilidad y separación de responsabilidades.

---

## 📂 Estructura del proyecto

- **`dwese-api-gateway/`**  
  Microservicio que actúa como **puerta de entrada** al sistema. Gestiona las peticiones de los clientes y las enruta a los demás servicios.

- **`dwese-auth-service/`**  
  Servicio encargado de la **autenticación y autorización** de usuarios (JWT).

- **`dwese-ticket-logger-api/`**  
  Microservicio principal que permite **crear, consultar y gestionar tickets** de incidencias.

- **`docker-compose.yml`**  
  Orquestador que levanta todos los microservicios y dependencias necesarias en contenedores Docker.

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MySQL**
- **Docker & Docker Compose**
- **API Gateway (Spring Cloud Gateway)**

---

## ▶️ Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/ImanolTB/microservicios-ticket-logger.git
   cd microservicios-ticket-logger
Levantar los servicios con Docker:

```bash
Copiar código
docker-compose up --build
Acceder a los endpoints a través del API Gateway:

```bash
Copiar código
http://localhost:8080
