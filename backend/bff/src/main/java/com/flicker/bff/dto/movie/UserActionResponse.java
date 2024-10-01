package com.flicker.bff.dto.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActionResponse {
    private int userSeq;

    private String keyword;

    private String action; // "SEARCH" or "DETAIL"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}