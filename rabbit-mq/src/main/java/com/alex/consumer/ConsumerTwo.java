package com.alex.consumer;

import com.alex.utils.Utils;

public class ConsumerTwo {

    public static void main(String[] args) {
        System.out.println(Utils.receive(false, Utils.QUEUE_NAME, 2, false));
    }
}
