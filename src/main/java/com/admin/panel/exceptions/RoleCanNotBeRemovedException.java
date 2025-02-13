package com.admin.panel.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.admin.panel.exceptions.base.AdminPanelValidationException;

/*
    Name    : RoleCanNotBeRemovedException
    Author  : Boran Sahindal
    Created : 2024-12-02
*/

public class RoleCanNotBeRemovedException extends AdminPanelValidationException {
    static final Logger LOG = LogManager.getLogger(RoleCanNotBeRemovedException.class);

    public RoleCanNotBeRemovedException(String message) {
        super(message);
    }
}