package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.RecommendActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendActorRepository extends JpaRepository<RecommendActor, Integer> {
    void deleteByUserSeq(Integer userSeq);

    List<RecommendActor> findByUserSeq(int userSeq);
}
