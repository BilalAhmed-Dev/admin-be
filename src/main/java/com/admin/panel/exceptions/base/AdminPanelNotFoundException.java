package com.admin.panel.exceptions.base;

public abstract non-sealed class AdminPanelNotFoundException extends AdminPanelException {
    public AdminPanelNotFoundException(String message) {
        super(message);
    }
}