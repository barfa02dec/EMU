package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRepoBranch is a Querydsl query type for RepoBranch
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QRepoBranch extends BeanPath<RepoBranch> {

    private static final long serialVersionUID = 1781431574L;

    public static final QRepoBranch repoBranch = new QRepoBranch("repoBranch");

    public final StringPath branch = createString("branch");

    public final StringPath repoName = createString("repoName");

    public final EnumPath<RepoBranch.RepoType> type = createEnum("type", RepoBranch.RepoType.class);

    public final StringPath url = createString("url");

    public QRepoBranch(String variable) {
        super(RepoBranch.class, forVariable(variable));
    }

    public QRepoBranch(Path<? extends RepoBranch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRepoBranch(PathMetadata<?> metadata) {
        super(RepoBranch.class, metadata);
    }

}

