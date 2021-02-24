package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QHeatMap is a Querydsl query type for HeatMap
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QHeatMap extends EntityPathBase<HeatMap> {

    private static final long serialVersionUID = -675596502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHeatMap heatMap = new QHeatMap("heatMap");

    public final QBaseModel _super;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final DateTimePath<java.util.Date> formattedSubmissionDate = createDateTime("formattedSubmissionDate", java.util.Date.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final QProjectHeatmapData projectHeatmapData;

    public final StringPath projectId = createString("projectId");

    public final StringPath submissionDate = createString("submissionDate");

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QHeatMap(String variable) {
        this(HeatMap.class, forVariable(variable), INITS);
    }

    public QHeatMap(Path<? extends HeatMap> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QHeatMap(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QHeatMap(PathMetadata<?> metadata, PathInits inits) {
        this(HeatMap.class, metadata, inits);
    }

    public QHeatMap(Class<? extends HeatMap> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.projectHeatmapData = inits.isInitialized("projectHeatmapData") ? new QProjectHeatmapData(forProperty("projectHeatmapData")) : null;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

