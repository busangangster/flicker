package com.flicker.movie.movie.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMovieDetail is a Querydsl query type for MovieDetail
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMovieDetail extends BeanPath<MovieDetail> {

    private static final long serialVersionUID = -2102022761L;

    public static final QMovieDetail movieDetail = new QMovieDetail("movieDetail");

    public final StringPath audienceRating = createString("audienceRating");

    public final StringPath backgroundUrl = createString("backgroundUrl");

    public final StringPath country = createString("country");

    public final StringPath director = createString("director");

    public final StringPath genre = createString("genre");

    public final StringPath moviePlot = createString("moviePlot");

    public final StringPath moviePosterUrl = createString("moviePosterUrl");

    public final StringPath movieTitle = createString("movieTitle");

    public final NumberPath<Integer> movieYear = createNumber("movieYear", Integer.class);

    public final StringPath runningTime = createString("runningTime");

    public final StringPath trailerUrl = createString("trailerUrl");

    public QMovieDetail(String variable) {
        super(MovieDetail.class, forVariable(variable));
    }

    public QMovieDetail(Path<? extends MovieDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovieDetail(PathMetadata metadata) {
        super(MovieDetail.class, metadata);
    }

}

