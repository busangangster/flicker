package com.flicker.movie.movie.domain.entity;

import com.flicker.movie.movie.domain.vo.MongoMovie;
import lombok.Builder;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;

import java.util.List;

@Getter
@ToString
@Builder
@Document(collection = "movies")  // MongoDB 컬렉션 이름
public class MongoMovieList {

    @Id
    private String mongoKey;  // MongoDB 문서의 고유 키

    private List<MongoMovie> mongoMovies;  // 여러 영화 정보를 담을 리스트
}
