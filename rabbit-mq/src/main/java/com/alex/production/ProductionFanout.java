package com.alex.production;

import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 交换机
 * 发布订阅模式
 * 一个交换机对应多个队列
 * 消息发送到交换机A,所有与交换机A绑定的消费者queue都可以接收到生产者发送的消息，与routingKey无关
 */
public class ProductionFanout {

    public static void main(String[] args) throws Exception{
        exchangeProduction();
    }

    public static void exchangeProduction() throws Exception{
        Channel channel = Utils.getChannel();

        assert channel != null : "获取信道失败";

        /**
         * 创建交换机,fanout模式，发布订阅模式，
         */
        channel.exchangeDeclare(Utils.EXCHANGE, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish(Utils.EXCHANGE, Utils.ROUTING_KEY, null, msg.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(Utils.EXCHANGE, "", null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息：" + msg);
        }

    }

}
