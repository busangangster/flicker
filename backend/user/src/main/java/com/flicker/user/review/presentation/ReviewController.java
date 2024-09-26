package com.flicker.user.review.presentation;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.review.application.ReviewService;
import com.flicker.user.review.dto.AddLikeReviewReqDto;
import com.flicker.user.review.dto.DeleteReviewReqDto;
import com.flicker.user.review.dto.RegisterReviewReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping()
    public ResponseEntity<ResponseDto> registerReview(@RequestBody RegisterReviewReqDto dto){
        if(dto.getReviewRating() == null ||
                dto.getContent() == null ||
                dto.getUserSeq() == null ||
                dto.getMovieSeq() == null ||
                dto.getIsSpoiler() == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!reviewService.registerReview(dto)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "리뷰가 등록 되었습니다.");
    }

    // 리뷰 삭제
    @DeleteMapping()
    public ResponseEntity<ResponseDto> deleteReview(@RequestBody DeleteReviewReqDto dto){
        if(dto.getReviewSeq() == null || dto.getUserSeq() == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!reviewService.deleteReview(dto)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "리뷰가 삭젠 되었습니다.");
    }

    // 리뷰 좋아요 등록
    @PostMapping("/{reviewSeq}")
    public ResponseEntity<ResponseDto> addLikeReview(@RequestBody AddLikeReviewReqDto dto) {
        if(dto.get)
    }
    // 리뷰 좋아요 삭제

    // 전체 평점 및 리뷰 조회

    // 포토카드 조회
}
