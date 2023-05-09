package com.alex.config;

import com.alex.utils.PropertiesValue;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TTLQueueConfig {

    @Bean("normalExchange")
    public DirectExchange normalExchange() {
        return new DirectExchange(PropertiesValue.normalExchange);
    }

    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange(PropertiesValue.deadExchange);
    }

    @Bean("shortQueue")
    public Queue shortQueue() {
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", PropertiesValue.deadExchange);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", PropertiesValue.dqed);
        //设置TTL
        arguments.put("x-message-ttl", PropertiesValue.shortTTL);
        return QueueBuilder.durable(PropertiesValue.shortQueue).withArguments(arguments).build();
    }

    @Bean("longQueue")
    public Queue longQueue() {
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", PropertiesValue.deadExchange);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", PropertiesValue.dqed);
        //设置TTL
        arguments.put("x-message-ttl", PropertiesValue.longTTL);

        return QueueBuilder.durable(PropertiesValue.longQueue).withArguments(arguments).build();
    }

    @Bean("liveQueue")
    public Queue liveQueue() {
        Map<String, Object> arguments = new HashMap<>(2);
        //设置死信队列
        arguments.put("x-dead-letter-exchange", PropertiesValue.deadExchange);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", PropertiesValue.dqed);
        return QueueBuilder.durable(PropertiesValue.liveQueue).withArguments(arguments).build();
    }

    @Bean("deadQueue")
    public Queue deadQueue() {
        return QueueBuilder.durable(PropertiesValue.deadQueue).build();
    }

    @Bean
    public Binding deadQueueBindingDeadExchange(@Qualifier("deadQueue") Queue deadQueue,
                                                @Qualifier("deadExchange") DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(PropertiesValue.dqed);
    }

    @Bean
    public Binding shortQueueBindingNormalExchange(@Qualifier("shortQueue") Queue shortQueue,
                                                   @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(shortQueue).to(normalExchange).with(PropertiesValue.tsqen);
    }

    @Bean
    public Binding longQueueBingingNormalExchange(@Qualifier("longQueue") Queue longQueue,
                                                  @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(longQueue).to(normalExchange).with(PropertiesValue.tlqen);
    }

    @Bean
    public Binding liveQueueBindingNormalExchange(@Qualifier("liveQueue") Queue liveQueue,
                                                  @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(liveQueue).to(normalExchange).with(PropertiesValue.ntqen);
    }


}
