# Módulo de Gestión de Oportunidades Comerciales (Android + Spring Boot)

Aplicación móvil nativa desarrollada en **Kotlin** y **Jetpack Compose** para la captura, gestión y seguimiento de oportunidades comerciales en terreno, integrada con un microservicio REST propio en **Spring Boot** y consumo de APIs externas.

---

## 1. Contexto del Proyecto (EFT)

El sistema resuelve la necesidad de equipos comerciales de registrar eventos, licitaciones y prospectos de negocio en tiempo real desde dispositivos móviles. La solución implementa una arquitectura híbrida (*offline-first*) que garantiza continuidad operativa incluso sin conexión a internet y sincronización automática con el servidor central cuando hay red disponible.

### Funcionalidades Principales
* **Autenticación de Usuarios:** Registro e inicio de sesión con validación de credenciales y persistencia local (`UserEntity`, `UserDao`).
* **Gestión CRUD de Oportunidades:** Creación, visualización, edición y eliminación de oportunidades comerciales con validación en tiempo real de campos obligatorios (`nombre`, `tipo`, `descripción`, `fuente`, `prioridad`, `potencial`, `estado` y `observaciones`).
* **Recurso Nativo (Cámara):** Captura de evidencia fotográfica mediante la cámara física del dispositivo (`ActivityResultContracts.TakePicturePreview`), gestión dinámica de permisos (`android.permission.CAMERA`) y almacenamiento local vinculado al registro.
* **Sincronización con Microservicio Spring Boot:** Persistencia remota de operaciones CRUD mediante peticiones HTTP REST.
* **Consumo de API Externa:** Consulta de información en tiempo real desde un servicio público externo mediante cliente Retrofit independiente.

---

## 2. Arquitectura y Stack Tecnológico

### Aplicación Móvil (Android)
* **Lenguaje:** Kotlin
* **Patrón de Arquitectura:** MVVM (*Model - View - ViewModel*) + Patrón Repositorio
* **Interfaz de Usuario:** Jetpack Compose + Material Design 3 + Navigation Compose
* **Persistencia Local:** Room Database (`AppDatabase`, `OportunidadDao`, `UserDao`)
* **Consumo de Red:** Retrofit 2 + Gson Converter + Corrutinas de Kotlin (`Flow` / `StateFlow`)
* **Pruebas Unitarias:** JUnit 4, MockK y `kotlinx-coroutines-test`

### Microservicio Backend (`oportunidades-service`)
* **Framework:** Spring Boot 3 (Java 17 / 21)
* **Módulos:** Spring Web, Spring Data JPA
* **Base de Datos:** H2 Database (persistencia local en archivo `./data/oportunidadesdb`)

---

## 3. Endpoints Integrados

### Microservicio Propio (Spring Boot)
* **Base URL:** `http://<IP_LOCAL>:8080/api/`

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/oportunidades` | Obtiene el listado completo de oportunidades registradas en el servidor. |
| `POST` | `/oportunidades` | Crea y persiste una nueva oportunidad comercial en la base de datos. |
| `PUT` | `/oportunidades/{id}` | Actualiza los datos de una oportunidad existente según su ID. |
| `DELETE` | `/oportunidades/{id}` | Elimina una oportunidad comercial del servidor. |

### API Externa
* **Base URL:** `https://rickandmortyapi.com/api/`
* **Endpoint:** `GET /character`
* **Implementación:** Gestionado a través de `RetrofitClient.externalApi` y visualizado desde la barra superior de `ListaOportunidadesScreen`.

---

## 4. Estructura del Proyecto Android

```text
app/src/main/java/com/example/androidapp/
├── datos/local/         # Entidades (@Entity), DAOs (@Dao) y base de datos Room (AppDatabase)
├── navegacion/          # Grafo de navegación declarativa en Compose (AppNavigation)
├── network/             # Interfaces Retrofit (ApiService), modelos DTO y RetrofitClient
├── repository/          # Lógica de sincronización remota + local (OportunidadRepository, UserRepository)
├── ui/
│   ├── auth/            # Pantallas LoginScreen, RegisterScreen y AuthViewModel
│   ├── oportunidad/     # Pantallas ListaOportunidadesScreen, OportunidadFormScreen y OportunidadViewModel
│   └── theme/           # Paleta de colores, tipografía y tema Material Design 3
└── MainActivity.kt      # Punto de entrada de la aplicación e inyección de dependencias
```

---

## 5. Instrucciones de Instalación y Ejecución

### Paso 1: Ejecutar el Microservicio Spring Boot
1. Abrir el proyecto `oportunidades-service` en IntelliJ IDEA (o ejecutar `./mvnw spring-boot:run` desde la terminal).
2. Verificar que el servidor inicie correctamente en el puerto `8080`.
3. Comprobar disponibilidad abriendo `http://localhost:8080/api/oportunidades` en el navegador (o acceder a la consola de base de datos en `http://localhost:8080/h2-console`).

### Paso 2: Configurar y Ejecutar la App Android
1. Abrir el proyecto `AndroidApp` en Android Studio (configurado con **Gradle JDK 17** o **21**).
2. En el archivo `network/RetrofitClient.kt`, actualizar la constante `BASE_URL_MICROSERVICE` con la dirección IPv4 local del equipo donde corre Spring Boot (ejemplo: `http://192.168.1.15:8080/api/`), o usar `http://10.0.2.2:8080/api/` si se prueba en emulador.
3. Asegurar que el teléfono físico y el computador estén conectados a la misma red Wi-Fi.
4. Presionar **Run (`Shift + F10`)** para compilar e instalar la aplicación.

---

## 6. Pruebas Unitarias y Compilación Release

### Ejecución de Pruebas Unitarias
Las pruebas unitarias validan la lógica de negocio del `OportunidadViewModel` (validaciones de formulario y estados) y del `OportunidadRepository` (sincronización entre la API de Spring Boot y Room DAO mediante mocks).
* Ejecutar desde Android Studio haciendo clic derecho sobre `app/src/test/java/com/example/androidapp` $\rightarrow$ **Run 'Tests in 'androidapp''**, o mediante consola con:
  ```bash
  ./gradlew test
  ```

### Generación de APK Firmado (`release`)
El proyecto cuenta con configuración de firma digital (`signingConfigs.release`) vinculada al almacén de claves del proyecto:
1. Ir a **Build $\rightarrow$ Generate Signed App Bundle / APK...**
2. Seleccionar **APK** y elegir la variante **`release`**.
3. El instalador firmado se genera en `app/release/app-release.apk`.

---

## 7. Control de Versiones
El desarrollo del proyecto se gestionó mediante **Git** y **GitHub**, manteniendo trazabilidad de avances por hitos funcionales, pruebas de integración y documentación técnica.