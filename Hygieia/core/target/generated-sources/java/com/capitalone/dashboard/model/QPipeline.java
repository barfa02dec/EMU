package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPipeline is a Querydsl query type for Pipeline
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPipeline extends EntityPathBase<Pipeline> {

    private static final long serialVersionUID = 1260488836L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPipeline pipeline = new QPipeline("pipeline");

    public final QBaseModel _super;

    public final org.bson.types.QObjectId collectorItemId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final MapPath<String, EnvironmentStage, QEnvironmentStage> environmentStageMap = this.<String, EnvironmentStage, QEnvironmentStage>createMap("environmentStageMap", String.class, EnvironmentStage.class, QEnvironmentStage.class);

    public final SetPath<Build, QBuild> failedBuilds = this.<Build, QBuild>createSet("failedBuilds", Build.class, QBuild.class, PathInits.DIRECT2);

    // inherited
    public final org.bson.types.QObjectId id;

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QPipeline(String variable) {
        this(Pipeline.class, forVariable(variable), INITS);
    }

    public QPipeline(Path<? extends Pipeline> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPipeline(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPipeline(PathMetadata<?> metadata, PathInits inits) {
        this(Pipeline.class, metadata, inits);
    }

    public QPipeline(Class<? extends Pipeline> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.collectorItemId = inits.isInitialized("collectorItemId") ? new org.bson.types.QObjectId(forProperty("collectorItemId")) : null;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

