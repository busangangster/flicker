package com.flicker.movie.movie.domain.entity;

import com.flicker.common.module.exception.RestApiException;
import com.flicker.common.module.status.StatusCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 제한하여 외부에서 직접 호출하지 못하게 함
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@Builder // Builder 패턴을 통해 객체를 생성할 수 있도록 설정
@ToString // 객체 정보를 출력할 때 사용되는 toString 메서드를 자동으로 생성
@Getter // 모든 필드에 대해 getter 메서드 자동 생성
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int actorSeq; // 배우의 고유 식별자 (자동 생성됨)

    @Column(nullable = false)
    private String actorName; // 배우 이름

    @Column
    private String role; // 영화에서 배우가 맡은 역할

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_seq", nullable = false) // 외래키 설정, 영화와의 관계를 나타냄
    private Movie movie; // 다대일 관계로 연결된 Movie 엔티티, 배우는 한 영화에만 속할 수 있음

    // 양방향 관계 설정을 위한 메서드
    protected void setMovie(Movie movie) {
        this.movie = movie; // 영화와 배우 간의 양방향 관계 설정
    }

    // 배우 정보 변경 메서드 (비즈니스 로직)
    public void updateActor(String newRole, String newActorName) {
        this.role = newRole; // 배우의 역할을 업데이트
        this.actorName = newActorName; // 배우 이름을 업데이트
    }

    // 빌더 내부에서 유효성 검증 메서드 추가
    public static class ActorBuilder {
        public Actor build() {
            validate();  // 빌드 시 유효성 검증 수행
            return new Actor(actorSeq, actorName, role, movie);
        }

        // 유효성 검증 메서드
        private void validate() {
            if (actorName == null || actorName.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "배우 이름을 확인해주세요 (길이초과 또는 null)");
            } else if (role != null && role.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "배우 역할을 확인해주세요 (길이초과)");
            }
        }
    }
}
