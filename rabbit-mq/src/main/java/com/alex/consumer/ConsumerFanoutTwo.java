package com.alex.consumer;

import com.alex.utils.Utils;

public class ConsumerFanoutTwo {
    public static void main(String[] args) throws Exception {
        ConsumerFanoutOne.consumerExchage(Utils.ROUTING_KEY+"_two");
    }
}
