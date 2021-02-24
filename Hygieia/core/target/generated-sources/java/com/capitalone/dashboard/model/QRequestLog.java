package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QRequestLog is a Querydsl query type for RequestLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRequestLog extends EntityPathBase<RequestLog> {

    private static final long serialVersionUID = 1262806935L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequestLog requestLog = new QRequestLog("requestLog");

    public final QBaseModel _super;

    public final StringPath client = createString("client");

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final StringPath endpoint = createString("endpoint");

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath method = createString("method");

    public final StringPath parameter = createString("parameter");

    public final SimplePath<Object> requestBody = createSimple("requestBody", Object.class);

    public final StringPath requestContentType = createString("requestContentType");

    public final NumberPath<Long> requestSize = createNumber("requestSize", Long.class);

    public final SimplePath<Object> responseBody = createSimple("responseBody", Object.class);

    public final NumberPath<Integer> responseCode = createNumber("responseCode", Integer.class);

    public final StringPath responseContentType = createString("responseContentType");

    public final NumberPath<Long> responseSize = createNumber("responseSize", Long.class);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QRequestLog(String variable) {
        this(RequestLog.class, forVariable(variable), INITS);
    }

    public QRequestLog(Path<? extends RequestLog> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRequestLog(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRequestLog(PathMetadata<?> metadata, PathInits inits) {
        this(RequestLog.class, metadata, inits);
    }

    public QRequestLog(Class<? extends RequestLog> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

