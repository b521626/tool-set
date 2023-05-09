package com.alex.config;

import com.alex.utils.PropertiesValue;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {

    /**
     * 确认发布交换机
     * @return
     */
    @Bean
    public DirectExchange confirmExchange() {
        /**
         * 退回消息转发到备份交换机
         */
        return ExchangeBuilder
                .directExchange(PropertiesValue.confirmExchange)
                .durable(true)
                .withArgument("alternate-exchange", PropertiesValue.backupExchange)
                .build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(PropertiesValue.confirmQueue).build();
    }

    @Bean
    public Binding confirmQueueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                               @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(PropertiesValue.confirmRoutingKey);
    }
}
