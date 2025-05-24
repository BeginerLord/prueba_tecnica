# Prueba Técnica Backend - Sistema de Gestión Académica

Este proyecto es una API REST desarrollada en **Java Spring Boot** para la gestión de usuarios en un sistema académico. Está diseñada para cubrir el proceso completo de registro, autenticación, verificación y recuperación de contraseñas, con control de acceso basado en roles.

## Funcionalidades principales

- **Registro de usuarios:** Permite registrar usuarios con nombre y correo, enviando un correo de verificación con token.
- **Verificación de correo electrónico:** Valida el correo del usuario mediante token enviado por email.
- **Asignación y restablecimiento de contraseña:** Establece o restablece la contraseña usando tokens seguros y únicos.
- **Inicio de sesión:** Autentica usuarios activos y retorna tokens JWT para acceso protegido.
- **Gestión de usuarios:** Endpoints para obtener perfil, listar usuarios y consultar usuarios por UUID, protegidos con roles.
- **Recuperación de contraseña:** Solicita y gestiona el restablecimiento de contraseña mediante correo electrónico y token.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3
- Spring Security con JWT
- PostgreSQL (base de datos)
- Hibernate / JPA (ORM)
- Swagger/OpenAPI para documentación automática

## Estructura principal

- Controladores REST para Auth y Users.
- Servicios para registro, login, recuperación y validación.
- Mapeo y entidades para usuarios, roles y tokens.
- Manejo global de excepciones con respuestas estandarizadas.
- Configuración de seguridad y validación de JWT.

## Cómo usar

1. Clona el repositorio y configura las variables de entorno para la conexión a la base de datos y correo.
2. Ejecuta la aplicación Spring Boot.
3. Accede a la documentación Swagger en `http://localhost:8080/swagger-ui/index.html#/`.
4. Usa los endpoints para registrar usuarios, activar cuentas, iniciar sesión y gestionar perfiles.

## Contacto

Desarrollado por Nilson Ruiz  
[LinkedIn](https://www.linkedin.com/in/nilson-ruiz-ing2027)
