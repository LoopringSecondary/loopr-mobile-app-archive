//package com.tomcat360.lyqb.core.model.loopr.response;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//
//import java.io.IOException;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@JsonDeserialize(using = NonceResult.NonceDeserializer.class)
//public class NonceResult {
//    private long nonce;
//
//    public static class NonceDeserializer extends JsonDeserializer<NonceResult> {
//        @Override
//        public NonceResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//            JsonNode node = p.getCodec().readTree(p);
//            long nonce = node.get("result").numberValue().longValue();
//            return new NonceResult(nonce);
//        }
//    }
//}
//
