import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

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

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // Función iniciar sesion
  iniciarSesion(): void {
    this.mensajeError = '';
    
    // Verificacion de que los campos no estén vacíos
    if (!this.usuario || !this.password) {
      this.mensajeError = 'Por favor ingresa usuario y contraseña';
      return;
    }
    
    this.cargando = true;
    
    const loginData = {
      username: this.usuario,
      password: this.password
    };
    
    // Llamar al servicio de autenticación
    this.authService.login(loginData).subscribe({
      next: (response) => {
        console.log('Respuesta del servidor:', response);
        
        if (response.success && response.usuario) {
          this.authService.guardarUsuario(response.usuario);
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
