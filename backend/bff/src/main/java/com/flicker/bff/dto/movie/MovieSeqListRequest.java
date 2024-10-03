package com.flicker.bff.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSeqListRequest {
    String movieTitle;
    String movieYear;
}
