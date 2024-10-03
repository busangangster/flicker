package com.flicker.movie.movie.dto;

import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import lombok.Data;

@Data
public class MovieListResponse {
    private int movieSeq;

    private String movieTitle;

    private String moviePosterUrl;

    private Integer movieYear;

    private String backgroundUrl;

    private double movieRating;

    public MovieListResponse(Movie movie, MovieDetail movieDetail) {
        this.movieSeq = movie.getMovieSeq();
        this.movieTitle = movieDetail.getMovieTitle();
        this.moviePosterUrl = movieDetail.getMoviePosterUrl();
        this.movieYear = movieDetail.getMovieYear();
        this.backgroundUrl = movieDetail.getBackgroundUrl();
        this.movieRating = movie.getMovieRating();
    }

    public MovieListResponse(MongoMovie mongoMovie) {
        this.movieSeq = mongoMovie.getMovieSeq();
        this.movieTitle = mongoMovie.getMovieTitle();
        this.moviePosterUrl = mongoMovie.getMoviePosterUrl();
        this.movieYear = mongoMovie.getMovieYear();
        this.backgroundUrl = mongoMovie.getBackgroundUrl();
        this.movieRating = mongoMovie.getMovieRating();
    }
}
