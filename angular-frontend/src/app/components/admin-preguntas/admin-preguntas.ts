import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface Habilidad {
  id: number;
  nombre: string;
}

interface Pregunta {
  id?: number;
  habilidadId: number;
  habilidadNombre?: string;
  pregunta: string;
  tipoPregunta: string;
}

@Component({
  selector: 'app-admin-preguntas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-preguntas.html',
  styleUrls: ['./admin-preguntas.scss']
})
export class AdminPreguntas implements OnInit {
  preguntas: Pregunta[] = [];
  habilidades: Habilidad[] = [];
  nuevaPregunta: Pregunta = { habilidadId: 0, pregunta: '', tipoPregunta: '' };
  
  mostrarFormulario: boolean = false;
  modoEdicion: boolean = false;
  cargando: boolean = false;
  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';

  tiposPregunta: string[] = ['PREFERENCIA', 'AUTOEVALUACION'];

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarHabilidades();
    this.cargarPreguntas();
  }

  cargarHabilidades() {
    this.http.get<any>('http://localhost:8080/api/admin/habilidades')
      .subscribe({
        next: (response) => {
          if (response.success && response.habilidades) {
            this.habilidades = response.habilidades;
          }
        },
        error: (err) => {
          console.error('Error cargando habilidades:', err);
        }
      });
  }

  cargarPreguntas() {
    this.cargando = true;
    this.http.get<any>('http://localhost:8080/api/admin/preguntas')
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
          this.error = 'Error al cargar las preguntas';
          this.cargando = false;
        }
      });
  }

  mostrarNuevoFormulario() {
    this.mostrarFormulario = true;
    this.modoEdicion = false;
    this.nuevaPregunta = { habilidadId: 0, pregunta: '', tipoPregunta: '' };
    this.limpiarMensajes();
  }

  editarPregunta(pregunta: Pregunta) {
    this.mostrarFormulario = true;
    this.modoEdicion = true;
    this.nuevaPregunta = { ...pregunta };
    this.limpiarMensajes();
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.nuevaPregunta = { habilidadId: 0, pregunta: '', tipoPregunta: '' };
    this.limpiarMensajes();
  }

  guardarPregunta() {
    if (!this.validarFormulario()) return;

    this.guardando = true;
    this.limpiarMensajes();

    const payload = {
      habilidadId: this.nuevaPregunta.habilidadId,
      texto: this.nuevaPregunta.pregunta,
      tipoPregunta: this.nuevaPregunta.tipoPregunta
    };

    if (this.modoEdicion && this.nuevaPregunta.id) {
      this.http.put<any>(`http://localhost:8080/api/admin/preguntas/${this.nuevaPregunta.id}`, payload)
        .subscribe({
          next: () => {
            this.mensaje = '✅ Pregunta actualizada exitosamente';
            this.cargarPreguntas();
            this.cancelarFormulario();
            this.guardando = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Error al actualizar la pregunta';
            this.guardando = false;
          }
        });
    } else {
      this.http.post<any>('http://localhost:8080/api/admin/preguntas', payload)
        .subscribe({
          next: (response) => {
            if (response.success) {
              this.mensaje = '✅ Pregunta creada exitosamente';
              this.cargarPreguntas();
              this.cancelarFormulario();
            } else {
              this.error = response.message || 'Error al crear la pregunta';
            }
            this.guardando = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Error al crear la pregunta';
            this.guardando = false;
          }
        });
    }
  }

  eliminarPregunta(id: number, pregunta: string) {
    if (!confirm(`¿Estás seguro de eliminar esta pregunta?\n\n"${pregunta}"`)) return;

    this.http.delete<any>(`http://localhost:8080/api/admin/preguntas/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Pregunta eliminada exitosamente';
          this.cargarPreguntas();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al eliminar la pregunta';
        }
      });
  }

  validarFormulario(): boolean {
    if (this.nuevaPregunta.habilidadId === 0) {
      this.error = 'Debes seleccionar una habilidad';
      return false;
    }
    if (!this.nuevaPregunta.pregunta.trim()) {
      this.error = 'La pregunta es obligatoria';
      return false;
    }
    if (!this.nuevaPregunta.tipoPregunta) {
      this.error = 'El tipo de pregunta es obligatorio';
      return false;
    }
    return true;
  }

  getNombreHabilidad(id: number): string {
    return this.habilidades.find(h => h.id === id)?.nombre || 'Desconocida';
  }

  getTipoLabel(tipo: string): string {
    const labels: { [key: string]: string } = {
      'PREFERENCIA': 'Preferencia',
      'AUTOEVALUACION': 'Autoevaluación',
      'GUSTO': 'Preferencia'
    };
    return labels[tipo] || tipo;
  }

  limpiarMensajes() {
    this.mensaje = '';
    this.error = '';
  }

  volverAlDashboard() {
    this.router.navigate(['/admin/dashboard']);
  }
}
