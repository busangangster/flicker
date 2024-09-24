# Node.js를 사용한 빌드 단계
FROM node:16 AS build

# 애플리케이션 디렉토리 생성
WORKDIR /app

# 애플리케이션 종속성 설치
COPY package*.json ./
RUN npm install --production=false

# vite와 tsc에 실행 권한 부여
RUN chmod 777 ./node_modules/.bin/tsc ./node_modules/.bin/vite

# tsc 파일이 있는지 확인 (디버깅용)
RUN ls -la ./node_modules/.bin/tsc

# 애플리케이션 소스 복사
COPY . .

# 애플리케이션 빌드
RUN npm run build

# Nginx 설정 단계
FROM nginx:alpine

# 빌드된 정적 파일을 Nginx 디렉토리로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx 설정 파일을 Nginx 설정 디렉토리로 복사
COPY nginx.conf /etc/nginx/nginx.conf

# Nginx의 80번 포트를 엽니다
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
