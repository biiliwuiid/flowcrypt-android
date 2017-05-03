package com.flowcrypt.email.test;

import com.eclipsesource.v8.V8Object;

public class Attachment extends MeaningfulV8ObjectContainer {

    public Attachment(V8Object o) {
        super(o);
    }

    public String getName() {
        return this.v8object.getString("name");
    }

    public String getType() {
        return this.v8object.getString("type");
    }

    public Double getSize() {
        return this.v8object.getDouble("size");
    }

}