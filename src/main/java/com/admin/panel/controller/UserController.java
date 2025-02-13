package com.admin.panel.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.admin.panel.domain.model.AssignRole;
import com.admin.panel.domain.model.UserInfo;
import com.admin.panel.service.JwtService;
import com.admin.panel.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers(HttpServletRequest request) {
        Long loggedInUserId = jwtService.getLoggedInUserId(request, "accessToken");
        List<UserInfo> users = userService.getAllUsers(loggedInUserId);
        return ResponseEntity.ok(users);

    }

    @PostMapping("assignrole")
    public void assignRole(
            HttpServletRequest request,
            @RequestBody AssignRole assignRole
    ) {
        Long loggedInUserId = jwtService.getLoggedInUserId(request, "accessToken");
        userService.assignRole(loggedInUserId, assignRole);
    }
}