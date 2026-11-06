# Intelidoc AI 🤖
### Analizador Arquitectónico Local con Inteligencia Artificial Agéntica

Intelidoc AI es una aplicación **Full Stack Enterprise Multi-tier** diseñada bajo arquitectura limpia. Permite procesar y auditar documentos técnicos complejos (como requerimientos de software o diseños de sistemas) extrayendo un análisis de arquitectura estructurado y métricas críticas de desarrollo en tiempo real.

El núcleo del sistema destaca por operar de forma **100% local, privada y gratuita**, eliminando dependencias de APIs Cloud comerciales y asegurando el cumplimiento estricto de la gobernanza de datos confidenciales mediante el uso de LLMs Open Source.

---

## Ficha Técnica y Arquitectura Modern Stack

El proyecto ha sido concebido para implementar y demostrar las **últimas tendencias del mercado tecnológico (2026)**, aplicando patrones avanzados y optimizaciones de rendimiento a nivel Senior.

### Backend: Spring Boot 4.0 & Java 25
*   **Java 25 Native Features:** Implementación de expresiones `switch` avanzadas con *Pattern Matching* jerárquico de tipos y cláusulas `when` declarativas para la gestión de flujos de control.
*   **Spring AI Integration:** Inyección modular a través de la API fluida de `ChatClient.Builder` desacoplada del proveedor, con soporte nativo para *Structured Outputs* (`.entity()`) mapeados a *Java Records* inmutables mediante Jackson.
*   **Spring Security 7:** Configuración defensiva desacoplada del contenedor con desactivación táctica de CSRF para endpoints reactivos locales y control granular de CORS mediante patrones dinámicos (`originPatterns`).
*   **Engine IA Local:** Conexión nativa sobre el puerto local a través de **Ollama** consumiendo modelos a gran escala **Llama 3** de forma autónoma.
*   **Persistencia:** Capa de datos gestionada por Spring Data JPA sobre motor relacional **H2 database** en memoria con consola de administración integrada para desarrollo ágil.

### Frontend: Angular 21 Reactive (Zoneless)
*   **Arquitectura 100% Zoneless:** Eliminación total de la dependencia clásica `Zone.js` mediante la activación del flag `provideExperimentalZonelessChangeDetection()`, reduciendo drásticamente la sobrecarga de ciclos de renderizado en el navegador.
*   **Signal State Management:** Encapsulación estricta de estados de red asíncronos (`RequestStatus`) utilizando Señales (`signal`) de solo lectura (`asReadonly()`) y valores computados (`computed`), asegurando mutaciones controladas exclusivamente desde la capa de servicios.
*   **Modern Angular Templates:** Adopción del nuevo flujo de control nativo (`@if` / `@for`) integrado en el compilador, optimizando el rendimiento de listas complejas mediante la directiva estricta `track`.
*   **Asincronía Senior:** Implementación de servicios desacoplados basados en la función funcional `inject()` y procesamiento asíncrono con `async/await` mediante la conversión de flujos RxJS con `firstValueFrom`.
*   **UI/UX Componentization:** Interfaz *Standalone* modular limpia estilizada con SCSS nativo estructurado bajo la metodología BEM, implementando mecánicas puras de *Drag & Drop* de ficheros en el navegador.

---

## Requisitos e Instalación Local

### Requisitos Previos
*   **Java 25** instalado localmente.
*   **Node.js v24.x** y **Angular CLI v21.x**.
*   **Ollama** ejecutándose en segundo plano.

### 1. Inicializar el motor de IA Local
Descarga y arrastra el modelo de lenguaje ejecutando en tu terminal de comandos:
```bash
ollama run llama3
```

### 2. Levantar el Backend (Spring Boot)
Navega a la carpeta del microservicio e inicializa Maven:
```bash
cd intelidoc-backend
./mvnw spring-boot:run
```
*El servidor arrancará en el puerto `8080`. Puedes auditar la base de datos en `http://localhost:8080/h2-console` (Credenciales: User: `sa`, sin contraseña).*

### 3. Levantar el Frontend (Angular)
Instala las dependencias de Node e inicia el servidor web:
```bash
cd intelidoc-frontend
npm install
npm start
```
*La aplicación compilará de forma reactiva y abrirá automáticamente tu navegador en la dirección local asignada.*
