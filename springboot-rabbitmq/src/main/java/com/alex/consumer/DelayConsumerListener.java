package com.alex.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class DelayConsumerListener {

    @RabbitListener(queues = "delay_queue")
    public void delayListener(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间：{}，delay_queue接收到消息：{}", new Date().toString(), msg);
    }

}
