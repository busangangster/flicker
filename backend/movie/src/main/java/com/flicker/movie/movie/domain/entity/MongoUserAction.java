package com.flicker.movie.movie.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@Document(collection = "user_action")  // MongoDB 컬렉션 이름
public class MongoUserAction {

    @Id
    private String id;

    @Indexed
    private int userSeq;

    private String keyword;

    private String action; // "SEARCH" or "DETAIL" or "REVIEW"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
