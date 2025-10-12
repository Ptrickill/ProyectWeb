import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { UserManagement } from './components/user-management/user-management';
import { AuthGuard } from './guards/auth.guard';


export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  
  {
    path: 'login',
    component: Login,
    title: 'Iniciar Sesión'
  },
  
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [AuthGuard],
    title: 'Dashboard - Sistema de Usuarios'
  },

  {
    path: 'usuarios',
    component: UserManagement,
    canActivate: [AuthGuard],
    title: 'Gestión de Usuarios'
  },
  
  {
    path: '**',
    redirectTo: '/login'
  }
];
