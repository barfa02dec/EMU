package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = 1154703679L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final QBaseModel _super;

    public final StringPath assetState = createString("assetState");

    public final StringPath changeDate = createString("changeDate");

    public final QCollector collector;

    public final org.bson.types.QObjectId collectorId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final BooleanPath enabled = createBoolean("enabled");

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath isDeleted = createString("isDeleted");

    public final StringPath name = createString("name");

    public final StringPath teamId = createString("teamId");

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTeam(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTeam(PathMetadata<?> metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.collector = inits.isInitialized("collector") ? new QCollector(forProperty("collector"), inits.get("collector")) : null;
        this.collectorId = inits.isInitialized("collectorId") ? new org.bson.types.QObjectId(forProperty("collectorId")) : null;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

