package com.flicker.user.review.application;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.DeleteReviewReqDto;
import com.flicker.user.review.dto.RegisterReviewReqDto;
import com.flicker.user.review.infrastructure.ReviewRepository;
import com.flicker.user.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public boolean registerReview(RegisterReviewReqDto dto){

        Review review = reviewRepository.findByUserSeqAndMovieSeq(dto.getUserSeq(), dto.getMovieSeq());
        if(review == null){
            throw new RestApiException(StatusCode.DUPLICATED_REVIEW);
        }
        reviewRepository.save(review);
        return true;
    }

    @Transactional
    public boolean deleteReview(DeleteReviewReqDto dto){
        Optional<Review> byId = reviewRepository.findById(dto.getReviewSeq());

        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));

        reviewRepository.delete(review);

        return true;
    }
}
