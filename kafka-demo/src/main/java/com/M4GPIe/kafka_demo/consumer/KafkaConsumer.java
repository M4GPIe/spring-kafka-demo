package com.M4GPIe.kafka_demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.M4GPIe.kafka_demo.models.Student;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "newTopic", groupId = "myGroup")
    public void consumeMsg(Student msg) {
        log.info("Consuming the message from newTopic:: {}", msg);
    }
}
