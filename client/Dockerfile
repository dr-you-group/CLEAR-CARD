# ---- Build stage
FROM node:20-alpine AS build
WORKDIR /app
# Speed up install; optionally set npm ci if lockfile exists
COPY package*.json ./
# If you use pnpm or yarn, adjust accordingly
RUN npm ci
COPY . .
# Produce static assets into /app/dist
RUN npm run build

# ---- Dist-only stage (to export build artifacts to nginx)
# (This stage is optional; we’ll copy from it in docker-compose)
FROM alpine AS dist
WORKDIR /dist
COPY --from=build /app/dist ./
