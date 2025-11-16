import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

interface Habilidad {
  id: number;
  nombre: string;
  descripcion: string;
}

interface Pregunta {
  id: number;
  habilidadId: number;
  habilidadNombre?: string;
  pregunta: string;
  tipoPregunta: string; // GUSTO o AUTOEVALUACION
}

interface Respuesta {
  id?: number;
  estudianteId: number;
  preguntaId: number;
  respuesta: number; // 1-5
}

@Component({
  selector: 'app-estudiante-test',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estudiante-test.html',
  styleUrls: ['./estudiante-test.scss']
})
export class EstudianteTest implements OnInit {
  habilidades: Habilidad[] = [];
  preguntas: Pregunta[] = [];
  respuestas: Map<number, number> = new Map(); // preguntaId -> respuesta
  respuestasGuardadas: Respuesta[] = [];
  
  cargando: boolean = false;
  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';
  estudianteId: number = 0;
  testCompletado: boolean = false;

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

    this.estudianteId = usuario.id;
    this.cargarHabilidades();
    this.cargarPreguntas();
    this.cargarRespuestasGuardadas();
  }

  cargarHabilidades() {
    this.http.get<any>('http://localhost:8080/api/public/habilidades')
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.habilidades = response.data;
          }
        },
        error: (err) => {
          console.error('Error cargando habilidades:', err);
        }
      });
  }

  cargarPreguntas() {
    this.cargando = true;
    this.http.get<any>('http://localhost:8080/api/estudiante/test/preguntas')
      .subscribe({
        next: (response) => {
          if (response.success && response.preguntas) {
            this.preguntas = response.preguntas.map((p: any) => ({
              id: p.id,
              habilidadId: p.habilidad.id,
              habilidadNombre: p.habilidad.nombre,
              pregunta: p.texto,
              tipoPregunta: p.tipoPregunta
            }));
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando preguntas:', err);
          this.error = 'No se pudieron cargar las preguntas';
          this.cargando = false;
        }
      });
  }

  cargarRespuestasGuardadas() {
    this.http.get<any>(`http://localhost:8080/api/estudiante/test/respuestas/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.respuestas) {
            this.respuestasGuardadas = response.respuestas;
            // Cargar respuestas en el Map
            response.respuestas.forEach((r: any) => {
              this.respuestas.set(r.pregunta.id, r.puntaje);
            });
            this.verificarTestCompleto();
          }
        },
        error: (err) => {
          console.log('No hay respuestas guardadas aún');
        }
      });
  }

  seleccionarRespuesta(preguntaId: number, valor: number) {
    this.respuestas.set(preguntaId, valor);
    this.verificarTestCompleto();
  }

  getRespuesta(preguntaId: number): number {
    return this.respuestas.get(preguntaId) || 0;
  }

  verificarTestCompleto() {
    this.testCompletado = this.preguntas.length > 0 && 
                          this.preguntas.every(p => this.respuestas.has(p.id));
  }

  guardarTest() {
    if (!this.testCompletado) {
      this.error = 'Por favor responde todas las preguntas antes de guardar';
      return;
    }

    this.guardando = true;
    this.error = '';
    this.mensaje = '';

    const respuestasArray = Array.from(this.respuestas.entries()).map(([preguntaId, puntaje]) => ({
      estudianteId: this.estudianteId,
      preguntaId: preguntaId,
      puntaje: puntaje
    }));

    // Guardar cada respuesta
    let guardadas = 0;
    let errores = 0;

    respuestasArray.forEach(respuesta => {
      // Verificar si ya existe
      const yaExiste = this.respuestasGuardadas.find(r => r.preguntaId === respuesta.preguntaId);
      
      if (yaExiste) {
        // Actualizar (nota: backend no tiene PUT, solo POST)
        this.http.post<any>('http://localhost:8080/api/estudiante/test/respuestas', respuesta)
          .subscribe({
            next: () => {
              guardadas++;
              if (guardadas + errores === respuestasArray.length) {
                this.finalizarGuardado(errores);
              }
            },
            error: () => {
              errores++;
              if (guardadas + errores === respuestasArray.length) {
                this.finalizarGuardado(errores);
              }
            }
          });
      } else {
        // Crear
        this.http.post<any>('http://localhost:8080/api/estudiante/test/respuestas', respuesta)
          .subscribe({
            next: () => {
              guardadas++;
              if (guardadas + errores === respuestasArray.length) {
                this.finalizarGuardado(errores);
              }
            },
            error: () => {
              errores++;
              if (guardadas + errores === respuestasArray.length) {
                this.finalizarGuardado(errores);
              }
            }
          });
      }
    });
  }

  finalizarGuardado(errores: number) {
    this.guardando = false;
    if (errores === 0) {
      this.mensaje = '✅ Test guardado exitosamente. Representa el 30% de tu puntaje vocacional.';
      this.cargarRespuestasGuardadas();
    } else {
      this.error = `Se guardaron algunas respuestas, pero hubo ${errores} error(es)`;
    }
  }

  getPreguntasPorHabilidad(habilidadId: number): Pregunta[] {
    return this.preguntas.filter(p => p.habilidadId === habilidadId);
  }

  getIconoTipoPregunta(tipo: string): string {
    return tipo === 'GUSTO' ? '❤️' : '⭐';
  }

  volverAlDashboard() {
    this.router.navigate(['/estudiante/dashboard']);
  }

  get progresoPercentaje(): number {
    if (this.preguntas.length === 0) return 0;
    const respondidas = Array.from(this.respuestas.values()).filter(v => v > 0).length;
    return (respondidas / this.preguntas.length) * 100;
  }
}
