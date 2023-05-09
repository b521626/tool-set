package com.alex.config;

import com.alex.utils.PropertiesValue;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayQueueConfig {

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> arguments = new HashMap<>();
        /**
         * 正常我们申明一个Exchange只需要指定其类型（direct，fanout，topic等）即可，
         * 而声明延迟Exchange需要指定type为x-delayed-message，
         * 并通过参数x-delay-type指定其Exchange的类型（direct，fanout，topic等）
         */
        arguments.put("x-delayed-type", "direct");

        /**
         * 1、交换机名称
         * 2、交换机类型
         * 3、持久化
         * 4、自动删除
         * 5、其他参数
         */
        return new CustomExchange(PropertiesValue.delayExchange, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(PropertiesValue.delayQueue).build();
    }

    @Bean
    public Binding delayQueueBindingExchange(@Qualifier("delayQueue") Queue delayQueue,
                                             @Qualifier("delayExchange") CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(PropertiesValue.delayRoutingKey).noargs();
    }

}
