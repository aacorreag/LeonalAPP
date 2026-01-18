# leonalApp - Sistema Clínico LIS

Sistema de Información de Laboratorio (LIS) Open Source, diseñado para laboratorios pequeños, enfocado en eficiencia, trazabilidad y seguridad.

## Arquitectura

El proyecto sigue una arquitectura **Clean Architecture Estricta** modularizada con Maven:

- **leonal-domain**: Reglas de negocio puras, entidades y contratos (interfaces). Sin dependencias de frameworks.
- **leonal-application**: Casos de uso y orquestación. Depende solo de `domain`.
- **leonal-ui**: Interfaz de usuario JavaFX. Depende solo de `application` (y sus DTOs).
- **leonal-infrastructure**: Implementaciones técnicas (Persistencia JPA, PDF, Email). Depende de `domain` y `application`.
- **leonal-launcher**: Módulo de arranque (Spring Boot). Único punto donde se realiza el cableado (Wiring) de dependencias.

## Requisitos

- Java 17+
- PostgreSQL 14+
- Maven 3.8+

## Ejecución

El punto de entrada es el módulo `leonal-launcher`.

```bash
mvn clean install
cd leonal-launcher
mvn spring-boot:run
```

## Estado del Proyecto

En desarrollo inicial (Fase 1: Configuración y Modelo de Datos).
