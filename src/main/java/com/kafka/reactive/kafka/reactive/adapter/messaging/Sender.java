package com.kafka.reactive.kafka.reactive.adapter.messaging;

import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    private final KafkaSender<UUID, String> kafkaSender;
    private final String topic;

    @Autowired
    public Sender(KafkaSender kafkaSender, @Value("${kafka.topic}") String topic) {
        this.kafkaSender = kafkaSender;
        this.topic = topic;
    }

    public void sendToKafka(String message) {

        final Mono<ProducerRecord<UUID, String>> producerRecord = Mono.just(
            new ProducerRecord<>(topic, UUID.randomUUID(), message));

        kafkaSender.createOutbound()
            .send(producerRecord)
            .then()
            .doOnSuccess(result -> logger.info("Message {} sent", message))
            .doOnError(error -> logger.error("Error sending message {} :: {}", message, error))
            .subscribe();

    }

}
