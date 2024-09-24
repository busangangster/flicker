# 베이스 이미지로 Node.js 사용
FROM node:16 AS build

# 애플리케이션 디렉토리 생성
WORKDIR /app

# 애플리케이션 종속성 설치
COPY package*.json ./
RUN npm install

# 애플리케이션 소스 복사
COPY . .

# 애플리케이션 빌드
RUN npm run build

# Nginx 이미지로 변경하여 빌드된 파일을 서비스
FROM nginx:alpine

# 빌드된 파일을 Nginx의 기본 제공 디렉토리로 복사
COPY --from=build /app/build /usr/share/nginx/html

# 컨테이너가 사용할 포트
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
