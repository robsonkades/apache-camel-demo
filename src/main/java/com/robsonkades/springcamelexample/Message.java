package com.robsonkades.springcamelexample;

import lombok.Data;

@Data
public class Message<T> {
    private String eventType;
    private T data;
}
