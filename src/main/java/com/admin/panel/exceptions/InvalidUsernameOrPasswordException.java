package com.admin.panel.exceptions;

import com.admin.panel.exceptions.base.AdminPanelAuthorisationException;

public class InvalidUsernameOrPasswordException extends AdminPanelAuthorisationException {
    public InvalidUsernameOrPasswordException(String message) {
        super(message);
    }
}