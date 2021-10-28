package com.kafka.reactive.kafka.reactive.adapter.messaging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

public abstract class AbstractKafkaReceiver<K, T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKafkaReceiver.class);

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${spring.application.name}")
    private String applicationName;

    protected abstract String getTopic();

    protected abstract Class getKeyDeserializer();
    protected abstract Class getValueDeserializer();

    @EventListener(ApplicationStartedEvent.class)
    protected abstract void startConsumption();

    protected Flux<ReceiverRecord<K, T>> createInboundFlux() {
        logger.info("Creating kafka receiver");
        final Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, getConsumerGroup());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, getKeyDeserializer());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, getValueDeserializer());

        final ReceiverOptions<K, T> receiverOptions = ReceiverOptions.<K, T>create(consumerProps)
            .subscription(Collections.singleton(getTopic()));

        return KafkaReceiver.create(receiverOptions)
            .receive();
    }

    private String getConsumerGroup() {
        return getTopic() + "." + applicationName;
    }

}
