# Fase 1: Compilación de Angular 21
FROM node:22-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# Fase 2: Servidor web Nginx para servir la app compilada
FROM nginx:alpine
# Copiamos los archivos compilados (ajusta la subcarpeta si tu angular.json tiene otra ruta)
COPY --from=build /app/dist/intelidoc-frontend/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
