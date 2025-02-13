package com.admin.panel.exceptions.generic;

import com.admin.panel.exceptions.base.AdminPanelAuthorisationException;

public class AdminPanelMissingRightsException extends AdminPanelAuthorisationException {
    public AdminPanelMissingRightsException(String message) {
        super(message);
    }
}
