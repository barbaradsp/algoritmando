# =========================
# BUILD ANGULAR
# =========================

FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build && \
    mkdir -p /app/dist-ready && \
    BUILD_DIR="$(dirname "$(find dist -type f -name index.html | head -n 1)")" && \
    echo "Angular build encontrado em: $BUILD_DIR" && \
    cp -r "$BUILD_DIR"/. /app/dist-ready/


# =========================
# NGINX
# =========================

FROM nginx:1.27-alpine

RUN rm -rf /usr/share/nginx/html/*

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist-ready/ /usr/share/nginx/html/

EXPOSE 80
