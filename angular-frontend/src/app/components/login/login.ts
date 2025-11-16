import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  usuario: string = '';
  password: string = '';
  cargando: boolean = false;
  mensajeError: string = '';
  mostrarPassword: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    // Si ya está logueado, redirigir al dashboard
    if (this.authService.estaLogueado()) {
      this.redirigirSegunRole();
    }
  }

  // Iniciar sesión
  iniciarSesion(): void {
    this.mensajeError = '';
    
    // Validar campos
    if (!this.usuario || !this.password) {
      this.mensajeError = 'Por favor ingresa usuario y contraseña';
      return;
    }
    
    if (this.password.length < 6) {
      this.mensajeError = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }
    
    this.cargando = true;
    
    const loginData: LoginRequest = {
      username: this.usuario,
      password: this.password
    };
    
    // Llamar al servicio de autenticación
    this.authService.login(loginData).subscribe({
      next: (response) => {
        console.log('✅ Login exitoso:', response);
        
        if (response.success && response.usuario) {
          // El servicio ya guardó el usuario, solo redirigir
          this.redirigirSegunRole();
        } else {
          this.mensajeError = response.message || 'Usuario o contraseña incorrectos';
          this.cargando = false;
        }
      },
      error: (error) => {
        console.error('❌ Error de login:', error);
        this.mensajeError = 'Error al conectar con el servidor. Verifica que el backend esté corriendo.';
        this.cargando = false;
      }
    });
  }

  // Redirigir según el role del usuario
  private redirigirSegunRole(): void {
    const usuario = this.authService.obtenerUsuario();
    
    if (usuario?.role === 'ADMIN') {
      console.log('🔧 Redirigiendo a panel de administrador');
      this.router.navigate(['/admin/dashboard']);
    } else {
      console.log('👤 Redirigiendo a panel de estudiante');
      this.router.navigate(['/estudiante/dashboard']);
    }
  }

  // Mostrar/ocultar contraseña
  togglePassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  // Ir a registro (opcional - puedes implementarlo después)
  irARegistro(): void {
    this.router.navigate(['/register']);
  }
}
