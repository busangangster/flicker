package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.RedisSearchResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisSearchResultRepository extends CrudRepository<RedisSearchResult, String> {
    // 키워드를 기반으로 SearchResult를 찾는 메서드
    Optional<RedisSearchResult> findByKeyword(String redisKey);
}
