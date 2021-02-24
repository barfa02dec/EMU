package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QUserGroup is a Querydsl query type for UserGroup
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QUserGroup extends BeanPath<UserGroup> {

    private static final long serialVersionUID = -612982830L;

    public static final QUserGroup userGroup = new QUserGroup("userGroup");

    public final StringPath user = createString("user");

    public final SetPath<ProjectRoles, SimplePath<ProjectRoles>> userRoles = this.<ProjectRoles, SimplePath<ProjectRoles>>createSet("userRoles", ProjectRoles.class, SimplePath.class, PathInits.DIRECT2);

    public QUserGroup(String variable) {
        super(UserGroup.class, forVariable(variable));
    }

    public QUserGroup(Path<? extends UserGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserGroup(PathMetadata<?> metadata) {
        super(UserGroup.class, metadata);
    }

}

