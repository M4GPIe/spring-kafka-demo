package com.M4GPIe.kafka_demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.M4GPIe.kafka_demo.models.Student;
import com.M4GPIe.kafka_demo.producer.KafkaProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final KafkaProducer kafkaProducer;

    @PostMapping()
    public ResponseEntity<String> sendMessage(@RequestBody Student data) {
        kafkaProducer.sendMessage(data);
        return ResponseEntity.ok("Message queued successfully");
    }

}
