package com.alex.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesValue {

    public static String normalExchange;

    public static String deadExchange;

    public static String shortQueue;

    public static String longQueue;

    public static String deadQueue;

    public static String liveQueue;

    public static String tsqen;

    public static String tlqen;

    public static String ntqen;

    public static String dqed;

    public static Integer shortTTL;

    public static Integer longTTL;

    public static String confirmExchange;

    public static String confirmQueue;

    public static String confirmRoutingKey;

    public static String delayExchange;

    public static String delayQueue;

    public static String delayRoutingKey;

    public static String backupExchange;
    public static String processQueue;
    public static String alarmQueue;
    public static String processRoutingKey;
    public static String alarmRoutingKey;

    @Value("${confirm.returns.backup.exchange.name}")
    public void setBackupExchange(String backupExchange) {
        this.backupExchange = backupExchange;
    }

    @Value("${confirm.returns.backup.process.queue.name}")
    public void setProcessQueue(String processQueue) {
        this.processQueue = processQueue;
    }

    @Value("${confirm.returns.backup.alarm.queue.name}")
    public void setAlarmQueue(String alarmQueue) {
        this.alarmQueue = alarmQueue;
    }

    @Value("${confirm.returns.backup.process.routing.key}")
    public void setProcessRoutingKey(String processRoutingKey) {
        this.processRoutingKey = processRoutingKey;
    }

    @Value("${confirm.returns.backup.alarm.routing.key}")
    public void setAlarmRoutingKey(String alarmRoutingKey) {
        this.alarmRoutingKey = alarmRoutingKey;
    }

    @Value("${dead.letter.queue.exchange.normal}")
    public void setNormalExchange(String normalExchange) {
        this.normalExchange = normalExchange;
    }

    @Value("${dead.letter.queue.exchange.dead}")
    public void setDeadExchange(String deadExchange) {
        this.deadExchange = deadExchange;
    }

    @Value("${dead.letter.queue.ttl.short.queue}")
    public void setShortQueue(String shortQueue) {
        this.shortQueue = shortQueue;
    }

    @Value("${dead.letter.queue.ttl.long.queue}")
    public void setLongQueue(String longQueue) {
        this.longQueue = longQueue;
    }

    @Value("${dead.letter.queue.dead.queue}")
    public void setDeadQueue(String deadQueue) {
        this.deadQueue = deadQueue;
    }

    @Value("${dead.letter.queue.no.ttl.queue}")
    public void setLiveQueue(String liveQueue) {
        this.liveQueue = liveQueue;
    }

    @Value("${dead.letter.queue.routing.key.tsqen}")
    public void setTsqen(String tsqen) {
        this.tsqen = tsqen;
    }

    @Value("${dead.letter.queue.routing.key.tlqen}")
    public void setTlqen(String tlqen) {
        this.tlqen = tlqen;
    }

    @Value("${dead.letter.queue.routing.key.ntqen}")
    public void setNtqen(String ntqen) {
        this.ntqen = ntqen;
    }

    @Value("${dead.letter.queue.routing.key.dqed}")
    public void setDqed(String dqed) {
        this.dqed = dqed;
    }

    @Value("${dead.letter.queue.short.queue.ttl}")
    public void setShortTTL(Integer shortTTL) {
        this.shortTTL = shortTTL;
    }

    @Value("${dead.letter.queue.long.queue.ttl}")
    public void setLongTTL(Integer longTTL) {
        this.longTTL = longTTL;
    }

    @Value("${confirm.exchange.name}")
    public void setConfirmExchange(String confirmExchange) {
        this.confirmExchange = confirmExchange;
    }

    @Value("${confirm.queue.name}")
    public void setConfirmQueue(String confirmQueue) {
        this.confirmQueue = confirmQueue;
    }

    @Value("${confirm.routing.key.name}")
    public void setConfirmRoutingKey(String confirmRoutingKey) {
        this.confirmRoutingKey = confirmRoutingKey;
    }

    @Value("${delay.queue.exchange.name}")
    public void setDelayExchange(String delayExchange) {
        this.delayExchange = delayExchange;
    }

    @Value("${delay.queue.queue.name}")
    public void setDelayQueue(String delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Value("${delay.queue.routing.key.name}")
    public void setDelayRoutingKey(String delayRoutingKey) {
        this.delayRoutingKey = delayRoutingKey;
    }
}
