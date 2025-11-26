# ⚽ Picadito Backend

API REST desarrollada con Spring Boot para gestionar partidos de fútbol. Permite crear partidos, gestionar inscripciones, buscar partidos y administrar participantes.

## 📋 Tabla de Contenidos

- [Características Principales](#-características-principales)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos](#-requisitos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Arquitectura](#-arquitectura)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Modelos de Datos](#-modelos-de-datos)
- [Validaciones y Reglas de Negocio](#-validaciones-y-reglas-de-negocio)
- [Manejo de Errores](#-manejo-de-errores)
- [Prácticas de Desarrollo](#-prácticas-de-desarrollo)
- [Testing](#-testing)
- [Configuración](#-configuración)

## 🚀 Características Principales

- ✅ **Gestión de Partidos**: Crear, editar, eliminar y buscar partidos
- ✅ **Gestión de Sedes**: Crear y administrar sedes (lugares donde se juegan los partidos)
- ✅ **Sistema de Partidos Seleccionados**: Agregar partidos a una lista temporal antes de confirmar (carrito para reservas)
- ✅ **Sistema de Partidos Guardados**: Guardar partidos favoritos para inscribirse después
- ✅ **Sistema de Reservas**: Confirmar múltiples reservas a partidos a la vez
- ✅ **Búsqueda Avanzada**: Filtrar partidos por múltiples criterios
- ✅ **Gestión de Participantes**: Inscribirse y desinscribirse de partidos
- ✅ **Precios y Costos**: Gestión de precios por partido y cálculo de costo por jugador
- ✅ **Validaciones de Negocio**: Estado del partido, capacidad máxima, fechas futuras, validación de partidos completos
- ✅ **Manejo Centralizado de Excepciones**: Errores consistentes y claros
- ✅ **Bloqueo Optimista**: Previene race conditions en inscripciones
- ✅ **Sistema de Categorías**: Clasificar partidos por múltiples categorías (Many-to-Many) - Un partido puede tener varias categorías (Fútbol 11, Fútbol 7, Mixto, etc.)
- ✅ **Sistema de Alertas/Notificaciones**: Alertas automáticas para cupos bajos, partidos próximos y confirmaciones
- ✅ **Sistema de Estadísticas y Reportes**: Dashboard administrativo con métricas y reportes detallados
- ✅ **Sistema de Calificaciones**: Los usuarios pueden calificar partidos después de jugarlos
- ✅ **Sistema de Equipos Automáticos**: Generación automática de equipos balanceados por posición y nivel
- ✅ **Normalización de Jugadores**: Los partidos aleatorios se normalizan automáticamente a un número par de jugadores entre 10 y 22

## 🔧 Tecnologías Utilizadas

- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Abstracción de acceso a datos
- **Hibernate** - ORM para mapeo objeto-relacional
- **H2 Database** - Base de datos en memoria para desarrollo
- **MySQL** - Base de datos para producción (opcional)
- **Lombok** - Reducción de boilerplate code
- **Java 21** - Lenguaje de programación
- **Maven** - Gestión de dependencias y build

## 📋 Requisitos

- **Java 21** o superior
- **Maven 3.6+** (incluido wrapper Maven en el proyecto)

## 🛠️ Instalación y Ejecución

### Opción 1: Usando Maven Wrapper (Recomendado)

```bash
# Windows
cd picadito-backend
.\mvnw.cmd spring-boot:run

# Linux/Mac
cd picadito-backend
./mvnw spring-boot:run
```

### Opción 2: Usando Maven instalado

```bash
cd picadito-backend
mvn spring-boot:run
```

### Verificar que el backend está corriendo

- Espera a ver el mensaje: `Started PicaditoApplication`
- El backend estará disponible en: `http://localhost:8080`
- **H2 Console** (solo en perfil `dev`): `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Password: (vacío)

## 📁 Estructura del Proyecto

```
src/main/java/com/techlab/picadito/
├── controller/          # Controladores REST (API endpoints)
│   ├── PartidoController.java
│   ├── ParticipanteController.java
│   ├── PartidosSeleccionadosController.java
│   ├── PartidosGuardadosController.java
│   ├── ReservaController.java
│   ├── SedeController.java
│   └── AdminController.java
├── service/             # Lógica de negocio
│   ├── PartidoService.java
│   ├── ParticipanteService.java
│   ├── PartidosSeleccionadosService.java
│   ├── PartidosGuardadosService.java
│   ├── ReservaService.java
│   ├── SedeService.java
│   └── UsuarioService.java
├── repository/          # Acceso a datos (JPA)
│   ├── PartidoRepository.java
│   ├── ParticipanteRepository.java
│   ├── PartidosSeleccionadosRepository.java
│   ├── PartidosGuardadosRepository.java
│   ├── ReservaRepository.java
│   ├── SedeRepository.java
│   └── UsuarioRepository.java
├── model/               # Entidades JPA
│   ├── Partido.java
│   ├── Participante.java
│   ├── PartidosSeleccionados.java
│   ├── PartidosGuardados.java
│   ├── Reserva.java
│   ├── Sede.java
│   └── Usuario.java
├── dto/                 # Objetos de transferencia
│   ├── PartidoDTO.java
│   ├── BusquedaPartidoDTO.java
│   └── ...
├── exception/           # Excepciones personalizadas
│   ├── GlobalExceptionHandler.java
│   └── ...
├── config/              # Configuraciones
│   ├── CorsConfig.java
│   └── DataInitializer.java
└── util/                # Utilidades
    └── MapperUtil.java
```

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura en capas** (Layered Architecture) con separación clara de responsabilidades:

### Capas de la Aplicación

1. **Controller Layer** (`controller/`)
   - Maneja las peticiones HTTP
   - Valida parámetros de entrada
   - Delega la lógica de negocio a los servicios
   - Retorna respuestas HTTP apropiadas

2. **Service Layer** (`service/`)
   - Contiene la lógica de negocio
   - Valida reglas de negocio
   - Coordina transacciones
   - Maneja la comunicación entre repositorios

3. **Repository Layer** (`repository/`)
   - Abstrae el acceso a datos
   - Extiende `JpaRepository` para operaciones CRUD
   - Define queries personalizadas cuando es necesario

4. **Model Layer** (`model/`)
   - Define las entidades JPA
   - Representa la estructura de la base de datos
   - Contiene anotaciones de validación

5. **DTO Layer** (`dto/`)
   - Objetos de transferencia de datos
   - Separación entre modelo interno y API externa
   - Previene exposición de entidades internas

### Principios de Diseño Aplicados

- **Separación de Responsabilidades (SRP)**: Cada clase tiene una única responsabilidad
- **Inversión de Dependencias (DIP)**: Los controladores dependen de abstracciones (servicios)
- **Principio Abierto/Cerrado (OCP)**: Extensible mediante herencia e interfaces
- **DRY (Don't Repeat Yourself)**: Reutilización de código mediante servicios y utilidades

## 📚 Endpoints de la API

### Partidos

- `GET /api/partidos` - Listar todos los partidos
- `GET /api/partidos/disponibles` - Listar partidos disponibles
- `POST /api/partidos/buscar` - Búsqueda avanzada (soporta múltiples categorías mediante `categoriaIds`)
- `GET /api/partidos/{id}` - Obtener partido por ID
- `POST /api/partidos` - Crear nuevo partido
- `PUT /api/partidos/{id}` - Actualizar partido
- `DELETE /api/partidos/{id}` - Eliminar partido
- `GET /api/partidos/{id}/costo-por-jugador` - Obtener costo por jugador
- `GET /api/partidos/categoria/{categoriaId}` - Obtener partidos por categoría

### Categorías

- `GET /api/categorias` - Listar todas las categorías
- `GET /api/categorias/{id}` - Obtener categoría por ID
- `POST /api/categorias` - Crear nueva categoría
- `PUT /api/categorias/{id}` - Actualizar categoría
- `DELETE /api/categorias/{id}` - Eliminar categoría

### Alertas

- `GET /api/alertas/usuario/{usuarioId}` - Obtener alertas de un usuario
- `GET /api/alertas/usuario/{usuarioId}/no-leidas` - Obtener alertas no leídas
- `POST /api/alertas` - Crear nueva alerta
- `PUT /api/alertas/{id}/marcar-leida` - Marcar alerta como leída
- `PUT /api/alertas/usuario/{usuarioId}/marcar-todas-leidas` - Marcar todas como leídas
- `DELETE /api/alertas/{id}` - Eliminar alerta

### Estadísticas y Reportes (Admin)

- `GET /api/admin/estadisticas` - Obtener estadísticas generales
- `GET /api/admin/estadisticas/periodo` - Obtener estadísticas por período
- `GET /api/admin/reportes/ventas` - Generar reporte de ventas
- `GET /api/admin/reportes/partidos` - Generar reporte de partidos
- `GET /api/admin/reportes/usuarios` - Generar reporte de usuarios
- `GET /api/admin/partidos-capacidad-baja` - Obtener partidos con capacidad baja

### Calificaciones

- `POST /api/calificaciones/usuario/{usuarioId}` - Crear calificación
- `GET /api/calificaciones/partido/{partidoId}` - Obtener calificaciones de un partido
- `GET /api/calificaciones/partido/{partidoId}/promedio` - Obtener promedio de calificaciones
- `GET /api/calificaciones/creador/{creadorNombre}/promedio` - Obtener promedio por creador
- `GET /api/calificaciones/sede/{sedeId}/promedio` - Obtener promedio por sede
- `GET /api/calificaciones/{id}` - Obtener calificación por ID
- `DELETE /api/calificaciones/{id}` - Eliminar calificación

### Equipos

- `POST /api/equipos/partido/{partidoId}/generar` - Generar equipos automáticos
- `GET /api/equipos/partido/{partidoId}` - Obtener equipos de un partido
- `GET /api/equipos/{id}` - Obtener equipo por ID
- `DELETE /api/equipos/partido/{partidoId}` - Eliminar equipos de un partido

### Partidos Seleccionados

- `GET /api/partidos-seleccionados/usuario/{usuarioId}` - Obtener partidos seleccionados
- `POST /api/partidos-seleccionados/usuario/{usuarioId}/agregar` - Agregar partido
- `PUT /api/partidos-seleccionados/usuario/{usuarioId}/item/{lineaId}` - Actualizar cantidad
- `DELETE /api/partidos-seleccionados/usuario/{usuarioId}/item/{lineaId}` - Eliminar item
- `DELETE /api/partidos-seleccionados/usuario/{usuarioId}` - Vaciar selección

### Partidos Guardados

- `GET /api/partidos-guardados/usuario/{usuarioId}` - Obtener partidos guardados
- `POST /api/partidos-guardados/usuario/{usuarioId}/agregar` - Agregar partido a favoritos
- `DELETE /api/partidos-guardados/usuario/{usuarioId}/partido/{lineaPartidoGuardadoId}` - Eliminar partido
- `DELETE /api/partidos-guardados/usuario/{usuarioId}` - Vaciar partidos guardados

### Reservas

- `GET /api/reservas` - Listar todas las reservas
- `GET /api/reservas/{id}` - Obtener reserva por ID
- `GET /api/reservas/usuario/{usuarioId}` - Obtener reservas de usuario
- `GET /api/reservas/usuario/{usuarioId}/total-gastado` - Total gastado
- `POST /api/reservas/desde-partidos-seleccionados/{usuarioId}` - Crear reserva
- `PUT /api/reservas/{id}/estado` - Actualizar estado
- `PUT /api/reservas/{id}/cancelar` - Cancelar reserva

### Participantes

- `POST /api/partidos/{partidoId}/participantes` - Inscribirse a partido
- `GET /api/partidos/{partidoId}/participantes` - Ver participantes
- `DELETE /api/partidos/{partidoId}/participantes/{participanteId}` - Desinscribirse

### Sedes

- `GET /api/sedes` - Listar todas las sedes
- `GET /api/sedes/{id}` - Obtener sede por ID
- `POST /api/sedes` - Crear nueva sede
- `PUT /api/sedes/{id}` - Actualizar sede
- `DELETE /api/sedes/{id}` - Eliminar sede
- `POST /api/sedes/migrar` - Migrar ubicaciones a sedes

## 📖 Modelos de Datos

### Partido
- `id`: Identificador único (auto-generado)
- `titulo`: Título del partido (máx. 200 caracteres)
- `descripcion`: Descripción opcional (máx. 1000 caracteres)
- `fechaHora`: Fecha y hora del partido (debe ser futura - formato ISO 8601)
- `ubicacion`: Ubicación del partido (máx. 300 caracteres) - Deprecated: Usar `sedeId`
- `sedeId`: ID de la sede donde se juega el partido (opcional)
- `sede`: Objeto Sede completo (incluido en respuesta)
- `maxJugadores`: Número máximo de jugadores (1-50, default: 22)
- `estado`: Estado del partido (DISPONIBLE, COMPLETO, FINALIZADO, CANCELADO)
- `creadorNombre`: Nombre del creador (máx. 100 caracteres)
- `fechaCreacion`: Fecha de creación (auto-generada)
- `cantidadParticipantes`: Cantidad actual de participantes
- `precio`: Precio total del partido (opcional)
- `imagenUrl`: URL de imagen del partido (opcional, máx. 500 caracteres)
- `categoriaIds`: Lista de IDs de categorías del partido (opcional, Many-to-Many)
- `categorias`: Lista de objetos Categoria completos (incluido en respuesta)
- `promedioCalificacion`: Promedio de calificaciones del partido (opcional)
- `equipos`: Lista de equipos generados para el partido (opcional)

### Sede
- `id`: Identificador único (auto-generado)
- `nombre`: Nombre de la sede (máx. 200 caracteres, opcional)
- `direccion`: Dirección completa (máx. 300 caracteres, opcional)
- `descripcion`: Descripción de la sede (máx. 1000 caracteres, opcional)
- `telefono`: Teléfono de contacto (máx. 50 caracteres, opcional)
- `coordenadas`: Coordenadas GPS (máx. 100 caracteres, opcional)
- `fechaCreacion`: Fecha de creación (auto-generada)
- `fechaActualizacion`: Fecha de última actualización (auto-generada)

### Participante
- `id`: Identificador único (auto-generado)
- `nombre`: Nombre del participante (máx. 100 caracteres, requerido)
- `apodo`: Apodo opcional (máx. 100 caracteres, puede ser null)
- `posicion`: Posición preferida (PORTERO, DEFENSA, MEDIOCAMPISTA, DELANTERO, opcional)
- `nivel`: Nivel de juego (PRINCIPIANTE, INTERMEDIO, AVANZADO, EXPERTO, opcional)
- `fechaInscripcion`: Fecha de inscripción (auto-generada)
- `partido`: Relación con el partido

### Categoria

- `id`: Identificador único (auto-generado)
- `nombre`: Nombre de la categoría (máx. 100 caracteres, requerido, único)
- `descripcion`: Descripción opcional (máx. 500 caracteres)
- `icono`: Icono de la categoría (máx. 50 caracteres, opcional)
- `color`: Color de la categoría (máx. 20 caracteres, opcional)
- `fechaCreacion`: Fecha de creación (auto-generada)
- `fechaActualizacion`: Fecha de última actualización (auto-generada)

### Alerta

- `id`: Identificador único (auto-generado)
- `tipo`: Tipo de alerta (CUPOS_BAJOS, PARTIDO_PROXIMO, PARTIDO_CANCELADO, RESERVA_CONFIRMADA, PARTIDO_COMPLETO)
- `mensaje`: Mensaje de la alerta (requerido)
- `leida`: Indica si la alerta ha sido leída (default: false)
- `usuario`: Usuario al que pertenece la alerta (opcional)
- `partido`: Partido relacionado (opcional)
- `fechaCreacion`: Fecha de creación (auto-generada)

### Calificacion

- `id`: Identificador único (auto-generado)
- `puntuacion`: Puntuación de 1 a 5 (requerido)
- `comentario`: Comentario opcional (máx. 1000 caracteres)
- `usuario`: Usuario que califica (requerido)
- `partido`: Partido calificado (requerido)
- `fechaCreacion`: Fecha de creación (auto-generada)
- **Restricción**: Un usuario solo puede calificar un partido una vez
- **Validación**: Solo se pueden calificar partidos finalizados

### Equipo

- `id`: Identificador único (auto-generado)
- `nombre`: Nombre del equipo (máx. 100 caracteres, requerido)
- `partido`: Partido al que pertenece (requerido)
- `participantes`: Lista de participantes del equipo
- `cantidadParticipantes`: Cantidad de participantes (calculado)

## ⚠️ Validaciones y Reglas de Negocio

### Partidos
- El título, ubicación y nombre del creador son obligatorios
- **La fecha y hora (`fechaHora`) DEBE ser una fecha futura**
- El número máximo de jugadores debe estar entre 1 y 50
- **Partidos Aleatorios**: El número de jugadores se normaliza automáticamente a un valor par entre 10 y 22 (inclusive)
- No se puede actualizar un partido finalizado o cancelado
- No se puede reducir el máximo de jugadores por debajo de la cantidad actual de participantes

### Participantes
- El nombre es obligatorio
- No se puede inscribir a un partido que no está disponible
- No se puede inscribir a un partido completo
- No puede haber dos participantes con el mismo nombre en el mismo partido
- El apodo, la posición preferida y el nivel son completamente opcionales

### Partidos Seleccionados
- Al agregar partidos a la selección, se valida que el partido esté disponible y tenga cupos
- Permite especificar cantidad de jugadores por partido
- Permite agregar el mismo partido varias veces (suma cantidades)
- Se puede actualizar la cantidad de cada partido

### Partidos Guardados
- Al agregar partidos guardados, se valida que el partido esté disponible y tenga cupos
- No permite duplicados (cada partido solo puede estar una vez en la lista)
- Cantidad fija (siempre 1 por partido)
- Funciona como lista de favoritos para inscribirse después

### Reservas
- Al confirmar reservas, se valida que todos los partidos sigan disponibles
- Se verifica que haya cupos disponibles en todos los partidos
- Se crean los participantes automáticamente al confirmar la reserva
- Los partidos se marcan como COMPLETO si se llenan
- Se calcula el total de la reserva basado en los precios de los partidos

### Reservas
- Solo se permiten transiciones de estado válidas según el ciclo de vida
- No se puede retroceder estados (ej: EN_PROCESO → CONFIRMADO)
- CANCELADO y FINALIZADO son estados terminales
- Los estados se actualizan automáticamente basándose en las fechas de los partidos
- Se calcula el total gastado por usuario sumando todas las reservas confirmadas

### Sedes
- Las sedes pueden tener nombre, dirección, descripción, teléfono y coordenadas
- Los partidos pueden estar asociados a una sede mediante `sedeId`
- La migración automática crea sedes únicas basadas en las ubicaciones existentes de los partidos
- No se puede eliminar una sede si hay partidos asociados (validación de integridad referencial)

### Categorías
- Las categorías permiten clasificar partidos (Fútbol 11, Fútbol 7, Mixto, etc.)
- Cada categoría puede tener nombre, descripción, icono y color
- **Relación Many-to-Many**: Un partido puede tener múltiples categorías mediante `categoriaIds` (lista)
- Se pueden filtrar partidos por una o más categorías en la búsqueda avanzada
- La relación se almacena en la tabla intermedia `partido_categorias`

### Alertas
- Las alertas se generan automáticamente cuando:
  - Un partido tiene pocos cupos disponibles (≤ 5)
  - Un partido está próximo a jugarse (24-48 horas antes)
  - Una reserva es confirmada
- Las alertas se pueden marcar como leídas individualmente o todas a la vez
- Un job programado verifica partidos próximos cada hora
- Las alertas antiguas (más de 30 días) se eliminan automáticamente

### Calificaciones
- Los usuarios pueden calificar partidos después de que finalicen
- La calificación es de 1 a 5 estrellas
- Se puede incluir un comentario opcional
- Un usuario solo puede calificar un partido una vez
- Se calculan promedios por partido, creador y sede

### Equipos
- Los equipos se generan automáticamente dividiendo los participantes en 2 equipos balanceados
- El algoritmo considera:
  - Posiciones preferidas (portero, defensa, mediocampo, delantera)
  - Niveles de juego (principiante, intermedio, avanzado, experto)
- Los equipos se balancean para que tengan similar cantidad de participantes y distribución de niveles
- Se pueden regenerar los equipos en cualquier momento

## 🛡️ Manejo de Errores

La API utiliza un `GlobalExceptionHandler` que maneja todos los errores de forma centralizada:

- **404 Not Found**: Recurso no encontrado
- **400 Bad Request**: Errores de validación o negocio
- **409 Conflict**: Conflictos de concurrencia (bloqueo optimista)
- **500 Internal Server Error**: Errores inesperados

### Excepciones Personalizadas

- `ResourceNotFoundException`: Recurso no encontrado
- `BusinessException`: Error de lógica de negocio
- `ValidationException`: Error de validación
- `CuposInsuficientesException`: No hay cupos disponibles
- `PartidoNoDisponibleException`: Partido no disponible para inscripciones

Ejemplo de respuesta de error:
```json
{
  "timestamp": "2024-11-04T20:30:00",
  "status": 400,
  "error": "Business Error",
  "message": "El partido ya está completo. Máximo de jugadores: 22",
  "path": "/api/partidos/1/participantes"
}
```

### Ejemplos de Uso

#### Crear un partido con múltiples categorías

```json
POST /api/partidos
Content-Type: application/json

{
  "titulo": "Partido Mixto Fútbol 7",
  "descripcion": "Partido mixto de fútbol 7",
  "fechaHora": "2024-12-15T18:00:00",
  "maxJugadores": 14,
  "creadorNombre": "Juan Pérez",
  "categoriaIds": [3, 4],  // Fútbol 7 y Mixto
  "precio": 5000.0
}
```

#### Buscar partidos por múltiples categorías

```json
POST /api/partidos/buscar
Content-Type: application/json

{
  "categoriaIds": [1, 2],  // Fútbol 11 o Fútbol 7
  "soloDisponibles": true,
  "fechaDesde": "2024-12-01T00:00:00"
}
```

## 💻 Prácticas de Desarrollo

### Convenciones de Código

- **Nombres en español**: Todas las clases, métodos y variables usan nombres descriptivos en español
- **Comentarios en español**: Todos los comentarios están en español
- **CamelCase**: Para nombres de clases y métodos
- **camelCase**: Para variables y parámetros
- **UPPER_SNAKE_CASE**: Para constantes

### Patrones Utilizados

1. **DTO Pattern**: Separación entre entidades de dominio y objetos de transferencia
2. **Repository Pattern**: Abstracción del acceso a datos
3. **Service Layer Pattern**: Encapsulación de lógica de negocio
4. **Exception Handler Pattern**: Manejo centralizado de excepciones
5. **Builder Pattern**: Construcción de objetos complejos (mediante Lombok)

### Mejores Prácticas

- ✅ Validación en múltiples capas (DTO, Service, Model)
- ✅ Uso de transacciones para operaciones críticas
- ✅ Bloqueo optimista para prevenir race conditions
- ✅ Separación de responsabilidades
- ✅ Código limpio y mantenible
- ✅ Manejo de errores consistente

## 🧪 Testing

El proyecto incluye tests unitarios y de integración:

### Ejecutar Tests

```bash
# Todos los tests
./mvnw test

# Test específico
./mvnw test -Dtest=PartidoControllerTest

# Con coverage
./mvnw test jacoco:report
```

### Estructura de Tests

```
src/test/java/com/techlab/picadito/
├── controller/          # Tests de controladores
├── service/             # Tests de servicios
└── integration/        # Tests de integración
```

### Tipos de Tests

- **Controller Tests**: Usan `@WebMvcTest` para probar endpoints REST
- **Service Tests**: Tests unitarios con mocks usando Mockito
- **Integration Tests**: Tests end-to-end con `@SpringBootTest`

## ⚙️ Configuración

### CORS
Configurado para permitir orígenes específicos:
- `http://localhost:3000`
- `http://localhost:8080`
- `http://localhost:5173`

### Base de Datos

El proyecto utiliza **Spring Profiles** para configurar diferentes bases de datos según el entorno:

#### Perfil de Desarrollo (`dev`) - H2 Database

**Configuración**: `src/main/resources/application-dev.properties`

**Características**:
- **Motor**: H2 Database (en memoria)
- **Consola H2**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Password: (vacío)
- **⚠️ Nota**: Los datos se pierden al reiniciar la aplicación
- **SQL visible**: Las consultas SQL se muestran en la consola (útil para debugging)

**Ejecutar con H2**:
```bash
# Opción 1: Modificar application.properties
# Cambiar: spring.profiles.active=dev

# Opción 2: Variable de entorno
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run

# Linux/Mac
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run

# Opción 3: Argumento de línea de comandos
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Perfil de Producción (`prod`) - MySQL Database

**Configuración**: `src/main/resources/application-prod.properties`

**Setup inicial**:
1. Copia `application-prod.properties.example` a `application-prod.properties`
   ```bash
   # Windows
   copy src\main\resources\application-prod.properties.example src\main\resources\application-prod.properties
   
   # Linux/Mac
   cp src/main/resources/application-prod.properties.example src/main/resources/application-prod.properties
   ```

2. Edita `application-prod.properties` y actualiza las credenciales:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=TU_PASSWORD_AQUI
   ```

3. Crea la base de datos en MySQL:
   ```sql
   CREATE DATABASE picadito_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. Activa el perfil `prod` (ver sección "Cambiar entre Perfiles" abajo)

**Ejecutar con MySQL**:
```bash
# Opción 1: Modificar application.properties
# Cambiar: spring.profiles.active=prod

# Opción 2: Variable de entorno
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="prod"
.\mvnw.cmd spring-boot:run

# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
./mvnw spring-boot:run

# Opción 3: Argumento de línea de comandos
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Cambiar entre Perfiles

**Opción 1: Modificar `application.properties`** (Recomendado para desarrollo)
```properties
# Para H2 (desarrollo)
spring.profiles.active=dev

# Para MySQL (producción)
spring.profiles.active=prod
```

**Opción 2: Variable de entorno** (Útil para diferentes entornos)
```bash
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="dev"   # Para H2
$env:SPRING_PROFILES_ACTIVE="prod"  # Para MySQL
.\mvnw.cmd spring-boot:run

# Linux/Mac
export SPRING_PROFILES_ACTIVE=dev   # Para H2
export SPRING_PROFILES_ACTIVE=prod  # Para MySQL
./mvnw spring-boot:run
```

**Opción 3: Argumento de línea de comandos** (Útil para pruebas rápidas)
```bash
# Windows
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev   # H2
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod  # MySQL

# Linux/Mac
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev   # H2
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod  # MySQL
```

**Opción 4: En tu IDE**
- **IntelliJ IDEA**: 
  - Run → Edit Configurations → Active profiles: `dev` o `prod`
  - O en VM options: `-Dspring.profiles.active=prod`
- **Eclipse**: 
  - Run → Run Configurations → Arguments → `--spring.profiles.active=prod`
- **VS Code**: 
  - `.vscode/launch.json` → `"vmArgs": "-Dspring.profiles.active=prod"`

#### Seguridad: Archivos de Configuración

**⚠️ IMPORTANTE**: El archivo `application-prod.properties` contiene credenciales sensibles y está en `.gitignore`. 

Si el archivo ya fue commitado al repositorio, elimínalo del historial de git (pero mantén el archivo local):

```bash
# Eliminar del índice de git (mantiene el archivo local)
git rm --cached src/main/resources/application-prod.properties

# Commit el cambio
git commit -m "Remove application-prod.properties from repository"

# Push al repositorio remoto
git push
```

El archivo `application-prod.properties.example` es un template seguro que puede ser commitado.

## 📝 Datos de Prueba

El backend crea automáticamente usuarios de ejemplo al iniciar (ver `DataInitializer.java`):
- **Usuario Demo**: ID 1

## 🐛 Solución de Problemas

### Puerto 8080 en uso
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Errores de compilación
```bash
cd picadito-backend
mvn clean install
mvn spring-boot:run
```

### Error de conexión con base de datos

**H2 (Desarrollo)**:
- Verifica que el perfil `dev` esté activo
- La base de datos H2 se crea automáticamente en memoria

**MySQL (Producción)**:
- Verifica que MySQL esté corriendo:
  ```bash
  # Windows
  netstat -ano | findstr :3306
  
  # Linux/Mac
  lsof -ti:3306
  ```
- Confirma que el perfil `prod` esté activo en `application.properties` o mediante variable de entorno
- Verifica las credenciales en `application-prod.properties` (debe existir, no el `.example`)
- Asegúrate de que la base de datos `picadito_db` existe:
  ```sql
  CREATE DATABASE IF NOT EXISTS picadito_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
- Verifica que el usuario de MySQL tenga permisos:
  ```sql
  GRANT ALL PRIVILEGES ON picadito_db.* TO 'root'@'localhost';
  FLUSH PRIVILEGES;
  ```

### Error de CORS
- Verifica que el origen del frontend esté en `CorsConfig.java`
- Asegúrate de que el frontend esté usando el puerto correcto

---

**¡Disfruta organizando tus partidos de fútbol! ⚽**

