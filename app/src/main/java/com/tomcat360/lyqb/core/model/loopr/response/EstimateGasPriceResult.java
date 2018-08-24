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
//
//@Data
//@AllArgsConstructor
//@JsonDeserialize(using = EstimateGasPriceResult.Deseriliazer.class)
//public class EstimateGasPriceResult {
//    private String gasPrice;
//
//    public static class Deseriliazer extends JsonDeserializer<EstimateGasPriceResult> {
//
//        @Override
//        public EstimateGasPriceResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//            JsonNode node =  p.getCodec().readTree(p);
//            String result = node.get("result").asText();
//            return new EstimateGasPriceResult(result);
//        }
//    }
//}
