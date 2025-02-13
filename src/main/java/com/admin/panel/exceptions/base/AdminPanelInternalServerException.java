package com.admin.panel.exceptions.base;


public abstract non-sealed class AdminPanelInternalServerException extends AdminPanelException {
    public AdminPanelInternalServerException(String message) {
        super(message);
    }
}