package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QCloudVolumeStorage is a Querydsl query type for CloudVolumeStorage
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCloudVolumeStorage extends EntityPathBase<CloudVolumeStorage> {

    private static final long serialVersionUID = -1707352786L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCloudVolumeStorage cloudVolumeStorage = new QCloudVolumeStorage("cloudVolumeStorage");

    public final QBaseModel _super;

    public final StringPath accountNumber = createString("accountNumber");

    public final ListPath<String, StringPath> attachInstances = this.<String, StringPath>createList("attachInstances", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdOn;

    public final NumberPath<Long> creationDate = createNumber("creationDate", Long.class);

    public final BooleanPath encrypted = createBoolean("encrypted");

    // inherited
    public final org.bson.types.QObjectId id;

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath status = createString("status");

    public final ListPath<NameValue, QNameValue> tags = this.<NameValue, QNameValue>createList("tags", NameValue.class, QNameValue.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    //inherited
    public final StringPath updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedOn;

    public final StringPath volumeId = createString("volumeId");

    public final StringPath zone = createString("zone");

    public QCloudVolumeStorage(String variable) {
        this(CloudVolumeStorage.class, forVariable(variable), INITS);
    }

    public QCloudVolumeStorage(Path<? extends CloudVolumeStorage> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudVolumeStorage(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCloudVolumeStorage(PathMetadata<?> metadata, PathInits inits) {
        this(CloudVolumeStorage.class, metadata, inits);
    }

    public QCloudVolumeStorage(Class<? extends CloudVolumeStorage> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseModel(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdOn = _super.createdOn;
        this.id = _super.id;
        this.updatedBy = _super.updatedBy;
        this.updatedOn = _super.updatedOn;
    }

}

