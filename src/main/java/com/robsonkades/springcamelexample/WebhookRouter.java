package com.robsonkades.springcamelexample;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebhookRouter extends RouteBuilder {

    @Autowired
    private WebhookService webhookService;

    @Override
    public void configure() {
        from("direct:event:save")
            .process(exchange -> {
                    Message<?> message = exchange.getIn().getBody(Message.class);
                    webhookService.save(message);
                    exchange.getIn().setBody(message);
                })
            .to("direct:event:publish");

        from("direct:event:publish")
            .process(exchange -> {
                    // Event event = exchange.getIn().getBody(Event.class);
                    // exchange.getIn().setBody(event, Event.class);
                })
            .to("direct:webhook");

        from("direct:webhook")
            .process(exchange -> {
                    Message<?> message = exchange.getIn().getBody(Message.class);
                    List<Webhook> webhooks = webhookService.getWebhookUrl(message.getEventType());
//                    List<WebhookEvent<Object>> webhooks = webhookService
//                            .getWebhookUrl(message.getEventType())
//                            .stream()
//                            .map(webhook -> {
//                                WebhookEvent<Object> webhookEvent = new WebhookEvent<>();
//                                webhookEvent.setUrl(webhook.getUrl());
//                                webhookEvent.setHeaders(webhook.getHeaders());
//                                webhookEvent.setEventType(webhook.getEvent());
//                                webhookEvent.setBody(message.getData());
//                                return webhookEvent;
//                            })
//                            .toList();
                    exchange.getMessage().setHeader("event", message.getEventType());
                    exchange.getMessage().setHeader("content", message);
                    exchange.getMessage().setBody(webhooks);
                })
            .split(body())
                .choice()
                    .when(header("event").isEqualTo("DFE"))
                        .to("direct:webhook:dfe")
                    .when(header("event").isEqualTo("MDFE"))
                        .to("direct:webhook:execute");


        from("direct:webhook:dfe")
            .process(exchange -> {
                    System.out.println("");
                })
            .to("direct:webhook:execute");


        from("direct:webhook:execute")
            .process(exchange -> {
                    Message<?> message = exchange.getIn().getBody(Message.class);
                    System.out.println("event: " + message.toString());
                    exchange.getIn().setHeader(Exchange.HTTP_URI, message);
                })
//            .choice()
//                .when(header(Exchange.HTTP_RESPONSE_CODE).isNotEqualTo(200))
//                    .to("direct:typeA")
//                .when(header("type").isEqualTo("B"))
//                    .to("direct:typeB")
//                .otherwise()
//                    .to("direct:defaultType")
//                .end()
        ;
    }

    private long calculateDelay(int retryCount) {
        long initialDelay = 5;
        double multiplier = 2.0;
        return initialDelay * (long) Math.pow(multiplier, retryCount);
    }
}
