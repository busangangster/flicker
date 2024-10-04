package com.flicker.logger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MovieRating {

    private Integer movieSeq;
    private Double movieRating;
}
