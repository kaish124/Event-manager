package com.reza.events.config;

import com.reza.events.modelmapper.MappingConfigurer;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CommonConfig {
    private final List<MappingConfigurer> mappers;

    @Autowired
    public CommonConfig(List<MappingConfigurer> mappers) {
        this.mappers = mappers;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setDeepCopyEnabled(true)
                .setPropertyCondition(Conditions.isNotNull());
        for (MappingConfigurer mapper : mappers) {
            mapper.configure(modelMapper);
        }

        return modelMapper;
    }
}
