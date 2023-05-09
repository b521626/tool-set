package com.alex.production;

import com.alex.utils.Utils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 死信
 * 由于某些原因无法被消费的消息，，这样的消息如果没有后续处理，就变成了死信
 * 死信的来源：
 * 消息过期TTL
 * 队列达到最大长度（队列满了，无法再添加消息到mq中）
 * 消息被拒绝（basic.reject或basic.nack）并且requeue=false(不返回到队列中)
 * <p>
 * 为了解决死信的问题出现了死信队列
 * 死信队列：存放死信的队列，防止消息丢失
 *
 * 应用：消息过期可以做延时队列
 *      延时队列：需要在某个事件发生之后或之前的指定时间完成某一项任务
 *          应用场景：
 *              1、订单在十分钟内未付款则自动取消订单
 *              2、用户申请退款，如果一天内没有得到处理，则通知相关人员处理
 *              3、预定会议后，需要在预定会议开始前十分钟通知各个与会人员参加会议
 */
public class ProductionDeadMsg {

    public static final String EXCHANGE_DEAD = "exchange_dead";

    public static final String EXCHANGE = "exchange_TTL";

    public static final String DEAD_QUEUE = "dead_queue";

    public static final String NORMAL_QUEUE = "normal_queue";

    public static final String ROUTING_KEY = "routing_key";
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要发送的消息：");
        while (scanner.hasNext()) {
            System.out.println("请输入要发送的消息：");
            String msg = scanner.next();
            deadMsg(msg);
        }
    }

    /**
     * 案例模拟死信及死信队列
     * 生产者发送消息时，设置消息过期时间，消息对应的消费者客户端通过拒绝消息或者不启动让消息过期，来产生死信，
     * 将死信发送的死信队列
     *
     * @throws Exception
     */
    public static void deadMsg(String msg) throws Exception {

        final Channel channel = Utils.getChannel();

        assert channel != null : "";

        //声明交换机
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

        /**
         *  设置消息过期时间,来模拟
         */
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        channel.basicPublish(EXCHANGE, ROUTING_KEY, properties, msg.getBytes(StandardCharsets.UTF_8));
    }
}
