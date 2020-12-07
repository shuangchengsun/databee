package com.alan.databee.common;

public enum TaskStatus {
    SUCCESS("success"),
    FAILED("failed"),
    FINISH("finish")
    ;
    String status;

    TaskStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "status='" + status + '\'' +
                '}';
    }
}
