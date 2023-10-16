package com.robsonkades.springcamelexample;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class SpringCamelExampleApplication implements CommandLineRunner {

    @Autowired
    private ProducerTemplate producerTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringCamelExampleApplication.class, args);
    }

    @Override
    public void run(String... args) {

        DFeEvent dFeEvent = new DFeEvent();
        dFeEvent.setLink("https://localhost:8080/file.xml");
        dFeEvent.setNsu(UUID.randomUUID().toString());
        dFeEvent.setCurrentDate(LocalDateTime.now());

        Message<DFeEvent> message = new Message<>();
        message.setEventType("DFE");
        message.setData(dFeEvent);

        producerTemplate.sendBody("direct:event:save", message);
    }
}
