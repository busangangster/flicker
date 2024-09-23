package com.flicker.movie.movie.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActor is a Querydsl query type for Actor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActor extends EntityPathBase<Actor> {

    private static final long serialVersionUID = -1272366955L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActor actor = new QActor("actor");

    public final StringPath actorName = createString("actorName");

    public final NumberPath<Integer> actorSeq = createNumber("actorSeq", Integer.class);

    public final QMovie movie;

    public final StringPath role = createString("role");

    public QActor(String variable) {
        this(Actor.class, forVariable(variable), INITS);
    }

    public QActor(Path<? extends Actor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActor(PathMetadata metadata, PathInits inits) {
        this(Actor.class, metadata, inits);
    }

    public QActor(Class<? extends Actor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movie = inits.isInitialized("movie") ? new QMovie(forProperty("movie"), inits.get("movie")) : null;
    }

}

