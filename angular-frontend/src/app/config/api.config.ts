import { environment } from '../../environments/environment';

export class ApiConfig {
  private static readonly LOCAL_API = 'http://localhost:8080';
  private static readonly PROD_API = environment.apiUrl || 'http://localhost:8080';

  static getApiUrl(): string {
    return environment.production ? this.PROD_API : this.LOCAL_API;
  }

  static getEndpoint(path: string): string {
    return `${this.getApiUrl()}${path}`;
  }
}
