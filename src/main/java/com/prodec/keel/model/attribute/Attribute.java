package com.prodec.keel.model.attribute;

public class Attribute {

    private int id;

    String label;
    AttributeType type = AttributeType.UNKNOWN;
    AttributeListener listener;

    public Attribute(String label, AttributeType type) {
        super();
        this.label = label;
        this.type = type;
    }

    public Attribute(String label, AttributeType type, AttributeListener listener) {
        super();
        this.label = label;
        this.type = type;
        this.listener = listener;
    }

    public AttributeType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListener(AttributeListener listener) {
        this.listener = listener;
    }

    public int getId() {
        return id;
    }
}
