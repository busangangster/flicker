package com.flicker.movie.movie.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovie is a Querydsl query type for Movie
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovie extends EntityPathBase<Movie> {

    private static final long serialVersionUID = -1260925488L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovie movie = new QMovie("movie");

    public final ListPath<Actor, QActor> actors = this.<Actor, QActor>createList("actors", Actor.class, QActor.class, PathInits.DIRECT2);

    public final StringPath delYN = createString("delYN");

    public final com.flicker.movie.movie.domain.vo.QMovieDetail movieDetail;

    public final NumberPath<Double> movieRating = createNumber("movieRating", Double.class);

    public final NumberPath<Integer> movieSeq = createNumber("movieSeq", Integer.class);

    public final ListPath<WordCloud, QWordCloud> wordClouds = this.<WordCloud, QWordCloud>createList("wordClouds", WordCloud.class, QWordCloud.class, PathInits.DIRECT2);

    public QMovie(String variable) {
        this(Movie.class, forVariable(variable), INITS);
    }

    public QMovie(Path<? extends Movie> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovie(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovie(PathMetadata metadata, PathInits inits) {
        this(Movie.class, metadata, inits);
    }

    public QMovie(Class<? extends Movie> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movieDetail = inits.isInitialized("movieDetail") ? new com.flicker.movie.movie.domain.vo.QMovieDetail(forProperty("movieDetail")) : null;
    }

}

