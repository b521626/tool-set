package com.alex.consumer;

import com.alex.utils.PropertiesValue;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = "confirm_queue")
    public void receiver(Message message, Channel channel){
        String msgId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
        final String consumerQueue = message.getMessageProperties().getConsumerQueue();
        final String receivedExchange = message.getMessageProperties().getReceivedExchange();
        final String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        String msg = new String(message.getBody());
        log.info("当前时间：{}，队列{}收到消息：{}", new Date().toString(), PropertiesValue.confirmQueue, msg);
    }
}
