package com.alex.consumer;

import com.alex.utils.Utils;

public class ConsumerOne {

    public static void main(String[] args) {
        System.out.println(Utils.receive(false, Utils.QUEUE_NAME, 8, true));

    }
}
