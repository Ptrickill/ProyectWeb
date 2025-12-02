import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

interface Carrera {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
}

interface Resultado {
  carrera: Carrera;
  puntajeAcademico: number;
  puntajeHabilidades: number;
  puntajeAfinidad: number;
  puntajeTotal: number;
  mensaje: string;
}

@Component({
  selector: 'app-estudiante-resultados',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './estudiante-resultados.html',
  styleUrls: ['./estudiante-resultados.scss']
})
export class EstudianteResultados implements OnInit {
  resultados: Resultado[] = [];
  cargando: boolean = false;
  error: string = '';
  estudianteId: number = 0;
  
  // Información de progreso
  tieneNotas: boolean = false;
  tieneTestHabilidades: boolean = false;
  tieneIntereses: boolean = false;

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
    
    // Obtener el ID del estudiante desde el perfil
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiantes/usuario/${usuario.id}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.estudianteId = response.data.id;
            this.verificarProgresoYCargarResultados();
          } else {
            this.error = 'Debes crear tu perfil primero';
            setTimeout(() => this.router.navigate(['/estudiante/perfil']), 2000);
          }
        },
        error: (err) => {
          this.error = 'Debes crear tu perfil primero';
          setTimeout(() => this.router.navigate(['/estudiante/perfil']), 2000);
        }
      });
  }

  verificarProgresoYCargarResultados() {
    this.cargando = true;
    this.error = '';

    // Verificar progreso del estudiante
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiantes/${this.estudianteId}/progreso`)
      .subscribe({
        next: (response) => {
          this.tieneNotas = response.tieneNotas || false;
          this.tieneTestHabilidades = response.tieneTestHabilidades || false;
          this.tieneIntereses = response.tieneIntereses || false;

          // Si tiene todo completo, calcular resultados
          if (this.tieneNotas && this.tieneTestHabilidades && this.tieneIntereses) {
            this.calcularResultados();
          } else {
            this.cargando = false;
          }
        },
        error: (err) => {
          console.error('Error verificando progreso:', err);
          this.error = 'No se pudo verificar tu progreso';
          this.cargando = false;
        }
      });
  }

  calcularResultados() {
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiante/resultados/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.resultados = response.data.slice(0, 3); // Top 3
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error calculando resultados:', err);
          this.error = 'Error al calcular tus resultados vocacionales';
          this.cargando = false;
        }
      });
  }

  obtenerColorPorPosicion(index: number): string {
    if (index === 0) return 'gold';
    if (index === 1) return 'silver';
    if (index === 2) return 'bronze';
    return 'normal';
  }

  obtenerMedalla(index: number): string {
    if (index === 0) return '🥇';
    if (index === 1) return '🥈';
    if (index === 2) return '🥉';
    return '';
  }

  volverAlDashboard() {
    this.router.navigate(['/estudiante/dashboard']);
  }

  irANotas() {
    this.router.navigate(['/estudiante/notas']);
  }

  irATest() {
    this.router.navigate(['/estudiante/test']);
  }

  irAIntereses() {
    this.router.navigate(['/estudiante/intereses']);
  }

  get progresoCompleto(): boolean {
    return this.tieneNotas && this.tieneTestHabilidades && this.tieneIntereses;
  }
}

