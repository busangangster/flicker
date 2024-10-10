package com.flicker.movie.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewMovieEvent {
    List<Integer> movieSeqList;
}
