package com.alex.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfirmProcessConsumer {

    @RabbitListener(queues = "process_queue")
    public void receiver(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("收到回退消息：{}", msg);
    }
}
