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

    public MovieListResponse(int movieSeq, String movieTitle, String moviePosterUrl) {
        this.movieSeq = movieSeq;
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
    }

    public MovieListResponse(Movie movie, MovieDetail movieDetail) {
        this.movieSeq = movie.getMovieSeq();
        this.movieTitle = movieDetail.getMovieTitle();
        this.moviePosterUrl = movieDetail.getMoviePosterUrl();
    }

    public MovieListResponse(MongoMovie mongoMovie) {
        this.movieSeq = mongoMovie.getMovieSeq();
        this.movieTitle = mongoMovie.getMovieTitle();
        this.moviePosterUrl = mongoMovie.getMoviePosterUrl();
    }

}
