package com.flicker.logger.batch;

import com.flicker.logger.entity.MovieAverageRating;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewAggregationService {

    private final MongoTemplate mongoTemplate;

    public ReviewAggregationService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<MovieAverageRating> aggregateMovieRatings() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("movieSeq")
                        .count().as("reviewCount")
                        .sum("rating").as("totalRating"),
                Aggregation.project("reviewCount", "totalRating")
                        .and("totalRating").divide("reviewCount").as("averageRating")
                        .and("_id").as("movieSeq")
        );

        AggregationResults<MovieAverageRating> results = mongoTemplate.aggregate(
                aggregation, "reviews", MovieAverageRating.class);

        return results.getMappedResults();
    }
}
