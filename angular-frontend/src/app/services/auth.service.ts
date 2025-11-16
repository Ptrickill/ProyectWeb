import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

// Interfaces para tipado
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  nombreCompleto: string;
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  nombreCompleto: string;
  role: 'USER' | 'ADMIN';
}

export interface AuthResponse {
  success: boolean;
  message?: string;
  usuario?: Usuario;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  
  // BehaviorSubject para que otros componentes puedan suscribirse
  private usuarioSubject = new BehaviorSubject<Usuario | null>(null);
  public usuario$ = this.usuarioSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.cargarUsuarioGuardado();
  }

  // Login
  login(credenciales: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credenciales)
      .pipe(
        tap(response => {
          if (response.success && response.usuario) {
            this.guardarUsuario(response.usuario);
          }
        })
      );
  }

  // Registro
  register(datos: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, datos)
      .pipe(
        tap(response => {
          if (response.success && response.usuario) {
            this.guardarUsuario(response.usuario);
          }
        })
      );
  }

  // Guardar usuario después de loguearse
  guardarUsuario(usuario: Usuario): void {
    this.usuarioSubject.next(usuario);
    localStorage.setItem('usuarioLogueado', JSON.stringify(usuario));
  }

  // Obtener usuario actual
  obtenerUsuario(): Usuario | null {
    return this.usuarioSubject.value;
  }

  // Verificar si está logueado
  estaLogueado(): boolean {
    return this.usuarioSubject.value !== null;
  }

  // Verificar si es admin
  esAdmin(): boolean {
    const usuario = this.usuarioSubject.value;
    return usuario?.role === 'ADMIN';
  }

  // Verificar si es estudiante
  esEstudiante(): boolean {
    const usuario = this.usuarioSubject.value;
    return usuario?.role === 'USER';
  }

  // Logout
  logout(): void {
    this.usuarioSubject.next(null);
    localStorage.removeItem('usuarioLogueado');
    this.router.navigate(['/login']);
  }

  // Cargar usuario guardado del localStorage
  private cargarUsuarioGuardado(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      try {
        const usuario = JSON.parse(usuarioGuardado);
        this.usuarioSubject.next(usuario);
      } catch (error) {
        console.error('Error al cargar usuario guardado:', error);
        localStorage.removeItem('usuarioLogueado');
      }
    }
  }
}