package com.admin.panel.exceptions.base;

public abstract non-sealed class AdminPanelValidationException extends AdminPanelException {
    public AdminPanelValidationException(String message) {
        super(message);
    }
}