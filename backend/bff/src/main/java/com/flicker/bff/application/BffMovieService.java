package com.flicker.bff.application;

import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BffMovieService {

    private final Util util; // Util 클래스 의존성 주입

    @Value("${movie.baseurl}")
    private String movieBaseUrl; // 영화 API의 기본 URL

    @Value("${user.baseurl}")
    private String userBaseUrl; // 사용자 API의 기본 URL

    @Value("${batch.baseurl}")
    private String batchBaseUrl; // 배치서버 API의 기본 URL


    public ResponseEntity<ResponseDto> createMovie(MovieCreateRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/create");
        // 2. POST 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendPostRequest(movieBaseUrl, path, request);
    }

    public ResponseEntity<ResponseDto> updateMovie(MovieUpdateRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/update/detail");
        // 2. PUT 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendPutRequest(movieBaseUrl, path, request);
    }

    public ResponseEntity<ResponseDto> deleteMovie(int movieSeq) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/delete?movieSeq=" + movieSeq);
        // 2. PUT 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendPutRequest(movieBaseUrl, path, null);
    }

    public ResponseEntity<ResponseDto> addActor(ActorAddRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/add/actor");
        // 2. POST 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendPostRequest(movieBaseUrl, path, request);
    }

    public ResponseEntity<ResponseDto> deleteActor(int actorSeq, int movieSeq) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/delete/actor/" + actorSeq + "/" + movieSeq);
        // 2. DELETE 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendDeleteRequest(movieBaseUrl, path);
    }

    public ResponseEntity<ResponseDto> updateActor(ActorAddRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/update/actor");
        // 2. PUT 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendPutRequest(movieBaseUrl, path, request);
    }

    public ResponseEntity<ResponseDto> getMovieList(int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/" + page + "/" + size);
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequest(movieBaseUrl, path);
    }

    public ResponseEntity<ResponseDto> getMovieListByGenre(String genre, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/genre/" + genre + "/" + page + "/" + size);
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequest(movieBaseUrl, path);
    }

    public ResponseEntity<ResponseDto> getMovieListByActor(String actorName, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/actor/" + actorName + "/" + page + "/" + size);
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequest(movieBaseUrl, path);
    }

    public ResponseEntity<ResponseDto> getMovieListBySearch(String keyword, int userSeq, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/search/" + keyword + "/" + userSeq + "/" + page + "/" + size);
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequest(movieBaseUrl, path);
    }

    public ResponseEntity<ResponseDto> getMovieDetail(int movieSeq, int userSeq) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/detail/" + movieSeq + "/" + userSeq);
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequest(movieBaseUrl, path);
    }

    public ResponseEntity<ResponseDto> getActionRecommendationList(int userSeq) {
        // 1. 배치 서버에서 추천된 영화 번호 목록을 가져옵니다. TODO: 경로 수정
        String path = util.getUri("/list/recommendation/action/" + userSeq);
        List<Integer> movieSeqList = (List<Integer>) util.sendGetRequest(batchBaseUrl, path).getBody().getData();
        // 2. 해당 영화 번호 목록에 해당 하는 영화 목록을 가져옵니다.
        path = util.getUri("/list/recommendation");
        return util.sendPostRequest(movieBaseUrl, path, movieSeqList);
    }

    public ResponseEntity<ResponseDto> getReviewRecommendationList(int userSeq) {
        // 1. 배치 서버에서 추천된 영화 번호 목록을 가져옵니다. TODO: 경로 수정
        String path = util.getUri("/list/recommendation/review/" + userSeq);
        List<Integer> movieSeqList = (List<Integer>) util.sendGetRequest(batchBaseUrl, path).getBody().getData();
        // 2. 해당 영화 번호 목록에 해당 하는 영화 목록을 가져옵니다.
        path = util.getUri("/list/recommendation");
        return util.sendPostRequest(movieBaseUrl, path, movieSeqList);
    }

    public ResponseEntity<ResponseDto> getTopMovieList() {
        // 1. 배치 서버에서 추천된 영화 번호 목록을 가져옵니다. TODO: 경로 수정
        String path = util.getUri("/list/recommendation/top10");
        List<Integer> movieSeqList = (List<Integer>) util.sendGetRequest(batchBaseUrl, path).getBody().getData();
        // 2. 해당 영화 번호 목록에 해당 하는 영화 목록을 가져옵니다.
        path = util.getUri("/list/recommendation");
        return util.sendPostRequest(movieBaseUrl, path, movieSeqList);
    }
}
