package com.flicker.user.review.application;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.AddLikeReviewReqDto;
import com.flicker.user.review.dto.DeleteReviewReqDto;
import com.flicker.user.review.dto.RegisterReviewReqDto;
import com.flicker.user.review.dto.RemoveLikeReviewReqDto;
import com.flicker.user.review.infrastructure.ReviewRepository;
import com.flicker.user.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public boolean registerReview(RegisterReviewReqDto dto){

        Review find = reviewRepository.findByUserSeqAndMovieSeq(dto.getUserSeq(), dto.getMovieSeq());
        if(find != null){
            throw new RestApiException(StatusCode.DUPLICATED_REVIEW);
        }

        Review review = new Review(dto.getContent(), dto.getIsSpoiler(),dto.getMovieSeq(), dto.getReviewRating(), dto.getUserSeq());
        reviewRepository.save(review);
        return true;
    }

    @Transactional
    public boolean deleteReview(DeleteReviewReqDto dto){

        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));

        reviewRepository.delete(review);

        return true;
    }

    @Transactional
    public boolean addLikeReview(AddLikeReviewReqDto dto){
        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));

        return review.addLikeReview(dto.getUserSeq());
    }

    @Transactional
    public boolean removeLikeReview(RemoveLikeReviewReqDto dto){
        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));
        return review.removeLikeReview(dto.getUserSeq());
    }

    public List<Review> getMovieReviews(Integer movieSeq){
        List<Review> allByMovieSeq = reviewRepository.findAllByMovieSeq(movieSeq);
        System.out.println("allByMovieSeq = " + allByMovieSeq);
        return reviewRepository.findAllByMovieSeq(movieSeq);
    }

    public List<Review> getUserReviews(Integer userSeq){
        return reviewRepository.findAllByUserSeq(userSeq);
    }
}
