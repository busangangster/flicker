package com.flicker.logger.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordCloudRequest {

    private Integer movieSeq;
    private String content;
}
