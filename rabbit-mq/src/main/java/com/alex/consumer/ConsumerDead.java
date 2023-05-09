package com.alex.consumer;

import com.alex.production.ProductionDeadMsg;
import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class ConsumerDead {

    public static void main(String[] args) throws Exception {
        dead();
    }

    public static void dead() throws Exception {

        final Channel channel = Utils.getChannel();

        assert channel != null;

        //声明普通队列交换机
        channel.exchangeDeclare(ProductionDeadMsg.EXCHANGE, BuiltinExchangeType.TOPIC);
        //声明死信队列交换机
        channel.exchangeDeclare(ProductionDeadMsg.EXCHANGE_DEAD, BuiltinExchangeType.TOPIC);

        //声明普通队列参数用于模拟产生死信
        Map<String, Object> argumentMap = new HashMap<>();
        //过期时间
        argumentMap.put("x-message-ttl", 10000);
        //正常队列设置死信交换机
        argumentMap.put("x-dead-letter-exchange", ProductionDeadMsg.EXCHANGE_DEAD);
        //设置死信routingKey
        argumentMap.put("x-dead-letter-routing-key", ProductionDeadMsg.DEAD_ROUTING_KEY);
        //设置正常队列的长度限制
        argumentMap.put("x-max-length", 6);

        //声明普通消息队列
        channel.queueDeclare(ProductionDeadMsg.NORMAL_QUEUE, false, false, false, argumentMap);
        //声明死信消息队列
        channel.queueDeclare(ProductionDeadMsg.DEAD_QUEUE, false, false, false, null);

        //普通消息队列通过routingKey绑定交换机
        channel.queueBind(ProductionDeadMsg.NORMAL_QUEUE, ProductionDeadMsg.EXCHANGE, ProductionDeadMsg.ROUTING_KEY);
        //死信队列绑定交换机
        channel.queueBind(ProductionDeadMsg.DEAD_QUEUE, ProductionDeadMsg.EXCHANGE_DEAD, ProductionDeadMsg.DEAD_ROUTING_KEY);

        System.out.println("普通队列开始接收消息：");

        //消费者处理消息回调模拟拒绝消息
        DeliverCallback deliverCallback = (consumerTag,deliverMsg)->{
            String msg = new String(deliverMsg.getBody());
            if ("rejectMsg".equals(msg)) {
                System.out.println("queue" + ProductionDeadMsg.NORMAL_QUEUE + "拒绝接收消息：" + msg);
                //拒绝消息且不返回队列
                channel.basicReject(deliverMsg.getEnvelope().getDeliveryTag(), false);
            }else {
                System.out.println("queue" + ProductionDeadMsg.NORMAL_QUEUE + "接收到消息：" + msg);
                channel.basicAck(deliverMsg.getEnvelope().getDeliveryTag(), false);
            }
        };

        //接收消息，关闭自动ack用于模拟拒绝消息
        channel.basicConsume(ProductionDeadMsg.NORMAL_QUEUE, false, deliverCallback, Utils.cancelCallback);
    }
}
