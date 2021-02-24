package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QCodeQuality is a Querydsl query type for CodeQuality
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCodeQuality extends EntityPathBase<CodeQuality> {

    private static final long serialVersionUID = -910994128L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCodeQuality codeQuality = new QCodeQuality("codeQuality");

    public final QBaseModel _super;

    public final org.bson.types.QObjectId buildId;

    public final org.bson.types.QObjectId collectorItemId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    // inherited
    public final org.bson.types.QObjectId id;

    public final SetPath<CodeQualityMetric, QCodeQualityMetric> metrics = this.<CodeQualityMetric, QCodeQualityMetric>createSet("metrics", CodeQualityMetric.class, QCodeQualityMetric.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final EnumPath<CodeQualityType> type = createEnum("type", CodeQualityType.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath url = createString("url");

    public final StringPath version = createString("version");

    public QCodeQuality(String variable) {
        this(CodeQuality.class, forVariable(variable), INITS);
    }

    public QCodeQuality(Path<? extends CodeQuality> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCodeQuality(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCodeQuality(PathMetadata<?> metadata, PathInits inits) {
        this(CodeQuality.class, metadata, inits);
    }

    public QCodeQuality(Class<? extends CodeQuality> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.buildId = inits.isInitialized("buildId") ? new org.bson.types.QObjectId(forProperty("buildId")) : null;
        this.collectorItemId = inits.isInitialized("collectorItemId") ? new org.bson.types.QObjectId(forProperty("collectorItemId")) : null;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

