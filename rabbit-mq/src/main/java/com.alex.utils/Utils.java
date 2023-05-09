package com.alex.utils;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Utils {

    public static final String QUEUE_NAME = "refuel_D";

    public static final String EXCHANGE = "exchange_refuel_122";

    public static final String ROUTING_KEY = "routingKey";


    /**
     * 接收消息回调处理
     */
    public static DeliverCallback deliverCallback = (consumerTag, message) -> {
        String msg = new String(message.getBody());
        System.out.printf("消费者接收到消息Tag:%s,DeliveryTag:%s,msg:%s%n",
                consumerTag, message.getEnvelope().getDeliveryTag(), msg);
    };

    /**
     * 关闭消费回调
     */
    public static CancelCallback cancelCallback = (consumerTag) -> {
        System.out.println("消费者取消接收消息：" + consumerTag);
    };

    public static String receive(boolean autoAck, String queue, int prefetchCount, boolean slow) {
        final Channel channel = getChannel();

        assert channel != null : "获取消息通道失败";

        /**
         *
         * 不公平分发（能者多劳）:依赖autoAck=false
         * 接收消息获取channel时设置
         * prefetchCount = 0:轮询分发
         * prefetchCount = 1:不公平分发
         * prefetchCount > 1:预取值
         */
        try {
            channel.basicQos(prefetchCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到消息：");
            String msg = new String(message.getBody());
            System.out.println("消息标识：" + message.getEnvelope().getDeliveryTag());
            System.out.println("消息内容：" + msg);

            if (slow) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if ("123".equals(msg) && !autoAck) {
                /**
                 * 1、消息标记(唯一)
                 * 2、是否批量应答
                 * 3、是否重新回到队列
                 */
                channel.basicNack(message.getEnvelope().getDeliveryTag(), false, true);
                System.out.println("noAck and return");
                return;
            }

            if (!autoAck) {
                /**
                 * 1、消息标记
                 * 2、批量应当
                 */
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                System.out.println("ack is done");
            }
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者关闭了");
        };
        try {
            channel.basicConsume(queue, autoAck, deliverCallback, cancelCallback);
            return "SUCCESS";
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public static String sender(String jsonString, String queue) {
        final Channel channel = Utils.getChannel();

        assert channel != null : "获取消息通道失败";

        /**
         * 1、交换机名称
         * 2、路由的key值，队列名称
         * 3、其他参数:
         *          消息持久化：MessageProperties.PERSISTENT_TEXT_PLAIN(发送消息获取channel时设置)
         * 4、发送的消息体
         */
        try {
            //channel.basicPublish(EXCHANGE, queue, null, jsonString.getBytes(StandardCharsets.UTF_8));
            //消息持久化
            channel.basicPublish(EXCHANGE, queue, MessageProperties.PERSISTENT_TEXT_PLAIN, jsonString.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送成功:");
            System.out.println(jsonString);
            return "SUCCESS";
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public static Channel getChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("121.5.149.129");
//        factory.setPort(15672);
        factory.setUsername("admin");
        factory.setPassword("wo2admin");
//        factory.setVirtualHost("/");

        try {

            final Connection connection = factory.newConnection();

            final Channel channel = connection.createChannel();

            /**
             *  1、队列名称
             *  2、队列是否持久化
             *  3、消息是否共享
             *  4、消息是否自动删除
             *  5、其他参数
             */
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            /**
             * 开启发布确认
             * 默认时关闭的
             */
            channel.confirmSelect();

            return channel;
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
