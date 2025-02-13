package com.admin.panel.configuration.beans;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.admin.panel.domain.converter.AdminPanelModelMapperConverter;

import java.util.Set;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper(Set<AdminPanelModelMapperConverter<?, ?>> AdminPanelModelMapperConverters) {
        ModelMapper modelMapper = new ModelMapper();
        AdminPanelModelMapperConverters.forEach(modelMapper::addConverter);
        return modelMapper;
    }
}