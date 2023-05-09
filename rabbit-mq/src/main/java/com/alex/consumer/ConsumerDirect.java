package com.alex.consumer;

import com.alex.production.ProductionDirect;
import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ConsumerDirect {

    public static void main(String[] args) throws Exception {
        direct("direct_consumer1");
        direct("direct_consumer2");
    }

    public static void direct(String routingKey) throws Exception {

        final Channel channel = Utils.getChannel();

        assert channel != null : "消费者获取通道失败";

        /**
         * 声明交换机，DIRECT类型,路由模式
         */
        channel.exchangeDeclare(ProductionDirect.EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT);

        /**
         * 声明一个随机队列
         */
        final String queue = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机和队列queue
         */
        channel.queueBind(queue, ProductionDirect.EXCHANGE_DIRECT, routingKey);

        /**
         * 声明消息处理回调
         */
        DeliverCallback deliverCallback = (consumerTag,deliverMsg)->{
            System.out.println(routingKey + "消费者等待消息：");
            String msg = new String(deliverMsg.getBody());
            System.out.println(routingKey + "消费者接收到消息：" + msg);
        };

        /**
         * 声明关闭消费回调
         */
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消费者取消了消费消息：" + consumerTag);
        };

        /**
         * 接收消息
         */
        channel.basicConsume(queue, deliverCallback, cancelCallback);
    }
}
