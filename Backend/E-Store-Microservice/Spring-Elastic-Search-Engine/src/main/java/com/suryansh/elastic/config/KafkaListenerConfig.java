package com.suryansh.elastic.config;

import com.suryansh.elastic.dto.ProductDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaListenerConfig {
    final String KAFKA_SERVER_URL="localhost:9200";
    final String GROUP_ID="Elastic-Service";

    @Bean
    public ConsumerFactory<String, ProductDto>addNewProductListenerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,KAFKA_SERVER_URL );
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(ProductDto.class));
    }
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer
                <String, ProductDto>> orderInventKafkaListener() {
        ConcurrentKafkaListenerContainerFactory<String, ProductDto>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(addNewProductListenerFactory());
        return factory;
    }
}
