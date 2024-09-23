package com.flicker.movie.movie.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieEvent {
    private int userSeq;
    private int movieSeq;
    private String keyword;
    private String action; // "SEARCH" or "DETAIL"
    private LocalDateTime timestamp;
}
