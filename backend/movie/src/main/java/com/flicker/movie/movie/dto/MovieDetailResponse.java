package com.flicker.movie.movie.dto;

import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import lombok.Data;

@Data
public class MovieDetailResponse {
    private int movieSeq;
    private String movieTitle;
    private String director;
    private String genre;
    private String country;
    private String moviePlot;
    private String audienceRating;
    private int movieYear;
    private String runningTime;
    private String moviePosterUrl;
    private String trailerUrl;
    private String backgroundUrl;
    private double movieRating;

    public MovieDetailResponse(Movie movie, MovieDetail movieDetail) {
        this.movieSeq = movie.getMovieSeq();
        this.movieTitle = movieDetail.getMovieTitle();
        this.director = movieDetail.getDirector();
        this.genre = movieDetail.getGenre();
        this.country = movieDetail.getCountry();
        this.moviePlot = movieDetail.getMoviePlot();
        this.audienceRating = movieDetail.getAudienceRating();
        this.movieYear = movieDetail.getMovieYear();
        this.runningTime = movieDetail.getRunningTime();
        this.moviePosterUrl = movieDetail.getMoviePosterUrl();
        this.trailerUrl = movieDetail.getTrailerUrl();
        this.backgroundUrl = movieDetail.getBackgroundUrl();
        this.movieRating = movie.getMovieRating();
    }
}
