package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QScopeOwner is a Querydsl query type for ScopeOwner
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QScopeOwner extends EntityPathBase<ScopeOwner> {

    private static final long serialVersionUID = 1019905569L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScopeOwner scopeOwner = new QScopeOwner("scopeOwner");

    public final QCollectorItem _super;

    public final StringPath assetState = createString("assetState");

    public final StringPath changeDate = createString("changeDate");

    // inherited
    public final QCollector collector;

    // inherited
    public final org.bson.types.QObjectId collectorId;

    public final org.bson.types.QObjectId collectorItemId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    //inherited
    public final StringPath description;

    //inherited
    public final BooleanPath enabled;

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath isDeleted = createString("isDeleted");

    //inherited
    public final NumberPath<Long> lastUpdated;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath niceName;

    //inherited
    public final MapPath<String, Object, SimplePath<Object>> options;

    //inherited
    public final StringPath project;

    //inherited
    public final BooleanPath pushed;

    //inherited
    public final BooleanPath taggedInComponents;

    public final StringPath teamId = createString("teamId");

    //inherited
    public final BooleanPath toShowIndashboard;

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QScopeOwner(String variable) {
        this(ScopeOwner.class, forVariable(variable), INITS);
    }

    public QScopeOwner(Path<? extends ScopeOwner> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QScopeOwner(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QScopeOwner(PathMetadata<?> metadata, PathInits inits) {
        this(ScopeOwner.class, metadata, inits);
    }

    public QScopeOwner(Class<? extends ScopeOwner> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QCollectorItem(type, metadata, inits);
        this.collector = _super.collector;
        this.collectorId = _super.collectorId;
        this.collectorItemId = inits.isInitialized("collectorItemId") ? new org.bson.types.QObjectId(forProperty("collectorItemId")) : null;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.description = _super.description;
        this.enabled = _super.enabled;
        this.id = _super.id;
        this.lastUpdated = _super.lastUpdated;
        this.niceName = _super.niceName;
        this.options = _super.options;
        this.project = _super.project;
        this.pushed = _super.pushed;
        this.taggedInComponents = _super.taggedInComponents;
        this.toShowIndashboard = _super.toShowIndashboard;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

