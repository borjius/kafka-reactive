package com.kafka.reactive.kafka.reactive;

import com.kafka.reactive.kafka.reactive.adapter.messaging.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final Sender sender;

    @Autowired
    public MessageService(Sender sender) {
        this.sender = sender;
    }

    public void includeMessage(String name) {
        sender.sendToKafka(name);
    }

}
