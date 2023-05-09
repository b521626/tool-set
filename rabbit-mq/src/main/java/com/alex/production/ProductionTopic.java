package com.alex.production;

import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ProductionTopic {

    public static final String EXCHANGE_TOPIC = "exchange_topic";

    public static void main(String[] args) throws Exception {
        System.out.println("请输入要发送的消息");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            System.out.println("请输入要发送的消息");
            final String next = scanner.next();
            topic(next);
        }
    }

    public static void topic(String routingKey) throws Exception {

        final Channel channel = Utils.getChannel();

        assert channel != null : "获取信道失败";

        /**
         * 声明交换机，topic类型
         */
        channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);

        channel.basicPublish(EXCHANGE_TOPIC, routingKey, null, routingKey.getBytes(StandardCharsets.UTF_8));
    }
}
