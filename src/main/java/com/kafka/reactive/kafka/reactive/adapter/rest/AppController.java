package com.kafka.reactive.kafka.reactive.adapter.rest;

import com.kafka.reactive.kafka.reactive.MessageService;
import com.kafka.reactive.kafka.reactive.adapter.rest.dto.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    private final MessageService messageService;

    @Autowired
    public AppController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/events")
    public void createEvent(@RequestBody Payload payload) {
        logger.info("received payload {}", payload.toString());
        messageService.includeMessage(payload.getName());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void adminEndpoint() {
        logger.info("Yeah, you are an admin!");
    }

}
