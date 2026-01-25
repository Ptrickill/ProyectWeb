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
  puntajeFinal: number;
  mensaje?: string;
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
    
    // Usar directamente el ID del usuario como estudianteId
    this.estudianteId = usuario.id;
    this.verificarProgresoYCargarResultados();
  }

  verificarProgresoYCargarResultados() {
    this.cargando = true;
    this.error = '';

    // Verificar notas - Ruta correcta
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiante/notas/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          console.log('Notas response:', response);
          this.tieneNotas = response.success && response.notas && response.notas.length > 0;
          this.verificarHabilidades();
        },
        error: (err) => {
          console.error('Error verificando notas:', err);
          this.tieneNotas = false;
          this.verificarHabilidades();
        }
      });
  }

  verificarHabilidades() {
    // Verificar respuestas de habilidades - Ruta correcta
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiante/test/respuestas/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          console.log('Habilidades response:', response);
          this.tieneTestHabilidades = response.success && response.respuestas && response.respuestas.length > 0;
          this.verificarIntereses();
        },
        error: (err) => {
          console.error('Error verificando habilidades:', err);
          this.tieneTestHabilidades = false;
          this.verificarIntereses();
        }
      });
  }

  verificarIntereses() {
    // Verificar afinidades - Ruta correcta (usa "data" en lugar de "afinidades")
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiante/afinidades/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          console.log('Afinidades response:', response);
          this.tieneIntereses = response.success && response.data && response.data.length > 0;
          
          // Si tiene todo completo, calcular resultados automáticamente
          if (this.tieneNotas && this.tieneTestHabilidades && this.tieneIntereses) {
            this.calcularResultados();
          } else {
            this.cargando = false;
          }
        },
        error: (err) => {
          console.error('Error verificando intereses:', err);
          this.tieneIntereses = false;
          this.cargando = false;
        }
      });
  }

  calcularResultados() {
    // Primero ejecutar el cálculo
    this.http.post<any>(`https://proyectweb-rech.onrender.com/api/estudiante/resultados/calcular/${this.estudianteId}`, {})
      .subscribe({
        next: (response) => {
          console.log('Resultados calculados:', response);
          if (response.success && response.resultados && response.resultados.length > 0) {
            // Ordenar por puntaje final descendente y tomar top 3
            this.resultados = response.resultados
              .sort((a: any, b: any) => b.puntajeFinal - a.puntajeFinal)
              .slice(0, 3)
              .map((r: any) => ({
                ...r,
                mensaje: this.obtenerMensajePorPosicion(r.puntajeFinal)
              }));
          } else {
            this.error = 'No se pudieron calcular resultados. Verifica tus datos.';
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error calculando resultados:', err);
          this.error = 'Error al calcular tus resultados vocacionales: ' + (err.error?.message || err.message);
          this.cargando = false;
        }
      });
  }

  calcularResultadosManual() {
    this.cargando = true;
    this.error = '';
    // Forzar el cálculo sin verificar progreso
    this.calcularResultados();
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

  obtenerMensajePorPosicion(puntaje: number): string {
    const porcentaje = puntaje * 100;
    if (porcentaje >= 80) return '¡Excelente compatibilidad! Esta carrera se ajusta muy bien a tu perfil.';
    if (porcentaje >= 60) return '¡Buena opción! Tienes bastante afinidad con esta carrera.';
    if (porcentaje >= 40) return 'Opción viable. Considera explorar más sobre esta carrera.';
    return 'Compatibilidad moderada. Explora todas las opciones disponibles.';
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

