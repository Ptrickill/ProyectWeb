import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

// Servicio de Usuarios SUPER SIMPLE - Fácil de entender
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // ========== MÉTODO SIMPLE PARA OBTENER USUARIOS ==========
  
  // Obtener todos los usuarios - MÉTODO SIMPLE
  getAllUsers(): Observable<User[]> {
    console.log('Obteniendo usuarios del backend...');
    return this.http.get<User[]>(this.API_URL);
  }

  // ========== MÉTODO SIMPLE PARA CREAR USUARIO ==========
  
  // Crear nuevo usuario - MÉTODO SIMPLE  
  createUser(usuario: any): Observable<any> {
    console.log('Creando usuario:', usuario);
    return this.http.post<any>(this.API_URL, usuario);
  }
}