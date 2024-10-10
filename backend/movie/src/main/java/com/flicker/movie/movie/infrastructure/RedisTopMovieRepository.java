package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.RedisTopMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTopMovieRepository extends CrudRepository<RedisTopMovie, String> {
}
