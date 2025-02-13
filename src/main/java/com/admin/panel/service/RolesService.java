package com.admin.panel.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.admin.panel.domain.entity.RolesEntity;
import com.admin.panel.domain.entity.UserEntity;
import com.admin.panel.domain.model.Role;
import com.admin.panel.domain.repository.RolesRepository;
import com.admin.panel.domain.repository.UserRepository;
import com.admin.panel.exceptions.RoleAlreadyExistsException;
import com.admin.panel.exceptions.RoleCanNotBeRemovedException;
import com.admin.panel.exceptions.RoleNotFoundException;
import com.admin.panel.exceptions.UserIsNotAdminException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    Name    : RolesService
    Author  : Boran Sahindal
    Created : 2024-12-02
*/

@Component
public class RolesService {
    static final Logger LOG = LogManager.getLogger(RolesService.class);
    private final UserService userService;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public RolesService(UserService userService,
                        RolesRepository rolesRepository,
                        ModelMapper modelMapper,
                        UserRepository userRepository) {
        this.userService = userService;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public Role addRole(Long loggedInUserId, Role role) {
        checkIfUserIsAdminThrowOtherwise(loggedInUserId);

        if (rolesRepository.existsByName(role.getName())) {
            throw new RoleAlreadyExistsException("Role already exists");
        }

        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setName(role.getName());
        rolesEntity = rolesRepository.save(rolesEntity);
        return modelMapper.map(rolesEntity, Role.class);
    }

    public List<Role> getRoles(Long loggedInUserId) {
        checkIfUserIsAdminThrowOtherwise(loggedInUserId);
        return rolesRepository.findAll().stream().map(rolesEntity -> modelMapper.map(rolesEntity, Role.class)).toList();
    }

    public void removeRole(Long loggedInUserId, Long roleId) {
        checkIfUserIsAdminThrowOtherwise(loggedInUserId);

        Optional<RolesEntity> rolesEntityOptional = rolesRepository.findById(roleId);
        if (rolesEntityOptional.isEmpty()) {
            throw new RoleNotFoundException("Role does not exist");
        }
        RolesEntity rolesEntity = rolesEntityOptional.get();

        List<UserEntity> allByRole = userRepository.findAllByRole(rolesEntity);

        if (!allByRole.isEmpty()) {
            throw new RoleCanNotBeRemovedException("Some users are assigned to this role: " + allByRole.stream().map(UserEntity::getUsername).collect(Collectors.joining(", ")));
        }
        rolesRepository.delete(rolesEntity);

    }

    private void checkIfUserIsAdminThrowOtherwise(Long loggedInUserId) {
        UserEntity loggedInUser = userService.getUserById(loggedInUserId);
        if (!loggedInUser.isAdmin()) {
            throw new UserIsNotAdminException("You are not an admin");
        }
    }
}