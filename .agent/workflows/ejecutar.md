---
description: Levantar App
---

Ejecuta estos comandos en la terminal en ese orden:

1: .\mvnw.cmd clean install -DskipTests
2: docker-compose down
3: docker-compose up -d
4: .\mvnw.cmd spring-boot:run -pl leonal-launcher
