import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

// Dashboard SUPER SIMPLE - Fácil de entender
@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  usuario: any = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Verificar si está logueado
    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
      return;
    }

    // Obtener usuario actual
    this.usuario = this.authService.obtenerUsuario();
    console.log('Usuario en dashboard:', this.usuario);
  }

  // Método simple para ir a lista de usuarios
  irAUsuarios(): void {
    this.router.navigate(['/usuarios']);
  }

  // Método simple para logout
  cerrarSesion(): void {
    if (confirm('¿Seguro que quieres cerrar sesión?')) {
      this.authService.logout();
    }
  }
}
