package com.flicker.bff.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import com.flicker.bff.dto.user.*;
import com.flicker.bff.dto.user.photocard.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BffUserService {

    private static final Logger log = LoggerFactory.getLogger(BffUserService.class);
    private final Util util; // Util 클래스 의존성 주입

    private final ObjectMapper objectMapper;

    @Value("${user-review.baseurl}")
    private String userReviewBaseUrl; // 사용자-리뷰 서버 API의 기본 URL

    @Value("${movie.baseurl}")
    private String movieBaseUrl; // 영화 서버 API의 기본 URL


    // 1. 회원가입
    public Mono<ResponseEntity<ResponseDto>> registerUser(UserRegisterReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("");
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendPostRequestAsync(userReviewBaseUrl, path, request);
    }

    // 2. 로그인
    public Mono<ResponseEntity<ResponseDto>> loginUser(UserLoginReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/login");
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendPostRequestAsyncWithToken(userReviewBaseUrl, path, request);
    }

    // 3. 회원수정(LOW)
    public Mono<ResponseEntity<ResponseDto>> update(Integer userSeq, UserUpdateDto dto) {
        String path = util.getUri("/" + userSeq);
        return util.sendPutRequestAsyncWithToken(userReviewBaseUrl, path,dto);
    }
    // 4. 회원탈퇴(LOW)
    public Mono<ResponseEntity<ResponseDto>> delete(Integer userSeq) {
        String path = util.getUri("/" + userSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }

    // 3. 영화 디테일 페이지의 대표 리뷰 조회
    public Mono<ResponseEntity<ResponseDto>> getPopularMovieReviews(MovieReviewReqDto dto) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/review/movies/"+dto.getMovieSeq()+"/popular-review?userSeq="+dto.getUserSeq());
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }

    // 4. 리뷰 등록
    public Mono<ResponseEntity<ResponseDto>> registerReview(RegisterReviewReqDto dto) {
        String path = util.getUri("/review");
        return util.sendPostRequestAsync(userReviewBaseUrl, path, dto);
    }

    // 5. 리뷰 목록
    public Mono<ResponseEntity<ResponseDto>> getMovieReview(MovieReviewReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/review/movies/"+request.getMovieSeq()+"?userSeq="+request.getUserSeq()+"&page="+request.getPage())+"&size="+request.getSize()+"&option="+request.getOption();
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
    // 6. 리뷰 삭제
    public Mono<ResponseEntity<ResponseDto>> deleteReview(DeleteReviewReqDto dto) {
        String path = util.getUri("/review?reviewSeq="+dto.getReviewSeq()+"&userSeq="+dto.getUserSeq());
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }

    // 7. 포토 리뷰 조회
    public Mono<ResponseEntity<ResponseDto>> getUserReviews(UserReviewReqDto dto){
        String path = util.getUri("/review/"+dto.getUserSeq());
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }

    // 8. 선호 영화 등록
    public Mono<ResponseEntity<ResponseDto>> registerFavoriteMovie(Integer userSeq, MovieSeqListDto dto){
        String path = util.getUri("/"+userSeq+"/favorite-movie");
        return util.sendPostRequestAsync(userReviewBaseUrl, path, dto);
    }

    // 9. 선호 영화 조회
    public Mono<ResponseEntity<ResponseDto>> getFavoriteMovie(Integer userSeq){
        String path = util.getUri("/"+userSeq+"/favorite-movie");
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
    // 10. 비선호 영화 등록
    public Mono<ResponseEntity<ResponseDto>> registerUnlikeMovie(Integer userSeq, Integer movieSeq){
        String path = util.getUri("/"+userSeq+"/unlike-movie/"+movieSeq);
        return util.sendPostRequestAsync(userReviewBaseUrl,path,null);
    }
    // 11. 비선호 영화 삭제
    public Mono<ResponseEntity<ResponseDto>> deleteUnlikeMovie(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/"+userSeq+"/unlike-movie/"+movieSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl,path);
    }
    // 12. 비선호 영화 조회
    public Mono<ResponseEntity<ResponseDto>> getUnlikeMovie(Integer userSeq) {

        String path = util.getUri("/"+userSeq+"/unlike-movie");
        return util.sendGetRequestAsync(userReviewBaseUrl,path).flatMap(getResponse ->{
            //바로 받는 코드
            ResponseDto reviewResponseDto;


            try {
                reviewResponseDto = Objects.requireNonNull(getResponse.getBody());
            } catch (Exception e) {
                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "reviewResponseDto: getResponse.getBody()가 null입니다." + e.getMessage()));
            }
            // reviewResponseDto의 상태 코드가 성공이 아닌 경우 처리
            if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                return Mono.error(new RestApiException(
                        StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                        reviewResponseDto.getData()
                ));
            }


            // ObjectMapper를 이용하여 JSON 데이터를 ReviewListDto로 변환
            List<Integer> movieSeqList;
            try {
                movieSeqList = objectMapper.convertValue(reviewResponseDto.getData(), new TypeReference<List<Integer>>() {});
            } catch (Exception e) {
                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 리스트를 역직렬화하는데 오류 발생: " + e.getMessage()));
            }


            // 영화 정보 받아오는 요청 보내기
            String path2 = util.getUri("/list/movieId");
            return util.sendPostRequestAsync(movieBaseUrl, path2, movieSeqList).flatMap(getResponseTwo -> {

                //바로 받는 코드
                ResponseDto movieDto;
                try {
                    movieDto = Objects.requireNonNull(getResponseTwo.getBody());
                } catch (Exception e) {
                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "movieDto: getResponse.getBody()가 null입니다." + e.getMessage()));
                }

                // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                if (movieDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                    return Mono.error(new RestApiException(
                            StatusCode.of(movieDto.getHttpStatus(), movieDto.getServiceStatus(), movieDto.getMessage()),
                            movieDto.getData()
                    ));
                }


                // ObjectMapper를 이용하여 JSON 데이터를 REviewListDto로 역직렬화
                List<MovieListDto> movieListDtoList;
                try {
                    movieListDtoList = objectMapper.convertValue(movieDto.getData(), new TypeReference<List<MovieListDto>>() {});
                } catch (Exception e) {
                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                }

                // 결과 만들기
                ArrayList<MovieInfoDto> result = new ArrayList<>();
                for(MovieListDto item : movieListDtoList){
                    MovieInfoDto movieInfoDto = new MovieInfoDto();
                    movieInfoDto.setMovieSeq(item.getMovieSeq());
                    movieInfoDto.setMovieTitle(item.getMovieTitle());
                    movieInfoDto.setMovieYear(item.getMovieYear());
                    movieInfoDto.setMoviePosterUrl(item.getMoviePosterUrl());
                    movieInfoDto.setBackgroundUrl(item.getBackgroundUrl());
                    result.add(movieInfoDto);
                }
                return Mono.just(ResponseDto.response(StatusCode.SUCCESS, result));
            });

        }).onErrorResume(e -> {
            if (e instanceof RestApiException ex) {
                return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
            } else {
                return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 정보를 가져오는 데 문제 발생" + e.getMessage()));
            }
        });



    }
    // 13. 찜한 영화 등록
    public Mono<ResponseEntity<ResponseDto>> registerBookmarkMovie(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/"+userSeq+"/bookmark-movie/"+movieSeq);
        return util.sendPostRequestAsync(userReviewBaseUrl, path, null);
    }
    // 14. 찜한 영화 삭제
    public Mono<ResponseEntity<ResponseDto>> deleteBookmarkMovie(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/"+userSeq+"/bookmark-movie/"+movieSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }
    // 15. 찜한 영화 조회
    public Mono<ResponseEntity<ResponseDto>> getBookmarkMovie(Integer userSeq) {
        String path = util.getUri("/"+userSeq+"/bookmark-movie");

        return util.sendGetRequestAsync(userReviewBaseUrl,path).flatMap(getResponse ->{
            //바로 받는 코드
            ResponseDto reviewResponseDto;
            try {
                reviewResponseDto = Objects.requireNonNull(getResponse.getBody());
            } catch (Exception e) {
                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "reviewResponseDto: getResponse.getBody()가 null입니다." + e.getMessage()));
            }
            // reviewResponseDto의 상태 코드가 성공이 아닌 경우 처리
            if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                return Mono.error(new RestApiException(
                        StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                        reviewResponseDto.getData()
                ));
            }

            // ObjectMapper를 이용하여 JSON 데이터를 ReviewListDto로 변환
            List<Integer> movieSeqList;
            try {
                movieSeqList = objectMapper.convertValue(reviewResponseDto.getData(), new TypeReference<List<Integer>>() {});
            } catch (Exception e) {
                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 리스트를 역직렬화하는데 오류 발생: " + e.getMessage()));
            }


            // 영화 정보 받아오는 요청 보내기
            String path2 = util.getUri("/list/movieId");
            return util.sendPostRequestAsync(movieBaseUrl, path2, movieSeqList).flatMap(getResponseTwo -> {

                //바로 받는 코드
                ResponseDto movieDto;
                try {
                    movieDto = Objects.requireNonNull(getResponseTwo.getBody());
                } catch (Exception e) {
                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "movieDto: getResponse.getBody()가 null입니다." + e.getMessage()));
                }

                // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                if (movieDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                    return Mono.error(new RestApiException(
                            StatusCode.of(movieDto.getHttpStatus(), movieDto.getServiceStatus(), movieDto.getMessage()),
                            movieDto.getData()
                    ));
                }


                // ObjectMapper를 이용하여 JSON 데이터를 REviewListDto로 역직렬화
                List<MovieListDto> movieListDtoList;
                try {
                    movieListDtoList = objectMapper.convertValue(movieDto.getData(), new TypeReference<List<MovieListDto>>() {});
                } catch (Exception e) {
                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                }

                // 결과 만들기
                ArrayList<MovieInfoDto> result = new ArrayList<>();
                for(MovieListDto item : movieListDtoList){
                    MovieInfoDto movieInfoDto = new MovieInfoDto();
                    movieInfoDto.setMovieSeq(item.getMovieSeq());
                    movieInfoDto.setMovieTitle(item.getMovieTitle());
                    movieInfoDto.setMovieYear(item.getMovieYear());
                    movieInfoDto.setMoviePosterUrl(item.getMoviePosterUrl());
                    movieInfoDto.setBackgroundUrl(item.getBackgroundUrl());
                    result.add(movieInfoDto);
                }

                
                return Mono.just(ResponseDto.response(StatusCode.SUCCESS, result));
            });

        }).onErrorResume(e -> {
            if (e instanceof RestApiException ex) {
                return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
            } else {
                return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 정보를 가져오는 데 문제 발생" + e.getMessage()));
            }
        });
    }
    // 16. 리뷰 좋아요 등록
    public Mono<ResponseEntity<ResponseDto>> addLikeReview(AddLikeReviewReqDto dto) {
        String path = util.getUri("/review/likeReview");
        return util.sendPostRequestAsync(userReviewBaseUrl, path, dto);
    }
    // 17. 리뷰 좋아요 삭제
    public Mono<ResponseEntity<ResponseDto>> removeLikeReview(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/review/likeReview?userSeq="+userSeq+"&reviewSeq="+movieSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }

    // 18. 포토 카드 조회
    public Mono<ResponseEntity<ResponseDto>> getPhotoCard(Integer userSeq) {
        
        // 1. 리류 서버에서 자신의 리뷰 가졍기
        UserReviewReqDto reqDto = new UserReviewReqDto(userSeq,userSeq);
        
        return getUserReviews(reqDto).flatMap(getResponse -> {
            
            //바로 받는 코드
            ResponseDto reviewResponseDto;
            try {
                reviewResponseDto = Objects.requireNonNull(getResponse.getBody());
            } catch (Exception e) {
                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "reviewResponseDto: getResponse.getBody()가 null입니다." + e.getMessage()));
            }
            // reviewResponseDto의 상태 코드가 성공이 아닌 경우 처리
            if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                return Mono.error(new RestApiException(
                        StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                        reviewResponseDto.getData()
                ));
            }
            
            

            // ObjectMapper를 이용하여 JSON 데이터를 ReviewListDto로 변환
            List<ReviewDto> reviewListDto;
            try {
                reviewListDto = objectMapper.convertValue(reviewResponseDto.getData(), new TypeReference<List<ReviewDto>>() {});
            } catch (Exception e) {
                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
            }
            
            // movieSeqList를 post로 전송해야해서 List에 담기
            List<Integer> movieSeqList = new ArrayList<>();
            for (ReviewDto item : reviewListDto) {
                movieSeqList.add(item.getMovieSeq());
            }

            
            
            // 영화 정보 받아오는 요청 보내기
            String path = util.getUri("/list/movieId");
            return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, path, movieSeqList).flatMap(getResponseTwo -> {

                //바로 받는 코드
                ResponseDto movieDto;
                try {
                    movieDto = Objects.requireNonNull(getResponseTwo.getBody());
                } catch (Exception e) {
                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "movieDto: getResponse.getBody()가 null입니다." + e.getMessage()));
                }

                // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                if (movieDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                    return Mono.error(new RestApiException(
                            StatusCode.of(movieDto.getHttpStatus(), movieDto.getServiceStatus(), movieDto.getMessage()),
                            movieDto.getData()
                    ));
                }


                // ObjectMapper를 이용하여 JSON 데이터를 REviewListDto로 역직렬화
                List<MovieListDto> movieListDtoList;
                try {
                    movieListDtoList = objectMapper.convertValue(movieDto.getData(), new TypeReference<List<MovieListDto>>() {});
                } catch (Exception e) {
                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                }

                
                // 결과 만들기
                ArrayList<PhotoCardDto> result = new ArrayList<>();
                for(int i=0;i<movieListDtoList.size();i++){

                    PhotoCardDto photoCardDto = new PhotoCardDto();

                    MovieImageDto movieImageDto = new MovieImageDto();
                    movieImageDto.setMoviePosterUrl(movieListDtoList.get(i).getMoviePosterUrl());
                    movieImageDto.setMovieTitle(movieListDtoList.get(i).getMovieTitle());
                    movieImageDto.setMovieYear(movieListDtoList.get(i).getMovieYear());
                    movieImageDto.setBackgroundUrl(movieListDtoList.get(i).getBackgroundUrl());
                    photoCardDto.setMovieImageDto(movieImageDto);


                    for(int j=0;j<reviewListDto.size();j++){
                        if(reviewListDto.get(j).getMovieSeq().equals(movieListDtoList.get(i).getMovieSeq())){
                            photoCardDto.setReviewDto(reviewListDto.get(j));
                            result.add(photoCardDto);
                        }
                    }

                }

                return Mono.just(ResponseDto.response(StatusCode.SUCCESS, result));
            });
//                    return Mono.just(ResponseDto.response(StatusCode.SUCCESS, responseEntityMono));
        }).onErrorResume(e -> {
            if (e instanceof RestApiException ex) {
                return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
            } else {
                return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 서버의 포스터 가져오는데 문제 발생 " + e.getMessage()));
            }
        });



    }

    public Mono<ResponseEntity<ResponseDto>> getMovieReviewRatingDistribute(Integer movieSeq) {
        String path = util.getUri("/review/movies/"+movieSeq+"/distribute");
        return util.sendGetRequestAsync(userReviewBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMyPage(Integer userSeq) {
        String path = util.getUri("/"+userSeq+"/myPage");
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }


    public Mono<ResponseEntity<ResponseDto>> checkAlreadyReview(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/review/check-already-review?userSeq="+userSeq+"&movieSeq="+movieSeq);
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }

    public Mono<ResponseEntity<ResponseDto>> refresh(String refresh) {

        String path = util.getUri("/refresh");
        return util.sendPostRequestAsyncForTokenGenerate(userReviewBaseUrl,path,null,refresh);

    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Mono<ResponseEntity<ResponseDto>> checkFirstLogin(Integer userSeq) {
        String path = util.getUri("/first-login-check/"+userSeq);
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
}
