package com.robsonkades.springcamelexample;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class WebhookService {

    private final List<Webhook> webhooks = List.of(
            new Webhook("http://example.com/webhook1", false, "DFE", new HashMap<>()),
            new Webhook("http://example.com/webhook2", true, "DFE_EVENT", new HashMap<>()),
            new Webhook("http://example.com/webhook3", false, "DFE_EVENT", new HashMap<>())
    );

    private final List<Message<?>> messages = new ArrayList<>();

    public List<Webhook> getWebhookUrl(String eventType) {
        return webhooks.stream().filter(webhook -> webhook.getEvent().equals(eventType)).toList();
    }

    public void save(Message<?> message) {
        messages.add(message);
    }
}
