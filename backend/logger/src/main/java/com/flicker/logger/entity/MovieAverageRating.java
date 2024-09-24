package com.flicker.logger.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movie_avg_rating")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieAverageRating {

    @Id
    private String id;
    private Long movieSeq;
    private int reviewCount;
    private int totalRating;
    private double averageRating;

    public MovieAverageRating(Long movieSeq, int reviewCount, int totalRating) {
        this.movieSeq = movieSeq;
        this.reviewCount = reviewCount;
        this.totalRating = totalRating;
    }
}
