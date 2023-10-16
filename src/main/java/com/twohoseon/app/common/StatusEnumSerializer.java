package com.twohoseon.app.common;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.twohoseon.app.enums.StatusEnum;

import java.io.IOException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : StatusEnumSerializer
 * @date : 10/16/23 10:04 PM
 * @modifyed : $
 **/
public class StatusEnumSerializer extends JsonSerializer<StatusEnum> {
    @Override
    public void serialize(StatusEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getStatusCode());
    }
}
