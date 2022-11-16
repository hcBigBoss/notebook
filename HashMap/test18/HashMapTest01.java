package com.itheima.test18;

import java.util.HashMap;

public class HashMapTest01 implements Runnable{

    public static HashMap<String,String> maps = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        //创建10个线程，存储元素
        for (int i = 0; i < 10; i++) {
            new Thread(new HashMapTest01()).start();
        }
        Thread.sleep(4000);
        System.out.println("总共存储了元素个数为:"+maps.size());
    }

    @Override
    public void run() {
        for(int i = 0 ;i <1000; i++){
            maps.put(Thread.currentThread().getName()+i,i+"");
        }
    }
}
