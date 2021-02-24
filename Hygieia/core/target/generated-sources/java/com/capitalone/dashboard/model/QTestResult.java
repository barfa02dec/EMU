package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTestResult is a Querydsl query type for TestResult
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTestResult extends EntityPathBase<TestResult> {

    private static final long serialVersionUID = -1383944015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTestResult testResult = new QTestResult("testResult");

    public final QBaseModel _super;

    public final org.bson.types.QObjectId buildId;

    public final org.bson.types.QObjectId collectorItemId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final StringPath description = createString("description");

    public final NumberPath<Long> duration = createNumber("duration", Long.class);

    public final NumberPath<Long> endTime = createNumber("endTime", Long.class);

    public final StringPath executionId = createString("executionId");

    public final NumberPath<Integer> failureCount = createNumber("failureCount", Integer.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final NumberPath<Integer> skippedCount = createNumber("skippedCount", Integer.class);

    public final NumberPath<Long> startTime = createNumber("startTime", Long.class);

    public final NumberPath<Integer> successCount = createNumber("successCount", Integer.class);

    public final StringPath targetAppName = createString("targetAppName");

    public final StringPath targetEnvName = createString("targetEnvName");

    public final CollectionPath<TestCapability, QTestCapability> testCapabilities = this.<TestCapability, QTestCapability>createCollection("testCapabilities", TestCapability.class, QTestCapability.class, PathInits.DIRECT2);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final NumberPath<Integer> totalCount = createNumber("totalCount", Integer.class);

    public final EnumPath<TestSuiteType> type = createEnum("type", TestSuiteType.class);

    public final NumberPath<Integer> unknownStatusCount = createNumber("unknownStatusCount", Integer.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath url = createString("url");

    public QTestResult(String variable) {
        this(TestResult.class, forVariable(variable), INITS);
    }

    public QTestResult(Path<? extends TestResult> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTestResult(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTestResult(PathMetadata<?> metadata, PathInits inits) {
        this(TestResult.class, metadata, inits);
    }

    public QTestResult(Class<? extends TestResult> type, PathMetadata<?> metadata, PathInits inits) {
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

