import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

// Servicio de Autenticación SUPER SIMPLE - Fácil de entender
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  
  // Variable simple para saber si está logueado
  private usuarioLogueado: any = null;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Al iniciar, verificar si hay un usuario guardado
    this.cargarUsuarioGuardado();
  }

  // ========== LOGIN SIMPLE ==========
  login(credenciales: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/login`, credenciales);
  }

  // ========== GUARDAR USUARIO DESPUÉS DEL LOGIN ==========
  guardarUsuario(usuario: any): void {
    this.usuarioLogueado = usuario;
    // Guardar en localStorage para que persista
    localStorage.setItem('usuarioLogueado', JSON.stringify(usuario));
  }

  // ========== OBTENER USUARIO ACTUAL ==========
  obtenerUsuario(): any {
    return this.usuarioLogueado;
  }

  // ========== VERIFICAR SI ESTÁ LOGUEADO ==========
  estaLogueado(): boolean {
    return this.usuarioLogueado !== null;
  }

  // ========== LOGOUT ==========
  logout(): void {
    this.usuarioLogueado = null;
    localStorage.removeItem('usuarioLogueado');
    this.router.navigate(['/login']);
  }

  // ========== CARGAR USUARIO GUARDADO ==========
  private cargarUsuarioGuardado(): void {
    const usuarioGuardado = localStorage.getItem('usuarioLogueado');
    if (usuarioGuardado) {
      try {
        this.usuarioLogueado = JSON.parse(usuarioGuardado);
      } catch (error) {
        console.log('Error al cargar usuario guardado:', error);
        localStorage.removeItem('usuarioLogueado');
      }
    }
  }
}