package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.SearchResult;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SearchResultRepository extends CrudRepository<SearchResult, String> {
    // 키워드를 기반으로 SearchResult를 찾는 메서드
    Optional<SearchResult> findByKeyword(String redisKey);
}
