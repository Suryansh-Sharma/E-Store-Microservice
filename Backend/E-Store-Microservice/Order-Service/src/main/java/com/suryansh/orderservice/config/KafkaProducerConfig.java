package com.suryansh.orderservice.config;

import com.suryansh.orderservice.model.OrderInventoryModel;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
    final String KAFKA_SERVER_URL = "localhost:9092";
    @Bean
    public ProducerFactory<String, OrderInventoryModel> orderDetailProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, OrderInventoryModel> kafkaInventOrderTemplate() {
        return new KafkaTemplate<>(orderDetailProducerFactory());
    }
    ProducerFactory<String, String> clearCartProducerFactory(){
        Map<String,Object>config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,KAFKA_SERVER_URL);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    KafkaTemplate<String,String>clearCartKafkaTemplate(){
        return new KafkaTemplate<>(clearCartProducerFactory());
    }
}
