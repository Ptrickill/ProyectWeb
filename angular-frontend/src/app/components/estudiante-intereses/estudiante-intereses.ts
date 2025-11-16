import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

interface Carrera {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
}

interface Afinidad {
  id?: number;
  estudianteId: number;
  carreraId: number;
  carreraNombre?: string;
  nivelInteres: number; // 1-5
}

@Component({
  selector: 'app-estudiante-intereses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estudiante-intereses.html',
  styleUrls: ['./estudiante-intereses.scss']
})
export class EstudianteIntereses implements OnInit {
  carreras: Carrera[] = [];
  afinidades: Map<number, number> = new Map(); // carreraId -> nivelInteres
  afinidadesGuardadas: Afinidad[] = [];
  
  cargando: boolean = false;
  guardando: boolean = false;
  mensaje: string = '';
  error: string = '';
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

    this.estudianteId = usuario.id;
    this.cargarCarreras();
    this.cargarAfinidadesGuardadas();
  }

  cargarCarreras() {
    this.cargando = true;
    this.http.get<any>('http://localhost:8080/api/public/carreras')
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.carreras = response.data;
          }
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando carreras:', err);
          this.error = 'No se pudieron cargar las carreras';
          this.cargando = false;
        }
      });
  }

  cargarAfinidadesGuardadas() {
    this.http.get<any>(`http://localhost:8080/api/afinidades/estudiante/${this.estudianteId}`)
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.afinidadesGuardadas = response.data;
            // Cargar en el Map
            response.data.forEach((a: any) => {
              this.afinidades.set(a.carrera.id, a.nivelInteres);
            });
          }
        },
        error: (err) => {
          console.log('No hay afinidades guardadas aún');
        }
      });
  }

  seleccionarNivel(carreraId: number, nivel: number) {
    this.afinidades.set(carreraId, nivel);
  }

  getNivelInteres(carreraId: number): number {
    return this.afinidades.get(carreraId) || 0;
  }

  guardarIntereses() {
    if (this.afinidades.size === 0) {
      this.error = 'Por favor indica tu nivel de interés en al menos una carrera';
      return;
    }

    this.guardando = true;
    this.error = '';
    this.mensaje = '';

    const afinidadesArray = Array.from(this.afinidades.entries()).map(([carreraId, nivelInteres]) => ({
      estudianteId: this.estudianteId,
      carreraId: carreraId,
      nivelInteres: nivelInteres
    }));

    // Guardar cada afinidad
    let guardadas = 0;
    let errores = 0;

    afinidadesArray.forEach(afinidad => {
      const yaExiste = this.afinidadesGuardadas.find(a => a.carreraId === afinidad.carreraId);
      
      if (yaExiste) {
        // Actualizar
        this.http.put<any>(`http://localhost:8080/api/afinidades/${yaExiste.id}`, afinidad)
          .subscribe({
            next: () => {
              guardadas++;
              if (guardadas + errores === afinidadesArray.length) {
                this.finalizarGuardado(errores);
              }
            },
            error: () => {
              errores++;
              if (guardadas + errores === afinidadesArray.length) {
                this.finalizarGuardado(errores);
              }
            }
          });
      } else {
        // Crear
        this.http.post<any>('http://localhost:8080/api/afinidades', afinidad)
          .subscribe({
            next: () => {
              guardadas++;
              if (guardadas + errores === afinidadesArray.length) {
                this.finalizarGuardado(errores);
              }
            },
            error: () => {
              errores++;
              if (guardadas + errores === afinidadesArray.length) {
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
      this.mensaje = '✅ Intereses guardados exitosamente. Representan el 20% de tu puntaje vocacional.';
      this.cargarAfinidadesGuardadas();
    } else {
      this.error = `Se guardaron algunos intereses, pero hubo ${errores} error(es)`;
    }
  }

  getColorNivel(nivel: number): string {
    if (nivel === 0) return '#e0e0e0';
    if (nivel === 1) return '#ff6b6b';
    if (nivel === 2) return '#ffa500';
    if (nivel === 3) return '#ffd700';
    if (nivel === 4) return '#90ee90';
    if (nivel === 5) return '#4caf50';
    return '#e0e0e0';
  }

  getTextoNivel(nivel: number): string {
    if (nivel === 0) return 'Sin seleccionar';
    if (nivel === 1) return 'Muy bajo interés';
    if (nivel === 2) return 'Bajo interés';
    if (nivel === 3) return 'Interés medio';
    if (nivel === 4) return 'Alto interés';
    if (nivel === 5) return 'Muy alto interés';
    return '';
  }

  volverAlDashboard() {
    this.router.navigate(['/estudiante/dashboard']);
  }

  get carrerasConInteres(): number {
    return this.afinidades.size;
  }
}
