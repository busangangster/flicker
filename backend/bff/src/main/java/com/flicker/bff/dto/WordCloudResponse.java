package com.flicker.bff.dto;

import lombok.Data;

@Data
public class WordCloudResponse {
    private String keyword;
    private int count;
}
