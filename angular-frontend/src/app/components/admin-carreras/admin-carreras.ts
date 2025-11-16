import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface Carrera {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion: string;
}

@Component({
  selector: 'app-admin-carreras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-carreras.html',
  styleUrls: ['./admin-carreras.scss']
})
export class AdminCarreras implements OnInit {
  carreras: Carrera[] = [];
  carreraEditando: Carrera | null = null;
  nuevaCarrera: Carrera = { codigo: '', nombre: '', descripcion: '' };
  
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
    this.cargarCarreras();
  }

  cargarCarreras() {
    this.cargando = true;
    this.http.get<any>('http://localhost:8080/api/admin/carreras')
      .subscribe({
        next: (response) => {
          if (response.success && response.carreras) {
            this.carreras = response.carreras;
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando carreras:', err);
          this.error = 'Error al cargar las carreras';
          this.cargando = false;
        }
      });
  }

  mostrarNuevoFormulario() {
    this.mostrarFormulario = true;
    this.modoEdicion = false;
    this.nuevaCarrera = { codigo: '', nombre: '', descripcion: '' };
    this.limpiarMensajes();
  }

  editarCarrera(carrera: Carrera) {
    this.mostrarFormulario = true;
    this.modoEdicion = true;
    this.nuevaCarrera = { ...carrera };
    this.carreraEditando = carrera;
    this.limpiarMensajes();
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.nuevaCarrera = { codigo: '', nombre: '', descripcion: '' };
    this.carreraEditando = null;
    this.limpiarMensajes();
  }

  guardarCarrera() {
    if (!this.validarFormulario()) return;

    this.guardando = true;
    this.limpiarMensajes();

    if (this.modoEdicion && this.nuevaCarrera.id) {
      // Actualizar
      this.http.put<any>(`http://localhost:8080/api/admin/carreras/${this.nuevaCarrera.id}`, this.nuevaCarrera)
        .subscribe({
          next: (response) => {
            this.mensaje = '✅ Carrera actualizada exitosamente';
            this.cargarCarreras();
            this.cancelarFormulario();
            this.guardando = false;
          },
          error: (err) => {
            console.error('Error actualizando carrera:', err);
            this.error = err.error?.message || 'Error al actualizar la carrera';
            this.guardando = false;
          }
        });
    } else {
      // Crear
      this.http.post<any>('http://localhost:8080/api/admin/carreras', this.nuevaCarrera)
        .subscribe({
          next: (response) => {
            if (response.success) {
              this.mensaje = '✅ Carrera creada exitosamente';
              this.cargarCarreras();
              this.cancelarFormulario();
            } else {
              this.error = response.message || 'Error al crear la carrera';
            }
            this.guardando = false;
          },
          error: (err) => {
            console.error('Error creando carrera:', err);
            this.error = err.error?.message || 'Error al crear la carrera';
            this.guardando = false;
          }
        });
    }
  }

  eliminarCarrera(id: number, nombre: string) {
    if (!confirm(`¿Estás seguro de eliminar la carrera "${nombre}"?\n\nEsta acción también eliminará todos los pesos asociados.`)) {
      return;
    }

    this.http.delete<any>(`http://localhost:8080/api/admin/carreras/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Carrera eliminada exitosamente';
          this.cargarCarreras();
        },
        error: (err) => {
          console.error('Error eliminando carrera:', err);
          this.error = err.error?.message || 'Error al eliminar la carrera';
        }
      });
  }

  validarFormulario(): boolean {
    if (!this.nuevaCarrera.codigo.trim()) {
      this.error = 'El código es obligatorio';
      return false;
    }
    if (!this.nuevaCarrera.nombre.trim()) {
      this.error = 'El nombre es obligatorio';
      return false;
    }
    if (!this.nuevaCarrera.descripcion.trim()) {
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
