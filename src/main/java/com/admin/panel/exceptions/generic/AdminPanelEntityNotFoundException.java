package com.admin.panel.exceptions.generic;

import com.admin.panel.exceptions.base.AdminPanelNotFoundException;

public class AdminPanelEntityNotFoundException extends AdminPanelNotFoundException {
    public AdminPanelEntityNotFoundException(String message) {
        super(message);
    }
}