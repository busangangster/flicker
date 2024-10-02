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

    public MovieListResponse(Movie movie, MovieDetail movieDetail) {
        this.movieSeq = movie.getMovieSeq();
        this.movieTitle = movieDetail.getMovieTitle();
        this.moviePosterUrl = movieDetail.getMoviePosterUrl();
        this.movieYear = movieDetail.getMovieYear();
    }

    public MovieListResponse(MongoMovie mongoMovie) {
        this.movieSeq = mongoMovie.getMovieSeq();
        this.movieTitle = mongoMovie.getMovieTitle();
        this.moviePosterUrl = mongoMovie.getMoviePosterUrl();
    }

}
