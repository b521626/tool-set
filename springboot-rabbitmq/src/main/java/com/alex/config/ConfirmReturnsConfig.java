package com.alex.config;

import com.alex.utils.PropertiesValue;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmReturnsConfig {

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(PropertiesValue.backupExchange);
    }

    @Bean
    public Queue processQueue() {
        return QueueBuilder.durable(PropertiesValue.processQueue).build();
    }

    @Bean
    public Queue alarmQueue() {
        return new Queue(PropertiesValue.alarmQueue);
    }

    @Bean
    public Binding processQueueBindingBackupExchange(@Qualifier("processQueue") Queue processQueue,
                                                     @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(processQueue).to(backupExchange);
    }

    @Bean
    public Binding alarmQueueBindingBackupExchange(@Qualifier("alarmQueue") Queue alarmQueue,
                                                   @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(alarmQueue).to(backupExchange);
    }
}
