package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTestCapability is a Querydsl query type for TestCapability
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QTestCapability extends BeanPath<TestCapability> {

    private static final long serialVersionUID = -906976340L;

    public static final QTestCapability testCapability = new QTestCapability("testCapability");

    public final StringPath description = createString("description");

    public final NumberPath<Long> duration = createNumber("duration", Long.class);

    public final NumberPath<Long> endTime = createNumber("endTime", Long.class);

    public final StringPath executionId = createString("executionId");

    public final NumberPath<Integer> failedTestSuiteCount = createNumber("failedTestSuiteCount", Integer.class);

    public final NumberPath<Integer> skippedTestSuiteCount = createNumber("skippedTestSuiteCount", Integer.class);

    public final NumberPath<Long> startTime = createNumber("startTime", Long.class);

    public final EnumPath<TestCaseStatus> status = createEnum("status", TestCaseStatus.class);

    public final NumberPath<Integer> successTestSuiteCount = createNumber("successTestSuiteCount", Integer.class);

    public final CollectionPath<TestSuite, SimplePath<TestSuite>> testSuites = this.<TestSuite, SimplePath<TestSuite>>createCollection("testSuites", TestSuite.class, SimplePath.class, PathInits.DIRECT2);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final NumberPath<Integer> totalTestSuiteCount = createNumber("totalTestSuiteCount", Integer.class);

    public final EnumPath<TestSuiteType> type = createEnum("type", TestSuiteType.class);

    public final NumberPath<Integer> unknownStatusTestSuiteCount = createNumber("unknownStatusTestSuiteCount", Integer.class);

    public final StringPath url = createString("url");

    public QTestCapability(String variable) {
        super(TestCapability.class, forVariable(variable));
    }

    public QTestCapability(Path<? extends TestCapability> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTestCapability(PathMetadata<?> metadata) {
        super(TestCapability.class, metadata);
    }

}

