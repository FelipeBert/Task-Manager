package org.FelipeBert.TaskManager.model.enums;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private String role;

    UserStatus(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
