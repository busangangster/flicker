package com.flicker.movie.movie.application;

import com.flicker.movie.movie.domain.entity.Actor;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.MovieCreateRequest;
import com.flicker.movie.movie.dto.MovieRatingUpdateRequest;
import com.flicker.movie.movie.dto.MovieUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieBuilderUtil movieBuilderUtil;
    private final MovieRepoUtil movieRepoUtil;

    @Transactional
    public void createMovie(MovieCreateRequest request) {
        // 중복 영화 확인
        movieRepoUtil.isDuplicatedMovie(request.getMovieTitle(), request.getMovieYear());
        // MovieDetail 빌드
        MovieDetail movieDetail = movieBuilderUtil.buildMovieDetail(request);
        // Movie 엔티티 생성
        Movie movie = movieBuilderUtil.buildMovie(movieDetail);
        // Actor 리스트 생성
        List<Actor> actorList = movieBuilderUtil.buildActorList(request);
        // Movie Actor 리스트 추가
        movie.addActors(actorList);
        // 데이터베이스에 저장
        movieRepoUtil.saveMovie(movie);
    }

    @Transactional
    public void updateMovie(MovieUpdateRequest request) {
        // 0. 중복 영화 확인
        movieRepoUtil.isDuplicatedMovie(request.getMovieTitle(), request.getMovieYear());
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 2. 영화 정보 업데이트
        MovieDetail movieDetail = movieBuilderUtil.buildMovieDetail(request); // MovieDetail 빌드
        movie.updateMovieDetail(movieDetail);
    }

    @Transactional
    public void updateMovieRating(MovieRatingUpdateRequest request) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 2. 영화 평점 업데이트
        movie.updateMovieRating(request.getMovieRating());
    }

    @Transactional
    public void deleteMovie(int movieSeq) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 2. 영화 삭제
        movie.deleteMovie();
    }
}
