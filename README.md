# Sistema de Botica - Spring Boot 3 + Java 17

Sistema backend completo para la gestion de una Botica/Farmacia, desarrollado con Spring Boot 3, siguiendo arquitectura MVC, principios SOLID e inyeccion de dependencias por constructor.

## Tecnologias

- Java 17
- Spring Boot 3.3.4
- Maven
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Validation
- PostgreSQL
- pgAdmin 4
- Lombok

## Estructura del proyecto

```
botica/
├── pom.xml
├── README.md
├── src/
│   └── main/
│       ├── java/
│       │   └── com/botica/
│       │       ├── controller/
│       │       ├── service/
│       │       │   ├── interfaces/
│       │       │   └── impl/
│       │       ├── repository/
│       │       ├── entity/
│       │       ├── dto/
│       │       ├── config/
│       │       ├── exception/
│       │       ├── util/
│       │       └── BoticaApplication.java
│       └── resources/
│           ├── application.properties
│           ├── schema.sql
│           └── data.sql
```

## Entidades y relaciones

- **Categoria** 1 → N **Producto**
- **Venta** 1 → N **DetalleVenta**
- **DetalleVenta** N → 1 **Producto**
- **Usuario** 1 → N **Venta**

## Configuracion de la base de datos

1. Instalar PostgreSQL y pgAdmin 4.
2. Crear una base de datos llamada `botica`:

```sql
CREATE DATABASE botica;
```

3. Editar `src/main/resources/application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/botica
spring.datasource.username=postgres
spring.datasource.password=123456
```

Hibernate creara automaticamente todas las tablas (`ddl-auto=update`) y `data.sql` cargara datos de ejemplo (categorias, usuarios, productos y una venta de muestra).

## Ejecucion

Con Maven:

```bash
mvn spring-boot:run
```

O ejecutando directamente la clase `BoticaApplication.java` desde IntelliJ IDEA o VS Code.

La aplicacion se levanta por defecto en: `http://localhost:8080`

## Endpoints disponibles

### Usuarios
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET    | /api/usuarios | Listar todos |
| GET    | /api/usuarios/{id} | Buscar por id |
| POST   | /api/usuarios | Crear usuario |
| PUT    | /api/usuarios/{id} | Actualizar usuario |
| DELETE | /api/usuarios/{id} | Eliminar usuario |

### Categorias
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET    | /api/categorias | Listar todas |
| GET    | /api/categorias/{id} | Buscar por id |
| POST   | /api/categorias | Crear categoria |
| PUT    | /api/categorias/{id} | Actualizar categoria |
| DELETE | /api/categorias/{id} | Eliminar categoria |

### Productos
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET    | /api/productos | Listar todos |
| GET    | /api/productos/{id} | Buscar por id |
| GET    | /api/productos/categoria/{categoriaId} | Listar por categoria |
| POST   | /api/productos | Crear producto |
| PUT    | /api/productos/{id} | Actualizar producto |
| DELETE | /api/productos/{id} | Eliminar producto |

### Ventas
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET    | /api/ventas | Listar todas |
| GET    | /api/ventas/{id} | Buscar por id |
| POST   | /api/ventas | Registrar venta (con detalles, descuenta stock) |
| PUT    | /api/ventas/{id} | Actualizar venta |
| DELETE | /api/ventas/{id} | Eliminar venta (restaura stock) |

### Detalle de venta
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET    | /api/detalles-venta/venta/{ventaId} | Listar detalles de una venta |
| GET    | /api/detalles-venta/{id} | Buscar detalle por id |

## Ejemplo de creacion de venta (POST /api/ventas)

```json
{
  "usuarioId": 1,
  "detalles": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 3, "cantidad": 1 }
  ]
}
```

El precio unitario y el subtotal de cada detalle, asi como el total de la venta, se calculan automaticamente en el backend a partir del precio actual del producto. El stock del producto se descuenta automaticamente.

## Manejo de errores

Las excepciones se gestionan de forma centralizada (`GlobalExceptionHandler`) devolviendo respuestas JSON consistentes:

```json
{
  "timestamp": "2026-06-29T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado con id: 99",
  "path": "/api/productos/99",
  "details": null
}
```
