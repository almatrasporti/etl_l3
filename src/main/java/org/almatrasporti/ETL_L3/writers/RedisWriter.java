package org.almatrasporti.ETL_L3.writers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.logging.Logger;

public class RedisWriter implements IWriter {

    private final Jedis jedis;

    public RedisWriter(String host, int port) {
        this.jedis = new Jedis(host, port);
    }


    public void upsertRecord(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(value);
            this.jedis.sadd("vehicles", jsonNode.get("VinVehicle").asText());
            this.jedis.zadd("readings/" + jsonNode.get("VinVehicle").asText(), jsonNode.get("Timestamp").asDouble(), objectMapper.writeValueAsString(jsonNode.get("Position")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
