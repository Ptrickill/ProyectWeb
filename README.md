# 🌐 User Management System

Sistema de gestión de usuarios con **Spring Boot** y **Angular**.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green)
![Angular](https://img.shields.io/badge/Angular-20-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

## ✨ Características

- 🔐 Login y logout de usuarios
- 👥 CRUD completo de usuarios
- 🎨 Interfaz responsive
- 🗄️ Base de datos PostgreSQL

## 🛠️ Tecnologías

**Backend:** Spring Boot, Spring Security, PostgreSQL  
**Frontend:** Angular, TypeScript, Bootstrap

## 🚀 Instalación

### Prerrequisitos
- Java 25+
- Node.js 18+  
- PostgreSQL

### Configuración
```bash
# 1. Clonar proyecto
git clone https://github.com/Ptrickill/ProyectWeb.git
cd proyectoingweb

# 2. Crear base de datos
createdb IngenieriaWeb

# 3. Configurar application.properties con tus credenciales

# 4. Ejecutar backend
./mvnw spring-boot:run

# 5. Ejecutar frontend
cd angular-frontend
npm install
npm start
```

## 💻 Uso

**URL:** http://localhost:4200

**Usuarios de prueba:**
- admin / admin123 (ADMIN)
- usuario / user123 (USER)

## 📡 API Endpoints

```
POST /api/auth/login     # Login
GET  /api/users         # Listar usuarios
POST /api/users         # Crear usuario
PUT  /api/users/{id}    # Actualizar usuario  
DELETE /api/users/{id}  # Eliminar usuario
```

## 👨‍💻 Autor

**Danny** - [Ptrickill](https://github.com/Ptrickill)

