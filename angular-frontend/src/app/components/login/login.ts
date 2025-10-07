import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

// Componente de Login SIMPLIFICADO - Más fácil de entender
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  // Variables simples para el formulario
  usuario: string = '';
  password: string = '';
  cargando: boolean = false;
  mensajeError: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // Función simple para iniciar sesión
  iniciarSesion(): void {
    // Limpiar mensaje de error
    this.mensajeError = '';
    
    // Verificar que los campos no estén vacíos
    if (!this.usuario || !this.password) {
      this.mensajeError = 'Por favor ingresa usuario y contraseña';
      return;
    }
    
    // Mostrar que está cargando
    this.cargando = true;
    
    // Crear objeto de login
    const loginData = {
      username: this.usuario,
      password: this.password
    };
    
    // Llamar al servicio de autenticación
    this.authService.login(loginData).subscribe({
      next: (response) => {
        console.log('Respuesta del servidor:', response);
        
        // Si el login fue exitoso
        if (response.success && response.usuario) {
          // Guardar el usuario
          this.authService.guardarUsuario(response.usuario);
          // Ir a la lista de usuarios
          this.router.navigate(['/usuarios']);
        } else {
          this.mensajeError = response.message || 'Usuario o contraseña incorrectos';
        }
        
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error de login:', error);
        this.mensajeError = 'Error al conectar con el servidor';
        this.cargando = false;
      }
    });
  }

  // Función para mostrar/ocultar contraseña
  mostrarPassword: boolean = false;
  
  togglePassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }
}
