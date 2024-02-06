package com.ssafy.lam.reviewBoard.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewBoard is a Querydsl query type for ReviewBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewBoard extends EntityPathBase<ReviewBoard> {

    private static final long serialVersionUID = 2059165217L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewBoard reviewBoard = new QReviewBoard("reviewBoard");

    public final NumberPath<Integer> cnt = createNumber("cnt", Integer.class);

    public final BooleanPath complain = createBoolean("complain");

    public final StringPath content = createString("content");

    public final StringPath doctor = createString("doctor");

    public final StringPath hospital = createString("hospital");

    public final BooleanPath isdeleted = createBoolean("isdeleted");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Long> regdate = createNumber("regdate", Long.class);

    public final StringPath region = createString("region");

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath surgery = createString("surgery");

    public final StringPath title = createString("title");

    public final com.ssafy.lam.user.domain.QUser user;

    public QReviewBoard(String variable) {
        this(ReviewBoard.class, forVariable(variable), INITS);
    }

    public QReviewBoard(Path<? extends ReviewBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewBoard(PathMetadata metadata, PathInits inits) {
        this(ReviewBoard.class, metadata, inits);
    }

    public QReviewBoard(Class<? extends ReviewBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.ssafy.lam.user.domain.QUser(forProperty("user")) : null;
    }

}
