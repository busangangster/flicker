package com.flicker.logger.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModelUpdateRequest {

    private Integer userSeq;
    private Integer movieSeq;
    private Integer reviewSeq;
    private String action; // "create" or "delete"
    private Double rating;
    private Double sentimentScore;
}
