package com.flicker.bff.dto.client;

import lombok.Data;

@Data
public class MovieDetailDto {
    public String movieTitle; // 영화 제목
    public String director; // 감독 이름
    public String genre; // 영화 장르
    public String country; // 제작 국가
    public String moviePlot; // 영화 줄거리
    public String audienceRating; // 관람 등급
    public Integer movieYear; // 영화 제작 연도
    public String runningTime; // 영화 상영 시간
    public String moviePosterUrl; // 영화 포스터 이미지 URL
    public String trailerUrl; // 트레일러 영상 url
    public String backgroundUrl; // 영화 배경 이미지 URL
}
