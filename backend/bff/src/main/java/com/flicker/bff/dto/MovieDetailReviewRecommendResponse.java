package com.flicker.bff.dto;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class MovieDetailReviewRecommendResponse {
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
    private List<ActorResponse> actors;
    private List<WordCloudResponse> wordClouds;

    @Setter
    private List<ReviewResponse> reviews;

    @Setter
    private List<MovieListResponse> similarMovies;


    public void setMovieDetail(MovieDetailResponse movieDetailResponse) {
        this.movieSeq = movieDetailResponse.getMovieSeq();
        this.movieTitle = movieDetailResponse.getMovieTitle();
        this.director = movieDetailResponse.getDirector();
        this.genre = movieDetailResponse.getGenre();
        this.country = movieDetailResponse.getCountry();
        this.moviePlot = movieDetailResponse.getMoviePlot();
        this.audienceRating = movieDetailResponse.getAudienceRating();
        this.movieYear = movieDetailResponse.getMovieYear();
        this.runningTime = movieDetailResponse.getRunningTime();
        this.moviePosterUrl = movieDetailResponse.getMoviePosterUrl();
        this.trailerUrl = movieDetailResponse.getTrailerUrl();
        this.backgroundUrl = movieDetailResponse.getBackgroundUrl();
        this.movieRating = movieDetailResponse.getMovieRating();
        this.actors = movieDetailResponse.getActors();
        this.wordClouds = movieDetailResponse.getWordClouds();
    }
}
