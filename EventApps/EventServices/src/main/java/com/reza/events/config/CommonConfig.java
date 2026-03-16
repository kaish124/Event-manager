package com.reza.events.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.reza.events.modelmapper.MappingConfigurer;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

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

    @Bean
    public ObjectMapper  objectMapper() {
        Jackson2ObjectMapperFactoryBean factoryBean = new Jackson2ObjectMapperFactoryBean();
        factoryBean.afterPropertiesSet();
        ObjectMapper objectmapper = factoryBean.getObject();
        if(objectmapper != null){
            return objectmapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .registerModule(new JodaModule())
                    .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                    .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        }
        throw new RuntimeException("ObjectMapper object was not created by factoryBean.");
    }
}
