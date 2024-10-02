package com.flicker.bff.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class MovieSeqListRequest {
    String movieTitle;
    String movieYear;
}
