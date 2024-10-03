package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.RedisNewMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisNewMovieRepository extends CrudRepository<RedisNewMovie, String> {
}
