package com.drinkeg.drinkeg.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class StringIntegerMapConverter implements AttributeConverter<Map<String, Integer>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Integer> stringIntegerMap) {
        if (stringIntegerMap == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(stringIntegerMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert map to JSON string.", e);
        }
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<String, Integer>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not convert JSON string to map.", e);
        }
    }
}
