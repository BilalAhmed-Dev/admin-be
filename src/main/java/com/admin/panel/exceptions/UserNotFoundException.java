package com.admin.panel.exceptions;

import com.admin.panel.exceptions.base.AdminPanelNotFoundException;

public class UserNotFoundException extends AdminPanelNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}