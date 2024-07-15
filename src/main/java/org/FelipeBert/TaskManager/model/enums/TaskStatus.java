package org.FelipeBert.TaskManager.model.enums;

public enum TaskStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed");

    private String role;

    TaskStatus(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
