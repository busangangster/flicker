package com.flicker.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserAndMovieIdDto {
    private Integer movieSeq;
    private Integer userSeq;

    public UserAndMovieIdDto() {
    }

    public UserAndMovieIdDto(Integer movieSeq, Integer userSeq) {
        this.movieSeq = movieSeq;
        this.userSeq = userSeq;
    }
}
