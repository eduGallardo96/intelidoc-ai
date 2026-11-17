# Intelidoc AI 🤖
### Analizador Arquitectónico Local con Inteligencia Artificial Agéntica

Intelidoc AI es una aplicación **Full Stack Enterprise Multi-tier** diseñada bajo arquitectura limpia y orientada a eventos. Permite procesar y auditar documentos técnicos complejos (como requerimientos de software o diseños de sistemas) extrayendo un análisis de arquitectura estructurado y métricas críticas de desarrollo en tiempo real.

El núcleo del sistema destaca por operar de forma **100% local, privada y gratuita**, eliminando dependencias de APIs Cloud comerciales y asegurando el cumplimiento estricto de la gobernanza de datos confidenciales mediante el uso de LLMs Open Source.

---

## Ficha Técnica y Arquitectura Modern Stack

El proyecto ha sido concebido para implementar y demostrar las **últimas tendencias del mercado tecnológico (2026)**, aplicando patrones avanzados y optimizaciones de rendimiento a nivel Senior.

### Backend: Spring Boot 4.0 & Java 25
*   **Java 25 Native Features:** Implementación de expresiones `switch` avanzadas con *Pattern Matching* jerárquico de tipos y cláusulas `when` declarativas para la gestión de flujos de control.
*   **Spring AI Integration:** Inyección modular a través de la API fluida de `ChatClient.Builder` desacoplada del proveedor, forzando modo JSON nativo (`format: json`) y deserialización resiliente hacia *Java Records* inmutables mediante Jackson 3.
*   **Event-Driven Architecture (Apache Kafka):** Desacoplamiento de tareas secundarias mediante mensajería asíncrona. El servicio actúa como Productor (`KafkaTemplate`) publicando eventos JSON en el tópico `document-analysis-completed`, mientras un Consumidor dedicado (`@KafkaListener`) procesa auditorías y notificaciones en segundo plano.
*   **Spring Security 7:** Configuración defensiva desacoplada del contenedor con desactivación táctica de CSRF para endpoints reactivos locales y control granular de CORS mediante patrones dinámicos (`originPatterns`).
*   **Engine IA Local:** Conexión nativa con **Ollama** ejecutando el modelo **Llama 3** de forma autónoma.
*   **Persistencia:** Capa de datos gestionada por Spring Data JPA sobre motor relacional **H2 database** en memoria con consola de administración integrada.

### Frontend: Angular 21 Reactive (Zoneless)
*   **Arquitectura 100% Zoneless:** Eliminación total de la dependencia clásica `Zone.js` mediante la activación de `provideExperimentalZonelessChangeDetection()`, reduciendo drásticamente la sobrecarga de ciclos de renderizado en el navegador.
*   **Signal State Management:** Encapsulación estricta de estados de red asíncronos (`RequestStatus`) utilizando Señales (`signal`) de solo lectura (`asReadonly()`) y valores computados (`computed`), asegurando mutaciones controladas exclusivamente desde la capa de servicios.
*   **Modern Angular Templates:** Adopción del nuevo flujo de control nativo (`@if` / `@for`) integrado en el compilador, optimizando el rendimiento de listas complejas mediante la directiva estricta `track`.
*   **Asincronía Senior:** Implementación de servicios desacoplados basados en la función `inject()` y procesamiento asíncrono con `async/await` mediante la conversión de flujos RxJS con `firstValueFrom`.
*   **UI/UX Componentization:** Interfaz *Standalone* modular estilizada con SCSS nativo bajo metodología BEM, implementando mecánicas puras de *Drag & Drop* de ficheros en el navegador.

### Infraestructura & Calidad (DevOps & Testing)
*   **Contenerización (Docker):** Diseño de *Multi-stage builds* para minimizar el tamaño de las imágenes de producción (compilación aislada con Maven/Node y ejecución ligera sobre `eclipse-temurin:25-jre-alpine` y `nginx:alpine`).
*   **Orquestación (Docker Compose):** Despliegue unificado mediante red puente (`bridge`) levantando el servidor web Nginx, el microservicio Spring Boot y un broker de **Apache Kafka en modo KRaft** (sin Zookeeper), enrutando peticiones hacia el host local.
*   **Testing Automatizado:** 
    *   *Backend:* Suite unitaria con **JUnit 5 y Mockito**, utilizando `RETURNS_DEEP_STUBS` para simular cadenas fluidas de Spring AI y `verify()` para auditar la publicación de eventos en Kafka.
    *   *Frontend:* Pruebas unitarias ultrarrápidas con **Vitest** y `HttpTestingController`, validando mutaciones reactivas de Signals en flujos HTTP asíncronos.

---

## Despliegue Rápido con Docker (Recomendado)

### Requisitos Previos
*   **Docker Desktop** instalado y en ejecución.
*   **Ollama** ejecutándose localmente con el modelo descargado (`ollama run llama3`).

### Levantar toda la infraestructura
Desde la raíz del monorrepo, ejecuta el siguiente comando para compilar imágenes y levantar los contenedores de Frontend, Backend y Kafka:
```bash
docker compose up --build
```
*   **Frontend (Nginx):** Accesible en `http://localhost:4200`
*   **Backend API:** Accesible en `http://localhost:8080`
*   **Apache Kafka Broker:** Escuchando en el puerto interno `9092`

*(Para detener los contenedores y liberar recursos: `docker compose down`).*

---

## Ejecución de Suites de Testing

### Backend (JUnit 5 & Mockito)
```bash
cd intelidoc-backend
./mvnw test
```

### Frontend (Vitest)
```bash
cd intelidoc-frontend
npm test
```

---

## Instalación Manual (Sin Docker)

### 1. Backend (Java 25 + Spring Boot 4)
Requiere tener un broker de Kafka corriendo en `localhost:9092` y Ollama en `localhost:11434`.
```bash
cd intelidoc-backend
./mvnw spring-boot:run
```

### 2. Frontend (Node.js v24 + Angular 21)
```bash
cd intelidoc-frontend
npm install
npm start
```
