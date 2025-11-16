import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // Verificar si está logueado
    if (!this.authService.estaLogueado()) {
      console.log('⛔ No autenticado - Redirigiendo a login');
      this.router.navigate(['/login']);
      return false;
    }

    // Verificar roles si la ruta los requiere
    const rolesRequeridos = route.data['roles'] as string[];
    
    if (rolesRequeridos && rolesRequeridos.length > 0) {
      const usuario = this.authService.obtenerUsuario();
      const tienePermiso = rolesRequeridos.includes(usuario?.role || '');
      
      if (!tienePermiso) {
        console.log('⛔ Sin permisos - Rol requerido:', rolesRequeridos);
        this.router.navigate(['/acceso-denegado']);
        return false;
      }
    }

    return true;
  }
}