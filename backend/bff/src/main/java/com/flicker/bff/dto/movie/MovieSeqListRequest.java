package com.flicker.bff.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieSeqListRequest {
    private String movieTitle;
    private int movieYear;
}
