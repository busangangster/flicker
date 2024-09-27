package com.flicker.user.review.infrastructure;

import com.flicker.user.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    public Review findByUserSeqAndMovieSeq(Integer userSeq, Integer movieSeq);
    public List<Review> findAllByMovieSeq(Integer movieSeq);
    public List<Review> findAllByUserSeq(Integer userSeq);
    public Page<Review> findAllByMovieSeq(Integer movieSeq, Pageable pageable);
    // movieSeq가 같고, spoiler가 false인 리뷰 중에서 likes가 가장 높은 3개를 가져오는 쿼리
    List<Review> findTop3ByMovieSeqAndIsSpoilerFalseOrderByLikesDesc(Integer movieSeq);

}
