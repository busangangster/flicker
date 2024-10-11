package com.flicker.movie.movie.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int topMovieSeq;

    @Column
    private int name;

    @Column
    private int movieSeq;
}
