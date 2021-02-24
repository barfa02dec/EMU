package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -1780668169L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final QBaseModel _super;

    public final BooleanPath automated = createBoolean("automated");

    public final StringPath businessUnit = createString("businessUnit");

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final StringPath customerCode = createString("customerCode");

    public final StringPath customerName = createString("customerName");

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath program = createString("program");

    public final StringPath projectId = createString("projectId");

    public final StringPath projectName = createString("projectName");

    public final StringPath projectOwner = createString("projectOwner");

    public final BooleanPath projectStatus = createBoolean("projectStatus");

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final SetPath<UserGroup, QUserGroup> usersGroup = this.<UserGroup, QUserGroup>createSet("usersGroup", UserGroup.class, QUserGroup.class, PathInits.DIRECT2);

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProject(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProject(PathMetadata<?> metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

