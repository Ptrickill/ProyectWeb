import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface Habilidad {
  id?: number;
  nombre: string;
  descripcion: string;
}

@Component({
  selector: 'app-admin-habilidades',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-habilidades.html',
  styleUrls: ['./admin-habilidades.scss']
})
export class AdminHabilidades implements OnInit {
  habilidades: Habilidad[] = [];
  nuevaHabilidad: Habilidad = { nombre: '', descripcion: '' };
  
  mostrarFormulario: boolean = false;
  modoEdicion: boolean = false;
  cargando: boolean = false;
  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarHabilidades();
  }

  cargarHabilidades() {
    this.cargando = true;
    this.http.get<any>('http://localhost:8080/api/admin/habilidades')
      .subscribe({
        next: (response) => {
          if (response.success && response.habilidades) {
            this.habilidades = response.habilidades;
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando habilidades:', err);
          this.error = 'Error al cargar las habilidades';
          this.cargando = false;
        }
      });
  }

  mostrarNuevoFormulario() {
    this.mostrarFormulario = true;
    this.modoEdicion = false;
    this.nuevaHabilidad = { nombre: '', descripcion: '' };
    this.limpiarMensajes();
  }

  editarHabilidad(habilidad: Habilidad) {
    this.mostrarFormulario = true;
    this.modoEdicion = true;
    this.nuevaHabilidad = { ...habilidad };
    this.limpiarMensajes();
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.nuevaHabilidad = { nombre: '', descripcion: '' };
    this.limpiarMensajes();
  }

  guardarHabilidad() {
    if (!this.validarFormulario()) return;

    this.guardando = true;
    this.limpiarMensajes();

    if (this.modoEdicion && this.nuevaHabilidad.id) {
      this.http.put<any>(`http://localhost:8080/api/admin/habilidades/${this.nuevaHabilidad.id}`, this.nuevaHabilidad)
        .subscribe({
          next: () => {
            this.mensaje = '✅ Habilidad actualizada exitosamente';
            this.cargarHabilidades();
            this.cancelarFormulario();
            this.guardando = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Error al actualizar la habilidad';
            this.guardando = false;
          }
        });
    } else {
      this.http.post<any>('http://localhost:8080/api/admin/habilidades', this.nuevaHabilidad)
        .subscribe({
          next: (response) => {
            if (response.success) {
              this.mensaje = '✅ Habilidad creada exitosamente';
              this.cargarHabilidades();
              this.cancelarFormulario();
            } else {
              this.error = response.message || 'Error al crear la habilidad';
            }
            this.guardando = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Error al crear la habilidad';
            this.guardando = false;
          }
        });
    }
  }

  eliminarHabilidad(id: number, nombre: string) {
    if (!confirm(`¿Estás seguro de eliminar la habilidad "${nombre}"?\n\nEsto también eliminará las preguntas asociadas.`)) return;

    this.http.delete<any>(`http://localhost:8080/api/admin/habilidades/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Habilidad eliminada exitosamente';
          this.cargarHabilidades();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al eliminar la habilidad';
        }
      });
  }

  validarFormulario(): boolean {
    if (!this.nuevaHabilidad.nombre.trim()) {
      this.error = 'El nombre es obligatorio';
      return false;
    }
    if (!this.nuevaHabilidad.descripcion.trim()) {
      this.error = 'La descripción es obligatoria';
      return false;
    }
    return true;
  }

  limpiarMensajes() {
    this.mensaje = '';
    this.error = '';
  }

  volverAlDashboard() {
    this.router.navigate(['/admin/dashboard']);
  }
}
