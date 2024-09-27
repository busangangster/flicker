from fastapi import FastAPI
from app.models import Sentence
from app.utils.model_loader import load_sentiment_model
from app.utils.predictor import movie_evaluation_predict
import logging

app = FastAPI()
logger = logging.getLogger(__name__)

model_path = '/app/weights/sentiment_model_weights.h5'

try:
    sentiment_model = load_sentiment_model(model_path)
    # 레이턴시를 줄이기 위해 더미 데이터를 사용하여 더미 데이터로 Warm-up
    dummy_sentence = "dummy sentence!"
    dummy_prediction = movie_evaluation_predict(sentiment_model, dummy_sentence)
    logger.info(f"Model warm-up completed with dummy prediction: {dummy_prediction}")
    logger.info("Model loaded successfully.")
except Exception as e:
    sentiment_model = None
    logger.error(f"Failed to load model: {e}")

@app.post("/test")
async def test(data: Sentence):  # JSON body에서 Sentence 모델 받기

    if not sentiment_model:
        return {"error": "Model not loaded."}

    sentence = data.sentence
    sentiment_score = movie_evaluation_predict(sentiment_model, sentence)
    return {"sentiment_score": sentiment_score}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
