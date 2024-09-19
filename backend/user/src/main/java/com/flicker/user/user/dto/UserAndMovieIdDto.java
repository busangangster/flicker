package com.flicker.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserAndMovieIdDto {
    private Long movieSeq;
    private Long userSeq;

    public UserAndMovieIdDto() {
    }

    public UserAndMovieIdDto(Long movieSeq, Long userSeq) {
        this.movieSeq = movieSeq;
        this.userSeq = userSeq;
    }
}
