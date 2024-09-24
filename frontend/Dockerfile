# 베이스 이미지로 Node.js 사용
FROM node:16

# 애플리케이션 디렉토리 생성
WORKDIR /app

# 애플리케이션 종속성 설치
COPY package*.json ./
RUN npm install

# vite 실행 파일에 권한 추가 (npm install 후에 실행)
RUN chmod +x /app/node_modules/.bin/vite

# 애플리케이션 소스 복사
COPY . .

# 애플리케이션 실행
CMD ["npm", "run", "dev"]

# 컨테이너가 사용할 포트
EXPOSE 5173 5174
