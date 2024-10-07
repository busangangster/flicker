# models.py
from sqlalchemy import Column, Integer, String, Double, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.orm import declarative_base
BaseMovie = declarative_base()

from sqlalchemy import Column, String, Integer
from sqlalchemy.orm import relationship

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

class Actor(BaseMovie):
    __tablename__ = 'actor'
    
    actor_seq = Column(Integer, primary_key=True, autoincrement=True)
    actor_name = Column(String(255), nullable=False)
    role = Column(String(255), nullable=True)  # 'YES'로 되어 있으므로 nullable=True
    movie_seq = Column(Integer, ForeignKey('movie.movie_seq'), nullable=False)

    # 관계 설정
    movie = relationship("Movie", back_populates="actors")
