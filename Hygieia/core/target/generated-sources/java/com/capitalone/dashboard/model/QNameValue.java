package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QNameValue is a Querydsl query type for NameValue
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QNameValue extends BeanPath<NameValue> {

    private static final long serialVersionUID = 294708228L;

    public static final QNameValue nameValue = new QNameValue("nameValue");

    public final StringPath name = createString("name");

    public final StringPath value = createString("value");

    public QNameValue(String variable) {
        super(NameValue.class, forVariable(variable));
    }

    public QNameValue(Path<? extends NameValue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNameValue(PathMetadata<?> metadata) {
        super(NameValue.class, metadata);
    }

}

