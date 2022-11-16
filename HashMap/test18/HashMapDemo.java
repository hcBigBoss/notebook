package com.itheima.test18;

public class HashMapDemo {
    public static void main(String[] args) {
        /**
         * 存储特点：
         * key (无序且不可重复)  value(没有特性)
         * 1.hashMap底层结构     对Entry数组 + 单向链表      Entry = Node对象
         * 初始容量 0  第一次put 初始化容量为16（默认）   table 数组的长度一定是2的幂次方
         * 为什么无序？因为存储的位置由hash值和数组的长度计算得到
         * 为什么不可重复，如果下标相同会判断key 值是否eq ,hash ,== 那么会用新的value替换老value
         * 扩容大小：原来的两倍
         * 扩容条件：存储的容量 >= 扩容阈值（12）  && 出现hash冲突 （数组下标位置有值）
         * 极端情况下，hashMap可以存储多少个元素不扩容？ 27个不扩容  28必定扩容
         * 链表插入元素的时候，使用的是头插法 ，新来元素放在投节点
         * 为什么初始容量是16? 2的幂次方在位运算情况下速度最快。 hashmap 扩容最损耗性能   2048？ 00100101011100
         * 为什么加载因子是0.75?  泊松分布？  尽可能降低hash冲突的情况。
         * hashMap 线程不安全的集合？
         * 在并发情况下会出现什么问题呢？1.丢值   2.环链 （尾插法）   3.链表存储过长，影响查询性能  （1.8 红黑树）
         *
         * 1.核心属性
         * static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16  默认初始容量16
         *static final int MAXIMUM_CAPACITY = 1 << 30; 最大容量
         * static final float DEFAULT_LOAD_FACTOR = 0.75f; 加载因子  （和扩容有关）
         * static final Entry<?,?>[] EMPTY_TABLE = {}; 空数组
         * transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;  table 就是hashMap底层存储数据的对象
         * transient int size; 记录map存储元素的个数
         * final float loadFactor; 扩容阈值  =  DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR = 12
         *
         * 2.构造方法
         * public HashMap() {
         *         this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
         *     }
         * 3. 核心方法
         */



    }
}
