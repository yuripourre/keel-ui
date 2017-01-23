package com.prodec.keel.model.attribute;

public class PathAttribute extends Attribute {

    String path;

    public PathAttribute(String label) {
        super(label, AttributeType.PATH);
        this.path = "";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
