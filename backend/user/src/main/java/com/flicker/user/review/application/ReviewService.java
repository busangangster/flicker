package com.flicker.user.review.application;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.kafka.dto.MovieInfo;
import com.flicker.user.common.kafka.dto.SentimentReview;
import com.flicker.user.common.kafka.dto.WordCloudReview;
import com.flicker.user.common.kafka.producer.CustomerProducer;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.review.domain.ReviewConverter;
import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.*;
import com.flicker.user.review.infrastructure.ReviewRepository;
import com.flicker.user.user.application.UserService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
        kafkaProducer.sendSentimentLog(sentimentReview);

        WordCloudReview wordCloudReview = new WordCloudReview();
        wordCloudReview.setMovieSeq(save.getMovieSeq());
        wordCloudReview.setTimestamp(LocalDateTime.now());
        wordCloudReview.setContent(save.getContent());
        wordCloudReview.setUserSeq(save.getUserSeq());
        wordCloudReview.setRating(save.getReviewRating());
        kafkaProducer.sendWordCloudLog(wordCloudReview);

        MovieInfo movieInfo = new MovieInfo();
        movieInfo.setUserSeq(dto.getUserSeq());
        movieInfo.setMovieSeq(save.getMovieSeq());
        movieInfo.setReviewSeq(save.getReviewSeq());
        movieInfo.setRating(save.getReviewRating());
        movieInfo.setType("REVIEW");
        movieInfo.setAction("CREATE");
        movieInfo.setTimestamp(LocalDateTime.now());
        kafkaProducer.sendMovieInfo(movieInfo);

        return true;
    }

    @Transactional
    public boolean deleteReview(DeleteReviewReqDto dto){

        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));

        reviewRepository.delete(review);

        MovieInfo movieInfo = new MovieInfo();
        movieInfo.setUserSeq(dto.getUserSeq());
        movieInfo.setMovieSeq(review.getMovieSeq());
        movieInfo.setReviewSeq(review.getReviewSeq());
        movieInfo.setRating(review.getReviewRating());
        movieInfo.setType("REVIEW");
        movieInfo.setAction("DELETE");
        movieInfo.setTimestamp(LocalDateTime.now());
        kafkaProducer.sendMovieInfo(movieInfo);

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
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "likes").and(Sort.by(Sort.Direction.ASC, "reviewSeq")));
        } else if("date".equals(option)){
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.ASC, "reviewSeq")));
        }
        else {
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt").and(Sort.by(Sort.Direction.ASC, "reviewSeq")));
        }

        List<Integer> list = new ArrayList<>();
        list.add(26301);
        list.add(25588);
        Page<Review> allByMovieSeq = reviewRepository.findAllByMovieSeqIn(list, sortedPageable);

        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for (Review review : allByMovieSeq.getContent()) {
            String nickname = userService.getNicknameByUserSeq(review.getUserSeq());
            ReviewDto reviewDto = reviewConverter.reviewToReviewDto(review, nickname, myUserSeq);
            reviewDtoList.add(reviewDto);
        }

        return reviewDtoList;
    }

    public List<ReviewDto> getPopularMovieReviews(Integer movieSeq, Integer myUserSeq) {
        List<Review> result = reviewRepository.findTop3ByMovieSeqAndIsSpoilerFalseAndContentIsNotNullOrderByLikesDesc(movieSeq);
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


        return reviewDtos;
    }

    @Transactional
    public boolean updateSentimentScore(UpdateSentimentScoreDto dto){

        Review review = reviewRepository.findById(dto.getReviewSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.CAN_NOT_FIND_REVIEW));
        review.updateSentimentScore(dto.getSentimentScore());
        return true;
    }

    public MovieReviewRatingCount getMovieReviewRatingDistribute(Integer movieSeq){


        List<ReviewRatingCountDto> reviewRatingCountDtos = reviewRepository.countReviewRatingsByMovieSeq(movieSeq);


        ReviewRatingCountDto tmp = new ReviewRatingCountDto(0.5,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(1.0,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(1.5,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(2.0,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(2.5,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(3.0,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(3.5,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(4.0,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(4.5,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);
        tmp = new ReviewRatingCountDto(5.0,0L);
        if(!reviewRatingCountDtos.contains(tmp)) reviewRatingCountDtos.add(tmp);

        reviewRatingCountDtos.sort(Comparator.comparing(ReviewRatingCountDto::getReviewRating, Comparator.nullsFirst(Double::compareTo)));


        MovieReviewRatingCount movieReviewRatingCount = new MovieReviewRatingCount();

        int sum = 0;
        for(ReviewRatingCountDto item : reviewRatingCountDtos){
            sum += item.getCount();
        }
        movieReviewRatingCount.setTotalCnt(sum);
        movieReviewRatingCount.setReviewRatingCount(reviewRatingCountDtos);

        return movieReviewRatingCount;
    }

    public MyPageReviewCntDto getMyPageReviewCnt(Integer userSeq){
        Tuple result = reviewRepository.findLikesSumAndReviewCountByUserSeq(userSeq);

        Long totalLikes = result.get(0, Long.class); // SUM(likes)
        Long reviewCount = result.get(1, Long.class); // COUNT(*)

        MyPageReviewCntDto dto = new MyPageReviewCntDto();
        dto.setReviewCnt(reviewCount);
        dto.setLikes(totalLikes);
        return dto;
    }

    public CheckAlreadyReviewDto checkAlreadyReview(Integer userSeq, Integer movieSeq){
        Review review = reviewRepository.findByUserSeqAndMovieSeq(userSeq, movieSeq);

        if(review == null){
            CheckAlreadyReviewDto dto = new CheckAlreadyReviewDto();

            dto.setAlreadyReview(false);
            dto.setReviewDto(null);

            return dto;
        }
        else{
            CheckAlreadyReviewDto dto = new CheckAlreadyReviewDto();

            String nicknameByUserSeq = userService.getNicknameByUserSeq(userSeq);
            dto.setReviewDto(reviewConverter.reviewToReviewDto(review,nicknameByUserSeq,userSeq));
            dto.setAlreadyReview(true);

            return dto;
        }

    }

    public List<Integer> getMostReviewMovieSeq(){
        List<Integer> movieSeqWithAtLeast2000Reviews = reviewRepository.findMovieSeqWithAtLeast2000Reviews();

        if(movieSeqWithAtLeast2000Reviews == null){
            throw new RestApiException(StatusCode.NO_SUCH_ELEMENT);
        }
        return movieSeqWithAtLeast2000Reviews;
    }
}
