package com.alex.consumer;

import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class ConsumerFanoutOne {

    public static void main(String[] args) throws Exception {
        consumerExchage(Utils.ROUTING_KEY);
    }

    public static void consumerExchage(String routingKey) throws IOException {
        Channel channel = Utils.getChannel();

        assert channel != null : "消费者获取信道失败";

        /**
         * 声明一个交换机
         */
        channel.exchangeDeclare(Utils.EXCHANGE, BuiltinExchangeType.FANOUT);

        /**
         * 生成一个随机队列，队列的名称市随机的
         * 随即队列：当消费者与队列断开连接后，随即队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        System.out.println("queueName:" + queueName);
        /**
         * 绑定交换机和队列
         */
        channel.queueBind(queueName, Utils.EXCHANGE, routingKey);
        System.out.println("等待接收消息：");

        /**
         * 接收消息
         */
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            System.out.println(String.format("消费者接收到消息Tag:%s,msg:%s", consumerTag, msg));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者取消接收消息：" + consumerTag);
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

}
