from pydantic import BaseModel

class SentimentReviewEvent(BaseModel):
    reviewSeq: int
    content: str

class SentimentResult(BaseModel):
    reviewSeq: int
    sentimentScore: float

class ContentMovieRequest(BaseModel):
    movieTitle: str
    year: int

class ContentMovieResponse(BaseModel):
    movieTitle: str
    year: int