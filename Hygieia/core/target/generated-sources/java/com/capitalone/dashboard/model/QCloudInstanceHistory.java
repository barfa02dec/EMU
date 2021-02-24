package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QCloudInstanceHistory is a Querydsl query type for CloudInstanceHistory
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCloudInstanceHistory extends EntityPathBase<CloudInstanceHistory> {

    private static final long serialVersionUID = -1289639380L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCloudInstanceHistory cloudInstanceHistory = new QCloudInstanceHistory("cloudInstanceHistory");

    public final QBaseModel _super;

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Double> cpu = createNumber("cpu", Double.class);

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final StringPath currency = createString("currency");

    public final NumberPath<Double> diskRead = createNumber("diskRead", Double.class);

    public final NumberPath<Double> diskWrite = createNumber("diskWrite", Double.class);

    public final NumberPath<Double> estimatedCharge = createNumber("estimatedCharge", Double.class);

    public final NumberPath<Integer> expiredImage = createNumber("expiredImage", Integer.class);

    // inherited
    public final org.bson.types.QObjectId id;

    public final NumberPath<Double> networkIn = createNumber("networkIn", Double.class);

    public final NumberPath<Double> networkOut = createNumber("networkOut", Double.class);

    public final NumberPath<Integer> nonTagged = createNumber("nonTagged", Integer.class);

    public final NumberPath<Integer> stopped = createNumber("stopped", Integer.class);

    public final NumberPath<Long> time = createNumber("time", Long.class);

    public final NumberPath<Integer> total = createNumber("total", Integer.class);

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public QCloudInstanceHistory(String variable) {
        this(CloudInstanceHistory.class, forVariable(variable), INITS);
    }

    public QCloudInstanceHistory(Path<? extends CloudInstanceHistory> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudInstanceHistory(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudInstanceHistory(PathMetadata<?> metadata, PathInits inits) {
        this(CloudInstanceHistory.class, metadata, inits);
    }

    public QCloudInstanceHistory(Class<? extends CloudInstanceHistory> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

