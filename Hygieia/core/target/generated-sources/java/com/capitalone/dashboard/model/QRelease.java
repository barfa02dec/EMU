package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QRelease is a Querydsl query type for Release
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRelease extends EntityPathBase<Release> {

    private static final long serialVersionUID = -380762651L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRelease release = new QRelease("release");

    public final QBaseModel _super;

    public final BooleanPath archived = createBoolean("archived");

    public final BooleanPath automated = createBoolean("automated");

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final StringPath description = createString("description");

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath jiraProjectId = createString("jiraProjectId");

    public final StringPath name = createString("name");

    public final StringPath originalreleaseData = createString("originalreleaseData");

    public final StringPath overdue = createString("overdue");

    public final StringPath projectId = createString("projectId");

    public final StringPath projectName = createString("projectName");

    public final BooleanPath released = createBoolean("released");

    public final DateTimePath<java.util.Date> releaseDate = createDateTime("releaseDate", java.util.Date.class);

    public final NumberPath<Long> releaseId = createNumber("releaseId", Long.class);

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final QVersionData versionData;

    public QRelease(String variable) {
        this(Release.class, forVariable(variable), INITS);
    }

    public QRelease(Path<? extends Release> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRelease(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRelease(PathMetadata<?> metadata, PathInits inits) {
        this(Release.class, metadata, inits);
    }

    public QRelease(Class<? extends Release> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
        this.versionData = inits.isInitialized("versionData") ? new QVersionData(forProperty("versionData")) : null;
    }

}

