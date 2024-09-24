package com.flicker.movie.movie.dto;


import com.flicker.movie.movie.domain.entity.WordCloud;
import lombok.Data;

@Data
public class WordCloudResponse {
    private String keyword;
    private int count;

    public WordCloudResponse(WordCloud wordCloud) {
        this.keyword = wordCloud.getKeyword();
        this.count = wordCloud.getCount();
    }
}
