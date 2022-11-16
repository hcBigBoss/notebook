package com.itheima.test18;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayListDemo {
    public static void main(String[] args) {
        /**
         * 空间换时间   - 时间换空间
         *  1.arraylist 存储特点 ： 有序且可重复
         *  2.为什么有序，为什么可以重复？ size++ 根据下标进行有序存储   没有对存储的只做任何判断
         *  3.arraylist 何时扩容，扩容大小？第一次添加元素会扩容一下，初始容量为10 。  存满扩容  原来的1.5倍
         *  JDK1.6之前，是1.5倍+1并且初始容量是10     JDK1.6以后  1.5倍 并且初始容量为0
         *  4.初始容量是多少？  0
         *  5.为什么arraylist 增删慢 ？ 因为需要保证有序，所以需要移位。  顺序增删，那么效率依然很快。
         *  6.arraylist 为什么默认容量是10？（效率2的幂次方） 1太小 100太大，所以选择了10
         *
         *  1.arraylist 底层是一个Object数组（一旦定义长度不可变）
         *
         *  看源码：
         *  1.看什么？  看核心属性    核心构造器   核心方法
         *  2.看哪些东西？
         *
         *  private static final int DEFAULT_CAPACITY = 10;  默认容量
         *
         *  private static final Object[] EMPTY_ELEMENTDATA = {}; 空数组
         *
         *  private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {}; 空数组
         *
         *  transient Object[] elementData; 没有初始化数据 ，就是arraylist 底层存储元素的数组
         *
         *  private int size; 记录元素存储的个数
         *
         *  public ArrayList() {  初始容量是0
         *         this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
         *     }
         *
         */
        //ConcurrentModificationException    CopyOnWriteArrayList
        ArrayList<String> arrayList1 = new ArrayList();
        CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList(arrayList1);
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        for (String s : arrayList) {
            if(s.equals("b")){
                arrayList.remove(s);
            }
        }



    }
}
