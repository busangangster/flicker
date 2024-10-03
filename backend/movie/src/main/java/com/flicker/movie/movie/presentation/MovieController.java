package com.flicker.movie.movie.presentation;

import com.flicker.movie.common.module.response.ResponseDto;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.application.ActorService;
import com.flicker.movie.movie.application.MovieService;
import com.flicker.movie.movie.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    * MovieController 클래스는 영화 정보, 배우 정보를 등록, 수정, 삭제하는 API 요청을 처리한다.
    * MovieService, ActorService를 주입받는다.
    * createMovie() 메서드는 영화 정보, 배우 정보를 등록한다.
    * updateMovie() 메서드는 영화 정보를 수정한다.
    * updateMovieRating() 메서드는 영화 평점을 수정한다.
    * deleteMovie() 메서드는 영화 정보를 삭제한다.
    * addActor() 메서드는 영화 배우를 추가한다.
    * deleteActor() 메서드는 영화 배우를 삭제한다.
    * updateActor() 메서드는 영화 배우를 수정한다.
    * getAllMovieList() 메서드는 모든 영화 리스트를 조회한다.
    * getMovieListByGenre() 메서드는 장르별 영화 리스트를 조회한다.
    * getMovieListByActor() 메서드는 배우별 영화 리스트를 조회한다.
    * getMovieListByKeyword() 메서드는 키워드를 포함하는 영화 리스트를 조회한다.
    * getMovieDetail() 메서드는 영화 상세 정보를 조회한다.
    * getMovieListByMovieSeqList() 메서드는 영화 ID 리스트로 영화 리스트를 조회한다.
    * getRecommendationList() 메서드는 추천된 영화 리스트를 조회한다.
    * getUserActionList() 메서드는 사용자 행동 로그를 조회한다.
    * getTopMovieList() 메서드는 Top10 영화 리스트를 조회한다.
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;

    // 영화 정보, 배우 정보를 등록
    @PostMapping("/admin/create")
    public ResponseEntity<ResponseDto> createMovie(@RequestBody MovieCreateRequest request) {
        movieService.createMovie(request);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 등록 성공");
    }

    // 영화 정보 수정
    @PutMapping("/admin/update/detail")
    public ResponseEntity<ResponseDto> updateMovie(@RequestBody MovieUpdateRequest request) {
        movieService.updateMovie(request);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 수정 성공");
    }

    // 영화 평점 수정
    @PutMapping("/admin/update/rating")
    public ResponseEntity<ResponseDto> updateMovieRating(@RequestBody MovieRatingUpdateRequest request) {
        movieService.updateMovieRating(request);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 평점 수정 성공");
    }

    // 영화 삭제
    @PutMapping("/admin/delete")
    public ResponseEntity<ResponseDto> deleteMovie(@RequestParam int movieSeq) {
        movieService.deleteMovie(movieSeq);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 삭제 성공");
    }

    // 영화 배우 추가
    @PostMapping("/admin/add/actor")
    public ResponseEntity<ResponseDto> addActor(@RequestBody ActorAddRequest request) {
        actorService.addActor(request);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 배우 추가 성공");
    }

    // 영화 배우 삭제
    @DeleteMapping("/admin/delete/actor/{actorSeq}/{movieSeq}")
    public ResponseEntity<ResponseDto> deleteActor(@PathVariable int actorSeq, @PathVariable int movieSeq) {
        actorService.deleteActor(actorSeq, movieSeq);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 배우 삭제 성공");
    }

    // 영화 배우 수정
    @PutMapping("/admin/update/actor")
    public ResponseEntity<ResponseDto> updateActor(@RequestBody ActorUpdateRequest request) {
        actorService.updateActor(request);
        return ResponseDto.response(StatusCode.SUCCESS, "영화 배우 수정 성공");
    }

    // 전체 영화 목록 조회
    @GetMapping("/list/{page}/{size}")
    public ResponseEntity<ResponseDto> getAllMovieList(@PathVariable int page, @PathVariable int size) {
        List<MovieListResponse> response = movieService.getAllMovieList(page, size);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 장르별 영화 목록 조회
    @GetMapping("/list/genre/{genre}/{page}/{size}")
    public ResponseEntity<ResponseDto> getMovieListByGenre(@PathVariable String genre, @PathVariable int page, @PathVariable int size) {
        List<MovieListResponse> response = movieService.getMovieListByGenre(genre, page, size);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 국가별 영화 목록 조회
    @GetMapping("/list/country/{country}/{page}/{size}")
    public ResponseEntity<ResponseDto> getMovieListByCountry(@PathVariable String country, @PathVariable int page, @PathVariable int size) {
        List<MovieListResponse> response = movieService.getMovieListByCountry(country, page, size);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 연도별 영화 목록 조회
    @GetMapping("/list/year/{year}/{page}/{size}")
    public ResponseEntity<ResponseDto> getMovieListByYear(@PathVariable int year, @PathVariable int page, @PathVariable int size) {
        List<MovieListResponse> response = movieService.getMovieListByYear(year, page, size);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }


    // 배우별 영화 목록 조회
    @GetMapping("/list/actor/{actorName}/{page}/{size}")
    public ResponseEntity<ResponseDto> getMovieListByActor(@PathVariable String actorName, @PathVariable int page, @PathVariable int size) {
        List<MovieListResponse> response = movieService.getMovieListByActor(actorName, page, size);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 검색 영화 목록 조회
    @GetMapping("/list/search/{keyword}/{userSeq}/{page}/{size}")
    public ResponseEntity<ResponseDto> getMovieListByKeyword(@PathVariable String keyword, @PathVariable int userSeq, @PathVariable int page, @PathVariable int size) {
        List<MovieListResponse> response = movieService.getMovieListByKeyword(keyword, userSeq, page, size);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 영화 상세 정보 조회
    @GetMapping("/detail/{movieSeq}/{userSeq}")
    public ResponseEntity<ResponseDto> getMovieDetail(@PathVariable int movieSeq, @PathVariable int userSeq) {
        MovieDetailResponse response = movieService.getMovieDetail(movieSeq, userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 영화 ID 리스트로 영화 목록 조회
    @PostMapping("/list/movieId")
    public ResponseEntity<ResponseDto> getMovieListByMovieSeqList(@RequestBody List<Integer> request) {
        List<MovieListResponse> response = movieService.getMovieListByMovieSeqList(request);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 추천된 영화 목록 조회
    @PostMapping("/list/recommendation")
    public ResponseEntity<ResponseDto> getRecommendationList(@RequestBody RecommendMovieListRequest request) {
        List<MovieListResponse> response = movieService.getRecommendationList(request);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 사용자 최근 행동 로그 조회
    @GetMapping("/actions/{userSeq}")
    public ResponseEntity<ResponseDto> getUserActionList(@PathVariable int userSeq) {
        List<UserActionResponse> response = movieService.getUserActionList(userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // Top10 영화 목록 조회
    @GetMapping("/list/top10")
    public ResponseEntity<ResponseDto> getTopMovieList() {
        List<MovieListResponse> response = movieService.getTopMovieList();
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 영화 워드클라우드 조회
    @GetMapping("/wordCloud/{movieSeq}")
    public ResponseEntity<ResponseDto> getWordCloud(@PathVariable int movieSeq) {
        List<WordCloudResponse> response = movieService.getWordCloud(movieSeq);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 평점 높은 영화 30개 조회
    @GetMapping("/list/topRating")
    public ResponseEntity<ResponseDto> getTopRatingMovieList() {
        List<MovieListResponse> response = movieService.getTopRatingMovieList();
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }

    // 영화 추천 배우 조회
    @GetMapping("/recommendActor/{userSeq}")
    public ResponseEntity<ResponseDto> getRecommendActor(@PathVariable int userSeq) {
        String response = movieService.getRecommendActor(userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, response);
    }
}
