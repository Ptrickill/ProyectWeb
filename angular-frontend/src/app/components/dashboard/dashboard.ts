import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, Usuario } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  usuario: Usuario | null = null;
  esAdmin: boolean = false;
  esEstudiante: boolean = false;

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
    this.esAdmin = this.authService.esAdmin();
    this.esEstudiante = this.authService.esEstudiante();
    
    console.log('👤 Usuario en dashboard:', this.usuario);
    console.log('🔧 Es Admin:', this.esAdmin);
    console.log('📚 Es Estudiante:', this.esEstudiante);
  }

  // ========== MÉTODOS DE ADMIN ==========
  
  irAGestionCarreras(): void {
    this.router.navigate(['/admin/carreras']);
  }

  irAGestionMaterias(): void {
    this.router.navigate(['/admin/materias']);
  }

  irAGestionHabilidades(): void {
    this.router.navigate(['/admin/habilidades']);
  }

  irAGestionPreguntas(): void {
    this.router.navigate(['/admin/preguntas']);
  }

  irAGestionPesos(): void {
    this.router.navigate(['/admin/pesos']);
  }

  irAGestionUsuarios(): void {
    this.router.navigate(['/admin/usuarios']);
  }

  // ========== MÉTODOS DE ESTUDIANTE ==========
  
  irAMiPerfil(): void {
    this.router.navigate(['/estudiante/perfil']);
  }

  irAMisNotas(): void {
    this.router.navigate(['/estudiante/notas']);
  }

  irATestHabilidades(): void {
    this.router.navigate(['/estudiante/test']);
  }

  irAMisIntereses(): void {
    this.router.navigate(['/estudiante/intereses']);
  }

  irAMisResultados(): void {
    this.router.navigate(['/estudiante/resultados']);
  }

  // ========== MÉTODOS COMUNES ==========
  
  cerrarSesion(): void {
    if (confirm('¿Seguro que quieres cerrar sesión?')) {
      this.authService.logout();
    }
  }
}
