package com.M4GPIe.kafka_demo.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.M4GPIe.kafka_demo.models.Student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Student> kafkaTemplate;

    public void sendMessage(Student data) {

        Message<Student> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, "newTopic")
                .build();

        kafkaTemplate.send(message);
    }

}
