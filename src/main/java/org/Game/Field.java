package org.Game;

public class Field {
    private int fieldValue;

    private String fieldName;

    public Field(int fieldValue, String fieldName){
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
    }

    public int getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(int fieldValue) {
        this.fieldValue = fieldValue;
    }
}
