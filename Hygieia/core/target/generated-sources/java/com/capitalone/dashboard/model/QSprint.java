package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSprint is a Querydsl query type for Sprint
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSprint extends EntityPathBase<Sprint> {

    private static final long serialVersionUID = 1550708860L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSprint sprint = new QSprint("sprint");

    public final QBaseModel _super;

    public final BooleanPath automated = createBoolean("automated");

    public final BooleanPath closed = createBoolean("closed");

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final BooleanPath editable = createBoolean("editable");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final NumberPath<Long> jiraProjectId = createNumber("jiraProjectId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath projectId = createString("projectId");

    public final StringPath projectName = createString("projectName");

    public final NumberPath<Long> sid = createNumber("sid", Long.class);

    public final QSprintData sprintData;

    public final NumberPath<Long> sprintId = createNumber("sprintId", Long.class);

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath viewBoardsUrl = createString("viewBoardsUrl");

    public QSprint(String variable) {
        this(Sprint.class, forVariable(variable), INITS);
    }

    public QSprint(Path<? extends Sprint> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprint(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprint(PathMetadata<?> metadata, PathInits inits) {
        this(Sprint.class, metadata, inits);
    }

    public QSprint(Class<? extends Sprint> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.sprintData = inits.isInitialized("sprintData") ? new QSprintData(forProperty("sprintData")) : null;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

