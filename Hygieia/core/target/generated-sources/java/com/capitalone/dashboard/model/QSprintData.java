package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSprintData is a Querydsl query type for SprintData
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QSprintData extends BeanPath<SprintData> {

    private static final long serialVersionUID = -1695959482L;

    public static final QSprintData sprintData = new QSprintData("sprintData");

    public final SimplePath<Burndown> burndown = createSimple("burndown", Burndown.class);

    public final NumberPath<Integer> committedIssueCount = createNumber("committedIssueCount", Integer.class);

    public final NumberPath<Double> committedStoryPoints = createNumber("committedStoryPoints", Double.class);

    public final DateTimePath<java.util.Date> completeDate = createDateTime("completeDate", java.util.Date.class);

    public final NumberPath<Integer> completedIssueCount = createNumber("completedIssueCount", Integer.class);

    public final NumberPath<Double> completedStoryPoints = createNumber("completedStoryPoints", Double.class);

    public final NumberPath<Integer> daysRemaining = createNumber("daysRemaining", Integer.class);

    public final SimplePath<DefectCount> defectsFound = createSimple("defectsFound", DefectCount.class);

    public final SimplePath<DefectCount> defectsResolved = createSimple("defectsResolved", DefectCount.class);

    public final SimplePath<DefectCount> defectsUnresolved = createSimple("defectsUnresolved", DefectCount.class);

    public final NumberPath<Integer> effortInPD = createNumber("effortInPD", Integer.class);

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final SimplePath<DefectCount> sprintDefectsResolved = createSimple("sprintDefectsResolved", DefectCount.class);

    public final NumberPath<Long> sprintId = createNumber("sprintId", Long.class);

    public final StringPath sprintName = createString("sprintName");

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public final StringPath state = createString("state");

    public QSprintData(String variable) {
        super(SprintData.class, forVariable(variable));
    }

    public QSprintData(Path<? extends SprintData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSprintData(PathMetadata<?> metadata) {
        super(SprintData.class, metadata);
    }

}

