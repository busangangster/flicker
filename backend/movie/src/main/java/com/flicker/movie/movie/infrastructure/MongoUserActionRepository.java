package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.MongoUserAction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoUserActionRepository extends MongoRepository<MongoUserAction, Integer> {

    List<MongoUserAction> findByUserSeqOrderByTimestampDesc(int userSeq, Pageable pageable);

}
