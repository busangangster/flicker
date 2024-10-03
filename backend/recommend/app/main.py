from fastapi import FastAPI
from typing import List
from models import SentimentReviewEvent, SentimentResult, ContentMovieRequest, ContentMovieResponse
from predict import sequential_movie_evaluation
from gensim.models import Word2Vec
from word2vec_model import load_word2vec_model, get_similar_words_with_T

# TODO : 새로운 리뷰데이터를 파이썬 서버에서 수집해야하는데 감성분석하러 들어올 때 같이 데이터 받고 처리하는 과정에 DB에 저장하면 될 듯!
# TODO : 지금 워드 클라우드 (단어 빈도 수) 추출해서 반환하는 로직이 지금 필요할 것 같아요!!
# TODO : 컨텐츠 기반의 추천에서 본 영화는 안나오게 하는 로직이 필요할 듯
# FastAPI 애플리케이션 생성
app = FastAPI()

BATCH_SIZE = 2024  # 배치 크기 설정

word2vec_model = load_word2vec_model()

# FastAPI 엔드포인트 정의
@app.post("/content")
async def get_similar_words(request: List[ContentMovieRequest]):
    """입력된 단어 리스트에서 'T'로 끝나는 유사 단어들을 추출하는 API"""
    # 요청에서 words 값을 모두 추출
    all_words = [req.movieTitle + "^" + str(req.year) + "T" for req in request]

    # 유사 단어 추출
    sorted_movies = get_similar_words_with_T(word2vec_model, all_words)
    movie_objects = [ContentMovieResponse(movieTitle=title, year=year) for title, year in sorted_movies]

    return movie_objects


# API 엔드포인트 정의 (배치 처리)
@app.post("/sentiment_score")
async def analyze_sentiment(reviews: List[SentimentReviewEvent]):
    print(f"Received reviews: {reviews}")
    sentiment_scores = sequential_movie_evaluation(reviews, batch_size=BATCH_SIZE)
    return sentiment_scores


@app.get("/")
async def root():
    return {"message": "Sentiment analysis API is running"}


# 메인 함수 정의
if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)