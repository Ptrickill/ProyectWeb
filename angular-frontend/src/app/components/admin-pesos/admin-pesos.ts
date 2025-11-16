import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface Carrera {
  id: number;
  codigo: string;
  nombre: string;
}

interface Materia {
  id: number;
  codigo: string;
  nombre: string;
}

interface Habilidad {
  id: number;
  nombre: string;
}

interface PesoMateria {
  id?: number;
  carreraId: number;
  carreraNombre?: string;
  materiaId: number;
  materiaNombre?: string;
  peso: number;
}

interface PesoHabilidad {
  id?: number;
  carreraId: number;
  carreraNombre?: string;
  habilidadId: number;
  habilidadNombre?: string;
  peso: number;
}

interface PesoAfinidad {
  id?: number;
  carreraId: number;
  carreraNombre?: string;
  nivelInteres: number;
  peso: number;
}

@Component({
  selector: 'app-admin-pesos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-pesos.html',
  styleUrls: ['./admin-pesos.scss']
})
export class AdminPesos implements OnInit {
  tabActiva: string = 'materias'; // materias, habilidades, afinidades
  
  carreras: Carrera[] = [];
  materias: Materia[] = [];
  habilidades: Habilidad[] = [];
  
  pesosMaterias: PesoMateria[] = [];
  pesosHabilidades: PesoHabilidad[] = [];
  pesosAfinidades: PesoAfinidad[] = [];
  
  nuevoPesoMateria: PesoMateria = { carreraId: 0, materiaId: 0, peso: 0 };
  nuevoPesoHabilidad: PesoHabilidad = { carreraId: 0, habilidadId: 0, peso: 0 };
  nuevoPesoAfinidad: PesoAfinidad = { carreraId: 0, nivelInteres: 0, peso: 0 };
  
  mostrarFormulario: boolean = false;
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
    this.cargarMaterias();
    this.cargarHabilidades();
    this.cargarPesos();
  }

  cambiarTab(tab: string) {
    this.tabActiva = tab;
    this.cancelarFormulario();
  }

  cargarCarreras() {
    this.http.get<any>('http://localhost:8080/api/admin/carreras')
      .subscribe({
        next: (response) => {
          if (response.success && response.carreras) {
            this.carreras = response.carreras;
          }
        },
        error: (err) => console.error('Error cargando carreras:', err)
      });
  }

  cargarMaterias() {
    this.http.get<any>('http://localhost:8080/api/admin/materias')
      .subscribe({
        next: (response) => {
          if (response.success && response.materias) {
            this.materias = response.materias;
          }
        },
        error: (err) => console.error('Error cargando materias:', err)
      });
  }

  cargarHabilidades() {
    this.http.get<any>('http://localhost:8080/api/admin/habilidades')
      .subscribe({
        next: (response) => {
          if (response.success && response.habilidades) {
            this.habilidades = response.habilidades;
          }
        },
        error: (err) => console.error('Error cargando habilidades:', err)
      });
  }

  cargarPesos() {
    this.cargando = true;
    
    // Cargar pesos de materias
    this.http.get<any>('http://localhost:8080/api/admin/pesos/carrera-materia')
      .subscribe({
        next: (response) => {
          if (response.success && response.pesos) {
            this.pesosMaterias = response.pesos.map((p: any) => ({
              id: p.id,
              carreraId: p.carrera.id,
              carreraNombre: p.carrera.nombre,
              materiaId: p.materia.id,
              materiaNombre: p.materia.nombre,
              peso: p.peso
            }));
          }
        }
      });

    // Cargar pesos de habilidades
    this.http.get<any>('http://localhost:8080/api/admin/pesos/carrera-habilidad')
      .subscribe({
        next: (response) => {
          if (response.success && response.pesos) {
            this.pesosHabilidades = response.pesos.map((p: any) => ({
              id: p.id,
              carreraId: p.carrera.id,
              carreraNombre: p.carrera.nombre,
              habilidadId: p.habilidad.id,
              habilidadNombre: p.habilidad.nombre,
              peso: p.peso
            }));
          }
        }
      });

    // Cargar pesos de afinidades
    this.http.get<any>('http://localhost:8080/api/admin/pesos/carrera-afinidad')
      .subscribe({
        next: (response) => {
          if (response.success && response.pesos) {
            this.pesosAfinidades = response.pesos.map((p: any) => ({
              id: p.id,
              carreraId: p.carrera.id,
              carreraNombre: p.carrera.nombre,
              nivelInteres: p.nivelInteres,
              peso: p.peso
            }));
          }
          this.cargando = false;
        }
      });
  }

  mostrarNuevoFormulario() {
    this.mostrarFormulario = true;
    this.limpiarMensajes();
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.nuevoPesoMateria = { carreraId: 0, materiaId: 0, peso: 0 };
    this.nuevoPesoHabilidad = { carreraId: 0, habilidadId: 0, peso: 0 };
    this.nuevoPesoAfinidad = { carreraId: 0, nivelInteres: 0, peso: 0 };
    this.limpiarMensajes();
  }

  guardarPesoMateria() {
    if (this.nuevoPesoMateria.carreraId === 0 || this.nuevoPesoMateria.materiaId === 0) {
      this.error = 'Selecciona carrera y materia';
      return;
    }

    this.guardando = true;
    this.http.post<any>('http://localhost:8080/api/admin/pesos/carrera-materia', this.nuevoPesoMateria)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.mensaje = '✅ Peso asignado exitosamente';
            this.cargarPesos();
            this.cancelarFormulario();
          } else {
            this.error = response.message;
          }
          this.guardando = false;
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al guardar peso';
          this.guardando = false;
        }
      });
  }

  guardarPesoHabilidad() {
    if (this.nuevoPesoHabilidad.carreraId === 0 || this.nuevoPesoHabilidad.habilidadId === 0) {
      this.error = 'Selecciona carrera y habilidad';
      return;
    }

    this.guardando = true;
    this.http.post<any>('http://localhost:8080/api/admin/pesos/carrera-habilidad', this.nuevoPesoHabilidad)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.mensaje = '✅ Peso asignado exitosamente';
            this.cargarPesos();
            this.cancelarFormulario();
          } else {
            this.error = response.message;
          }
          this.guardando = false;
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al guardar peso';
          this.guardando = false;
        }
      });
  }

  guardarPesoAfinidad() {
    if (this.nuevoPesoAfinidad.carreraId === 0 || this.nuevoPesoAfinidad.nivelInteres === 0) {
      this.error = 'Selecciona carrera y nivel de interés';
      return;
    }

    this.guardando = true;
    this.http.post<any>('http://localhost:8080/api/admin/pesos/carrera-afinidad', this.nuevoPesoAfinidad)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.mensaje = '✅ Peso asignado exitosamente';
            this.cargarPesos();
            this.cancelarFormulario();
          } else {
            this.error = response.message;
          }
          this.guardando = false;
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al guardar peso';
          this.guardando = false;
        }
      });
  }

  eliminarPesoMateria(id: number) {
    if (!confirm('¿Eliminar este peso?')) return;

    this.http.delete<any>(`http://localhost:8080/api/admin/pesos/carrera-materia/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Peso eliminado';
          this.cargarPesos();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al eliminar';
        }
      });
  }

  eliminarPesoHabilidad(id: number) {
    if (!confirm('¿Eliminar este peso?')) return;

    this.http.delete<any>(`http://localhost:8080/api/admin/pesos/carrera-habilidad/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Peso eliminado';
          this.cargarPesos();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al eliminar';
        }
      });
  }

  eliminarPesoAfinidad(id: number) {
    if (!confirm('¿Eliminar este peso?')) return;

    this.http.delete<any>(`http://localhost:8080/api/admin/pesos/carrera-afinidad/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Peso eliminado';
          this.cargarPesos();
        },
        error: (err) => {
          this.error = err.error?.message || 'Error al eliminar';
        }
      });
  }

  limpiarMensajes() {
    this.mensaje = '';
    this.error = '';
  }

  volverAlDashboard() {
    this.router.navigate(['/admin/dashboard']);
  }
}
