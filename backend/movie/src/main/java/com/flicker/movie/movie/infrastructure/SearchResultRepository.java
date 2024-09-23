package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.SearchResult;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SearchResultRepository extends CrudRepository<SearchResult, String> {

    Optional<SearchResult> findByKeyword(String redisKey);
}
