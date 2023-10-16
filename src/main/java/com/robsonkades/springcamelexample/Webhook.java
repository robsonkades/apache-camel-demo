package com.robsonkades.springcamelexample;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Webhook {

    private String url;
    private boolean apim;
    private String event;
    private Map<String, String> headers;
}
