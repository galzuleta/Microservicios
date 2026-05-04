# Microservicios - Cliente Service + Eureka Server

## Requisitos
- Java 17
- Maven 3.8+

---

## Orden de arranque (IMPORTANTE)

### 1. Levantar Eureka Server primero
```bash
cd eureka-server
mvn spring-boot:run
```
- Panel Eureka: http://localhost:8761

---

### 2. Levantar cliente-service
```bash
cd cliente-service
mvn spring-boot:run
```
- API base: http://localhost:8081/api/clientes
- Consola H2: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:clientesdb`
  - User: `sa` / Password: (vacío)

---

## Endpoints disponibles

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/clientes` | Listar todos (paginado) |
| GET | `/api/clientes?page=0&size=5&sort=nombre,asc` | Paginación y orden |
| GET | `/api/clientes/{id}` | Buscar por ID |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar completo |
| PATCH | `/api/clientes/{id}` | Actualización parcial |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

---

## Ejemplos con Postman / curl

### POST - Crear cliente
```json
POST http://localhost:8081/api/clientes
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan@email.com",
  "telefono": "0991234567",
  "direccion": "Av. Amazonas, Quito",
  "activo": true
}
```

### PATCH - Actualizar solo teléfono
```json
PATCH http://localhost:8081/api/clientes/1
Content-Type: application/json

{
  "telefono": "0999999999"
}
```

---

## Correr los tests

```bash
cd cliente-service
mvn test
```

Tests incluidos:
- `ClienteServiceTest` — Tests unitarios con Mockito
- `ClienteControllerTest` — Tests de integración con MockMvc

---

## Estructura del proyecto

```
cliente-service/
└── src/main/java/com/octavo/cliente/
    ├── ClienteApplication.java          ← Entrada principal
    └── clientes/
        ├── Cliente.java                 ← Entidad JPA
        ├── ClienteDTO.java              ← DTO con validaciones
        ├── ClienteRepository.java       ← Repositorio JPA
        ├── ClienteService.java          ← Interfaz del servicio
        ├── ClienteServiceImpl.java      ← Implementación
        └── ClienteController.java       ← Controlador REST
```
