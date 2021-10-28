package com.kafka.reactive.kafka.reactive.adapter.messaging;

import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

@Component
public class Receiver extends AbstractKafkaReceiver<UUID, String> {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Value("${kafka.topic}")
    private String topic;

    private Flux<ReceiverRecord<UUID, String>> inboundFlux;

    @PostConstruct
    public void createFlux() {
        this.inboundFlux = this.createInboundFlux();
    }

    @Override
    protected void startConsumption() {
        logger.info("Start consumption from topic {}", topic);
        this.inboundFlux
            .subscribe(record -> {
                logger.info("Received message: {}", record);
                record.receiverOffset().acknowledge();
            });
    }

    @Override
    protected String getTopic() {
        return topic;
    }

    @Override
    protected Class getKeyDeserializer() {
        return UUIDDeserializer.class;
    }

    @Override
    protected Class getValueDeserializer() {
        return StringDeserializer.class;
    }
}
