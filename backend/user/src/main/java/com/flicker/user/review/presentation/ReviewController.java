package com.flicker.user.review.presentation;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.review.application.ReviewService;
import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.AddLikeReviewReqDto;
import com.flicker.user.review.dto.DeleteReviewReqDto;
import com.flicker.user.review.dto.RegisterReviewReqDto;
import com.flicker.user.review.dto.RemoveLikeReviewReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록 TODO Kafka 메시지 발행
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

    // 리뷰 삭제 TODO Kafka 메시지 발행
    @DeleteMapping()
    public ResponseEntity<ResponseDto> deleteReview(@RequestBody DeleteReviewReqDto dto){
        if(dto.getReviewSeq() == null || dto.getUserSeq() == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!reviewService.deleteReview(dto)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "리뷰가 삭제 되었습니다.");
    }

    // 리뷰 좋아요 등록
    @PostMapping("/likeReview")
    public ResponseEntity<ResponseDto> addLikeReview(@RequestBody AddLikeReviewReqDto dto) {
        if(dto.getUserSeq() == null || dto.getReviewSeq() == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!reviewService.addLikeReview(dto)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS,"리뷰 좋아요 성공");
    }
    // 리뷰 좋아요 삭제
    @DeleteMapping("/likeReview")
    public ResponseEntity<ResponseDto> removeLikeReview(@RequestBody RemoveLikeReviewReqDto dto) {
        if(dto.getUserSeq() == null || dto.getReviewSeq() == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!reviewService.removeLikeReview(dto)){
            throw new RestApiException(StatusCode.UNAUTHORIZED_REQUEST,"자신의 리뷰 좋아요만 삭제할 수 있습니다.");
        }

        return ResponseDto.response(StatusCode.SUCCESS,"리뷰 좋아요 취소 성공");
    }

    // 영화별 리뷰 조회
    @GetMapping("/movies/{movieSeq}")
    public ResponseEntity<ResponseDto> getMovieReviews(@PathVariable("movieSeq") Integer movieSeq) {
        if(movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        List<Review> movieReviews = reviewService.getMovieReviews(movieSeq);
        return ResponseDto.response(StatusCode.SUCCESS, movieReviews);
    }

    // 사용자 별 리뷰 조회
    @GetMapping()
    public ResponseEntity<ResponseDto> getUserReviews(@RequestParam Integer userSeq) {
        if(userSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        List<Review> userReviews = reviewService.getUserReviews(userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, userReviews);
    }
}
