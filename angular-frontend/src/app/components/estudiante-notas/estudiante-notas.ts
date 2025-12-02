import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

interface Materia {
  id: number;
  codigo: string;
  nombre: string;
  area: string;
}

interface Nota {
  id?: number;
  estudianteId: number;
  materiaId: number;
  materiaNombre?: string;
  calificacion: number;
}

@Component({
  selector: 'app-estudiante-notas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estudiante-notas.html',
  styleUrls: ['./estudiante-notas.scss']
})
export class EstudianteNotas implements OnInit {
  materias: Materia[] = [];
  notas: Nota[] = [];
  nuevaNota: Nota = { estudianteId: 0, materiaId: 0, calificacion: 0 };
  
  cargando: boolean = false;
  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';
  mostrarFormulario: boolean = false;
  estudianteId: number = 0;

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

    // Primero obtener el ID del estudiante desde el perfil
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiantes/usuario/${usuario.id}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.estudianteId = response.data.id;
            this.nuevaNota.estudianteId = this.estudianteId;
            this.cargarMaterias();
            this.cargarNotas();
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

  cargarMaterias() {
    this.http.get<any>('https://proyectweb-rech.onrender.com/api/public/materias')
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.materias = response.data;
          }
        },
        error: (err) => {
          console.error('Error cargando materias:', err);
          this.error = 'No se pudieron cargar las materias';
        }
      });
  }

  cargarNotas() {
    if (!this.estudianteId) return;
    
    this.cargando = true;
    
    this.http.get<any>(`https://proyectweb-rech.onrender.com/api/estudiante/notas/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.notas) {
            this.notas = response.notas.map((nota: any) => ({
              id: nota.id,
              estudianteId: nota.estudiante.id,
              materiaId: nota.materia.id,
              materiaNombre: nota.materia.nombre,
              calificacion: nota.calificacion
            }));
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando notas:', err);
          this.cargando = false;
        }
      });
  }

  agregarNota() {
    if (this.nuevaNota.materiaId === 0 || this.nuevaNota.calificacion < 0 || this.nuevaNota.calificacion > 100) {
      this.error = 'Por favor selecciona una materia y una calificación válida (0-100)';
      return;
    }

    // Verificar si ya existe una nota para esta materia
    if (this.notas.some(n => n.materiaId === this.nuevaNota.materiaId)) {
      this.error = 'Ya registraste una nota para esta materia';
      return;
    }

    this.guardando = true;
    this.error = '';
    this.mensaje = '';

    this.http.post<any>('https://proyectweb-rech.onrender.com/api/estudiante/notas', this.nuevaNota)
      .subscribe({
        next: (response) => {
          this.mensaje = '✅ Nota agregada exitosamente';
          this.cargarNotas();
          this.cancelarFormulario();
          this.guardando = false;
        },
        error: (err) => {
          console.error('Error agregando nota:', err);
          this.error = err.error?.message || 'Error al agregar la nota';
          this.guardando = false;
        }
      });
  }

  eliminarNota(id: number) {
    if (!confirm('¿Estás seguro de eliminar esta nota?')) return;

    this.http.delete<any>(`https://proyectweb-rech.onrender.com/api/estudiante/notas/${id}`)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Nota eliminada';
          this.cargarNotas();
        },
        error: (err) => {
          console.error('Error eliminando nota:', err);
          this.error = 'Error al eliminar la nota';
        }
      });
  }

  getNombreMateria(materiaId: number): string {
    return this.materias.find(m => m.id === materiaId)?.nombre || 'Desconocida';
  }

  materiaYaRegistrada(materiaId: number): boolean {
    return this.notas.some(n => n.materiaId === materiaId);
  }

  mostrarNuevoFormulario() {
    this.mostrarFormulario = true;
    this.nuevaNota = { estudianteId: this.nuevaNota.estudianteId, materiaId: 0, calificacion: 0 };
    this.mensaje = '';
    this.error = '';
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.nuevaNota = { estudianteId: this.nuevaNota.estudianteId, materiaId: 0, calificacion: 0 };
    this.mensaje = '';
    this.error = '';
  }

  volverAlDashboard() {
    this.router.navigate(['/estudiante/dashboard']);
  }

  get promedio(): number {
    if (this.notas.length === 0) return 0;
    const suma = this.notas.reduce((acc, nota) => acc + nota.calificacion, 0);
    return suma / this.notas.length;
  }
}

