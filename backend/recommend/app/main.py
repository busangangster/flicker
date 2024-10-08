from fastapi import FastAPI, Body
from typing import List
from models import SentimentReviewEvent, SentimentResult, ContentMovieRequest, ContentMovieResponse, \
    CollaboMovieRequest, CollaboMovieResponse, ModelUpdateRequest, WordCloudRequest
from predict import sequential_movie_evaluation
from gensim.models import Word2Vec
from word2vec_model import load_word2vec_model, get_similar_words_with_T
from CollaboModel import updatePcaResultOptimal, recommendMovieByUserTime, load_initial_data
from word2vecUpdate import model_update
from wordcloud import wordcloudUpdate
import sqlite3
import asyncio
from contextlib import asynccontextmanager


# TODO : 새로운 리뷰데이터를 파이썬 서버에서 수집해야하는데 감성분석하러 들어올 때 같이 데이터 받고 처리하는 과정에 DB에 저장하면 될 듯!
# TODO : 지금 워드 클라우드 (단어 빈도 수) 추출해서 반환하는 로직이 지금 필요할 것 같아요!!
# TODO : 컨텐츠 기반의 추천에서 본 영화는 안나오게 하는 로직이 필요할 듯
# FastAPI 애플리케이션 생성

# Lifespan 이벤트 핸들러 정의
@asynccontextmanager
async def lifespan(app: FastAPI):
    global word2vec_model
    # 애플리케이션 시작 시 실행될 로직 (예: 데이터 로드)
    print("Loading initial data...")
    await load_initial_data()  # 비동기 데이터 로드 함수 호출
    word2vec_model = load_word2vec_model()
    print("Initial data loaded.")
    yield  # 애플리케이션이 실행되는 동안 유지
    # 애플리케이션 종료 시 실행될 로직 (필요 시 추가 가능)
    print("Cleaning up resources...")


# FastAPI 애플리케이션 생성
app = FastAPI(lifespan=lifespan)

BATCH_SIZE = 2024  # 배치 크기 설정


# word2vec_model = load_word2vec_model()

# updatePcaResultOptimal()

# FastAPI 엔드포인트 정의
@app.post("/content")
async def get_movie_with_content(request: List[ContentMovieRequest]):
    """입력된 단어 리스트에서 'T'로 끝나는 유사 단어들을 추출하는 API"""
    # 요청에서 words 값을 모두 추출
    all_words = [req.actorName if req.actorName is not None else req.movieTitle + "^" + str(req.year) + "T" for req in
                 request]

    # 유사 단어 추출
    sorted_movies = get_similar_words_with_T(word2vec_model, all_words)
    movie_objects = [ContentMovieResponse(movieTitle=title, movieYear=year) for title, year in sorted_movies]

    return movie_objects


# API 엔드포인트 정의 (배치 처리)
@app.post("/sentiment_score")
async def analyze_sentiment(reviews: List[SentimentReviewEvent]):
    print(f"Received reviews: {reviews}")
    sentiment_scores = sequential_movie_evaluation(reviews, batch_size=BATCH_SIZE)
    return sentiment_scores


# 협업 필터링
@app.post("/collabo")
async def get_movie_with_collabo(userSeq: int = Body(...)):
    # 결과 출력
    movies = recommendMovieByUserTime(userSeq)
    collabo_movie_responses = [CollaboMovieResponse(movieTitle=movie[0], movieYear=movie[1]) for movie in movies]
    return collabo_movie_responses


@app.post("/word_cloud")
async def word_cloud_update(reviews: List[WordCloudRequest]):
    conn = sqlite3.connect('recommend.db')
    cursor = conn.cursor()

    insert_query = """
        INSERT INTO wordcloud (movie_seq, content) 
        VALUES (?, ?)
        """
    try:
        for review in reviews:
            if review.content is not None and review.content.strip() != "":
                cursor.execute(insert_query, (review.movieSeq, review.content))

        conn.commit()

    except Exception as e:
        print(f"Error inserting data: {e}")
        conn.rollback()  # 오류 발생 시 롤백

    finally:
        # 연결 종료
        conn.close()


@app.post("/update_model")
async def update_model():
    conn = sqlite3.connect('recommend.db')
    # print(reviews)
    cursor = conn.cursor()

    # SQL INSERT 문 준비
    insert_query = """
    INSERT INTO review_info (review_seq, user_seq, movie_seq, review_rating, sentiment_score) 
    VALUES (?, ?, ?, ?, ?)
    """

    delete_query = """
    DELETE FROM review_info 
    WHERE review_seq = ?
    """

    try:
        # 받은 데이터를 반복하면서 삽입
        for review in reviews:
            if review.action == "CREATE":
                cursor.execute(insert_query, (
                review.reviewSeq, review.userSeq, review.movieSeq, review.rating, review.sentimentScore))
            elif review.action == "DELETE":
                cursor.execute(delete_query, (review.reviewSeq,))

        # 변경 사항 커밋
        conn.commit()

    except Exception as e:
        print(f"Error inserting data: {e}")
        conn.rollback()  # 오류 발생 시 롤백

    finally:
        # 연결 종료
        conn.close()

    # await updatePcaResultOptimal()


@app.post("/word2vec_update")
async def word2vec_update():
    asyncio.create_task(model_update())
    word2vec_model = load_word2vec_model()


@app.post("/wordcloud_update")
async def wordcloud_update():
    return wordcloudUpdate()


@app.get("/")
async def root():
    return {"message": "Sentiment analysis API is running"}


# 메인 함수 정의
if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)