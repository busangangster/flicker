package com.flicker.movie.movie.domain.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("NewMovies")
public class RedisNewMovie {
    @Id
    private String id; // 레디스 key 값

    private List<Integer> movieSeqs; // 영화 시퀀스 리스트

    @TimeToLive
    private Long expiration; // TTL 값을 동적으로 설정
}
