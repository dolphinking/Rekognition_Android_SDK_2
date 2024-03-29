package com.rekognition.adapter.model;


public class FieldNotFoundException extends Exception {

    private final String missingField;

    public FieldNotFoundException(String field) {
        super("Field : " + field + " is missing");
        this.missingField = field;
    }

    /**
     * @return the missingField
     */
    public String getMissingField() {
        return missingField;
    }

}
