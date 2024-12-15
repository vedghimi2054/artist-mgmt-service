package com.company.artistmgmt.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <E> String serialize(E data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException var2) {
            JsonProcessingException e = var2;
            log.error("Error in serializing.", e);
            return null;
        }
    }

}
