# � Sistema de Orientación Vocacional

Sistema integral de orientación vocacional que ayuda a estudiantes a descubrir carreras afines a través de test personalizados, análisis de notas académicas, evaluación de habilidades y preferencias personales.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![Angular](https://img.shields.io/badge/Angular-20-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 🌐 Demo en Vivo

- **Backend API:** https://proyectoweb.onrender.com
- **Frontend:** https://sistema-vocacional-frontend.onrender.com

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura-y-patrones-de-diseño)
- [Tecnologías](#-tecnologías)
- [API Endpoints](#-api-endpoints)
- [Instalación](#-instalación)
- [Modelo de Datos](#-modelo-de-datos)
- [Deploy](#-deploy-en-render)
- [Autores](#-autores)

---

## ✨ Características

### 🎯 Funcionalidades Principales

- **Sistema de Test Vocacional Inteligente**
  - Algoritmo de cálculo con ponderación configurable (Académico 50%, Habilidades 30%, Afinidad 20%)
  - Análisis de notas por materia con normalización
  - Evaluación de habilidades mediante test específicos
  - Registro de preferencias personales (afinidades)
  
- **Panel de Administración Completo**
  - Gestión de carreras, materias y habilidades
  - Configuración de pesos y relaciones
  - Reportes y estadísticas del sistema
  - Administración de usuarios

- **Panel de Estudiante**
  - Registro de notas académicas
  - Realización de test de habilidades
  - Evaluación de afinidades con carreras
  - Visualización de resultados y recomendaciones TOP 3

- **Sistema de Roles y Autenticación**
  - Login/Registro seguro
  - Roles: ADMIN y USER (Estudiante)
  - Protección de rutas según rol

- **Interfaz Responsive**
  - Diseño adaptable a dispositivos móviles y desktop
  - UI moderna con Bootstrap y SCSS

---

## 🏗️ Arquitectura y Patrones de Diseño

### Principios SOLID Implementados

✅ **Single Responsibility Principle (SRP)**
- Cada servicio tiene una responsabilidad única (UserService, CarreraService, CalculoVocacionalService, etc.)

✅ **Open/Closed Principle (OCP)**
- Uso de Strategy Pattern para cálculos extensibles sin modificar código existente

✅ **Dependency Inversion Principle (DIP)**
- Inyección de dependencias por constructor en todos los servicios

### Patrones de Diseño

🔷 **Strategy Pattern**
- `ComponenteCalculoStrategy` interface con 3 implementaciones:
  - `ComponenteAcademicoStrategy`: Cálculo basado en notas
  - `ComponenteHabilidadesStrategy`: Cálculo basado en test de habilidades
  - `ComponenteAfinidadStrategy`: Cálculo basado en preferencias

🔷 **Factory Pattern**
- `UserFactory`: Creación centralizada de usuarios con diferentes roles
- `ResultadoFactory`: Construcción de resultados con fórmulas predefinidas

🔷 **Builder Pattern**
- `Resultado.Builder`: Construcción fluida de objetos Resultado

### Arquitectura en Capas

```
📦 Backend (Spring Boot)
├── 🎮 Controller Layer    # API REST endpoints
├── 💼 Service Layer       # Lógica de negocio
├── 🗄️ Repository Layer    # Acceso a datos (JPA)
├── 📊 Entity Layer        # Modelos de datos
├── 🏭 Factory Layer       # Patrones Factory
└── 🎯 Strategy Layer      # Patrones Strategy

📦 Frontend (Angular)
├── 📱 Components          # Componentes reutilizables
├── 🔐 Guards              # Protección de rutas
├── 🌐 Services            # Comunicación con API
└── 📋 Models              # Interfaces TypeScript
```

---

## 🛠️ Tecnologías

### Backend
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistencia y ORM
- **Hibernate 6.6.29** - ORM con generación automática de esquema
- **PostgreSQL 12+** - Base de datos relacional
- **HikariCP** - Connection pooling
- **Maven** - Gestión de dependencias

### Frontend
- **Angular 20** - Framework SPA
- **TypeScript** - Lenguaje tipado
- **RxJS** - Programación reactiva
- **Bootstrap 5** - Framework CSS
- **SCSS** - Preprocesador CSS
- **Angular Router** - Navegación

### DevOps & Deploy
- **Render** - Hosting de backend y base de datos
- **Git/GitHub** - Control de versiones
- **Java 21** - Runtime del backend

---

## 📡 API Endpoints

### 🔐 Autenticación (`/api/auth`)

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Iniciar sesión | `{ username, password }` |
| POST | `/api/auth/register` | Registrar nuevo usuario | `{ username, email, password, nombreCompleto }` |
| POST | `/api/auth/logout` | Cerrar sesión | - |

### 👥 Usuarios (`/api/users`)

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| GET | `/api/users` | Listar todos los usuarios | ADMIN |
| GET | `/api/users/{id}` | Obtener usuario por ID | ADMIN |
| POST | `/api/users` | Crear nuevo usuario | ADMIN |
| PUT | `/api/users/{id}` | Actualizar usuario | ADMIN |
| DELETE | `/api/users/{id}` | Eliminar usuario | ADMIN |
| GET | `/api/users/exists/username/{username}` | Verificar si existe username | - |
| GET | `/api/users/exists/email/{email}` | Verificar si existe email | - |

### 🎓 Carreras (`/api/admin/carreras`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/carreras` | Listar todas las carreras |
| GET | `/api/admin/carreras/{id}` | Obtener carrera por ID |
| POST | `/api/admin/carreras` | Crear nueva carrera |
| PUT | `/api/admin/carreras/{id}` | Actualizar carrera |
| DELETE | `/api/admin/carreras/{id}` | Eliminar carrera |

### 📚 Materias (`/api/admin/materias`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/materias` | Listar todas las materias |
| GET | `/api/admin/materias/{id}` | Obtener materia por ID |
| POST | `/api/admin/materias` | Crear nueva materia |
| PUT | `/api/admin/materias/{id}` | Actualizar materia |
| DELETE | `/api/admin/materias/{id}` | Eliminar materia |
| GET | `/api/admin/materias/carrera/{carreraId}` | Obtener materias de una carrera |

### 💪 Habilidades (`/api/admin/habilidades`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/habilidades` | Listar todas las habilidades |
| GET | `/api/admin/habilidades/{id}` | Obtener habilidad por ID |
| POST | `/api/admin/habilidades` | Crear nueva habilidad |
| PUT | `/api/admin/habilidades/{id}` | Actualizar habilidad |
| DELETE | `/api/admin/habilidades/{id}` | Eliminar habilidad |

### ⚖️ Pesos (`/api/admin/pesos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/pesos` | Obtener configuración de pesos |
| PUT | `/api/admin/pesos` | Actualizar pesos del algoritmo |

### 📝 Notas del Estudiante (`/api/estudiante/notas`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/estudiante/notas/estudiante/{estudianteId}` | Obtener todas las notas |
| POST | `/api/estudiante/notas` | Registrar nueva nota |
| PUT | `/api/estudiante/notas/{id}` | Actualizar nota |
| DELETE | `/api/estudiante/notas/{id}` | Eliminar nota |

### 🎯 Afinidades (`/api/estudiante/afinidades`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/estudiante/afinidades/estudiante/{estudianteId}` | Obtener todas las afinidades |
| POST | `/api/estudiante/afinidades` | Registrar afinidad con carrera |
| PUT | `/api/estudiante/afinidades/{id}` | Actualizar afinidad |
| DELETE | `/api/estudiante/afinidades/{id}` | Eliminar afinidad |

### 🎯 Resultados Vocacionales (`/api/estudiante/resultados`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/estudiante/resultados/calcular/{estudianteId}` | **🌟 Calcular resultados vocacionales** |
| GET | `/api/estudiante/resultados/top3/{estudianteId}` | Obtener TOP 3 carreras recomendadas |
| GET | `/api/estudiante/resultados/estudiante/{estudianteId}` | Obtener todos los resultados |
| GET | `/api/estudiante/resultados/{id}` | Obtener resultado por ID |
| DELETE | `/api/estudiante/resultados/{id}` | Eliminar resultado |

### 📊 Reportes (`/api/admin/reportes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/reportes/estadisticas` | Obtener estadísticas generales |

### 📖 Vista Pública (`/api`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/carreras` | Listar carreras (público) |
| GET | `/api/materias` | Listar materias (público) |
| GET | `/api/habilidades` | Listar habilidades (público) |

---

## 🚀 Instalación

### Prerrequisitos

- **Java 21+** ([Descargar](https://www.oracle.com/java/technologies/downloads/))
- **Node.js 18+** y **npm** ([Descargar](https://nodejs.org/))
- **PostgreSQL 12+** ([Descargar](https://www.postgresql.org/download/))
- **Git** ([Descargar](https://git-scm.com/))

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/Ptrickill/ProyectWeb.git
cd ProyectWeb/proyectoingweb
```

### Paso 2: Configurar Base de Datos

#### En PostgreSQL:

```sql
-- Crear base de datos
CREATE DATABASE IngenieriaWeb;

-- Crear usuario (opcional)
CREATE USER postgres WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE IngenieriaWeb TO postgres;
```

#### Configurar `application.properties`:

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/IngenieriaWeb
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

### Paso 3: Ejecutar Backend

```bash
# Instalar dependencias y ejecutar
./mvnw clean install
./mvnw spring-boot:run

# O en Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

### Paso 4: Ejecutar Frontend

```bash
# Ir al directorio del frontend
cd angular-frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm start
```

El frontend estará disponible en: `http://localhost:4200`

### Paso 5: Inicialización Automática

Al iniciar el backend por primera vez, **DataInitializer** creará automáticamente:
- ✅ Tablas de la base de datos
- ✅ Usuarios de prueba (admin/user)
- ✅ Carreras de ejemplo
- ✅ Materias relacionadas
- ✅ Habilidades base

---

## 💻 Uso del Sistema

### Usuarios de Prueba

| Username | Password | Rol | Descripción |
|----------|----------|-----|-------------|
| `admin` | `admin123` | ADMIN | Acceso completo al sistema |
| `usuario` | `user123` | USER | Estudiante con acceso a test vocacional |

### Flujo de Uso - Estudiante

1. **Registrarse/Iniciar Sesión**
2. **Registrar Notas Académicas** - Ingresa tus calificaciones por materia
3. **Completar Test de Habilidades** - Responde preguntas sobre tus capacidades
4. **Evaluar Afinidades** - Indica tu nivel de interés en diferentes carreras
5. **Calcular Resultados** - El sistema analiza todos los datos y genera recomendaciones
6. **Ver TOP 3 Carreras** - Revisa las carreras más compatibles contigo

### Flujo de Uso - Administrador

1. **Gestionar Carreras** - CRUD de carreras disponibles
2. **Configurar Materias** - Asignar materias a carreras con pesos
3. **Definir Habilidades** - Crear habilidades y relacionarlas con carreras
4. **Ajustar Pesos del Algoritmo** - Modificar la importancia de cada componente
5. **Ver Reportes** - Analizar estadísticas del sistema

---

## 📊 Modelo de Datos

### Entidades Principales

#### 👤 User
- Información de usuarios (admin/estudiante)
- Roles: ADMIN, USER
- Relación 1:N con Nota, Afinidad, RespuestaHabilidad, Resultado

#### 🎓 Carrera
- Información de carreras profesionales
- Código único y nombre
- Relaciones con materias, habilidades y afinidades

#### 📚 Materia
- Materias académicas del sistema
- Relación N:M con Carrera (tabla CarreraMateria con peso)

#### 💪 Habilidad
- Habilidades evaluables
- Relación N:M con Carrera (tabla CarreraHabilidad con peso)

#### 📝 Nota
- Calificaciones del estudiante por materia
- Relación: Estudiante -> Materia

#### 🎯 Afinidad
- Nivel de interés del estudiante en una carrera (1-5)
- Relación: Estudiante -> Carrera

#### ❓ Pregunta y 💬 RespuestaHabilidad
- Preguntas del test de habilidades
- Respuestas del estudiante (1-5)

#### 🏆 Resultado
- Resultado del cálculo vocacional
- Puntajes: académico, habilidades, afinidad, final
- Ranking por estudiante

### Fórmula de Cálculo

```
Puntaje Final = (Académico × 0.5) + (Habilidades × 0.3) + (Afinidad × 0.2)

Donde:
- Académico: Promedio ponderado de notas en materias de la carrera
- Habilidades: Promedio ponderado de respuestas en habilidades de la carrera
- Afinidad: Nivel de interés declarado (normalizado 0-1)
```

---

## 🌐 Deploy en Render

### URLs de Producción

- **Backend:** https://proyectoweb.onrender.com
- **Frontend:** https://sistema-vocacional-frontend.onrender.com
- **Base de Datos:** PostgreSQL en Render (oregon-postgres)

### Variables de Entorno en Render

```bash
# Backend Service
SPRING_DATASOURCE_URL=jdbc:postgresql://[HOST]:5432/[DATABASE]?sslmode=require
SPRING_DATASOURCE_USERNAME=[USERNAME]
SPRING_DATASOURCE_PASSWORD=[PASSWORD]
PORT=10000
FRONTEND_URL=https://sistema-vocacional-frontend.onrender.com
```

### Configuración de PostgreSQL

- **Plan:** Free
- **Región:** Oregon (US West)
- **SSL:** Requerido
- **Acceso Externo:** Habilitado para desarrollo

---

## 📁 Estructura del Proyecto

```
ProyectWeb/
├── proyectoingweb/                    # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/proyectoingweb/proyectoingweb/
│   │   │   │   ├── controller/        # API REST Controllers
│   │   │   │   ├── service/           # Lógica de negocio
│   │   │   │   ├── repository/        # Repositorios JPA
│   │   │   │   ├── entity/            # Entidades JPA
│   │   │   │   ├── factory/           # Factory Pattern
│   │   │   │   ├── strategy/          # Strategy Pattern
│   │   │   │   └── config/            # Configuración
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── angular-frontend/              # Frontend Angular
│   │   ├── src/
│   │   │   ├── app/
│   │   │   │   ├── components/        # Componentes UI
│   │   │   │   ├── services/          # Servicios API
│   │   │   │   ├── guards/            # Guards de rutas
│   │   │   │   └── models/            # Modelos TypeScript
│   │   │   └── environments/
│   │   └── package.json
│   ├── pom.xml                        # Maven dependencies
│   ├── SOLID_Y_PATRONES.md           # Documentación técnica
│   └── README.md                      # Este archivo
```

---

## 🧪 Testing

### Backend Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Test con cobertura
./mvnw test jacoco:report
```

### Frontend Tests

```bash
cd angular-frontend

# Tests unitarios
npm test

# Tests e2e
npm run e2e
```

---

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📝 Documentación Adicional

- [SOLID_Y_PATRONES.md](SOLID_Y_PATRONES.md) - Documentación detallada de principios SOLID y patrones implementados
- [GUIA_EVALUACION.md](GUIA_EVALUACION.md) - Guía para evaluación del proyecto
- [RESUMEN_IMPLEMENTACION.md](RESUMEN_IMPLEMENTACION.md) - Resumen de implementación técnica

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

## 👨‍💻 Autores

- **Danny Patrick** - [Ptrickill](https://github.com/Ptrickill)
- **José Carvajal** - [JoseCarvajal1264](https://github.com/JoseCarvajal1264)

---

## 🙏 Agradecimientos

- Proyecto desarrollado como parte del curso de Ingeniería Web
- Agradecimientos a todos los que contribuyeron con ideas y feedback

---

## 📞 Contacto

Para preguntas, sugerencias o reportar bugs:
- **GitHub Issues:** https://github.com/Ptrickill/ProyectWeb/issues
- **Repositorio:** https://github.com/Ptrickill/ProyectWeb

---

⭐ **Si este proyecto te fue útil, considera darle una estrella en GitHub!** ⭐
