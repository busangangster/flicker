package com.flicker.logger.repository;

import com.flicker.logger.entity.MovieAverageRating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieAverageRatingRepository extends MongoRepository<MovieAverageRating, String> {
}
