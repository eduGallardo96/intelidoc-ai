import { ApplicationConfig, provideZonelessChangeDetection } from '@angular/core'; // Cambiamos el import
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = { // Asegúrate de que se llama appConfig para coincidir con tu main.ts
  providers: [
    provideZonelessChangeDetection(), // Activa el modo puro de Signals sin Zone.js
    provideRouter(routes),
    provideHttpClient()
  ]
};
