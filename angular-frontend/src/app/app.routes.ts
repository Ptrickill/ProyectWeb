import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { UserManagement } from './components/user-management/user-management';
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';

export const routes: Routes = [
  // Ruta por defecto - redirigir al login
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  
  // Ruta de login (pública)
  {
    path: 'login',
    component: Login,
    title: 'Iniciar Sesión'
  },
  
  // Ruta del dashboard (protegida)
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [AuthGuard],
    title: 'Dashboard - Sistema de Usuarios'
  },

  // Ruta de gestión de usuarios (protegida - solo para usuarios logueados)
  {
    path: 'usuarios',
    component: UserManagement,
    canActivate: [AuthGuard],
    title: 'Gestión de Usuarios'
  },
  
  // Ruta comodín para rutas no encontradas
  {
    path: '**',
    redirectTo: '/login'
  }
];
