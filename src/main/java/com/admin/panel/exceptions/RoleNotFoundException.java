package com.admin.panel.exceptions;

import com.admin.panel.exceptions.base.AdminPanelNotFoundException;

public class RoleNotFoundException extends AdminPanelNotFoundException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}