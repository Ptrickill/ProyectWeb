import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  imports: [CommonModule],
  templateUrl: './user-management.html',
  styleUrl: './user-management.scss'
})
export class UserManagement implements OnInit {
  users: User[] = [];
  loading = false;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (usuarios) => {
        this.users = usuarios;
        this.loading = false;
        console.log('Usuarios cargados:', usuarios);
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
}
