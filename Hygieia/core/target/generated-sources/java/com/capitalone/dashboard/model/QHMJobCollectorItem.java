package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QHMJobCollectorItem is a Querydsl query type for HMJobCollectorItem
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QHMJobCollectorItem extends EntityPathBase<HMJobCollectorItem> {

    private static final long serialVersionUID = 1083657962L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHMJobCollectorItem hMJobCollectorItem = new QHMJobCollectorItem("hMJobCollectorItem");

    public final QCollectorItem _super;

    // inherited
    public final QCollector collector;

    // inherited
    public final org.bson.types.QObjectId collectorId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    //inherited
    public final StringPath description;

    //inherited
    public final BooleanPath enabled;

    // inherited
    public final org.bson.types.QObjectId id;

    public final StringPath instanceUrl = createString("instanceUrl");

    public final StringPath jobName = createString("jobName");

    public final StringPath jobUrl = createString("jobUrl");

    //inherited
    public final NumberPath<Long> lastUpdated;

    //inherited
    public final StringPath niceName;

    //inherited
    public final MapPath<String, Object, SimplePath<Object>> options;

    public final StringPath project = createString("project");

    //inherited
    public final BooleanPath pushed;

    //inherited
    public final BooleanPath taggedInComponents;

    //inherited
    public final BooleanPath toShowIndashboard;

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QHMJobCollectorItem(String variable) {
        this(HMJobCollectorItem.class, forVariable(variable), INITS);
    }

    public QHMJobCollectorItem(Path<? extends HMJobCollectorItem> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QHMJobCollectorItem(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QHMJobCollectorItem(PathMetadata<?> metadata, PathInits inits) {
        this(HMJobCollectorItem.class, metadata, inits);
    }

    public QHMJobCollectorItem(Class<? extends HMJobCollectorItem> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QCollectorItem(type, metadata, inits);
        this.collector = _super.collector;
        this.collectorId = _super.collectorId;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.description = _super.description;
        this.enabled = _super.enabled;
        this.id = _super.id;
        this.lastUpdated = _super.lastUpdated;
        this.niceName = _super.niceName;
        this.options = _super.options;
        this.pushed = _super.pushed;
        this.taggedInComponents = _super.taggedInComponents;
        this.toShowIndashboard = _super.toShowIndashboard;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

