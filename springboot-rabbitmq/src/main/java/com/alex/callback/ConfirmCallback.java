package com.alex.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1、发送的消息交换机接收成功的回调
     * 1.1、correlationData 保存回调消息的id即相关信息
     * 1.2、ack = true,表示回调成功了
     * 1.3、cause = null,失败原因
     * <p>
     * 2、发送的消息交换机接收失败的回调
     * 1.1、correlationData 保存回调消息的id即相关信息
     * 1.2、ack = false,表示回调失败了
     * 1.3、cause,失败原因
     *
     * @param correlationData correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("消息id为：{}的消息被交换机接收成功了！", id);
        } else {
            log.info("消息id为：{}的消息被交换机接收失败了！失败原因：{}", id, cause);
        }
    }

    /**
     * 消息传递过程中，不可到达目的地时回调该方法
     * 优化升级方案，将回退的消息发给备份交换机，备份交换机路由两个队列，一个队列为报警，一个为处理回退消息
     *
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        String msg = new String(returned.getMessage().getBody());
        log.error("消息：{}，被交换机{}退回，退回原因：{}，routingKey：{}",
                msg, returned.getExchange(), returned.getReplyText(), returned.getRoutingKey());
    }
}
