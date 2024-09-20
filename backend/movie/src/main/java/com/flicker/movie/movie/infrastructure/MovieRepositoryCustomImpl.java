package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;

import static com.flicker.movie.movie.domain.entity.QActor.actor;
import static com.flicker.movie.movie.domain.entity.QMovie.movie;


@Repository
@RequiredArgsConstructor
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Movie> findByKeywordInTitlePlotActorGenre(String keyword, String delYN) {
        return queryFactory
                .selectFrom(movie)
                .join(movie.actors, actor) // Movie와 Actor의 조인
                .where(
                        movie.delYN.eq(delYN) // 삭제 여부 조건
                                .and(keywordContainsInTitlePlotGenreActor(keyword)) // 키워드 검색 조건
                )
                .orderBy(movie.movieDetail.movieYear.desc()) // 영화 출시년도 내림차순 정렬
                .fetch(); // 결과 리스트 반환
    }

    // 키워드가 제목, 줄거리, 장르 또는 배우 이름에 포함되는지 확인하는 조건을 구성하는 메서드
    private BooleanExpression keywordContainsInTitlePlotGenreActor(String keyword) {
        String keywordPattern = "%" + keyword + "%"; // LIKE 쿼리를 위한 패턴
        return movie.movieDetail.movieTitle.like(keywordPattern)
                .or(movie.movieDetail.moviePlot.like(keywordPattern))
                .or(movie.movieDetail.genre.like(keywordPattern))
                .or(actor.actorName.like(keywordPattern));
    }
}
