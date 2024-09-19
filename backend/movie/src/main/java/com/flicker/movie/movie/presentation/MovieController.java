package com.flicker.movie.movie.presentation;

import com.flicker.movie.common.module.response.ResponseDto;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.application.ActorService;
import com.flicker.movie.movie.application.MovieService;
import com.flicker.movie.movie.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
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

    // 배우 중복 처리

    // 영화 상세 정보 조회

    // 전체 영화 목록 조회

    // 장르별 영화 목록 조회

    // 검색 영화 목록 조회

    // 행동 기반 추천 영화 목록 조회

    // 평점,리뷰 기반 추천 영화 목록 조회

    // 관리자 여부 확인
}
