package com.flicker.movie.movie.domain.entity;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 제한하여 외부에서 직접 호출하지 못하게 함
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@Builder // Builder 패턴을 통해 객체를 생성할 수 있도록 설정
@Getter // 모든 필드에 대해 getter 메서드 자동 생성
public class WordCloud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wordCloudSeq; // 단어 클라우드의 고유 식별자 (자동 생성됨 )

    @Column(nullable = false)
    private String keyword; // 단어

    @Column(nullable = false)
    private int count; // 단어의 빈도수

    @Column(nullable = false)
    private LocalDateTime createdAt; // 단어 클라우드 생성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_seq", nullable = false) // 외래키 설정, 영화와의 관계를 나타냄
    private Movie movie; // 다대일 관계로 연결된 Movie 엔티티, 배우는 한 영화에만 속할 수 있음

    // 양방향 관계 설정을 위한 메서드
    protected void setMovie(Movie movie) {
        this.movie = movie; // 영화와 배우 간의 양방향 관계 설정
    }

    // 빌더 내부에서 유효성 검증 추가 (길이 검증 포함)
    public static class WordCloudBuilder {
        public WordCloud build() {
            validate();  // 빌드 시 유효성 검증 수행
            return new WordCloud(wordCloudSeq, keyword, count, createdAt, movie);
        }

        // 유효성 검증 메서드 (길이 검증 포함)
        private void validate() {
            if (keyword == null || keyword.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "키워드를 확인해주세요. (길이초과 또는 null).");
            } else if (count < 0) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "키워드 빈도 수가 잘못되었습니다.");
            }
        }
    }
}
