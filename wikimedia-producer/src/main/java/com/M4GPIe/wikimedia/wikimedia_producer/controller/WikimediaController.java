package com.M4GPIe.wikimedia.wikimedia_producer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.M4GPIe.wikimedia.wikimedia_producer.stream.WikimediaStreamConsumer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/wikimedia")
@RequiredArgsConstructor
public class WikimediaController {

    private final WikimediaStreamConsumer streamConsumer;

    @GetMapping()
    public void startPublishingToKafka() {
        streamConsumer.consumeStreamAndPublish();
    }

}
