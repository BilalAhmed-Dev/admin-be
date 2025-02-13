package com.admin.panel.exceptions;


import com.admin.panel.exceptions.base.AdminPanelAuthorisationException;

public class InvalidTokenException extends AdminPanelAuthorisationException {
    public InvalidTokenException(String message) {
        super(message);
    }
}