// Modelo Usuario
export interface User {
  id?: number;
  username: string;
  email: string;
  nombreCompleto: string;
  role: 'USER' | 'ADMIN';
  enabled: boolean;
}

// Modelo login
export interface LoginRequest {
  username: string;
  password: string;
}

// Modelo crear usuario
export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  nombreCompleto: string;
}