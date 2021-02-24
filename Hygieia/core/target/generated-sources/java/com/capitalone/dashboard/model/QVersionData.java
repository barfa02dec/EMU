package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QVersionData is a Querydsl query type for VersionData
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QVersionData extends BeanPath<VersionData> {

    private static final long serialVersionUID = 31069056L;

    public static final QVersionData versionData = new QVersionData("versionData");

    public final SimplePath<DefectCount> defectsFound = createSimple("defectsFound", DefectCount.class);

    public final SimplePath<DefectCount> defectsResolved = createSimple("defectsResolved", DefectCount.class);

    public final SimplePath<DefectCount> defectsUnresolved = createSimple("defectsUnresolved", DefectCount.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> effortInPD = createNumber("effortInPD", Integer.class);

    public final NumberPath<Integer> noofStoryCompleted = createNumber("noofStoryCompleted", Integer.class);

    public final NumberPath<Float> noofStoryPoints = createNumber("noofStoryPoints", Float.class);

    public final BooleanPath released = createBoolean("released");

    public final DateTimePath<java.util.Date> releaseDate = createDateTime("releaseDate", java.util.Date.class);

    public final NumberPath<Long> releaseId = createNumber("releaseId", Long.class);

    public final StringPath releaseName = createString("releaseName");

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public QVersionData(String variable) {
        super(VersionData.class, forVariable(variable));
    }

    public QVersionData(Path<? extends VersionData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVersionData(PathMetadata<?> metadata) {
        super(VersionData.class, metadata);
    }

}

