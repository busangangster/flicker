# 베이스 이미지로 Python 3.8 이미지 사용 (TensorFlow 호환 버전)
FROM python:3.8-slim

# 작업 디렉토리 설정
WORKDIR /app

# 시스템 의존성 설치 (필요 시)
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Python 패키지 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 모델 가중치 파일 복사
COPY weights/ /app/weights/

# 애플리케이션 코드 복사
COPY app/ /app/app/

# 환경 변수 설정 (예: 모델 가중치 경로)
ENV MODEL_WEIGHTS_PATH="/app/weights/sentiment_model_weights.h5"

# FastAPI 애플리케이션 실행
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]