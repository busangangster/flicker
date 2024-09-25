package com.flicker.movie.movie.application;

import com.flicker.movie.movie.domain.entity.Actor;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.dto.ActorAddRequest;
import com.flicker.movie.movie.dto.ActorUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    * ActorService 클래스는 배우 정보를 추가, 삭제, 수정하는 비즈니스 로직을 담당한다.
    * MovieBuilderUtil, MovieRepoUtil를 주입받는다.
    * addActor() 메서드는 배우 정보를 추가한다.
    * deleteActor() 메서드는 배우 정보를 삭제한다.
    * updateActor() 메서드는 배우 정보를 수정한다.
    * @Transactional 어노테이션을 사용하여 트랜잭션을 적용한다.
    * @RequiredArgsConstructor 어노테이션을 사용하여 final 필드를 생성자에서 초기화한다.
    * @Service 어노테이션을 사용하여 서비스 빈으로 등록한다.
 */
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
        // 수정 시 배우 중복 처리는 아직 안함
    }
}
