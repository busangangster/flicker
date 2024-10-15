# local_fastapi.py
from fastapi import FastAPI
import httpx  # HTTP 요청을 위한 라이브러리

app = FastAPI()

# 원격 GPU 서버의 API URL
GPU_SERVER_URL = "http://70.12.130.131:8000"  # GPU 서버 IP 주소

@app.get("/gpu-status")
async def get_gpu_status():
    async with httpx.AsyncClient() as client:
        response = await client.get(f"{GPU_SERVER_URL}/gpu-status")
        return response.json()