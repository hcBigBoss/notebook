package com.itheima.test18;

public class HashMap8Demo {
    public static void main(String[] args) {
        /**
         * 1.结构上改变，数组 + 链表  --> 数组 + 链表 + 红黑树
         * 为什么引入红黑树？提升查询效率，解决链表过长的原因？
         * 为什么不直接使用红黑树呢？1.因为链表在端的情况下，效率不一定比红黑树低，2.红黑树占用的存储空间大于链表
         * 为什么红黑树转化阈值是8？ 因为计算得到，链表大于8的情况在千万分之一
         * 链表转红黑树是8，红黑树转链表是6，那么7是什么?
         * 2.存储元素方式  头插法 -- 尾插法
         * 尽可能避免环链问题。不能解决。
         * 3.扩容机制
         * 扩容机制1：如果存储的元素 > 扩容阈值，必定扩容。
         * 扩容机制2：如果数组的长度 < 64 出现了红黑树的触发，那么就会先扩容。不会出现红黑树
         *
         * static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
         *  static final int MAXIMUM_CAPACITY = 1 << 30;
         *  static final float DEFAULT_LOAD_FACTOR = 0.75f;
         *  static final int TREEIFY_THRESHOLD = 8; 链表转换成红黑树的阈值
         *  static final int UNTREEIFY_THRESHOLD = 6; 红黑树转链表的阈值
         *  static final int MIN_TREEIFY_CAPACITY = 64; （于扩容有关）
         */
    }
}
