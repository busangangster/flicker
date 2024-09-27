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


    @GetMapping("/boarding/1")
    public ResponseEntity<ResponseDto> boardingPage() {


        BoardingPageDto boardingPageDto = new BoardingPageDto();

        boardingPageDto.bookMarkedMovie = true;
        boardingPageDto.unlikedMovie = false;

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
        movieDetail.trailerUrl = "https://www.youtube.com/embed/CF1rtd8_pxA";
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

    @GetMapping("/boarding/2")
    public ResponseEntity<ResponseDto> boardingPage2() {


        BoardingPageDto boardingPageDto = new BoardingPageDto();

        boardingPageDto.bookMarkedMovie = true;
        boardingPageDto.unlikedMovie = false;

        MovieDto movieDto = new MovieDto();
        movieDto.setMovieSeq(1);

        MovieDetailDto movieDetail = new MovieDetailDto();
        movieDetail.movieTitle = "어벤져스";
        movieDetail.director = "Joss Whedon";
        movieDetail.genre = "action";
        movieDetail.country = "English";
        movieDetail.moviePlot = "The Asgardian Loki encounters the Other, the leader of an extraterrestrial race known as the Chitauri. In exchange for obtaining the Tesseract,[c] a powerful energy source of unknown potential, the Other promises to provide Loki with an army to conquer Earth. Nick Fury, director of the espionage agency S.H.I.E.L.D., arrives at a remote research facility where physicist Dr. Erik Selvig is leading a team studying the Tesseract. It suddenly activates and opens a wormhole, allowing Loki to reach Earth. Loki steals the Tesseract and uses his scepter to enslave Selvig and other agents, including Clint Barton, to aid him.\n" +
                "\n" +
                "In response, Fury reactivates the \"Avengers Initiative\". Agent Natasha Romanoff travels to Kolkata to recruit Dr. Bruce Banner so he can trace the Tesseract through its gamma radiation emissions. Fury approaches Steve Rogers to discuss his knowledge of the Tesseract, and Agent Phil Coulson visits Tony Stark so he can check Selvig's research. Loki is in Stuttgart, where Barton steals iridium needed to stabilize the Tesseract. This leads to a confrontation with Rogers, Stark, and Romanoff that ends with Loki's surrender. While Loki is being escorted to S.H.I.E.L.D., his adoptive brother Thor arrives and frees him, hoping to convince him to abandon his plan and return to Asgard. Stark and Rogers intervene, and Loki is imprisoned in S.H.I.E.L.D.'s flying aircraft carrier, the Helicarrier.\n" +
                "아스가르드 의 로키는 치타우리 라고 알려진 외계 종족의 지도자인 어더를 만난다 . 알려지지 않은 잠재력 을 지닌 강력한 에너지원인 테서랙트를 얻는 대가로 어더는 로키에게 지구를 정복할 군대를 제공하겠다고 약속한다. 스파이 기관 SHIELD 의 국장인 닉 퓨리는 물리학 자 에릭 셀비 그 박사가 테서랙트를 연구하는 팀을 이끌고 있는 외딴 연구 시설에 도착한다 . 갑자기 웜홀 이 활성화되어 로키가 지구에 도달할 수 있게 된다. 로키는 테서랙트를 훔치고 자신의 홀을 사용하여 셀비그와 클린트 바튼을 포함한 다른 요원들을 노예로 만들어 자신을 돕게 한다.\n" +
                "\n" +
                "이에 대응하여 Fury는 \" Avengers Initiative \"를 재활성화합니다. Natasha Romanoff 요원은 Kolkata 로 가서 Bruce Banner 박사를 모집하여 감마선 방출을 통해 Tesseract를 추적할 수 있도록 합니다 . Fury는 Steve Rogers에게 접근하여 Tesseract에 대한 지식을 논의하고 Phil Coulson 요원은 Tony Stark를 방문하여 Selvig의 연구를 확인합니다. Loki는 Stuttgart 에 있으며 Barton은 Tesseract를 안정화하는 데 필요한 이리듐을 훔칩니다 . 이로 인해 Rogers, Stark 및 Romanoff와의 대립이 발생하고 Loki의 항복으로 끝납니다. Loki가 SHIELD로 호송되는 동안 그의 양형 Thor가 도착하여 그를 풀어주고 계획을 포기하고 Asgard로 돌아가도록 설득하기를 바랍니다. Stark와 Rogers가 개입하고 Loki는 SHIELD의 비행 항공 모함인 Helicarrier 에 투옥됩니다 .";
        movieDetail.audienceRating = "15세";
        movieDetail.movieYear = 2017;
        movieDetail.runningTime = "2시간 17분";
        movieDetail.moviePosterUrl = "https://an2-img.amz.wtchn.net/image/v2/Phee2kOFCGVU-DciofgxFg.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5Ea3dlRGN3TUhFNE1DSmRMQ0p3SWpvaUwzWXhMMjkyTnpSNmRUQnFhamhtYVRGb1pIUjVjM0YwSW4wLnp2MkVDS3dpYWdGa09CQVZkSEVXX1daYm11YWxFLWYtLUVCX1E4Q0FfYUk";
        movieDetail.backgroundUrl = "https://an2-img.amz.wtchn.net/image/v2/y3sqhEoHKDyfEqwpMytU2Q.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk1Ua3lNSGd4TURnd2NUZ3dJbDBzSW5BaU9pSXZkakV2Wm1rMU9EZDZkVE5oWVhwd01YUjBkamhqZVdraWZRLmxyUWpPeVIzU2FoZXBFYnA4NVFzVWNuZXJqckFnY3kxVjdwZkk3TmtuVEU";
        movieDetail.trailerUrl = "https://www.youtube.com/embed/CF1rtd8_pxA";
        movieDto.setMovieDetail(movieDetail);

        movieDto.setMovieRating(4.5);

        List<ActorDto> actors = new ArrayList<>();
        actors.add(new ActorDto("Robert Downey Jr", "(주연 | Tony Stark)"));
        actors.add(new ActorDto("Chris Evans", "(주연 | Captain America)"));
        actors.add(new ActorDto("Mark Ruffalo", "(주연 | Hulk)"));
        actors.add(new ActorDto("Chris Hemsworth", "(주연 | Thor)"));
        actors.add(new ActorDto("Scarlett Johansson", "(주연 | Black Widow)"));
        actors.add(new ActorDto("Jeremy Renner", "(주연 | Hawkeye)"));
        actors.add(new ActorDto("Tom Hiddleston", "(주연 | Loki)"));
        actors.add(new ActorDto("Stellan Skarsgård", "(조연 | Erik Selvig)"));
        actors.add(new ActorDto("Samuel L. Jackson", "(조연 | Nick Fury)"));
        movieDto.setActors(actors);

        boardingPageDto.setMovie(movieDto);


        List<ReviewDto> reviewList = new ArrayList<>();
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.nickname = "busangangstar";
        reviewDto.reviewRating = 5.0;
        reviewDto.content = "나도 3000만큼 사랑해\n" +
                " \n" +
                "# 이상하다. 영화가 뭐라고. 아니 그냥 시리즈의 마무리고 떠날 이들이 떠난거 뿐인데. 내 마음이 허전해. 진짜로 뭔가 떠나 보낸듯 해. 자꾸 끝이 아니라 생각하고 싶어. \n" +
                "# 그래 우리만 행복할 수 없지, 그들도 충분히 행복해질 자격이 있는데... \n" +
                "# 덕분에 십여년이 즐거웠어요. 그래도 뭔가 마음속이 허전한거는 어쩔수 없네요. 아직 보낼 준비가 안됐나봐요.\n" +
                "# 마블 역사를 관통한 모두에게 전하는 헌사\n" +
                "# 오늘 저녁은 치즈버거나 먹어야겠다.\n" +
                "# 이 영화가 나올수 있게 큰 힘 주신 피터 퀼에게 무한한 영광을...\n" +
                "# 지금 이 순간이 마블의 정점일듯, 이마저도 넘어설 수 있을까?\n" +
                "# 디즈니는 역시 마우스지 \n" +
                "# 마블 최고의 대사는 오늘부로 확정일 듯\n" +
                "# 캡틴과 아이언맨의 퇴장에 대해서 조금 적어보자면. 지금까지 22편의 마블 영화를 지켜본 이들이라면 이 두 캐릭터가 어벤져스에서 가장 쎈 히어로가 아닌걸 잘 알것이라 생각한다. 그럼에도 이들은 어벤져스의 리더이자 마블의 상징과 같은 캐릭터가 되었다는 걸 쉽게 부정하기는 어려울 것이다. 이는 두 캐릭터가 가진 일관된 성향과 관련이 있으리라 생각한다. 왜 이 얘기를 꺼내냐 하면 이 두 인물의 퇴장은 이제껏 보여준 성향과 정반대의 선택을 보여줬기 때문이다. 늘 대의가 우선이였던 캡틴, 좀 더 자기중심적이였던 토니, 엔드게임에서도 두 인물은 영화 초반에 이런 성향을 더욱 뚜렷하게 보여준다. 그러기에 난 영화가 시작하기 전부터 누군가가 죽게 된다면 그건 캡틴이 될 것이라는 생각을 해왔다. 그런데 이 영화는 두 인물이 지극히 정반대의 선택을 할 수 밖에 없게 만든다. 일상적인 삶으로 돌아간 캡틴, 자신을 뒤로하고 대의를 선택한 토니 이 과정에서의 두 인물의 성장과 감정의 흐름은 지극히 자연스러워 충격과 감동을 배가 시킨다. 두 인물의 선택은 가슴속에 묻어둔 짐을 덜어준 느낌이었다. 뭔가 이 두 주인공을 보고 있으면 늘 애잔한 기분이였다. 그 책임감과 부담을 지고 평생을 짓눌려 살아온 것 같은 그런 느낌말이다. 이런점에서 두 영웅의 마지막 선택은 이들을 자유롭게 놓아주는 최선이라 보인다. 십여년을 함께한 캐릭터에 사랑과 헌신, 존경을 담아 보내준 마블의 선택에 박수를 보내고 싶다. \n" +
                "# 잠시 뒤에 보자던 인사가 작별인사가 되다니..\n" +
                "# 어벤져스 1에 이런 대사가 나온다.\n" +
                "A: 그들은 돌아올거야\n" +
                "B: 확신하세요?\n" +
                "A: 물론\n" +
                "B: 왜죠?\n" +
                "A: 우리한텐 그들이 필요하니까\n" +
                "당장의 퇴장이 영원한 퇴장이 되진 않을 것이다.\n" +
                "# 3시간 그까짓 거 I can do this all day\n" +
                "# Whatever it takes\n" +
                "# Avengers 4 : End Game\n" +
                "   어벤져스 4 : 가망없음";
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
