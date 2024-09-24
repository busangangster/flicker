//package com.flicker.logger.batch;
//
//import com.flicker.logger.dto.MovieRatingData;
//import com.flicker.logger.entity.MovieAverageRating;
//import com.flicker.logger.entity.Review;
//import org.springframework.batch.item.ItemProcessor;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ReviewProcessor implements ItemProcessor<Review, MovieAverageRating> {
//
//    private Map<Long, MovieRatingData> movieRatingMap = new HashMap<>();
//
//    @Override
//    public MovieAverageRating process(Review review) throws Exception {
//
//        Long movieSeq = review.getMovieSeq();
//        if (movieRatingMap.containsKey(movieSeq)) {
//            MovieRatingData data = movieRatingMap.get(movieSeq);
//            data.setTotalRating();
//        }
//    }
//}
