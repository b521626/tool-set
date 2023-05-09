package com.alex.controller;

import com.alex.utils.PropertiesValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("confirm")
@Slf4j
public class ConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 模拟交换机成功回调
     * @param msg
     */
    @GetMapping("sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("当前时间：{}，向队列{}，发送消息内容：{}", new Date().toString(), PropertiesValue.confirmQueue, msg);

        CorrelationData correlationData = new CorrelationData("wo shi id");
        Message message = new Message(msg.getBytes(StandardCharsets.UTF_8));
        ReturnedMessage returnedMessage = new ReturnedMessage(message, 310, "wo shi replyText", PropertiesValue.confirmExchange + "01", PropertiesValue.confirmRoutingKey);
        correlationData.setReturned(returnedMessage);

        rabbitTemplate.convertAndSend(PropertiesValue.confirmExchange, PropertiesValue.confirmRoutingKey, msg, correlationData);
    }

    /**
     * 模拟交换机失败回调
     * 通过交换机名称不存在来模拟
     * @param msg
     */
    @GetMapping("send/{msg}")
    public void send(@PathVariable String msg) {
        log.info("当前时间：{}，向队列{}，发送消息内容：{}", new Date().toString(), PropertiesValue.confirmQueue, msg);

        CorrelationData correlationData = new CorrelationData("2");
        Message message = new Message(msg.getBytes(StandardCharsets.UTF_8));
        ReturnedMessage returnedMessage = new ReturnedMessage(message, 310, "wo shi replyText", PropertiesValue.confirmExchange + "01", PropertiesValue.confirmRoutingKey);
        correlationData.setReturned(returnedMessage);
        correlationData.setId("wo shi id");

        rabbitTemplate.convertAndSend(PropertiesValue.confirmExchange+"01", PropertiesValue.confirmRoutingKey, msg, correlationData);
    }

    /**
     * 模拟队列失败回调
     * 通过修改routingKey不存在来模拟
     *
     * 当前时间：Fri Sep 10 17:08:45 CST 2021，向队列confirm_queue，发送消息内容：确认发布高级成功
     * 消息id为：1的消息被交换机接收成功了！
     * 当前时间：Fri Sep 10 17:08:45 CST 2021，队列confirm_queue收到消息：确认发布高级成功
     * 当前时间：Fri Sep 10 17:08:47 CST 2021，向队列confirm_queue，发送消息内容：确认发布高级交换机失败
     * Shutdown Signal: channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'confirm_exchange01' in vhost '/', class-id=60, method-id=40)
     * 消息id为：2的消息被交换机接收失败了！失败原因：channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'confirm_exchange01' in vhost '/', class-id=60, method-id=40)
     * 当前时间：Fri Sep 10 17:08:49 CST 2021，向队列confirm_queue，发送消息内容：确认发布高级routingKey失败
     * 消息id为：3的消息被交换机接收成功了！
     *
     * 消息id为3 的消息虽然收到回调了，但是队列没有收到消息。
     *
     * 上面的模拟是仅开启了生产者确认机制的情况下，交换机接收到消息后，会直接给生产者发送确认消息，
     * 如果发现该消息不可路由，那么消息会被直接丢弃，此时生产者是不知道消息被丢弃的。
     *
     * 要解决这个问题，设置mandatory参数，需要开启回退消息
     *
     * @param msg
     */
    @GetMapping("callback/{msg}")
    public void callback(@PathVariable String msg) {
        log.info("当前时间：{}，向队列{}，发送消息内容：{}", new Date().toString(), PropertiesValue.confirmQueue, msg);

        CorrelationData correlationData = new CorrelationData("3");

        rabbitTemplate.convertAndSend(PropertiesValue.confirmExchange, PropertiesValue.confirmRoutingKey+"01", msg, correlationData);
    }
}
