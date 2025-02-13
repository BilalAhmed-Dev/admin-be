package com.admin.panel.controller;

import com.admin.panel.domain.model.AuthInfo;
import com.admin.panel.domain.model.AuthenticationResponse;
import com.admin.panel.domain.model.TokenContainer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.admin.panel.domain.model.*;
import com.admin.panel.service.AuthenticationService;
import com.admin.panel.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("signup")
    @CrossOrigin
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthInfo user
    ) {
        return ResponseEntity.ok(authService.register(user));
    }


    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthInfo user
    ) {
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("logout")
    @CrossOrigin
    public ResponseEntity<String> logout(@RequestBody TokenContainer tokenContainer){
        String logoutMessage = authService.logout(tokenContainer.accessToken(), tokenContainer.refreshToken());
        return ResponseEntity.ok(logoutMessage);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request
    ) {
        // Only valid refresh tokens are permitted for use at this endpoint.
        Long loggedInUserId = jwtService.getLoggedInUserId(request, "refreshToken");
        return ResponseEntity.ok(authService.refreshToken(loggedInUserId));
    }
}
