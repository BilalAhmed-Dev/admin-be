package com.admin.panel.exceptions.generic;

import com.admin.panel.exceptions.base.AdminPanelValidationException;

public class IncorrectInputException extends AdminPanelValidationException {
    public IncorrectInputException(String message) {
        super(message);
    }
}