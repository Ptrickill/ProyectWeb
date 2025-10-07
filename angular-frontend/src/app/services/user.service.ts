import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

// Servicio de usuario
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // Obtener todos los usuarios
  getAllUsers(): Observable<any> {
    console.log('Obteniendo usuarios del backend...');
    return this.http.get<any>(this.API_URL);
  }

  // Crud
  
  // Crear nuevo usuario
  createUser(usuario: any): Observable<any> {
    console.log('Creando usuario:', usuario);
    return this.http.post<any>(this.API_URL, usuario);
  }

  // Actualizar usuario existente
  updateUser(id: number, usuario: any): Observable<any> {
    console.log('Actualizando usuario:', id, usuario);
    return this.http.put<any>(`${this.API_URL}/${id}`, usuario);
  }

  // Eliminar usuario
  deleteUser(id: number): Observable<any> {
    console.log('Eliminando usuario:', id);
    return this.http.delete<any>(`${this.API_URL}/${id}`);
  }

  // Obtener usuario por ID
  getUserById(id: number): Observable<any> {
    console.log('Obteniendo usuario por ID:', id);
    return this.http.get<any>(`${this.API_URL}/${id}`);
  }
}