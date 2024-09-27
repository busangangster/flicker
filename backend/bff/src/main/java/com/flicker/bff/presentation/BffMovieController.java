package com.flicker.bff.presentation;

import com.flicker.bff.application.BffMovieService;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.dto.ActorAddRequest;
import com.flicker.bff.dto.MovieCreateRequest;
import com.flicker.bff.dto.MovieUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff/movie")
@RequiredArgsConstructor
public class BffMovieController {

    private final BffMovieService bffMovieService;

    // 영화 정보, 배우 정보를 등록
    @PostMapping("/admin/create")
    public Mono<ResponseEntity<ResponseDto>> createMovie(@RequestBody MovieCreateRequest request) {
        return bffMovieService.createMovie(request);
    }

    // 영화 정보 수정
    @PutMapping("/admin/update/detail")
    public Mono<ResponseEntity<ResponseDto>> updateMovie(@RequestBody MovieUpdateRequest request) {
        return bffMovieService.updateMovie(request);
    }

    // 영화 삭제
    @PutMapping("/admin/delete")
    public Mono<ResponseEntity<ResponseDto>> deleteMovie(@RequestParam int movieSeq) {
        return bffMovieService.deleteMovie(movieSeq);
    }

    // 영화 배우 추가
    @PostMapping("/admin/add/actor")
    public Mono<ResponseEntity<ResponseDto>> addActor(@RequestBody ActorAddRequest request) {
        return bffMovieService.addActor(request);
    }

    // 영화 배우 삭제
    @DeleteMapping("/admin/delete/actor/{actorSeq}/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> deleteActor(@PathVariable int actorSeq, @PathVariable int movieSeq) {
        return bffMovieService.deleteActor(actorSeq, movieSeq);
    }

    // 영화 배우 수정
    @PutMapping("/admin/update/actor")
    public Mono<ResponseEntity<ResponseDto>> updateActor(@RequestBody ActorAddRequest request) {
        return bffMovieService.updateActor(request);
    }

    // 전체 영화 목록 조회
    @GetMapping("/list/{page}/{size}")
    public Mono<ResponseEntity<ResponseDto>> getMovieList(@PathVariable int page, @PathVariable int size) {
        return bffMovieService.getMovieList(page, size);
    }

    // 장르별 영화 목록 조회
    @GetMapping("/list/genre/{genre}/{page}/{size}")
    public Mono<ResponseEntity<ResponseDto>> getMovieListByGenre(@PathVariable String genre, @PathVariable int page, @PathVariable int size) {
        return bffMovieService.getMovieListByGenre(genre, page, size);
    }

    // 배우별 영화 목록 조회
    @GetMapping("/list/actor/{actorName}/{page}/{size}")
    public Mono<ResponseEntity<ResponseDto>> getMovieListByActor(@PathVariable String actorName, @PathVariable int page, @PathVariable int size) {
        return bffMovieService.getMovieListByActor(actorName, page, size);
    }

    // 검색 영화 목록 조회
    @GetMapping("/list/search/{keyword}/{userSeq}/{page}/{size}")
    public Mono<ResponseEntity<ResponseDto>> getMovieListBySearch(@PathVariable String keyword, @PathVariable int userSeq, @PathVariable int page, @PathVariable int size) {
        return bffMovieService.getMovieListBySearch(keyword, userSeq, page, size);
    }

    // 영화 상세 정보 조회
    @GetMapping("/detail/{movieSeq}/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> getMovieDetail(@PathVariable int movieSeq, @PathVariable int userSeq) {
        return bffMovieService.getMovieDetail(movieSeq, userSeq);
    }

    // 행동 기반 추천 영화 목록 조회
    @GetMapping("/list/recommendation/action/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> getActionRecommendationList(@PathVariable int userSeq) {
        return bffMovieService.getActionRecommendationListAsync(userSeq);
    }

    // 리뷰 기반 추천 영화 목록 조회
    @GetMapping("/list/recommendation/review/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> getReviewRecommendationList(@PathVariable int userSeq) {
        return bffMovieService.getReviewRecommendationList(userSeq);
    }


    // 1일 기준 TOP 10 영화 목록 조회
    @GetMapping("/list/top10")
    public Mono<ResponseEntity<ResponseDto>> getTopMovieList() {
        return bffMovieService.getTopMovieList();
    }
}
