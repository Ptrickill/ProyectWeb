import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.html',
  styleUrl: './user-management.scss'
})
export class UserManagement implements OnInit {
  users: User[] = [];
  loading = false;
  
  // Variables para el formulario
  mostrarFormulario = false;
  modoEdicion = false;
  usuarioActual: any = {};
  mensajeError = '';
  mensajeExito = '';

  constructor(private userService: UserService) {}

  // Inicializar formulario vacío
  inicializarFormulario() {
    this.usuarioActual = {
      username: '',
      password: '',
      email: '',
      nombreCompleto: '',
      role: 'USER',
      enabled: true
    };
  }

  ngOnInit(): void {
    this.cargarUsuarios();
    this.inicializarFormulario();
  }

  cargarUsuarios(): void {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        // El backend devuelve {users: [...], total: X, success: true}
        // Necesitamos extraer solo el array de users
        if (response && response.users && Array.isArray(response.users)) {
          this.users = response.users;
        } else if (Array.isArray(response)) {
          // Por si acaso el backend devuelve directamente el array
          this.users = response;
        } else {
          this.users = [];
        }
        this.loading = false;
        console.log('Usuarios cargados:', response);
      },
      error: (error) => {
        console.error('Error cargando usuarios:', error);
        // Si hay error, mostramos usuarios de ejemplo
        this.users = [
          {
            id: 1,
            username: 'admin',
            email: 'admin@example.com',
            nombreCompleto: 'Administrador',
            role: 'ADMIN',
            enabled: true
          },
          {
            id: 2,
            username: 'usuario',
            email: 'usuario@example.com',
            nombreCompleto: 'Usuario Normal',
            role: 'USER',
            enabled: true
          }
        ];
        this.loading = false;
      }
    });
  }

  // ========== MÉTODOS PARA CRUD ==========

  // Mostrar formulario para crear nuevo usuario
  nuevoUsuario() {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.inicializarFormulario();
    this.mensajeError = '';
    this.mensajeExito = '';
  }

  // Mostrar formulario para editar usuario existente
  editarUsuario(usuario: any) {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.usuarioActual = { ...usuario }; // Copiar los datos del usuario
    this.mensajeError = '';
    this.mensajeExito = '';
  }

  // Guardar usuario (crear o actualizar)
  guardarUsuario() {
    // Validaciones básicas
    if (!this.usuarioActual.username || !this.usuarioActual.email) {
      this.mensajeError = 'Usuario y email son obligatorios';
      return;
    }

    if (!this.modoEdicion && !this.usuarioActual.password) {
      this.mensajeError = 'La contraseña es obligatoria para nuevos usuarios';
      return;
    }

    this.loading = true;
    this.mensajeError = '';

    if (this.modoEdicion) {
      // Actualizar usuario existente
      this.userService.updateUser(this.usuarioActual.id, this.usuarioActual).subscribe({
        next: (response) => {
          if (response.success) {
            this.mensajeExito = 'Usuario actualizado exitosamente';
            this.cargarUsuarios();
            this.cancelarFormulario();
          } else {
            this.mensajeError = response.message || 'Error al actualizar usuario';
          }
          this.loading = false;
        },
        error: (error) => {
          this.mensajeError = 'Error al actualizar usuario';
          this.loading = false;
          console.error('Error:', error);
        }
      });
    } else {
      // Crear nuevo usuario
      this.userService.createUser(this.usuarioActual).subscribe({
        next: (response) => {
          if (response.success) {
            this.mensajeExito = 'Usuario creado exitosamente';
            this.cargarUsuarios();
            this.cancelarFormulario();
          } else {
            this.mensajeError = response.message || 'Error al crear usuario';
          }
          this.loading = false;
        },
        error: (error) => {
          this.mensajeError = 'Error al crear usuario';
          this.loading = false;
          console.error('Error:', error);
        }
      });
    }
  }

  // Eliminar usuario
  eliminarUsuario(usuario: any) {
    if (confirm(`¿Estás seguro de que quieres eliminar al usuario "${usuario.username}"?`)) {
      this.loading = true;
      
      this.userService.deleteUser(usuario.id).subscribe({
        next: (response) => {
          if (response.success) {
            this.mensajeExito = 'Usuario eliminado exitosamente';
            this.cargarUsuarios();
          } else {
            this.mensajeError = response.message || 'Error al eliminar usuario';
          }
          this.loading = false;
        },
        error: (error) => {
          this.mensajeError = 'Error al eliminar usuario';
          this.loading = false;
          console.error('Error:', error);
        }
      });
    }
  }

  // Cancelar formulario
  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.inicializarFormulario();
    this.mensajeError = '';
  }

  // Limpiar mensajes
  limpiarMensajes() {
    this.mensajeError = '';
    this.mensajeExito = '';
  }
}
