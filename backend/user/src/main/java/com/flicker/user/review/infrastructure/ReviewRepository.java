package com.flicker.user.review.infrastructure;

import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.ReviewRatingCountDto;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    @Query("SELECT COALESCE(SUM(r.likes), 0), COUNT(r) FROM Review r WHERE r.userSeq = :userSeq")
    Tuple findLikesSumAndReviewCountByUserSeq(@Param("userSeq") Integer userSeq);

    public Review findByUserSeqAndMovieSeq(Integer userSeq, Integer movieSeq);
    public List<Review> findAllByMovieSeq(Integer movieSeq);
    public List<Review> findAllByUserSeq(Integer userSeq);
    public Page<Review> findAllByMovieSeq(Integer movieSeq, Pageable pageable);


    public Page<Review> findAllByMovieSeqIn(List<Integer> movieSeqs, Pageable pageable);
    public Page<Review> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM review r WHERE r.review_seq > :lastSeq ORDER BY r.review_seq ASC LIMIT :size", nativeQuery = true)
    List<Review> findAllNoOffset(@Param("lastSeq") int lastSeq, @Param("size") int size);

    // movieSeq가 같고, spoiler가 false인 리뷰 중에서 likes가 가장 높은 3개를 가져오는 쿼리
    List<Review> findTop3ByMovieSeqAndIsSpoilerFalseAndContentIsNotNullOrderByLikesDesc(Integer movieSeq);

    @Query("SELECT new com.flicker.user.review.dto.ReviewRatingCountDto(r.reviewRating, COUNT(r)) " +
            "FROM Review r " +
            "WHERE r.movieSeq = :movieSeq " +
            "GROUP BY r.reviewRating")
    List<ReviewRatingCountDto> countReviewRatingsByMovieSeq(Integer movieSeq);

    @Query("SELECT r.movieSeq " +
            "FROM Review r " +
            "GROUP BY r.movieSeq " +
            "HAVING COUNT(r) >= 2000")
    List<Integer> findMovieSeqWithAtLeast2000Reviews();

}
