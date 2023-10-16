package com.robsonkades.springcamelexample;

import lombok.Data;

import java.util.Map;

@Data
public class WebhookEvent<T> {

    private String eventType;
    private String url;
    private Map<String, String> headers;
    private T body;
}
