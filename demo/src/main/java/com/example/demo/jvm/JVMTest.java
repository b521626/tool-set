package com.example.demo.jvm;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JVMTest {

    /**
     * 线程死锁等待演示
     */
    static class SynAddRunnable implements Runnable {
        int a, b;

        public SynAddRunnable(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }
        }

        public static void main(String[] args) {
            for (int i = 0; i < 100; i++) {
                new Thread(new SynAddRunnable(1, 2)).start();
                new Thread(new SynAddRunnable(2, 1)).start();
            }
        }
    }

    /**
     * 线程死循环演示
     */
    public static void createBusyThread() {
        final Thread thread = new Thread(() -> {
            int i = 0;
            while (true) {
                System.out.println(i++);
            }
        }, "testBusyThread");
        thread.start();
    }

    /**
     * 线程锁等待演示
     */
    public static void createLockThread(final Object lock) {
        final Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "testLockThread");
        thread.start();
    }

    /**
     * 内存占位符对象,一个OOMObject大约占64kb
     */
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1204];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            //稍作延时，令监视曲线的变化更加明显，方便观察
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        Object obj = new Object();
        createLockThread(obj);
    }
}
