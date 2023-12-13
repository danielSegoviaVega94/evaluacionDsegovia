
# EvaluacionDsegovia API de Gestión de Usuarios

## Descripción
Esta API RESTful proporciona funcionalidades para la gestión de usuarios, incluyendo registro, autenticación, consulta, actualización y eliminación de usuarios.

## Configuración y Instalación
- **Clonar el repositorio**: `git clone https://github.com/danielSegoviaVega94/evaluacionDsegovia.git`
- **Ejecutar el proyecto**: `mvn spring-boot:run`

## Autenticación
Cada Endpoint debe incluir el token de acceso en el encabezado de la solicitud.

### Login
- **URL**: `POST /api/users/login`
- **Cuerpo de la solicitud**:
  ```json
  {
    "username": "admin@admin.com",
    "password": "admin"
  }
  ```
- **Respuesta**: Token de acceso JWT.

### GetAllUsers
- **URL**: `POST /api/users`
- **Respuesta**: Lista de usuarios.

### GetAllUserPageable
- **URL**: `POST /api/users/page`
- **Params**: `page`, `size`
- **Respuesta**: Lista de usuarios paginada.

### CreateUser
- **URL**: `POST /api/users`
- **Content-Type**: `application/json`
- **Cuerpo de la solicitud**:
  ```json
  {
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Password@123",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "contrycode": "57"
      }
    ]
  }
  ```
- **Respuesta**:
  ```json
  {
    "id": "ded0acdd-3e59-4553-b14c-d1d25696b1c3",
    ...
    "active": true
  }
  ```

### GetUser
- **URL**: `GET /api/users/{email}`
- **Respuesta**: Detalles del usuario.

### UpdateUser
- **URL**: `PUT /api/users`
- **Content-Type**: `application/json`
- **Cuerpo de la solicitud**: `{...}`
- **Respuesta**: Detalles del usuario actualizado.

### DeleteUser
- **URL**: `DELETE /api/users/{email}/{uuid}`

## Notas
- La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula y un carácter especial.
- Adjunto un Word Con imagenes de la documentacion de la API, además de un diagrama de secuencia.
