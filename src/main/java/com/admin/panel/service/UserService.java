package com.admin.panel.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.admin.panel.domain.entity.RolesEntity;
import com.admin.panel.domain.entity.UserEntity;
import com.admin.panel.domain.model.AssignRole;
import com.admin.panel.domain.model.UserInfo;
import com.admin.panel.domain.repository.RolesRepository;
import com.admin.panel.domain.repository.UserRepository;
import com.admin.panel.exceptions.RoleNotFoundException;
import com.admin.panel.exceptions.UserIsNotAdminException;
import com.admin.panel.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RolesRepository rolesRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.rolesRepository = rolesRepository;
    }

    public UserInfo getUserInfo(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User ID '" + userId + "' not found"));

        return modelMapper.map(userEntity, UserInfo.class);
    }

    public List<UserInfo> getAllUsers(Long loggedInUserId) {
        UserEntity userEntity = getUserById(loggedInUserId);
        if (userEntity.isAdmin()) {
            return getAllUsers();
        }
        throw new UserIsNotAdminException("You do not have the ADMIN role to view this resource");
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }


    private List<UserInfo> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserInfo.class))
                .collect(Collectors.toList());
    }


    public void assignRole(Long loggedInUserId, AssignRole assignRole) {
        UserEntity loggedInUserEntity = getUserById(loggedInUserId);
        if (!loggedInUserEntity.isAdmin()) {
            throw new UserIsNotAdminException("You do not have the required role to view this resource");
        }
        UserEntity userEntity = userRepository.findById(assignRole.getUserID())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + assignRole.getUserID() + " not found"));
        Optional<RolesEntity> rolesEntityOptional = rolesRepository.findById(assignRole.getRoleId());
        if (rolesEntityOptional.isEmpty()) {
            throw new RoleNotFoundException("Role with id " + assignRole.getRoleId() + " not found");
        }

        userEntity.setRole(rolesEntityOptional.get());
        userRepository.save(userEntity);
    }
}
