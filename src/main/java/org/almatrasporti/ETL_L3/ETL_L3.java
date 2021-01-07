package org.almatrasporti.ETL_L3;

import org.almatrasporti.ETL_L3.consumers.ConsumeToRedis;
import org.almatrasporti.ETL_L3.writers.RedisWriter;
import org.almatrasporti.common.utils.Config;

import java.util.logging.Logger;

public class ETL_L3 {

    private ConsumeToRedis consumer;

    public ETL_L3() {
        String[] redisHostAndPort = Config.getInstance().get("Redis.server").split(":");
        String redisHost = redisHostAndPort[0];
        int redisPort = Integer.valueOf(redisHostAndPort[1]);

        consumer = new ConsumeToRedis(new RedisWriter(redisHost, redisPort));
    }

    public void execute() {
        consumer.consume();
    }

    public static void main(String args[]) {
        ETL_L3 worker = new ETL_L3();
        worker.execute();
    }
}
