// ===== MODELOS SIMPLES - Fáciles de entender =====

// Modelo principal para el Usuario
export interface User {
  id?: number;
  username: string;
  email: string;
  nombreCompleto: string;
  role: 'USER' | 'ADMIN';
  enabled: boolean;
}

// Modelo simple para login
export interface LoginRequest {
  username: string;
  password: string;
}

// Modelo simple para crear usuario
export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  nombreCompleto: string;
}