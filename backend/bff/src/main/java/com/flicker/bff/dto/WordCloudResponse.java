package com.flicker.bff.dto;

import lombok.Data;

@Data
public class WordCloudResponse {
    private String actorName; // 배우 이름

    private String role; // 영화에서 배우가 맡은 역할
}
