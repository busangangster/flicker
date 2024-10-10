package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.NewMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NewMovieRepository extends JpaRepository<NewMovie, Integer> {
}
