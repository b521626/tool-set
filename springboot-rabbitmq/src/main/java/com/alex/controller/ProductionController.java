package com.alex.controller;

import com.alex.utils.PropertiesValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("sender")
@Slf4j
public class ProductionController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用延时队列实现固定时间的延迟队列
     * @param msg
     */
    @RequestMapping(value = "ttlQueue/{msg}", method = RequestMethod.GET)
    public void sendMsg(@PathVariable String msg){
        log.info("当前时间：{}，发送消息内容：{}", new Date().toString(), msg);
        rabbitTemplate.convertAndSend(PropertiesValue.normalExchange, PropertiesValue.tsqen, msg);
        rabbitTemplate.convertAndSend(PropertiesValue.normalExchange, PropertiesValue.tlqen, msg);
    }

    /**
     * 使用带TTL的消息实现延迟队列
     */
    @GetMapping("ttlMsg/{msg}/{ttlTime}")
    public void sendTTLMsg(@PathVariable String msg, @PathVariable String ttlTime){
        log.info("当前时间：{}，消息的过期时长：{}毫秒，发送消息内容：{}", new Date().toString(), ttlTime, msg);
        rabbitTemplate.convertAndSend(PropertiesValue.normalExchange,PropertiesValue.ntqen,msg,message -> {
            message.getMessageProperties().setExpiration(ttlTime);
            return message;
        });
        /**
         * 当前时间：Fri Sep 10 00:37:31 GMT+08:00 2021，消息的过期时长：20000毫秒，发送消息内容：msg2
         * 当前时间：Fri Sep 10 00:37:33 GMT+08:00 2021，消息的过期时长：10000毫秒，发送消息内容：msg3
         * 当前时间：Fri Sep 10 00:37:51 GMT+08:00 2021，接收到消息：msg2
         * 当前时间：Fri Sep 10 00:37:51 GMT+08:00 2021，接收到消息：msg3
         *
         * 缺点：消费者接收到的消息顺序与生产者发送到延迟队列的顺序一致，可能导致先到死信队列的消息不能及时被消费
         * 而是要等比它早进延迟队列但后到死信队列的消息被消费后才能被消费
         * 消息可能并不会按时”死亡“，因为RabbitMQ只会检查第一个消息是否过期，
         * 如果过期则丢到死信队列，如果第一个消息的延时时长很长，而第二个消息的延时时长很短，第二个消息并不会优先得到执行。
         */
    }

    /**
     * 使用延时队列
     * 当前时间：Fri Sep 10 14:28:47 CST 2021，消息的过期时长：20000毫秒，发送延迟队列的发送消息内容：msg2
     * 当前时间：Fri Sep 10 14:28:49 CST 2021，消息的过期时长：10000毫秒，发送延迟队列的发送消息内容：msg3
     * 当前时间：Fri Sep 10 14:28:59 CST 2021，delay_queue接收到消息：msg3
     * 当前时间：Fri Sep 10 14:29:07 CST 2021，delay_queue接收到消息：msg2
     *
     * 延时队列，消息到达交换机后，交换机不会立即将消息发送给对应的queue队列，而是到达延时时间后才将消息发给queue队列
     *
     * @return
     */
    @GetMapping("delayQueue/{msg}/{ttlTime}")
    public void delayQueue(@PathVariable String msg, @PathVariable Integer ttlTime) {
        log.info("当前时间：{}，消息的过期时长：{}毫秒，发送延迟队列的发送消息内容：{}", new Date().toString(), ttlTime, msg);
        rabbitTemplate.convertAndSend(PropertiesValue.delayExchange,PropertiesValue.delayRoutingKey,msg,message -> {
            message.getMessageProperties().setDelay(ttlTime);
            return message;
        });
    }

    @RequestMapping(value = "ok", method = RequestMethod.GET)
    public String hello() {
        return "ok";
    }
}

