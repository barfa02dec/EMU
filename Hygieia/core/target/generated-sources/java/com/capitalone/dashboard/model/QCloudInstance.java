package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QCloudInstance is a Querydsl query type for CloudInstance
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCloudInstance extends EntityPathBase<CloudInstance> {

    private static final long serialVersionUID = -1608625848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCloudInstance cloudInstance = new QCloudInstance("cloudInstance");

    public final QBaseModel _super;

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath autoScaleName = createString("autoScaleName");

    public final NumberPath<Double> cpuUtilization = createNumber("cpuUtilization", Double.class);

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final NumberPath<Double> diskRead = createNumber("diskRead", Double.class);

    public final NumberPath<Double> diskWrite = createNumber("diskWrite", Double.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final BooleanPath imageApproved = createBoolean("imageApproved");

    public final NumberPath<Long> imageExpirationDate = createNumber("imageExpirationDate", Long.class);

    public final StringPath imageId = createString("imageId");

    public final StringPath instanceId = createString("instanceId");

    public final StringPath instanceOwner = createString("instanceOwner");

    public final StringPath instanceType = createString("instanceType");

    public final BooleanPath isMonitored = createBoolean("isMonitored");

    public final BooleanPath isStopped = createBoolean("isStopped");

    public final BooleanPath isTagged = createBoolean("isTagged");

    public final StringPath lastAction = createString("lastAction");

    public final NumberPath<Long> lastUpdatedDate = createNumber("lastUpdatedDate", Long.class);

    public final BooleanPath monitored = createBoolean("monitored");

    public final NumberPath<Double> networkIn = createNumber("networkIn", Double.class);

    public final NumberPath<Double> networkOut = createNumber("networkOut", Double.class);

    public final StringPath privateDns = createString("privateDns");

    public final StringPath privateIp = createString("privateIp");

    public final StringPath publicDns = createString("publicDns");

    public final StringPath publicIp = createString("publicIp");

    public final StringPath rootDeviceName = createString("rootDeviceName");

    public final ListPath<String, StringPath> securityGroups = this.<String, StringPath>createList("securityGroups", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath status = createString("status");

    public final BooleanPath stopped = createBoolean("stopped");

    public final StringPath subnetId = createString("subnetId");

    public final BooleanPath tagged = createBoolean("tagged");

    public final ListPath<NameValue, QNameValue> tags = this.<NameValue, QNameValue>createList("tags", NameValue.class, QNameValue.class, PathInits.DIRECT2);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath virtualNetworkId = createString("virtualNetworkId");

    public QCloudInstance(String variable) {
        this(CloudInstance.class, forVariable(variable), INITS);
    }

    public QCloudInstance(Path<? extends CloudInstance> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudInstance(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudInstance(PathMetadata<?> metadata, PathInits inits) {
        this(CloudInstance.class, metadata, inits);
    }

    public QCloudInstance(Class<? extends CloudInstance> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

