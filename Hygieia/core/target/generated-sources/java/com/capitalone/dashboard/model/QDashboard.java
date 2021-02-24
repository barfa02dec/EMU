package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDashboard is a Querydsl query type for Dashboard
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDashboard extends EntityPathBase<Dashboard> {

    private static final long serialVersionUID = -1978152718L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDashboard dashboard = new QDashboard("dashboard");

    public final QBaseModel _super;

    public final QApplication application;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath owner = createString("owner");

    public final StringPath projectId = createString("projectId");

    public final StringPath template = createString("template");

    public final StringPath title = createString("title");

    public final EnumPath<DashboardType> type = createEnum("type", DashboardType.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final SetPath<String, StringPath> usersList = this.<String, StringPath>createSet("usersList", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<Widget, QWidget> widgets = this.<Widget, QWidget>createList("widgets", Widget.class, QWidget.class, PathInits.DIRECT2);

    public QDashboard(String variable) {
        this(Dashboard.class, forVariable(variable), INITS);
    }

    public QDashboard(Path<? extends Dashboard> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDashboard(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDashboard(PathMetadata<?> metadata, PathInits inits) {
        this(Dashboard.class, metadata, inits);
    }

    public QDashboard(Class<? extends Dashboard> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.application = inits.isInitialized("application") ? new QApplication(forProperty("application")) : null;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

