package com.nighthawk.spring_portfolio.mvc.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nighthawk.spring_portfolio.mvc.assignment.AssignmentSubmission;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

@Converter
public class AssignmentSubmissionConverter implements AttributeConverter<Collection<AssignmentSubmission>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Collection<AssignmentSubmission> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting object to JSON", e);
        }
    }

    @Override
    public Collection<AssignmentSubmission> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(Collection.class, AssignmentSubmission.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to object", e);
        }
    }
}