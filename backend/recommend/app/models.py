from pydantic import BaseModel
from typing import Optional

class SentimentReviewEvent(BaseModel):
    reviewSeq: int
    content: str

class SentimentResult(BaseModel):
    reviewSeq: int
    sentimentScore: float

class ContentMovieRequest(BaseModel):
    movieTitle: Optional[str] = None  # 생략되면 None이 기본값
    year: Optional[int] = None        # 생략되면 None이 기본값
    actorName: Optional[str] = None   # 생략되면 None이 기본값

class ContentMovieResponse(BaseModel):
    movieTitle: str
    movieYear: int

class CollaboMovieResponse(BaseModel):
    movieTitle: str
    movieYear: int

class CollaboMovieRequest(BaseModel):
    userSeq: int

# 모델 정의
class ModelUpdateRequest(BaseModel):
    userSeq: int
    movieSeq: int
    reviewSeq: int
    action: str
    rating: float
    sentimentScore: Optional[float] = None

class WordCloudRequest(BaseModel):
    movieSeq: int
    content: str