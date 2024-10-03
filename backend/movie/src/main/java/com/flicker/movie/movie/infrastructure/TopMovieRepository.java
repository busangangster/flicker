package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.TopMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopMovieRepository extends JpaRepository<TopMovie, Integer> {

    List<TopMovie> findByName(int i);
}
