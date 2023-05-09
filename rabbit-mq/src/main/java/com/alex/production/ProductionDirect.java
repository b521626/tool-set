package com.alex.production;

import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *
 */
public class ProductionDirect {

    public static final String EXCHANGE_DIRECT = "exchange_direct";

    public static void main(String[] args) throws Exception {
        System.out.println("请输入要发送的消息：");
        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            System.out.println("请输入要发送的消息：");
            final String msg = scanner.next();
            dircet(msg);
        }
    }

    public static void dircet(String routingKey) throws Exception {

        final Channel channel = Utils.getChannel();

        assert channel != null : "获取channel失败";

        /**
         * 声明交换机,DIRECT类型交换机，路由模式
         * 生产者发送消息到交换机B,交换机会根据消息的routingKey，将消息路由的对应的queue
         */
        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT);

        /**
         * 发送消息
         */
        channel.basicPublish(EXCHANGE_DIRECT, routingKey, null, routingKey.getBytes(StandardCharsets.UTF_8));

    }
}
