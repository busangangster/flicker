# Python 3.11.8 슬림 이미지를 기반으로 설정
FROM python:3.11.8-slim

# 작업 디렉토리를 /app으로 설정
WORKDIR /app

# 시스템 의존성 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    libglib2.0-0 \
    libnss3 \
    libgconf-2-4 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxtst6 \
    ca-certificates \
    fonts-liberation \
    libappindicator3-1 \
    lsb-release \
    xdg-utils \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

## 최신 Chrome 설치 (dpkg 후 실패 시 필요한 의존성 설치)
#RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
#    dpkg -i google-chrome-stable_current_amd64.deb || apt-get install -f -y && \
#    apt-get install -y google-chrome-stable
#
## ChromeDriver 설치
#RUN CHROME_VERSION=$(google-chrome --version | grep -oP '\d+\.\d+\.\d+') && \
#    wget -q "https://chromedriver.storage.googleapis.com/$CHROME_VERSION/chromedriver_linux64.zip" && \
#    unzip chromedriver_linux64.zip && \
#    mv chromedriver /usr/local/bin/ && \
#    rm chromedriver_linux64.zip

# Python 패키지 설치 (requirements.txt 복사 후 설치)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 코드 복사
COPY ./app /app

# PYTHONPATH 설정
ENV PYTHONPATH=/app

# FastAPI 애플리케이션 실행
CMD ["uvicorn", "getNewMovie:app", "--host", "localhost", "--port", "8082"]
