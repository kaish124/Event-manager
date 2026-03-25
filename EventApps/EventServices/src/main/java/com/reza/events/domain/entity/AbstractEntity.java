package com.reza.events.domain.entity;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    public static final String ENTITY_SEQUENCE_GEN_NAME = "ENTITY_SEQUENCE";

}


