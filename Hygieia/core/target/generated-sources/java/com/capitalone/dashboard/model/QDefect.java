package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDefect is a Querydsl query type for Defect
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDefect extends EntityPathBase<Defect> {

    private static final long serialVersionUID = 1110751187L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDefect defect = new QDefect("defect");

    public final QBaseModel _super;

    public final StringPath assignee = createString("assignee");

    public final NumberPath<Long> collectorId = createNumber("collectorId", Long.class);

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final NumberPath<Integer> defectAge = createNumber("defectAge", Integer.class);

    public final StringPath defectDescription = createString("defectDescription");

    public final StringPath defectId = createString("defectId");

    public final StringPath defectPriority = createString("defectPriority");

    public final NumberPath<Integer> defectResolutionInDays = createNumber("defectResolutionInDays", Integer.class);

    public final StringPath defectResolutionStatus = createString("defectResolutionStatus");

    public final StringPath defectSeverity = createString("defectSeverity");

    public final StringPath defectStatus = createString("defectStatus");

    public final StringPath dueDate = createString("dueDate");

    public final StringPath emuProjectId = createString("emuProjectId");

    public final StringPath environment = createString("environment");

    public final NumberPath<Integer> estimatedTime = createNumber("estimatedTime", Integer.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final NumberPath<Integer> originalEstimate = createNumber("originalEstimate", Integer.class);

    public final StringPath projectId = createString("projectId");

    public final StringPath projectName = createString("projectName");

    public final StringPath reporter = createString("reporter");

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QDefect(String variable) {
        this(Defect.class, forVariable(variable), INITS);
    }

    public QDefect(Path<? extends Defect> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDefect(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDefect(PathMetadata<?> metadata, PathInits inits) {
        this(Defect.class, metadata, inits);
    }

    public QDefect(Class<? extends Defect> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

