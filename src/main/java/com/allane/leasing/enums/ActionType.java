package com.allane.leasing.enums;

public enum ActionType {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    VIEW("VIEW");

    private final String action;

    ActionType(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
