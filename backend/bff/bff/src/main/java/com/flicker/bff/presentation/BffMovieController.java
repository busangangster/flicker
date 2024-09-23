package com.flicker.bff.presentation;

import com.flicker.bff.application.BffMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bff/movie")
@RequiredArgsConstructor
public class BffMovieController {

    private final BffMovieService bffMovieService;


}
