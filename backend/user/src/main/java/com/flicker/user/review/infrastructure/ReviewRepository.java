package com.flicker.user.review.infrastructure;

import com.flicker.user.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    public Review findByUserSeqAndMovieSeq(Integer userSeq, Integer movieSeq);
    public List<Review> findAllByMovieSeq(Integer movieSeq);
    public List<Review> findAllByUserSeq(Integer userSeq);
}
