package com.flicker.movie.movie.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int newMovieSeq;

    @Column
    private int movieSeq;
}
