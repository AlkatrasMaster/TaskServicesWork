package org.example.domains.enums;

public enum TaskStatus {

    PENDING("в ожидании");

    private final String label;


    TaskStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
