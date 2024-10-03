# models.py
from sqlalchemy import Column, Integer, String, Double, Boolean, DateTime, ForeignKey, Index
from sqlalchemy.dialects.mysql import SMALLINT  # SMALLINT는 sqlalchemy.dialects.mysql에서 import
from sqlalchemy.orm import relationship
from datetime import datetime
from sqlalchemy.orm import declarative_base
BaseMovie = declarative_base()
BaseUser = declarative_base()

from sqlalchemy import Column, String, Integer, Date, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime

class User(BaseUser):
    __tablename__ = 'user'

    user_seq = Column(Integer, primary_key=True, autoincrement=True)  # int
    user_id = Column(String(255), unique=True, nullable=False)  # varchar(255)
    email = Column(String(255), nullable=True)  # varchar(255), nullable=True로 수정
    hashed_pass = Column(String(255), nullable=True)  # varchar(255), nullable=True로 수정
    nickname = Column(String(255), nullable=True)  # varchar(255)
    birth_date = Column(Date, nullable=True)  # date
    gender = Column(String(1), nullable=True)  # char(1)
    created_at = Column(DateTime, nullable=True, default=datetime.now)  # datetime(6)
    updated_at = Column(DateTime, nullable=True, default=datetime.now, onupdate=datetime.now)  # datetime(6)
    is_active = Column(Integer, default=1, nullable=True)  # int
    profile_photo_url = Column(String(255), nullable=True)  # varchar(255)
    role = Column(String(255), default='user', nullable=True)  # varchar(255)

    # 리뷰와의 관계 설정
    reviews = relationship("Review", back_populates="user")

class Review(BaseUser):
    __tablename__ = 'review'
    
    review_seq = Column(Integer, primary_key=True, autoincrement=True)  # auto_increment
    content = Column(String(255), nullable=True)  # varchar(255), NULL 허용
    created_at = Column(DateTime, nullable=True, default=datetime.now)  # datetime(6), NULL 허용
    is_active = Column(Integer, nullable=True, default=1)  # int, NULL 허용, 기본값 1
    is_spoiler = Column(Boolean, nullable=True)  # bit(1), NULL 허용
    likes = Column(Integer, nullable=True)  # int, NULL 허용
    movie_seq = Column(Integer, nullable=False)  # int, NOT NULL
    review_rating = Column(Double, nullable=True)  # double, NULL 허용
    user_seq = Column(Integer, ForeignKey('user.user_seq'), nullable=False, index=True)  # ForeignKey, NOT NULL
    sentiment_score = Column(Double, nullable=True)  # double, NULL 허용

    # User와의 관계 설정
    user = relationship("User", back_populates="reviews")

    # movie_seq에 인덱스 추가
    __table_args__ = (
        Index('movie_seq', 'movie_seq'),
    )




class Movie(BaseMovie):
    __tablename__ = 'movie'

    movie_seq = Column(Integer, primary_key=True, autoincrement=True)  # auto_increment 적용
    movie_title = Column(String(255), nullable=False)  # NOT NULL
    director = Column(String(255), nullable=True)  # NULL 허용
    genre = Column(String(255), nullable=True)  # NULL 허용
    country = Column(String(255), nullable=True)  # NULL 허용
    movie_plot = Column(String(5000), nullable=True)  # varchar(5000), NULL 허용
    audience_rating = Column(String(50), nullable=True)  # NULL 허용
    movie_year = Column(Integer, nullable=False)  # NOT NULL
    running_time = Column(String(50), nullable=True)  # NULL 허용
    movie_rating = Column(Double, nullable=False)  # double, NOT NULL
    movie_poster_url = Column(String (500), nullable=True)  # NULL 허용
    trailer_url = Column(String(500), nullable=True)  # NULL 허용
    background_url = Column(String(500), nullable=True)  # NULL 허용
    del_yn = Column(String(255), nullable=False)  # varchar(255), NOT NULL

    actors = relationship("Actor", back_populates="movie")
    word_clouds = relationship("WordCloud", back_populates="movie")

class Actor(BaseMovie):
    __tablename__ = 'actor'
    
    actor_seq = Column(Integer, primary_key=True, autoincrement=True)
    actor_name = Column(String(255), nullable=False)
    role = Column(String(255), nullable=True)  # 'YES'로 되어 있으므로 nullable=True
    movie_seq = Column(Integer, ForeignKey('movie.movie_seq'), nullable=False)

    # 관계 설정
    movie = relationship("Movie", back_populates="actors")

class WordCloud(BaseMovie):
    __tablename__ = 'word_cloud'
    
    word_cloud_seq = Column(Integer, primary_key=True, autoincrement=True)  # auto_increment
    count = Column(Integer, nullable=False)  # MySQL의 count 필드
    created_at = Column(DateTime, nullable=False, default=datetime.now)  # datetime(6)
    keyword = Column(String(255), nullable=False)  # varchar(255)
    movie_seq = Column(Integer, ForeignKey('movie.movie_seq'), nullable=False)  # 외래 키, NOT NULL

    # 관계 설정 필요 시 추가 가능
    movie = relationship("Movie", back_populates="word_clouds")