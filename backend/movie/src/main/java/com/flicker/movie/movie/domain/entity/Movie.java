package com.flicker.movie.movie.domain.entity;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = "actors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieSeq;  // 영화의 고유 식별자, 자동으로 생성됨

    @Embedded
    @Column(nullable = false)
    private MovieDetail movieDetail;  // 영화의 세부 정보를 담은 값 객체 (Embedded 타입)

    @Column(nullable = false)
    private double movieRating;  // 영화 평점

    @Column(name = "DEL_YN", nullable = false)
    private String delYN; // 영화 삭제 여부를 나타내는 플래그 (Y/N)

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column
    private final List<Actor> actors = new ArrayList<>();  // 영화에 출연한 배우들의 리스트, 영화와 양방향 관계를 설정하며, 영화가 삭제되면 배우들도 함께 삭제됨 (CascadeType.ALL)

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column
    private final List<WordCloud> wordClouds = new ArrayList<>();  // 영화에 대한 단어 클라우드 리스트, 영화와 양방향 관계를 설정하며, 영화가 삭제되면 단어 클라우드도 함께 삭제됨 (CascadeType.ALL)


    @PrePersist
    public void prePersist() {
        this.delYN = "N";  // 영화가 생성되면 기본적으로 삭제되지 않음을 표시 (DEL_YN = "N")
        this.movieRating = 0;  // 영화 평점은 0으로 초기화
    }

    // 영화에 배우 추가
    private void addActor(Actor actor) {
        try {
            // 배우 이름을 기준으로 중복 여부 확인
            boolean actorExists = this.actors.stream()
                    .anyMatch(existingActor -> existingActor.getActorName().equals(actor.getActorName()));
            // 이미 같은 이름의 배우가 존재하는 경우 예외 발생
            if (actorExists) {
                throw new RestApiException(StatusCode.DUPLICATE_ACTOR, "이미 같은 이름의 배우가 추가되었습니다.");
            }
            actor.setMovie(this);  // 양방향 관계 설정 (Actor 객체가 이 영화에 속해 있음을 명시)
            this.actors.add(actor);  // 배우 리스트에 새로운 배우 추가
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "배우 추가 중 오류가 발생했습니다.");
        }
    }

    // 여러 배우를 영화에 추가
    @Transactional
    public void addActors(List<Actor> actorList) {
        try {
            for (Actor actor : actorList) {
                addActor(actor);  // 배우를 하나씩 추가
            }
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "여러 배우 추가 중 오류가 발생했습니다.");
        }
    }

    // 단어 클라우드 추가
    private void addWordCloud(WordCloud wordCloud) {
        try {
            // 단어 클라우드의 단어를 기준으로 중복 여부 확인
            boolean wordExists = this.wordClouds.stream()
                    .anyMatch(existingWordCloud -> existingWordCloud.getKeyword().equals(wordCloud.getKeyword()));
            // 이미 같은 단어의 단어 클라우드가 존재하는 경우 예외 발생
            if (wordExists) {
                throw new RestApiException(StatusCode.DUPLICATE_KEYWORD, "해당 영화 단어 클라우드에 이미 같은 키워드가 추가되었습니다.");
            }
            wordCloud.setMovie(this);  // 양방향 관계 설정
            this.wordClouds.add(wordCloud);  // 단어 클라우드 리스트에 새로운 단어 클라우드 추가
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "단어 클라우드 추가 중 오류가 발생했습니다.");
        }
    }

    // 여러 단어클라우드를 영화에 추가
    @Transactional
    public void addWordClouds(List<WordCloud> wordCloudList) {
        try {
            for (WordCloud wordCloud : wordCloudList) {
                addWordCloud(wordCloud);  // 단어 클라우드를 하나씩 추가
            }
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "여러 단어 클라우드 추가 중 오류가 발생했습니다.");
        }
    }

    // 배우 조회
    public Actor getActor(int actorSeq) {
        try {
            // actors 리스트에서 해당 배우를 찾아 반환
            return this.actors.stream()
                    .filter(a -> a.getActorSeq() == actorSeq)
                    .findFirst()
                    .orElseThrow(() -> new RestApiException(StatusCode.NOT_FOUND, "해당 배우 정보를 찾을 수 없습니다."));
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "배우 조회 중 오류가 발생했습니다.");
        }
    }

    // 영화에서 배우 제거
    @Transactional
    public void removeActor(int actorSeq) {
        try {
            Actor removeActor = this.getActor(actorSeq);
            removeActor.setMovie(null);  // 양방향 관계 해제
            this.actors.remove(removeActor);  // 배우 리스트에서 해당 배우를 제거
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "배우 제거 중 오류가 발생했습니다.");
        }
    }

    // 영화 상세 정보 업데이트
    public void updateMovieDetail(MovieDetail newMovieDetail) {
        try {
            this.movieDetail = newMovieDetail;  // 영화 세부 정보 업데이트
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 세부 정보 업데이트 중 오류가 발생했습니다.");
        }
    }

    // 영화 삭제
    @Transactional
    public void deleteMovie(RedisTopMovie redisTopMovie) {
        try {
            // TOP10에 포함된 영화인 경우 삭제 불가
            if (redisTopMovie.getMovieSeqs().contains(this.movieSeq)) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "TOP10에 포함된 영화는 삭제할 수 없습니다.");
            }
            // 영화에 연결된 배우들과의 관계 해제
            for (Actor actor : this.actors) {
                actor.setMovie(null);  // 양방향 관계 해제
            }
            this.actors.clear();  // 영화의 배우 리스트를 비움
            this.delYN = "Y";  // 영화 삭제를 표시
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 삭제 중 오류가 발생했습니다.");
        }
    }

    // 영화 평점 업데이트
    public void updateMovieRating(double newRating) {
        try {
            newRating = Math.round(newRating * 10) / 10.0;
            if (newRating < 0 || newRating > 5) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 평점은 0~5 사이여야 합니다.");
            }
            this.movieRating = newRating;  // 영화 평점을 새로운 값으로 업데이트
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 평점 업데이트 중 오류가 발생했습니다.");
        }
    }

    // 워드 클라우드 초기화
    @Transactional
    public void clearWordClouds() {
        try {
            // 모든 WordCloud의 movie 참조를 null로 설정하여 관계를 끊음
            for (WordCloud wordCloud : this.wordClouds) {
                wordCloud.setMovie(null);
            }
            this.wordClouds.clear();  // 워드 클라우드 리스트를 비움
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "워드 클라우드 초기화 중 오류가 발생했습니다.");
        }
    }
}
