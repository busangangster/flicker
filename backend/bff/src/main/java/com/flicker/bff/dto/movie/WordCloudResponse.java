package com.flicker.bff.dto.movie;

import lombok.Data;

@Data
public class WordCloudResponse {
    private String keyword;
    private int count;
}
