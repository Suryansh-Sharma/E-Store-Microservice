package com.suryansh.config;

import com.suryansh.model.InventoryModel;
import com.suryansh.model.RatingAndReviewModel;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerService {
    final String KAFKA_SERVER_URL = "localhost:9092";
    @Bean
    public ProducerFactory<String, InventoryModel>addNewItemInventoryProducer(){
        Map<String,Object>config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,KAFKA_SERVER_URL);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String,InventoryModel> kafkaInventoryAddTemplate(){
        return new KafkaTemplate<>(addNewItemInventoryProducer());
    }

    @Bean
    public ProducerFactory<String, RatingAndReviewModel>addRatingProducer(){
        Map<String,Object>config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,KAFKA_SERVER_URL);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String,RatingAndReviewModel> kafkaAddRatingTemplate(){
        return new KafkaTemplate<>(addRatingProducer());
    }
}
