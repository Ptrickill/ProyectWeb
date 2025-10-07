import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

// Servicio de Autenticación
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  
  // Variable para saber si está logueado
  private usuarioLogueado: any = null;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.cargarUsuarioGuardado();
  }

  // Login
  login(credenciales: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/login`, credenciales);
  }

  // Guardado de ususuaro despues de loguearse
  guardarUsuario(usuario: any): void {
    this.usuarioLogueado = usuario;
    
    localStorage.setItem('usuarioLogueado', JSON.stringify(usuario));
  }

  // Usuaro actual
  obtenerUsuario(): any {
    return this.usuarioLogueado;
  }

  // Verifiacion de logueado
  estaLogueado(): boolean {
    return this.usuarioLogueado !== null;
  }

  // Logout
  logout(): void {
    this.usuarioLogueado = null;
    localStorage.removeItem('usuarioLogueado');
    this.router.navigate(['/login']);
  }

  // Usuario guardado
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