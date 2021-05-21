package me.weekbelt.directorygenerator.persistence.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.persistence.AttributeConverter;

public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode myDoc) {
        try {
            return objectMapper.writeValueAsString(myDoc);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String myDocJson) {
        try {
            if (StringUtils.isBlank(myDocJson)) {
                return objectMapper.nullNode();
            }
            String converted = JsonUtil.jsonStringToObject(myDocJson);
            return objectMapper.readValue(converted, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
