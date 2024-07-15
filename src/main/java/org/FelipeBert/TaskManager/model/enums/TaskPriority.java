package org.FelipeBert.TaskManager.model.enums;

public enum TaskPriority {
    LOW("low"),
    AVERAGE("average"),
    HIGH("high");

    private String role;

    TaskPriority(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
