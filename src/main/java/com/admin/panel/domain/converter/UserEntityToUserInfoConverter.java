package com.admin.panel.domain.converter;

import org.springframework.stereotype.Component;
import com.admin.panel.domain.entity.RolesEntity;
import com.admin.panel.domain.entity.UserEntity;
import com.admin.panel.domain.model.Role;
import com.admin.panel.domain.model.UserInfo;
import org.modelmapper.spi.MappingContext;
import com.admin.panel.domain.repository.RolesRepository;
import com.admin.panel.exceptions.RoleNotFoundException;


@Component
public class UserEntityToUserInfoConverter implements AdminPanelModelMapperConverter<UserEntity, UserInfo> {

    private final RolesRepository rolesRepository;

    public UserEntityToUserInfoConverter(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public UserInfo convert(MappingContext<UserEntity, UserInfo> context) {
        UserEntity source = context.getSource();
        UserInfo destination = new UserInfo();
        Role role = new Role();

        destination.setId(source.getId());

        if (source.getRole() != null) {
            RolesEntity rolesEntity = rolesRepository.findById(source.getRole().getId()).orElseThrow(() -> new RoleNotFoundException("ID'si " + source.getRole().getId() + " olan kurum bulunamadı"));
            role.setId(source.getRole().getId());
            role.setName(rolesEntity.getName());
            destination.setRole(role);
        }

        // Map fields from UserEntity to UserInfo
        destination.setUsername(source.getUsername());
        destination.setEmail(source.getEmail());
        destination.setAdmin(source.isAdmin());


        // Add any additional fields as needed
        // e.g., destination.setRole(source.getRole());

        return destination;
    }
}