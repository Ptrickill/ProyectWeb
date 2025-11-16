import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface Materia {
  id?: number;
  codigo: string;
  nombre: string;
  area: string;
}

@Component({
  selector: 'app-admin-materias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-materias.html',
  styleUrls: ['./admin-materias.scss']
})
export class AdminMaterias implements OnInit {
  materias: Materia[] = [];
  nuevaMateria: Materia = { codigo: '', nombre: '', area: '' };
  
  mostrarFormulario: boolean = false;
  modoEdicion: boolean = false;
  cargando: boolean = false;
  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';

  areas: string[] = ['Ciencias', 'Humanidades', 'Ingeniería', 'Salud', 'Artes', 'Sociales'];

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarMaterias();
  }

  cargarMaterias() {
    this.cargando = true;
    this.http.get<any>('http://localhost:8080/api/admin/materias')
      .subscribe({
        next: (response) => {
          if (response.success && response.materias) {
            this.materias = response.materias;
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando materias:', err);
          this.error = 'Error al cargar las materias';
          this.cargando = false;
        }
      });
  }

  mostrarNuevoFormulario() {
    this.mostrarFormulario = true;
    this.modoEdicion = false;
    this.nuevaMateria = { codigo: '', nombre: '', area: '' };
    this.limpiarMensajes();
  }

  editarMateria(materia: Materia) {
    this.mostrarFormulario = true;
    this.modoEdicion = true;
    this.nuevaMateria = { ...materia };
    this.limpiarMensajes();
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.nuevaMateria = { codigo: '', nombre: '', area: '' };
    this.limpiarMensajes();
  }

  guardarMateria() {
    if (!this.validarFormulario()) return;

    this.guardando = true;
    this.limpiarMensajes();

    if (this.modoEdicion && this.nuevaMateria.id) {
      this.http.put<any>(`http://localhost:8080/api/admin/materias/${this.nuevaMateria.id}`, this.nuevaMateria)
        .subscribe({
          next: (response) => {
            this.mensaje = '✅ Materia actualizada exitosamente';
            this.cargarMaterias();
            this.cancelarFormulario();
            this.guardando = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Error al actualizar la materia';
            this.guardando = false;
          }
        });
    } else {
      this.http.post<any>('http://localhost:8080/api/admin/materias', this.nuevaMateria)
        .subscribe({
          next: (response) => {
            if (response.success) {
              this.mensaje = '✅ Materia creada exitosamente';
              this.cargarMaterias();
              this.cancelarFormulario();
            } else {
              this.error = response.message || 'Error al crear la materia';
            }
            this.guardando = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Error al crear la materia';
            this.guardando = false;
          }
        });
    }
  }

  eliminarMateria(id: number, nombre: string) {
    if (!confirm(`¿Estás seguro de eliminar la materia "${nombre}"?`)) return;

    this.http.delete<any>(`http://localhost:8080/api/admin/materias/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Materia eliminada exitosamente';
          this.cargarMaterias();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al eliminar la materia';
        }
      });
  }

  validarFormulario(): boolean {
    if (!this.nuevaMateria.codigo.trim()) {
      this.error = 'El código es obligatorio';
      return false;
    }
    if (!this.nuevaMateria.nombre.trim()) {
      this.error = 'El nombre es obligatorio';
      return false;
    }
    if (!this.nuevaMateria.area.trim()) {
      this.error = 'El área es obligatoria';
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
