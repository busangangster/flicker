package com.flicker.logger.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private int movieCount;
    private double averageRating;
}
