export class ApiConfig {
  private static readonly API_URL = 'https://proyectweb-rech.onrender.com';

  static getApiUrl(): string {
    return this.API_URL;
  }

  static getEndpoint(path: string): string {
    return `${this.getApiUrl()}${path}`;
  }
}
