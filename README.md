# Gestión de Trabajadores - Frontend - App móvil

Aplicación móvil Android que permite a un usuario autenticarse y gestionar el registro de trabajadores. Desarrollada en Kotlin con Jetpack Compose, consumiendo una API REST desarrollada en ASP.NET Core.

## Características

- Inicio de sesión con email y contraseña, autenticado mediante JWT.
- Listado de trabajadores registrados por el usuario autenticado.
- Registro de nuevos trabajadores mediante un formulario.
- Búsqueda de trabajadores por DNI.
- Persistencia de sesión (el usuario no necesita volver a loguearse cada vez que abre la app).

## Tecnologías

- **Kotlin** como lenguaje principal.
- **Jetpack Compose** para la interfaz de usuario (declarativa, sin XML).
- **Retrofit** + **OkHttp** para el consumo de la API REST.
- **Coroutines** y **StateFlow** para manejo de concurrencia y estado reactivo.
- **Navigation Compose** para la navegación entre pantallas.
- **SharedPreferences** para la persistencia local de la sesión (token JWT).

## Estructura del proyecto

```
com.example.gestiontrabajadoresiu/
├── data/
│   ├── ApiService.kt           # Definición de los endpoints consumidos
│   ├── RetrofitClient.kt       # Configuración del cliente HTTP (Retrofit + OkHttp)
│   ├── SessionManager.kt       # Persistencia local de la sesión (token, usuario)
│   └── models/
│       ├── dtos/
│       └── viewmodels/
├── navigation/
│   ├── AppNavigation.kt
│   └── Screen.kt
├── screens/
│   ├── CrearTrabajadorScreen.kt
│   ├── LoginScreen.kt
│   └── TrabajadoresScreen.kt
├── ui/
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt
```

## Pantallas

| Pantalla             | Descripción                                                           |
| -------------------- | --------------------------------------------------------------------- |
| **Login**            | Autenticación con email y contraseña                                  |
| **Trabajadores**     | Listado de trabajadores del usuario autenticado, con búsqueda por DNI |
| **Crear Trabajador** | Formulario para registrar un nuevo trabajador                         |

## Requisitos

- Android Studio (version reciente, con soporte para Jetpack Compose).
- SDK mínimo según `build.gradle.kts` del proyecto.
- Backend corriendo (ver proyecto del backend en ASP.NET Core)

## Configuración de conexión al backend

La app se conecta al backend definiendo la URL base en `RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "http://10.0.2.2:5258/"
```

- **Emulador de Android Studio**: usar `10.0.2.2`, que el emulador traduce automáticamente al `localhost` de la máquina donde corre el backend.
- **Dispositivo físico**: usar la IP local de la máquina donde corre el backend (ej. `192.168.1.X`), estando ambos en la misma red.

> Si el emulador no logra conectar con el backend (error de timeout) aunque el backend esté corriendo correctamente, puede deberse a un problema de enrutamiento de red específico del entorno. Como solución alternativa, ejecutar:
>
> ```bash
> adb reverse tcp:5258 tcp:5258
> ```
>
> y cambiar `BASE_URL` a `http://localhost:5258/`. Este túnel debe volver a ejecutarse cada vez que se reinicia el emulador.

## Cómo ejecutar el proyecto

1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Verificar que el backend esté corriendo y accesible.
4. Ajustar `BASE_URL` en `RetrofitClient.kt` según el entorno de pruebas (emulador o dispositivo físico).
5. Ejecutar la app (`Run ▶️`) en un emulador o dispositivo físico.

## Backend relacionado

Esta aplicación consume una API REST desarrollada en ASP.NET Core Web API, con SQL Server como base de datos y Stored Procedures para el acceso a datos. El backend expone, entre otros, los siguientes endpoints:

- `POST /api/usuarios/login`
- `POST /api/usuarios/registro`
- `GET /api/trabajadores`
- `POST /api/trabajadores`

## Notas

- La contraseña nunca se persiste localmente; solo se almacena el token JWT recibido tras un login exitoso.
- El token se envía automáticamente en cada petición autenticada mediante un interceptor de OkHttp (`Authorization: Bearer {token}`).
