package com.admin.panel.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.admin.panel.domain.model.Role;
import com.admin.panel.service.JwtService;
import com.admin.panel.service.RolesService;

import java.util.List;

/*
    Name    : RolesController
    Author  : Boran Sahindal
    Created : 2024-12-02
*/

@RestController()
@RequestMapping("roles")
@CrossOrigin
public class RolesController {
    static final Logger LOG = LogManager.getLogger(RolesController.class);
    private final JwtService jwtService;
    private final RolesService rolesService;

    public RolesController(JwtService jwtService, RolesService rolesService) {
        this.jwtService = jwtService;
        this.rolesService = rolesService;
    }

    @GetMapping
    public List<Role> getRoles(HttpServletRequest request) {
        Long loggedInUserId = jwtService.getLoggedInUserId(request, "accessToken");
        return rolesService.getRoles(loggedInUserId);
    }

    @PostMapping("add-role")
    public Role addRole(HttpServletRequest request, @RequestBody Role role) {
        Long loggedInUserId = jwtService.getLoggedInUserId(request, "accessToken");
        return rolesService.addRole(loggedInUserId, role);
    }

    @DeleteMapping("remove-role/{roleId}")
    public ResponseEntity<Boolean> removeRole(HttpServletRequest request, @PathVariable Long roleId) {
        Long loggedInUserId = jwtService.getLoggedInUserId(request, "accessToken");
        rolesService.removeRole(loggedInUserId, roleId);
        return ResponseEntity.ok(true);
    }
}