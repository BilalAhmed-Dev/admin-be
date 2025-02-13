package com.admin.panel.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.admin.panel.domain.model.ApiExceptionContainer;
import com.admin.panel.exceptions.base.AdminPanelAuthorisationException;
import com.admin.panel.exceptions.base.AdminPanelException;
import com.admin.panel.exceptions.base.AdminPanelNotFoundException;
import com.admin.panel.exceptions.base.AdminPanelValidationException;

import java.time.LocalDateTime;


@ControllerAdvice(basePackages = "com.admin.panel.controller")
public class CommonAdvice {


    @ExceptionHandler(AdminPanelValidationException.class)
    public ResponseEntity<ApiExceptionContainer> handle(AdminPanelValidationException AdminPanelValidationException) {
        System.out.println("EXCEPTION: " + AdminPanelValidationException.getClass().getSimpleName() + "  " + LocalDateTime.now());
        System.out.println(AdminPanelValidationException.getMessage());
        ApiExceptionContainer apiExceptionContainer = new ApiExceptionContainer(AdminPanelValidationException.getClass().getSimpleName(), AdminPanelValidationException.getMessage(), null);
        return ResponseEntity.badRequest().body(apiExceptionContainer);
    }

    @ExceptionHandler(AdminPanelNotFoundException.class)
    public ResponseEntity<ApiExceptionContainer> handle(AdminPanelNotFoundException AdminPanelNotFoundException) {
        System.out.println("EXCEPTION: " + AdminPanelNotFoundException.getClass().getSimpleName() + "  " + LocalDateTime.now());
        if (AdminPanelNotFoundException.getMessage() != null) {
            System.out.println(AdminPanelNotFoundException.getMessage());
        }
        ApiExceptionContainer apiExceptionContainer = new ApiExceptionContainer(AdminPanelNotFoundException.getClass().getSimpleName(), AdminPanelNotFoundException.getMessage(), null);
        return ResponseEntity.status(404).body(apiExceptionContainer);
    }

    @ExceptionHandler(AdminPanelAuthorisationException.class)
    public ResponseEntity<ApiExceptionContainer> handle(AdminPanelAuthorisationException AdminPanelAuthorisationException) {
        System.out.println("EXCEPTION: " + AdminPanelAuthorisationException.getClass().getSimpleName() + "  " + LocalDateTime.now());
        if (AdminPanelAuthorisationException.getMessage() != null) {
            System.out.println(AdminPanelAuthorisationException.getMessage());
        }
        ApiExceptionContainer apiExceptionContainer = new ApiExceptionContainer(AdminPanelAuthorisationException.getClass().getSimpleName(), AdminPanelAuthorisationException.getMessage(), null);
        return ResponseEntity.status(403).body(apiExceptionContainer);
    }

    @ExceptionHandler(AdminPanelException.class)
    public ResponseEntity<ApiExceptionContainer> handle(AdminPanelException AdminPanelException) {
        System.out.println("EXCEPTION: " + AdminPanelException.getClass().getSimpleName() + "  " + LocalDateTime.now());
        if (AdminPanelException.getMessage() != null) {
            System.out.println(AdminPanelException.getMessage());
        }

        ApiExceptionContainer apiExceptionContainer = new ApiExceptionContainer(AdminPanelException.getClass().getSimpleName(), AdminPanelException.getMessage(), null);
        return ResponseEntity.internalServerError().body(apiExceptionContainer);
    }

    @ExceptionHandler(Throwable.class)
    public synchronized ResponseEntity<ApiExceptionContainer> handle(Throwable throwable) {
        System.out.println("EXCEPTION THROWABLE: " + throwable.getClass().getSimpleName() + "  " + LocalDateTime.now());
        throwable.printStackTrace();

        ApiExceptionContainer apiExceptionContainer = new ApiExceptionContainer(throwable.getClass().getSimpleName(), "Sunucu hatası. En kısa zamanda ilgileniyoruz.", null);
        return ResponseEntity.internalServerError().body(apiExceptionContainer);
    }
}
