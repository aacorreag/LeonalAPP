# leonalApp - Sistema de Información de Laboratorio (LIS)

**Sistema de Información de Laboratorio (LIS) Open Source**, diseñado para laboratorios clínicos pequeños y medianos, enfocado en eficiencia, trazabilidad y seguridad.

**Versión:** 1.0.0-SNAPSHOT  
**Estado:** En desarrollo activo (Fase 1: Configuración, Modelo de Datos y Funcionalidades Core)  
**Licencia:** Por especificar

---

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Características Principales](#características-principales)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Setup](#instalación-y-setup)
- [Configuración](#configuración)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Módulos Detallados](#módulos-detallados)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Troubleshooting](#troubleshooting)
- [Guía de Contribución](#guía-de-contribución)

---

## 📖 Descripción General

**leonalApp** es un **Sistema de Información de Laboratorio (LIS)** completo y modular, desarrollado bajo el patrón **Clean Architecture** con Java y Spring Boot. Permite a laboratorios clínicos gestionar:

- **Órdenes de exámenes**: Creación, validación y seguimiento de órdenes clínicas
- **Resultados**: Ingreso de resultados, validación por profesionales, trazabilidad completa
- **Reportes**: Generación automática de reportes clínicos en PDF con formatos profesionales
- **Pacientes**: Gestión de datos demográficos, antecedentes y contacto
- **Catálogo de exámenes**: Configuración de pruebas, valores de referencia, costos
- **Facturación**: Generación de facturas, control de pagos, módulo de caja
- **Usuarios**: Gestión de permisos, roles y seguridad

Está diseñado para ser:
- **Modular**: Cada responsabilidad en su propio módulo Maven
- **Escalable**: Arquitectura limpia que facilita cambios y extensiones
- **Seguro**: Encriptación BCrypt, control de acceso por roles
- **Mantenible**: Bajo acoplamiento, fácil de testear y depurar

---

## 🎯 Características Principales

### ✅ Implementadas (Fase 1)

- [x] Autenticación y autorización de usuarios
- [x] Gestión completa de órdenes clínicas
- [x] Registro y validación de resultados
- [x] Catálogo configurables de exámenes
- [x] Generación de reportes en PDF (JasperReports)
- [x] Gestión de pacientes
- [x] Módulo de facturación y pagos
- [x] Módulo de caja/tesorería
- [x] Interface de usuario JavaFX profesional
- [x] Migraciones de base de datos automáticas (Flyway)
- [x] Persistencia en PostgreSQL con JPA/Hibernate

### 🚀 Planeadas (Fase 2+)

- [ ] Interfaz web (Spring Boot Web)
- [ ] Integración con laboratorios externos
- [ ] API REST para integraciones
- [ ] Analíticas avanzadas y reportes personalizados
- [ ] Exportación a formatos adicionales (Excel, XML)
- [ ] Sincronización multi-laboratorio
- [ ] Notificaciones vía email/SMS
- [ ] Auditoría completa (quién, cuándo, qué cambió)

---

## 📋 Requisitos Previos

Antes de instalar **leonalApp**, asegúrate de tener:

| Requisito | Versión | Descripción |
|-----------|---------|-------------|
| **Java** | 17+ | JDK (OpenJDK, Eclipse Temurin, etc.) |
| **PostgreSQL** | 14+ | Sistema de base de datos relacional |
| **Maven** | 3.8+ | Herramienta de build |
| **Docker** (opcional) | 20.10+ | Para ejecutar PostgreSQL en contenedor |
| **Git** (recomendado) | 2.0+ | Para clonar/contribuir al proyecto |

**Verificar versiones instaladas:**

```bash
java -version
psql --version
mvn --version
```

---

## 🚀 Instalación y Setup

### Opción 1: Con Docker Compose (Recomendado)

La opción más rápida. El proyecto incluye un archivo `docker-compose.yml` que levanta PostgreSQL automáticamente.

#### Pasos:

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd leonalAPP
   ```

2. **Iniciar PostgreSQL con Docker Compose:**
   ```bash
   docker-compose up -d
   ```
   Esto levantará:
   - PostgreSQL 15 en el puerto 5432
   - Usuario: `leonal_user`
   - Contraseña: `secret_password`
   - Base de datos: `leonal_db`

3. **Compilar el proyecto:**
   ```bash
   mvn clean install
   ```
   Maven descargará todas las dependencias y compilará los 5 módulos.

4. **Ejecutar la aplicación:**
   ```bash
   cd leonal-launcher
   mvn spring-boot:run
   ```
   La interfaz JavaFX se abrirá automáticamente.

5. **Detener PostgreSQL (cuando termines):**
   ```bash
   docker-compose down
   ```

---

### Opción 2: Setup Manual con PostgreSQL Instalado Localmente

Si ya tienes PostgreSQL instalado:

1. **Clonar repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd leonalAPP
   ```

2. **Crear base de datos:**
   ```sql
   -- Conectarse como superuser (por defecto 'postgres')
   psql -U postgres
   
   -- Crear usuario
   CREATE USER leonal_user WITH PASSWORD 'secret_password';
   
   -- Crear base de datos
   CREATE DATABASE leonal_db OWNER leonal_user;
   
   -- Salir
   \q
   ```

3. **Compilar:**
   ```bash
   mvn clean install
   ```

4. **Ejecutar:**
   ```bash
   cd leonal-launcher
   mvn spring-boot:run
   ```

---

## ⚙️ Configuración

### Variables de Entorno

Opcionalmente, puedes sobrescribir la configuración usando variables de entorno:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/leonal_db
export DB_USER=leonal_user
export DB_PASSWORD=secret_password

cd leonal-launcher
mvn spring-boot:run
```

### Archivo de Configuración (application.properties)

El archivo principal de configuración está en:
```
leonal-launcher/src/main/resources/application.properties
```

**Configuraciones principales:**

```properties
# Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/leonal_db
spring.datasource.username=leonal_user
spring.datasource.password=secret_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Flyway (Migraciones)
spring.flyway.locations=classpath:db/migration
spring.flyway.enabled=true

# Caracteres UTF-8
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
```

### ⚠️ Seguridad en Producción

**IMPORTANTE**: Las credenciales por defecto (`secret_password`) son **SOLO PARA DESARROLLO**.

En producción:
1. Cambia la contraseña de PostgreSQL
2. Usa variables de entorno seguras o vaults (HashiCorp Vault, AWS Secrets Manager, etc.)
3. Habilita SSL/TLS en la conexión a BD
4. Configura Spring Security con credenciales fuertes
5. Activa auditoría (quién accede, cuándo, qué cambios)

---

## 🏗️ Arquitectura

### Patrón Clean Architecture

**leonalApp** implementa **Clean Architecture Estricta**, garantizando:

- **Independencia de Frameworks**: El core de negocio no depende de Spring, JPA, JavaFX, etc.
- **Testabilidad**: Cada componente puede ser testeado aisladamente con mocks
- **Escalabilidad**: Cambiar implementaciones (BD, UI, etc.) sin afectar la lógica

### Estructura de Capas

```
┌─────────────────────────────────────────────┐
│         Interfaz de Usuario (JavaFX)        │  <- leonal-ui
├─────────────────────────────────────────────┤
│     Casos de Uso (DTOs, Orquestación)      │  <- leonal-application
├─────────────────────────────────────────────┤
│   Reglas de Negocio (Entidades, Puertos)   │  <- leonal-domain
├─────────────────────────────────────────────┤
│   Implementaciones (JPA, PDF, Reportes)    │  <- leonal-infrastructure
└─────────────────────────────────────────────┘
          ↓ Inyección de Dependencias ↓
     leonal-launcher (Spring Boot)
```

### Flujo de Dependencias

- **leonal-domain**: No depende de nada (excepto Lombok para testing)
- **leonal-application**: → Depende de `domain`
- **leonal-infrastructure**: → Depende de `domain` + `application`
- **leonal-ui**: → Depende de `application`
- **leonal-launcher**: → Depende de todos (punto de wiring)

---

## 📦 Estructura del Proyecto

```
leonalAPP/
├── README.md                          # Este archivo
├── pom.xml                            # POM padre (multimodular)
├── docker-compose.yml                 # Configuración de PostgreSQL
├── apache-maven-3.9.6/                # Maven incluido (opcional)
│
├── leonal-domain/                     # ⭐ Capa de dominio
│   ├── pom.xml
│   └── src/main/java/com/leonal/
│       ├── model/                     # Entidades y objetos de dominio
│       └── port/                      # Interfaces (contratos)
│
├── leonal-application/                # ⭐ Capa de aplicación (casos de uso)
│   ├── pom.xml
│   └── src/main/java/com/leonal/
│       ├── dto/                       # Data Transfer Objects
│       ├── usecase/                   # Casos de uso (servicios de aplicación)
│       └── port/                      # Interfaces implementadas por infrastructure
│
├── leonal-infrastructure/             # ⭐ Capa de infraestructura
│   ├── pom.xml
│   ├── src/main/java/com/leonal/
│   │   └── adapter/                   # Adaptadores (JPA, Spring, etc.)
│   └── src/main/resources/
│       ├── application.properties      # Configuración
│       ├── db/migration/              # Scripts Flyway (V001__, V002__, etc.)
│       ├── reports/                   # Plantillas JasperReports (.jrxml)
│       └── images/                    # Imágenes y assets
│
├── leonal-ui/                         # ⭐ Capa de interfaz (JavaFX)
│   ├── pom.xml
│   └── src/main/java/com/leonal/
│       ├── view/                      # Controladores FXML
│       └── resources/
│           ├── css/                   # Estilos CSS
│           └── fxml/                  # Vistas FXML
│
└── leonal-launcher/                   # ⭐ Punto de entrada (Spring Boot)
    ├── pom.xml
    ├── src/main/java/com/leonal/
    │   └── LeonalLauncher.java       # Clase main
    └── src/main/resources/
        └── application.properties
```

---

## 🔧 Módulos Detallados

### 1️⃣ leonal-domain

**Responsabilidad**: Lógica de negocio pura, independiente de cualquier framework.

**Contenido**:
- Entidades de dominio (`model/`): `Paciente`, `Orden`, `Resultado`, `Usuario`, `Examen`, etc.
- Puertos/Interfaces (`port/`): Contratos que define qué implementaciones de infraestructura se necesitan
- Excepciones de dominio

**Ejemplo**:
```java
// Entidad de dominio pura
public class Paciente {
    private UUID id;
    private String nombre;
    private String cédula;
    // ... getters/setters sin dependencias de BD
}

// Puerto (interfaz)
public interface PacienteRepositorio {
    void guardar(Paciente paciente);
    Paciente obtenerPorId(UUID id);
}
```

**Dependencias**:
- `org.projectlombok:lombok` (para reducir boilerplate)

---

### 2️⃣ leonal-application

**Responsabilidad**: Orquestación de casos de uso y flujos de negocio.

**Contenido**:
- Casos de uso (`usecase/`): `CrearOrdenUseCase`, `RegistrarResultadoUseCase`, etc.
- DTOs (`dto/`): `CrearOrdenRequest`, `ResultadoResponse`, etc.
- Implementaciones de puertos de dominio

**Ejemplo**:
```java
public class CrearOrdenUseCase {
    private final PacienteRepositorio pacienteRepo;
    
    public ResultadoCrearOrden ejecutar(CrearOrdenRequest request) {
        // Validación y lógica de caso de uso
        Paciente paciente = pacienteRepo.obtenerPorId(request.getPacienteId());
        // ... crear orden, guardar, retornar
    }
}
```

**Dependencias**:
- `leonal-domain`
- `org.mockito:mockito-core` (testing)

---

### 3️⃣ leonal-infrastructure

**Responsabilidad**: Implementaciones técnicas (persistencia, reportes, PDF, etc.).

**Contenido**:
- Adaptadores JPA (`adapter/`): `PacienteRepositorioJPA`, `OrdenRepositorioJPA`
- Configuración de Spring Data JPA
- Generador de reportes (JasperReports)
- Scripts de migraciones (Flyway) en `resources/db/migration/`

**Migraciones de BD**:
```
resources/db/migration/
├── V001__esquema_inicial.sql
├── V002__tabla_usuarios.sql
├── V003__tabla_seguridad.sql
├── V004__tabla_pacientes.sql
├── V005__tabla_examenes.sql
├── V006__catalogo_examenes.sql
├── V007__tabla_ordenes.sql
├── V008__tabla_resultados.sql
├── V009__tabla_facturacion.sql
└── V010__tabla_pagos.sql
```

**Plantillas de Reportes (JasperReports)**:
- `orden_comprobante.jrxml`: Comprobante de orden
- `resultados_laboratorio.jrxml`: Informe de resultados

**Dependencias**:
- `leonal-domain` + `leonal-application`
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.postgresql:postgresql` (driver)
- `org.flywaydb:flyway-core` (migraciones)
- `net.sf.jasperreports:jasperreports` (reportes)
- `org.apache.pdfbox:pdfbox` (manipulación PDF)

---

### 4️⃣ leonal-ui

**Responsabilidad**: Interfaz gráfica en JavaFX.

**Contenido**:
- Controladores FXML (`view/`): `DashboardController`, `OrdenesController`, etc.
- Vistas FXML (`resources/fxml/`): `dashboard.fxml`, `ordenes.fxml`, etc.
- Estilos CSS (`resources/css/styles.css`)

**Vistas FXML incluidas**:
- `login.fxml` - Autenticación
- `main_layout.fxml` - Layout principal
- `dashboard.fxml` - Dashboard/inicio
- `ordenes.fxml`, `nueva_orden_form.fxml` - Gestión de órdenes
- `pacientes.fxml` - Gestión de pacientes
- `examenes.fxml` - Catálogo de exámenes
- `resultados.fxml`, `ingreso_resultados_dialog.fxml` - Resultados
- `usuarios.fxml` - Gestión de usuarios
- `facturas.fxml`, `pagos.fxml` - Facturación
- `caja.fxml` - Módulo de caja
- `caja_backup.fxml` - Respaldo/backup

**Dependencias**:
- `leonal-application`
- `org.openjfx:javafx-*` (JavaFX 17.0.2)
- `org.springframework.boot:spring-boot-starter` (context)

---

### 5️⃣ leonal-launcher

**Responsabilidad**: Punto de entrada de la aplicación y configuración de Spring Boot.

**Contenido**:
- Clase main: `com.leonal.launcher.LeonalLauncher`
- Configuración de Spring Boot (`application.properties`)
- Plugin de Maven para ejecutar con `mvn spring-boot:run`

**Características**:
- Punto único de inyección de dependencias (DI)
- Carga automática de migraciones Flyway
- Inicialización de DataSource PostgreSQL
- Lanzamiento de interfaz JavaFX

**Dependencias**:
- Depende de todos los módulos anteriores

---

## 🚀 Cómo Ejecutar

### Ejecución en Desarrollo

**Pasos**:

1. **Iniciar PostgreSQL** (si usas Docker Compose):
   ```bash
   docker-compose up -d
   ```

2. **Compilar el proyecto**:
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación**:
   ```bash
   cd leonal-launcher
   mvn spring-boot:run
   ```

   La interfaz JavaFX debería abrirse en la pantalla.

### Compilar JAR Ejecutable

Para empaquetar la aplicación en un JAR ejecutable:

```bash
mvn clean package -DskipTests
```

Esto generará un JAR en `leonal-launcher/target/leonal-launcher-1.0.0-SNAPSHOT.jar`.

Para ejecutarlo:
```bash
java -jar leonal-launcher/target/leonal-launcher-1.0.0-SNAPSHOT.jar
```

### Compilación Rápida (sin tests)

```bash
mvn clean install -DskipTests
cd leonal-launcher
mvn spring-boot:run
```

---

## 🧪 Testing

### Ejecutar Todos los Tests

```bash
mvn test
```

### Ejecutar Tests de un Módulo Específico

```bash
mvn test -pl leonal-application
```

### Ejecutar con Cobertura de Código

```bash
mvn clean test jacoco:report
# Reporte en: target/site/jacoco/index.html
```

### Estructura de Tests

Cada módulo tiene tests unitarios en:
```
src/test/java/com/leonal/...
```

**Frameworks**:
- **JUnit 5** (`org.junit.jupiter:junit-jupiter`)
- **Mockito** (`org.mockito:mockito-core`) para mocks

**Ejemplo de test**:
```java
@Test
void debeCrearOrdenValida() {
    // Arrange
    CrearOrdenRequest request = new CrearOrdenRequest(...);
    
    // Act
    ResultadoCrearOrden resultado = useCase.ejecutar(request);
    
    // Assert
    assertTrue(resultado.esExitosa());
}
```

---

## 🗺️ Roadmap

### Fase 1 ✅ (Actual)
- [x] Configuración del proyecto (Clean Architecture, Maven multimodular)
- [x] Modelo de datos y migraciones
- [x] CRUD de pacientes, órdenes, resultados
- [x] Generación de reportes PDF
- [x] Interface JavaFX básica
- [x] Facturación y pagos
- [x] Autenticación y autorización

### Fase 2 🚀 (Próxima)
- [ ] API REST (Spring Boot Web)
- [ ] Interfaz web (React/Angular)
- [ ] Integración con PACS (sistemas de imágenes médicas)
- [ ] Notificaciones (email/SMS)
- [ ] Exportación avanzada (Excel, XML, HL7)

### Fase 3 📋 (Futuro)
- [ ] Analytics y business intelligence (Grafana, Kibana)
- [ ] Mobile app (Flutter/React Native)
- [ ] Multi-tenancy (soporte para múltiples laboratorios)
- [ ] Sincronización en tiempo real

---

## 🐛 Troubleshooting

### Problema: "Connection refused" a PostgreSQL

**Síntomas**: `org.postgresql.util.PSQLException: Connection to localhost:5432 refused`

**Soluciones**:
1. Verificar que PostgreSQL está corriendo:
   ```bash
   docker-compose ps
   # O si lo instalaste localmente:
   psql -U leonal_user -h localhost -d leonal_db
   ```

2. Esperar a que el contenedor esté listo (puede tardar 10-15 segundos):
   ```bash
   docker-compose up -d
   sleep 15
   cd leonal-launcher && mvn spring-boot:run
   ```

3. Verificar credenciales en `application.properties`

---

### Problema: "No suitable driver found"

**Síntomas**: `java.sql.SQLException: No suitable driver found`

**Solución**:
Asegurate de que Maven descargó el driver de PostgreSQL:
```bash
mvn dependency:tree | grep postgresql
```

Si no aparece, actualiza las dependencias:
```bash
mvn clean install
```

---

### Problema: "Flyway migration failed"

**Síntomas**: Error durante migraciones en `leonal-infrastructure/src/main/resources/db/migration/`

**Soluciones**:
1. Verifica que los scripts tienen nombres válidos: `V001__descripcion.sql`, `V002__descripcion.sql`
2. Revisa los logs para ver qué script falló
3. Si la BD ya existe con migraciones parciales, purga y comienza de nuevo:
   ```sql
   DROP DATABASE leonal_db;
   CREATE DATABASE leonal_db OWNER leonal_user;
   ```

---

### Problema: "JavaFX not found" o error visual

**Síntomas**: `java.lang.NoClassDefFoundError: javafx/*`

**Solución**:
Maven debería incluir JavaFX automáticamente. Si no:
```bash
mvn clean install -U
```

El flag `-U` fuerza la descarga de las últimas versiones de dependencias.

---

### Problema: Puerto 5432 ya en uso

**Síntomas**: `bind: address already in use` al ejecutar `docker-compose up`

**Solución**:
Cambia el puerto en `docker-compose.yml`:
```yaml
ports:
  - "5433:5432"  # Usa 5433 en lugar de 5432
```

Y actualiza `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/leonal_db
```

---

## 🤝 Guía de Contribución

Queremos que contribuyas a **leonalApp**. Aquí hay cómo:

### 1. Fork y Clonar

```bash
git clone 
cd leonalAPP
```

### 2. Crear Rama

```bash
git checkout -b feature/nombre-caracteristica
# O para bugfix:
git checkout -b bugfix/nombre-bug
```

### 3. Hacer Cambios

- Respeta la **Clean Architecture**: no mezcles capas
- Agrega tests para cada cambio
- Documenta código complejo
- Usa nombres descriptivos en clases/métodos

### 4. Commit y Push

```bash
git add .
git commit -m "feat: descripción clara de los cambios"
git push origin feature/nombre-caracteristica
```

### 5. Pull Request

Crea un PR describiendo:
- Qué problema resuelve
- Cómo se probó
- Screenshots si es interfaz

### Estándares de Código

- **Java**: Sigue convenciones estándar (camelCase, nombres descriptivos)
- **Testing**: Mínimo 70% de cobertura
- **Documentación**: Comenta lógica compleja, documenta métodos públicos
- **Módulos**: Respeta la separación de capas

---

## 📄 Licencia

**Licencia se especificará próximamente** (MIT, GPL, Apache, etc.)

---

## 👥 Contacto y Soporte

- **Reportar bugs**: Abre un issue en GitHub
- **Sugerencias**: Abre una discussion
- **Email**: Por especificar

---

## 📚 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JavaFX Documentation](https://openjfx.io/)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

---

**¡Gracias por usar y contribuir a leonalApp!** 🎉