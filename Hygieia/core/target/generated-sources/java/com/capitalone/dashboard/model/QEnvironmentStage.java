package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEnvironmentStage is a Querydsl query type for EnvironmentStage
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QEnvironmentStage extends BeanPath<EnvironmentStage> {

    private static final long serialVersionUID = 1288556685L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEnvironmentStage environmentStage = new QEnvironmentStage("environmentStage");

    public final QStage _super = new QStage(this);

    //inherited
    public final SetPath<PipelineCommit, SimplePath<PipelineCommit>> commits = _super.commits;

    public final QBinaryArtifact lastArtifact;

    public QEnvironmentStage(String variable) {
        this(EnvironmentStage.class, forVariable(variable), INITS);
    }

    public QEnvironmentStage(Path<? extends EnvironmentStage> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEnvironmentStage(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEnvironmentStage(PathMetadata<?> metadata, PathInits inits) {
        this(EnvironmentStage.class, metadata, inits);
    }

    public QEnvironmentStage(Class<? extends EnvironmentStage> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lastArtifact = inits.isInitialized("lastArtifact") ? new QBinaryArtifact(forProperty("lastArtifact"), inits.get("lastArtifact")) : null;
    }

}

