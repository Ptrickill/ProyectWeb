# Railway.app Deploy Instructions

## Pasos para Deploy:

### 1. Ir a Railway
- Visita: https://railway.app
- Click "Start a New Project"
- Selecciona "Deploy from GitHub repo"

### 2. Conectar GitHub
- Autoriza Railway a acceder a tu GitHub
- Selecciona el repositorio: Ptrickill/ProyectWeb
- Railway detectará automáticamente que es un proyecto Spring Boot

### 3. Agregar Base de Datos PostgreSQL
- En el dashboard del proyecto, click "New"
- Selecciona "Database" → "Add PostgreSQL"
- Railway creará automáticamente la base de datos

### 4. Configurar Variables de Entorno
Railway auto-detecta las siguientes variables de la base de datos:
- `DATABASE_URL` (automático)
- `PGDATABASE` (automático)
- `PGHOST` (automático)
- `PGPASSWORD` (automático)
- `PGPORT` (automático)
- `PGUSER` (automático)

**DEBES AGREGAR MANUALMENTE:**
- Click en tu servicio Spring Boot
- Ve a "Variables"
- Agrega:
  ```
  DB_USERNAME = ${{Postgres.PGUSER}}
  DB_PASSWORD = ${{Postgres.PGPASSWORD}}
  DATABASE_URL = postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
  PORT = 8080
  FRONTEND_URL = https://tu-frontend.netlify.app
  ```

### 5. Deploy
- Railway hará deploy automáticamente
- Espera 3-5 minutos para el primer deploy
- Obtendrás una URL: `https://tu-proyecto.up.railway.app`

### 6. Configurar Dominio Público
- En tu servicio, ve a "Settings"
- Scroll a "Networking"
- Click "Generate Domain"
- Copia la URL generada

### 7. Actualizar Frontend
- Usa la URL de Railway en `environment.prod.ts`:
  ```typescript
  apiUrl: 'https://tu-proyecto.up.railway.app/api'
  ```

## Deploy Automático
Cada vez que hagas `git push origin master`, Railway re-deployará automáticamente.

## Logs
- Click en tu servicio
- Ve a "Deployments"
- Click en el deployment activo para ver logs en tiempo real

## Costo
- $5 USD de crédito gratis mensual
- Suficiente para ~500 horas de uptime
- Para proyecto educativo, es gratis efectivamente
