package com.admin.panel.exceptions.base;

public sealed abstract class AdminPanelException extends RuntimeException
        permits
        AdminPanelInternalServerException,
        AdminPanelValidationException,
        AdminPanelNotFoundException,
        AdminPanelAuthorisationException {
    public AdminPanelException(String message) {
        super(message);
    }

    public AdminPanelException(String message, Throwable cause) {
        super(message, cause);
    }
}
