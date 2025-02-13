package com.admin.panel.exceptions;

import com.admin.panel.exceptions.base.AdminPanelAuthorisationException;

public class UserIsNotAdminException extends AdminPanelAuthorisationException {
    public UserIsNotAdminException(String message) {
        super(message);
    }
}