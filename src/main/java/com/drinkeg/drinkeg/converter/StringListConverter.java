package com.drinkeg.drinkeg.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> dataList) {
        try {
            if(dataList == null) {
                return null;
            }
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        try {
            if (data == null || data.isEmpty()) {
                return List.of(); // 비어있는 List 반환
            }
            return mapper.readValue(data, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}