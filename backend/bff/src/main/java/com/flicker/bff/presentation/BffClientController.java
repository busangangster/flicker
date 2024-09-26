package com.flicker.bff.presentation;

import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import com.flicker.bff.dto.client.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bff/client")
public class BffClientController {


    @GetMapping("/boarding/{movieSeq}")
    public ResponseEntity<ResponseDto> boardingPage(@PathVariable Integer movieSeq) {

        if(movieSeq == null){
            throw new RestApiException(StatusCode.NOT_FOUND);
        }

        BoardingPageDto boardingPageDto = new BoardingPageDto();

        boardingPageDto.isLikeMovie = true;

        MovieDto movieDto = new MovieDto();
        movieDto.setMovieSeq(1);

        MovieDetailDto movieDetail = new MovieDetailDto();
        movieDetail.movieTitle = "택시운전사";
        movieDetail.director = "장훈";
        movieDetail.genre = "드라마/가족";
        movieDetail.country = "한국";
        movieDetail.moviePlot = "1980년 5월, 서울 택시운전사. “광주? 돈 워리, 돈 워리! 아이 베스트 드라이버” 택시운전사 '만섭'(송강호)은 외국손님을 태우고 광주에 갔다 통금 전에 돌아오면 밀린 월세를 갚을 수 있는 거금 10만원을 준다는 말에 독일기자 '피터'(토마스 크레취만)를 태우고 영문도 모른 채 길을 나선다. 광주 그리고 사람들. “모르겄어라, 우덜도 우덜한테 와 그라는지…” 어떻게든 택시비를 받아야 하는 만섭의 기지로 검문을 뚫고 겨우 들어선 광주. 위험하니 서울로 돌아가자는 만섭의 만류에도 피터는 대학생 '재식'(류준열)과 '황기사'(유해진)의 도움 속에 촬영을 시작한다. 그러나 상황은 점점 심각해지고 만섭은 집에 혼자 있을 딸 걱정에 점점 초조해지는데…";
        movieDetail.audienceRating = "15세";
        movieDetail.movieYear = 2017;
        movieDetail.runningTime = "2시간 17분";
        movieDetail.moviePosterUrl = "https://an2-img.amz.wtchn.net/image/v2/Phee2kOFCGVU-DciofgxFg.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5Ea3dlRGN3TUhFNE1DSmRMQ0p3SWpvaUwzWXhMMjkyTnpSNmRUQnFhamhtYVRGb1pIUjVjM0YwSW4wLnp2MkVDS3dpYWdGa09CQVZkSEVXX1daYm11YWxFLWYtLUVCX1E4Q0FfYUk";
        movieDetail.backgroundUrl = "https://an2-img.amz.wtchn.net/image/v2/y3sqhEoHKDyfEqwpMytU2Q.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk1Ua3lNSGd4TURnd2NUZ3dJbDBzSW5BaU9pSXZkakV2Wm1rMU9EZDZkVE5oWVhwd01YUjBkamhqZVdraWZRLmxyUWpPeVIzU2FoZXBFYnA4NVFzVWNuZXJqckFnY3kxVjdwZkk3TmtuVEU";
        movieDetail.trailerUrl = "https://redirect.watcha.com/galaxy/aHR0cHM6Ly93d3cueW91dHViZS5jb20vd2F0Y2g_dj1QMFVmVmZBU2tfVQ";
        movieDto.setMovieDetail(movieDetail);

        movieDto.setMovieRating(4.1);
        List<ActorDto> actors = new ArrayList<>();
        actors.add(new ActorDto("송강호", "(주연 | 김만섭)"));
        actors.add(new ActorDto("토머스 크레치만", "(주연 | 위르겐 힌츠페터)"));
        actors.add(new ActorDto("유해진", "(주연 | 황태술)"));
        actors.add(new ActorDto("류준열", "(주연 | 구재식)"));
        actors.add(new ActorDto("박혁권", "(조연 | 최기자)"));
        actors.add(new ActorDto("전혜진", "(조연)"));
        actors.add(new ActorDto("고창석", "(조연)"));
        actors.add(new ActorDto("최귀화", "(조연 | 사복조장)"));
        actors.add(new ActorDto("이봉련", "(조연 | 서울 임산부)"));
        actors.add(new ActorDto("엄태구", "(조연 | 비포장 검문소 중사)"));
        actors.add(new ActorDto("차순배", "(조연 | 차기사)"));
        movieDto.setActors(actors);

        boardingPageDto.setMovie(movieDto);


        List<ReviewDto> reviewList = new ArrayList<>();
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.nickname = "busangangstar";
        reviewDto.reviewRating = 5.0;
        reviewDto.content = "탑건1(1986년)의 36년만의 나온 속편. \n" +
                "매우 만족 스러웠고 매우 재밌었다 무조건 특별관에서 봐야되는 영화 2022년 개봉작 영화중에서 범죄도시2 이후 2번째로 \n" +
                "엄청 좋았던 영화 톰 크루즈 미모는 여전히 잘생겼다 1편을 보고 가야되는 질문에서 답을 하자면 \n" +
                "1편 보고 가는게 더 좋다 감동도 2배 더 느낄 수 있음";
        reviewDto.isSpoiler = false;
        reviewDto.likes = 12333;
        reviewDto.liked = true;
        reviewDto.createdAt = LocalDateTime.now();
        reviewList.add(reviewDto);

        reviewDto = new ReviewDto();
        reviewDto.nickname = "jaechan";
        reviewDto.reviewRating = 4.5;
        reviewDto.content = "감동을 2배 더 느낄 수 있음";
        reviewDto.isSpoiler = true;
        reviewDto.likes = 345;
        reviewDto.liked = false;
        reviewDto.createdAt = LocalDateTime.now();
        reviewList.add(reviewDto);
        
        boardingPageDto.setReviewList(reviewList);

        List<RecommendMovieDto> recommendMovieDtos = new ArrayList<>();
        for(int i=0;i<10;i++){
            RecommendMovieDto recommendMovieDto = new RecommendMovieDto();
            recommendMovieDto.movieSeq = 1L;
            recommendMovieDto.moviePosterUrl= "https://an2-img.amz.wtchn.net/image/v2/xm228ExDiQWQClodDONRBA.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5Ea3dlRGN3TUhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpFMk16RTROVFk0T0RFNU5qRXpOREU1TWpJaWZRLnZkUmY4dUh1ZE9Ra2FEY0N0M3hIUWRDQlBvVzU4aEhNOXRxN3Z5ZnhERkU";
            recommendMovieDtos.add(recommendMovieDto);
        }

        boardingPageDto.recommendedMovieList = recommendMovieDtos;

        return ResponseDto.response(StatusCode.SUCCESS, boardingPageDto);
    }
}
