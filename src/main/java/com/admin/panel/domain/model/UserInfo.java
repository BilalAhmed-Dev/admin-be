package com.admin.panel.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfo {
    Long id;
    String username;
    String email;
    boolean isAdmin;
    Role role;
}
