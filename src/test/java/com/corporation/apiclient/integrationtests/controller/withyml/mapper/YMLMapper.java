package com.corporation.apiclient.integrationtests.controller.withyml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YMLMapper implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    protected TypeFactory typeFactory;

    public YMLMapper() {
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        typeFactory = TypeFactory.defaultInstance();
    }

    @Override
    public Object deserialize(ObjectMapperDeserializationContext objectMapperDeserializationContext) {
        try{
            String dataToDeserialize = objectMapperDeserializationContext.getDataToDeserialize().asString();
            Class type = (Class)objectMapperDeserializationContext.getType();
            return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
        }catch (JsonMappingException e){
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext objectMapperSerializationContext) {
        try{
            return objectMapper.writeValueAsString(objectMapperSerializationContext.getObjectToSerialize());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
}
