import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { UserManagement } from './components/user-management/user-management';
import { AuthGuard } from './guards/auth.guard';
import { EstudiantePerfil } from './components/estudiante-perfil/estudiante-perfil';
import { EstudianteResultados } from './components/estudiante-resultados/estudiante-resultados';
import { EstudianteNotas } from './components/estudiante-notas/estudiante-notas';
import { EstudianteTest } from './components/estudiante-test/estudiante-test';
import { EstudianteIntereses } from './components/estudiante-intereses/estudiante-intereses';
import { AdminCarreras } from './components/admin-carreras/admin-carreras';
import { AdminMaterias } from './components/admin-materias/admin-materias';
import { AdminHabilidades } from './components/admin-habilidades/admin-habilidades';
import { AdminPreguntas } from './components/admin-preguntas/admin-preguntas';
import { AdminPesos } from './components/admin-pesos/admin-pesos';

export const routes: Routes = [
  // Ruta por defecto
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  
  // Login (público)
  {
    path: 'login',
    component: Login,
    title: 'Iniciar Sesión - Sistema Vocacional'
  },
  
  // ========== RUTAS DE ADMIN ==========
  {
    path: 'admin/dashboard',
    component: Dashboard,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Panel Admin - Sistema Vocacional'
  },
  {
    path: 'admin/usuarios',
    component: UserManagement,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Gestión de Usuarios'
  },
  {
    path: 'admin/carreras',
    component: AdminCarreras,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Gestión de Carreras'
  },
  {
    path: 'admin/materias',
    component: AdminMaterias,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Gestión de Materias'
  },
  {
    path: 'admin/habilidades',
    component: AdminHabilidades,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Gestión de Habilidades'
  },
  {
    path: 'admin/preguntas',
    component: AdminPreguntas,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Gestión de Preguntas'
  },
  {
    path: 'admin/pesos',
    component: AdminPesos,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    title: 'Configuración de Pesos'
  },
  
  // ========== RUTAS DE ESTUDIANTE ==========
  {
    path: 'estudiante/dashboard',
    component: Dashboard,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
    title: 'Mi Dashboard - Orientación Vocacional'
  },
  {
    path: 'estudiante/perfil',
    component: EstudiantePerfil,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
    title: 'Mi Perfil'
  },
  {
    path: 'estudiante/notas',
    component: EstudianteNotas,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
    title: 'Mis Notas Académicas'
  },
  {
    path: 'estudiante/test',
    component: EstudianteTest,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
    title: 'Test de Habilidades'
  },
  {
    path: 'estudiante/intereses',
    component: EstudianteIntereses,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
    title: 'Mis Intereses Vocacionales'
  },
  {
    path: 'estudiante/resultados',
    component: EstudianteResultados,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
    title: 'Mis Resultados Vocacionales'
  },
  
  // Ruta de acceso denegado
  {
    path: 'acceso-denegado',
    component: Login,
    title: 'Acceso Denegado'
  },
  
  // Cualquier otra ruta redirige a login
  {
    path: '**',
    redirectTo: '/login'
  }
];
