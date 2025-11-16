import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

interface Estudiante {
  id?: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  direccion: string;
  fechaNacimiento: string;
}

@Component({
  selector: 'app-estudiante-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estudiante-perfil.html',
  styleUrls: ['./estudiante-perfil.scss']
})
export class EstudiantePerfil implements OnInit {
  estudiante: Estudiante = {
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    direccion: '',
    fechaNacimiento: ''
  };

  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';
  esNuevo: boolean = true;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const usuario = this.authService.obtenerUsuario();
    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }

    // Cargar datos del estudiante si existen
    this.http.get<any>(`http://localhost:8080/api/estudiantes/usuario/${usuario.id}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.estudiante = response.data;
            this.esNuevo = false;
          }
        },
        error: (err) => {
          console.log('No existe perfil aún, creando nuevo');
          this.esNuevo = true;
        }
      });
  }

  guardarPerfil() {
    this.guardando = true;
    this.mensaje = '';
    this.error = '';

    const usuario = this.authService.obtenerUsuario();
    if (!usuario) return;

    const url = this.esNuevo 
      ? `http://localhost:8080/api/estudiantes/usuario/${usuario.id}`
      : `http://localhost:8080/api/estudiantes/${this.estudiante.id}`;

    const metodo = this.esNuevo ? 'post' : 'put';

    this.http.request<any>(metodo, url, { body: this.estudiante })
      .subscribe({
        next: (response) => {
          this.mensaje = '✅ Perfil guardado exitosamente';
          if (response.data) {
            this.estudiante = response.data;
            this.esNuevo = false;
          }
          this.guardando = false;
        },
        error: (err) => {
          console.error('Error guardando perfil:', err);
          this.error = err.error?.message || 'Error al guardar el perfil';
          this.guardando = false;
        }
      });
  }

  volverAlDashboard() {
    this.router.navigate(['/estudiante/dashboard']);
  }

  limpiarMensajes() {
    this.mensaje = '';
    this.error = '';
  }
}
