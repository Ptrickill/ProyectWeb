import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    // Obtener usuario actual usando nuestro método simple
    const usuario = this.authService.obtenerUsuario();
    
    // Verificar si existe y es ADMIN
    if (usuario && usuario.role === 'ADMIN') {
      return true;
    } else {
      // Redirigir a usuarios si no es admin
      this.router.navigate(['/usuarios']);
      return false;
    }
  }
}