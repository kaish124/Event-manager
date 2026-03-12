package com.reza.events.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Instant;

@Configuration
@Order(1)
public class GlobalMappingConfigurer implements MappingConfigurer{
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.typeMap(Instant.class, Instant.class)
                .setConverter(MappingContext::getSource);
    }
}
