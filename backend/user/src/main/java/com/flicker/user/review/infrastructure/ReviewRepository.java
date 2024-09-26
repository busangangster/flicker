package com.flicker.user.review.infrastructure;

import com.flicker.user.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    public Review findByUserSeqAndMovieSeq(Integer userSeq, Integer movieSeq);
}
