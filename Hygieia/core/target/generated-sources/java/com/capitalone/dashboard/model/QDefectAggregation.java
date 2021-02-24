package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDefectAggregation is a Querydsl query type for DefectAggregation
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDefectAggregation extends EntityPathBase<DefectAggregation> {

    private static final long serialVersionUID = -8354545L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDefectAggregation defectAggregation = new QDefectAggregation("defectAggregation");

    public final QBaseModel _super;

    public final BooleanPath automated = createBoolean("automated");

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final MapPath<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>> defectsByAgeDetails = this.<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>>createMap("defectsByAgeDetails", String.class, java.util.List.class, SimplePath.class);

    public final MapPath<String, Integer, NumberPath<Integer>> defectsByEnvironment = this.<String, Integer, NumberPath<Integer>>createMap("defectsByEnvironment", String.class, Integer.class, NumberPath.class);

    public final MapPath<String, Integer, NumberPath<Integer>> defectsByPriority = this.<String, Integer, NumberPath<Integer>>createMap("defectsByPriority", String.class, Integer.class, NumberPath.class);

    public final MapPath<String, Integer, NumberPath<Integer>> defectsByProirity = this.<String, Integer, NumberPath<Integer>>createMap("defectsByProirity", String.class, Integer.class, NumberPath.class);

    public final MapPath<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>> defectsByResolutionDetails = this.<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>>createMap("defectsByResolutionDetails", String.class, java.util.List.class, SimplePath.class);

    public final MapPath<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>> fixeddefectsByResolutions = this.<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>>createMap("fixeddefectsByResolutions", String.class, java.util.List.class, SimplePath.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath metricsProjectId = createString("metricsProjectId");

    public final MapPath<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>> openDefectsByAge = this.<String, java.util.List<java.util.Map<String, String>>, SimplePath<java.util.List<java.util.Map<String, String>>>>createMap("openDefectsByAge", String.class, java.util.List.class, SimplePath.class);

    public final StringPath projectId = createString("projectId");

    public final StringPath projectName = createString("projectName");

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath valuesAsOn = createString("valuesAsOn");

    public QDefectAggregation(String variable) {
        this(DefectAggregation.class, forVariable(variable), INITS);
    }

    public QDefectAggregation(Path<? extends DefectAggregation> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDefectAggregation(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDefectAggregation(PathMetadata<?> metadata, PathInits inits) {
        this(DefectAggregation.class, metadata, inits);
    }

    public QDefectAggregation(Class<? extends DefectAggregation> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

