package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QCloudVirtualNetwork is a Querydsl query type for CloudVirtualNetwork
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCloudVirtualNetwork extends EntityPathBase<CloudVirtualNetwork> {

    private static final long serialVersionUID = 982715158L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCloudVirtualNetwork cloudVirtualNetwork = new QCloudVirtualNetwork("cloudVirtualNetwork");

    public final QBaseModel _super;

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath cidrBlock = createString("cidrBlock");

    public final org.bson.types.QObjectId collectorItemId;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final NumberPath<Long> creationDate = createNumber("creationDate", Long.class);

    public final BooleanPath defaultNetwork = createBoolean("defaultNetwork");

    // inherited
    public final org.bson.types.QObjectId id;

    public final NumberPath<Long> lastUpdateDate = createNumber("lastUpdateDate", Long.class);

    public final StringPath state = createString("state");

    public final MapPath<String, String, StringPath> tags = this.<String, String, StringPath>createMap("tags", String.class, String.class, StringPath.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath virtualNetworkId = createString("virtualNetworkId");

    public QCloudVirtualNetwork(String variable) {
        this(CloudVirtualNetwork.class, forVariable(variable), INITS);
    }

    public QCloudVirtualNetwork(Path<? extends CloudVirtualNetwork> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudVirtualNetwork(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudVirtualNetwork(PathMetadata<?> metadata, PathInits inits) {
        this(CloudVirtualNetwork.class, metadata, inits);
    }

    public QCloudVirtualNetwork(Class<? extends CloudVirtualNetwork> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.collectorItemId = inits.isInitialized("collectorItemId") ? new org.bson.types.QObjectId(forProperty("collectorItemId")) : null;
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

