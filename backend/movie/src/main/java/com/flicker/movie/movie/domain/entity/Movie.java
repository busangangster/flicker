package com.flicker.movie.movie.domain.entity;

import com.flicker.movie.movie.domain.vo.MovieDetail;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieSeq;

    @Embedded
    private MovieDetail movieDetail;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Actor> actors = new ArrayList<>();

    // 생성
    public Movie(MovieDetail newMovieDetail) {
        this.movieDetail = newMovieDetail;
    }

    // 비즈니스 메서드: 영화에 배우 추가
    public void addActor(Actor actor) {
        actor.setMovie(this); // 양방향 관계 설정
        this.actors.add(actor);
    }

    // 비즈니스 메서드: 영화에서 배우 제거
    public void removeActor(Actor actor) {
        this.actors.remove(actor);
        actor.setMovie(null); // 양방향 관계 해제
    }

    // 비즈니스 메서드: 영화 상세 정보 업데이트 (불변 객체를 새로 생성해서 할당)
    public void updateMovieDetail(MovieDetail newMovieDetail) {
        this.movieDetail = newMovieDetail;
    }

    // 영화에 대한 필수 정보 조회 메서드
    public String getTitle() {
        return this.movieDetail.getMovieTitle();
    }

}
