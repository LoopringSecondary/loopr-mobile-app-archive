package com.tomcat360.lyqb.core.singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperInstance {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static ObjectMapper getMapper() {
        return objectMapper;
    }

    private ObjectMapperInstance() {
    }
}
