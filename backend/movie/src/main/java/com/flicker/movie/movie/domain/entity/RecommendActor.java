package com.flicker.movie.movie.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendActor {

    // 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recommendActorSeq;

    @Column
    private int userSeq;

    @Column
    private int actorSeq;

    @Column
    private int movieSeq;
}
