package com.flicker.movie.movie.application;

import com.flicker.movie.movie.domain.entity.Actor;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.dto.ActorAddRequest;
import com.flicker.movie.movie.dto.ActorUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActorService {

    private final MovieBuilderUtil movieBuilderUtil;
    private final MovieRepoUtil movieRepoUtil;

    @Transactional
    public void addActor(ActorAddRequest request) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 2. 배우 정보 추가
        List<Actor> actorList = movieBuilderUtil.buildActorList(request);
        movie.addActors(actorList);
    }

    @Transactional
    public void deleteActor(int actorSeq, int movieSeq) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 2. 배우 정보 삭제
        movie.removeActor(actorSeq);
    }

    @Transactional
    public void updateActor(ActorUpdateRequest request) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 2. 배우 정보 수정
        Actor actor = movie.getActor(request.getActorSeq());
        actor.updateActor(request.getActorName(), request.getRole());
    }
}
