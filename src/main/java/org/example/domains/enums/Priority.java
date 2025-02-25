package org.example.domains.enums;

public enum Priority {
    HIGH("Высокий"),
    MEDIUM("Средний"),
    LOW("Низкий");

    private final String label;


    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
