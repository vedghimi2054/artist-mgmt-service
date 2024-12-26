package com.company.artistmgmt.model;

import com.company.artistmgmt.util.ImportArtistUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

public class CustomDateTimeSerializer extends JsonSerializer<Timestamp> {
    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String rawDate = ImportArtistUtils.DATE_TIME_FORMATTER.format(timestamp.toLocalDateTime());
        jsonGenerator.writeRawValue(rawDate);
    }
}
