package com.admin.panel.exceptions;

import com.admin.panel.exceptions.base.AdminPanelValidationException;


public class RoleAlreadyExistsException extends AdminPanelValidationException {
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}