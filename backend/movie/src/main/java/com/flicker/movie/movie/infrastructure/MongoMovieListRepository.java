package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.MongoMovieList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoMovieListRepository extends MongoRepository<MongoMovieList, String> {

    // 키워드를 기반으로 MongoMovieList를 찾는 메서드
    Optional<MongoMovieList> findByMongoKey(String mongoKey);
}
