package com.admin.panel.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
    Name    : Role
    Author  : Boran Sahindal
    Created : 2024-12-02
*/

@Getter
@Setter
public class Role {
    static final Logger LOG = LogManager.getLogger(Role.class);

    Long id;

    String name;

}