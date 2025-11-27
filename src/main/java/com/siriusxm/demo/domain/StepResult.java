package com.siriusxm.demo.domain;

public record StepResult(boolean successful, String stepName, String message) {

    public static StepResult success(String stepName, String message) {
        return new StepResult(true, stepName, message);
    }

    public static StepResult failure(String stepName, String message) {
        return new StepResult(false, stepName, message);
    }
}
