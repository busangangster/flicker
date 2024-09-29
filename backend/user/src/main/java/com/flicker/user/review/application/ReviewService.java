package com.flicker.user.review.application;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.kafka.dto.SentimentReview;
import com.flicker.user.common.kafka.dto.WordCloudReview;
import com.flicker.user.common.kafka.producer.CustomerProducer;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.review.domain.ReviewConverter;
import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.*;
import com.flicker.user.review.infrastructure.ReviewRepository;
import com.flicker.user.user.application.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final CustomerProducer kafkaProducer;

    @Transactional
    public boolean registerReview(RegisterReviewReqDto dto){

        Review find = reviewRepository.findByUserSeqAndMovieSeq(dto.getUserSeq(), dto.getMovieSeq());
        if(find != null){
            throw new RestApiException(StatusCode.DUPLICATED_REVIEW);
        }

        Review review = new Review(dto.getContent(), dto.getIsSpoiler(),dto.getMovieSeq(), dto.getReviewRating(), dto.getUserSeq());
        Review save = reviewRepository.save(review);

        //kafka 발행
        SentimentReview sentimentReview = new SentimentReview();
        sentimentReview.setReviewSeq(save.getReviewSeq());
        sentimentReview.setTimestamp(LocalDateTime.now());
        sentimentReview.setContent(save.getContent());
        System.out.println("sentimentReview = " + sentimentReview);
        kafkaProducer.sendSentimentLog(sentimentReview);

        WordCloudReview wordCloudReview = new WordCloudReview();
        wordCloudReview.setMovieSeq(save.getMovieSeq());
        wordCloudReview.setTimestamp(LocalDateTime.now());
        wordCloudReview.setContent(save.getContent());
        System.out.println("wordCloudReview = " + wordCloudReview);
        kafkaProducer.sendWordCloudLog(wordCloudReview);

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

    public List<ReviewDto> getMovieReviews(Integer movieSeq, Integer myUserSeq, String option, Pageable pageable) {

        Pageable sortedPageable;
        if ("like".equals(option)) {
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "likes"));
        } else {
            // 생성일 기준으로 최신순
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        Page<Review> allByMovieSeq = reviewRepository.findAllByMovieSeq(movieSeq, sortedPageable);

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (Review review : allByMovieSeq.getContent()) {
    //        System.out.println("review.getUserSeq() = " + review.getUserSeq());
            String nickname = userService.getNicknameByUserSeq(review.getUserSeq());
            ReviewDto reviewDto = reviewConverter.reviewToReviewDto(review, nickname, myUserSeq);
            reviewDtoList.add(reviewDto);
        }

        return reviewDtoList;
    }

    public List<ReviewDto> getPopularMovieReviews(Integer movieSeq, Integer myUserSeq) {
        List<Review> result = reviewRepository.findTop3ByMovieSeqAndIsSpoilerFalseOrderByLikesDesc(movieSeq);
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for(Review review : result){
            String nickname = userService.getNicknameByUserSeq(review.getUserSeq());
            ReviewDto reviewDto = reviewConverter.reviewToReviewDto(review, nickname, myUserSeq);
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
    }



    public List<ReviewDto> getUserReviews(Integer userSeq){

        List<Review> allByUserSeq = reviewRepository.findAllByUserSeq(userSeq);
        List<ReviewDto> reviewDtos = new ArrayList<>();

        for(Review review : allByUserSeq){
            String nickname = userService.getNicknameByUserSeq(review.getUserSeq());
            ReviewDto reviewDto = reviewConverter.reviewToReviewDto(review, nickname, userSeq);
            reviewDtos.add(reviewDto);
        }

        System.out.println(reviewDtos);

        return reviewDtos;
    }

    @Transactional
    public boolean updateSentimentScore(UpdateSentimentScoreDto dto){

        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));
        review.updateSentimentScore(dto.getSentimentScore());
        return true;
    }
}
