package com.shashank.linkedin.connection_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic AcceptedConnectionRequestTopic() {
        return new NewTopic("accepted-connection-request-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic SendConnectionRequestTopic() {
        return new NewTopic("send-connection-request-topic", 3, (short) 1);
    }

}
