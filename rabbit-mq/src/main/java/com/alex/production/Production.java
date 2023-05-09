package com.alex.production;

import com.alex.utils.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Production {

    public static final int COUNT = 1000;

    /**
     * 单独发布确认：
     *      同步等待确认，简单，但吞吐量非常有限
     * 批量发布确认：
     *      批量同步等待确认，简单，合理的吞吐量，但出现问题很难确认出现问题的消息
     * 异步发布确认：
     *      最佳性能和资源使用，出现错误的情况下也可以很好的控制，实现复杂
     */

    public static void main(String[] args) throws Exception {
//        productionTest();

        publishConfirmSingleton();//单个发布确认耗时：5530

        publishConfirmBatch();//批量发布确认耗时：81

        publishConfirmAsync();//异步发布确认耗时：10

    }

    private static void productionTest() {
        System.out.println(Utils.sender("hello word1", Utils.QUEUE_NAME));
        System.out.println(Utils.sender("hello word2", Utils.QUEUE_NAME));
        System.out.println(Utils.sender("hello word3", Utils.QUEUE_NAME));
        System.out.println(Utils.sender("hello word4", Utils.QUEUE_NAME));
        System.out.println(Utils.sender("hello word5", Utils.QUEUE_NAME));

        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String next = scanner.next();
            System.out.println(Utils.sender(next, Utils.QUEUE_NAME));
            try {
                int max = Integer.parseInt(next);
                for (int i = 0; i < max; i++) {
                    System.out.println(Utils.sender(String.valueOf(i), Utils.QUEUE_NAME));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发布确认：
     * 1、队列必须持久化
     * 2、队列中的消息必须持久化
     * 3、发布确认:生产者发送消息后，并且收到消息保存磁盘完成的确认.
     * 1）、单个确认发布（同步，发一条确认一条，之后才发下一条），发布速度特别慢
     * 2）、批量确认发布
     * 3）、异步确认发布
     */

    //单个确认发布
    public static void publishConfirmSingleton() throws IOException, InterruptedException {
        final Channel channel = Utils.getChannel();
        assert channel != null : "获取通道失败";
        //开启发布确认
        channel.confirmSelect();

        final long l = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            String msg = i + "";
            channel.basicPublish("", "confirm", null, msg.getBytes(StandardCharsets.UTF_8));
            //单个确认发布
            final boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println(msg + "-消息发送成功");
            }
        }
        final long l1 = System.currentTimeMillis();
        System.out.println("单个发布确认耗时：" + (l1 - l));
    }

    //批量确认发布
    public static void publishConfirmBatch() throws Exception {
        final Channel channel = Utils.getChannel();
        assert channel != null : "获取通道失败";
        //开启发布确认
        channel.confirmSelect();

        //批量确认消息大小
        int batchSize = 100;

        final long l = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            String msg = i + "";
            channel.basicPublish("", "confirm", null, msg.getBytes(StandardCharsets.UTF_8));
            if (i % batchSize == 0) {
                //批量确认发布
                final boolean b = channel.waitForConfirms();
                if (b) {
                    System.out.println("前" + i + "消息发送成功");
                }
            }
        }
        //批量确认发布
        final boolean b = channel.waitForConfirms();
        if (b) {
            System.out.println("消息发送成功");
        }
        final long l1 = System.currentTimeMillis();
        System.out.println("批量发布确认耗时：" + (l1 - l));
    }

    //异步确认发布
    public static void publishConfirmAsync() throws Exception {
        final Channel channel = Utils.getChannel();
        assert channel != null : "获取通道失败";
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的Hash表，适用于高并发的情况
         * 1、将序号与消息进行关联
         * 2、批量删除记录
         * 3、支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outStandingConfirms = new ConcurrentSkipListMap<>();

        //准备监听器，监听消息发送状态
        ConfirmCallback ackCallBack = (long deliveryTag, boolean multiple) -> {
            if (multiple) {
                //2、删除已经确认的消息
                final ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outStandingConfirms.remove(deliveryTag);
            }
            System.out.println("消息发送确认成功：" + deliveryTag);
        };
        /**
         * 如何处理异步未确认消息
         * 将未确认的消息放到一个基于内存的能被发布线程访问的队列。
         * 如：ConcurrentLinkedQueue 这个队列在confirm callbacks与发布线程之间进行消息传递
         */
        ConfirmCallback nackCallBack = (deliveryTag, multiple) -> {
            //3、剩下的未确认的消息
            final String msg = outStandingConfirms.get(deliveryTag);
            System.out.println("消息发送确认失败:" + msg);
        };
        channel.addConfirmListener(ackCallBack, nackCallBack);

        final long l = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            String msg = i + "";
            channel.basicPublish("", "confirm", null, msg.getBytes(StandardCharsets.UTF_8));
            //1、记录所有要发送的消息
            outStandingConfirms.put(channel.getNextPublishSeqNo(), msg);
        }
        final long l1 = System.currentTimeMillis();
        System.out.println("异步发布确认耗时：" + (l1 - l));
    }


}
