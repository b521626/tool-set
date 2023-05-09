package com.alex.consumer;

import com.alex.production.ProductionTopic;
import com.alex.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class ConsumerTopic {

    public static void main(String[] args) throws Exception {
        topic("*.topic.*");
        topic("#.topic.*");
        topic("*.topic.#");
        topic("topic.#");
        topic("#.topic");
        topic("#.topic.#");
    }

    public static void topic(String routingKey) throws Exception {

        final Channel channel = Utils.getChannel();

        assert channel != null : "获取channel失败";

        /**
         * 声明交换机，TOPIC类型
         */
        channel.exchangeDeclare(ProductionTopic.EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);

        /**
         * 声明消息队列
         */
        final String queue = channel.queueDeclare().getQueue();

        /**
         * 队列和交换机绑定，通过routingKey
         * routingKey格式：
         *      多个单词组成，单词之间用点.隔开，
         *      使用*代表一个单词，
         *      使用#代表0-n个单词
         *      routingKey配置*和#可以实现模糊匹配
         */
        channel.queueBind(queue, ProductionTopic.EXCHANGE_TOPIC, routingKey);

        System.out.println(routingKey + "绑定的queue：" + queue + "等待接收消息：");
        channel.basicConsume(queue, Utils.deliverCallback, Utils.cancelCallback);
    }

}
