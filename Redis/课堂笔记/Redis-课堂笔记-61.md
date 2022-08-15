## 今日内容

- [ ] `Redis`入门
- [ ] ==理解`Redis`数据类型，并总结规律==
- [ ] 练习`Redis`常用命令
- [ ] ==在Java中操作`Redis`==





## 1. `Redis`入门

### 1.1 背景

电商等高并发网站现状

- 用户比较多、海量用户
- 特定时刻高并发

关系型数据库数据存储的问题

- 性能瓶颈：磁盘IO性能低下
- 扩展瓶颈：数据关系复杂，扩展性差，不便于大规模集群
- 随着数据量增长，查询(95%)速度显著降低

解决思路

- 降低磁盘IO次数，越少越好。
- 去除数据间关系，越简单越好。

把这两个特征一合并一起，就有了一个新的概念：`NoSQL`。



### 1.2 `NoSQL`概念

`NoSQL`：即 `Not-Only SQL`（ 非关系型的数据库/不仅仅是数据存储），<span style="color:red">基于内存的数据存储，数据间没有关系。</span>

**作用：**作为关系型数据库的补充，应对基于海量用户和海量数据前提下的数据处理问题。



常见非关系型数据库(NoSql)：

- Redis
- Mongo db
- MemCached





### 1.3 `Redis`

`Redis(Remote Dictionary Server)`是用 C 语言开发的一个开源的**基于内存的高性能键值对（key-value）缓存和存储系统**。

**特征：**

- 高性能。

  内存存储，不走磁盘`IO`，在大数据量下也可以高性能运行。

  官方提供测试数据，50个并发执行100000个请求,读110000 次/s,写81000次/s

- 数据结构丰富

  支持五种数据类型：string（字符串），hash（哈希），list（列表），set（集合）及`zset`(sorted set：有序集合)。

- 原子性

  核心读写部分是单线程的，排队执行，对应的操作便具有了原子性，避免了多线程操作带来的复杂性和不安全因素。Redis6.0开始，网络传输支持多线程。

- 易拓展。

  关系型数据库中记录、表关系复杂，扩容难度高；`NoSQL`中数据无关系，`Redis`3.0开始支持集群，扩容简单。

- 高可用。（高并发、高可用、高性能）

  `Redis`3.0开始支持集群，可以多主多从，当某个节点发生异常时，可以由其他对应节点顶替，保持整个集群的高可用。

- 可持久化（异地灾备）

  支持把数据持久化存储到磁盘中，以便下次启动或遇到故障时，从磁盘加载恢复数据。



**支持的数据类型**（值支持的类型如下，键只有`String`）

- | 支持类型     | 表示形式          |
  | ------------ | ----------------- |
  | 字符串类型   | `string`          |
  | 列表类型     | `list`            |
  | Hash类型     | `hash`            |
  | 集合类型     | `set`             |
  | 有序集合类型 | `zset/sorted_set` |



<span style="color:red">**应用场景：**</span>

- <span style="color:red">缓存</span>。查询频率较高，长久保存，但又不经常变化的数据。
- <span style="color:red">即时信息</span>。临时性的，经常变化的数据。
- `Session`共享。解决分布式系统中`session`共享的问题。
- 其他。诸如：时效性信息、消息队列（MQ MessageQueue）等





### 1.4 `windows`下载安装、启动&测试

- 需要在`GitHub`上下载，非`redis`官方维护。最高版本

  > [https://github.com/ServiceStack/redis-windows](https://github.com/ServiceStack/redis-windows)

- 解压即完成安装

- 启动

  解压目录两个文件分别启动服务端和客户端，双击启动(默认端口(6379)、默认配置启动)。

  ```bash
  redis-server.exe #启动服务端，默认端口是6379
  redis-cli.exe	#启动客户端，默认连接本机的6379端口的reids服务器
  ```



- 指定端口/配置文件启动（命令行）

  ```bash
  # 在解压目录打开cmd
  
  # 默认端口启动服务端
  redis-server.exe
  
  # 指定端口使用默认配置文件启动
  redis-server.exe --port 6380
  
  # 指定端口、指定配置文件启动
  redis-server.exe redis.conf --port 6380
  ```

- 客户端连接服务端

  ```bash
  # 客户端连接本机上服务端默认端口
  redis-cli.exe
  
  # 客户端连接本机上服务端指定端口
  redis-cli.exe -p 6380
  
  # 客户端连接本机上服务端指定端口
  redis-cli.exe -h 192.168.115.130 -p 6380
  ```

- 测试是否可连接(在客户端)

  ```bash
  127.0.0.1:6379> ping
  PONG
  127.0.0.1:6379> set blog blog.sunxiaowei.net
  OK
  127.0.0.1:6379> get blog
  "blog.sunxiaowei.net"
  127.0.0.1:6379> flushdb	#清空当前数据库
  OK
  127.0.0.1:6379> keys *
  (empty list or set)		#数据库为空
  
  # 切库 select n
  ```

  

### 1.4 `linux`下载安装、启动&测试

- 下载

  登录`redis.cn`下载`5.0.5`的`linux`稳定版本

  ```bash
  http://download.redis.io/releases/redis-4.0.0.tar.gz
  ```

  

- 安装和启动

  ```bash
  # 0.准备工作1：安装wget
  yum install wget
  
  # 0.准备工作2：安装gcc编译环境
  yum install gcc-c++
  
  # 0.在/usr/loca目录（用户软件安装目录）创建redis文件夹（包括data文件夹、log文件夹）
  mkdir /usr/local/redis
  
  
  # 1.上传/下载redis安装包到centos，下面两个步骤（1.1和1.2）任选其一即可
  # 1.1 上传离线安装包，上传redis安装包到centos7
  # crt工具 Alt + p 打开sftp窗口，通过put命令上传到登录用户的home目录
  put redis-linux-5.0.5.tar.gz
  
  # 剪切压缩包到安装目录
  mv /root/redis-linux-5.0.5.tar.gz /usr/local/redis/redis-5.0.5.tar.gz
  
  # 1.2 在线下载安装包（需要安装好了wget）
  wget -P /usr/local/redis http://download.redis.io/releases/redis-5.0.5.tar.gz
  
  # 2. 进入redis目录
  cd  /usr/local/redis
  
  # 3. 解压到当前目录
  tar -zxvf redis-5.0.5.tar.gz
  
  # 4. 进入解压目录，执行编译
  cd redis-5.0.5
  make
  
  # 5. 进入src目录，可以看到服务端和客户端的启动程序
  cd src
  ./redis-server
  ./redis-cli
  
  # # 6.新建数据目录和日志目录
  
  mkdir /usr/local/redis/redis-5.0.5/data
  mkdir /usr/local/redis/redis-5.0.5/log
  mkdir /usr/local/redis/redis-5.0.5/conf
  
  # 7. 在任意位置位置启动Redis-server
  # 打开linux配置path的文件,
  vim /etc/profile
  #在文件末尾添加下面两行,其中REDIS_HOME的值是redis安装的根目录
  export REDIS_HOME=/usr/local/redis/redis-5.0.5
  export PATH=$REDIS_HOME/src:$PATH
  
  # 重新加载配置文件
  source /etc/profile
  ```

- 指定端口/配置文件启动

  ```bash
  # 默认端口启动服务端
  redis-server
  
  # 指定端口使用默认配置文件启动
  redis-server --port 6380
  
  # 指定端口、指定配置文件启动
  redis-server ../redis.conf
  
  # 客户端连接非默认端口的服务端
  redis-cli -p 6380
  
  # 使用密码连接
  redis-cli -p 6380 -a 123456
  # 或者连接后使用指定的命令认证
  auth 123456
  
  # 整体上和Windows一致
  ```

  

- 测试是否可连接(在客户端)

  ```bash
  127.0.0.1:6379> ping
  PONG
  127.0.0.1:6379> set blog blog.sunxiaowei.net
  OK
  127.0.0.1:6379> get blog
  "blog.sunxiaowei.net"
  127.0.0.1:6379> flushdb	#清空当前数据库
  OK
  127.0.0.1:6379> keys *
  (empty list or set)		#数据库为空
  
  # 切库 select n
  ```



> **退出**

(`quit`、`exit`、`ctrl + C` 、`Ctrl +D`)任选其一



> **注意**

宿主机连接虚拟机中的`redis`服务时，需要关闭保护模式或者绑定对应的`redis`服务所在设备的网卡。





### 1.5 Redis配置

通过修改配置文件，可以让Redis服务运行在不同端口上、允许通过不同的网卡连接等。

- Linux系统中Redis配置文件：REDIS_HOME/redis.conf
- Windows系统中Redis配置文件：REDIS_HOME/redis.windows.conf



#### 1.5.1 常见配置如下

**1）设置Redis服务运行的端口**

port 6379

**2）设置Redis服务后台运行**

​	将配置文件中的==daemonize==配置项改为yes，默认值为no。

​	注意：Windows版的Redis不支持后台运行。

**3）**~~设置`Redis`服务密码~~

​	将配置文件中的 ==# requirepass foobared== 配置项取消注释，默认为注释状态。foobared为密码，可以根据情况自己指定。

**4）**设置允许客户端远程连接Redis服务

​	Redis服务默认只能客户端本地连接，不允许客户端远程连接。将配置文件中的 ==bind 127.0.0.1== 配置项注释掉。



**解释说明：**

> Redis配置文件中 ==#== 表示注释
>
> Redis配置文件中的配置项前面不能有空格，需要顶格写
>
> daemonize：用来指定redis是否要用守护线程的方式启动，设置成yes时，代表开启守护进程模式。在该模式下，redis会在后台运行
>
> requirepass：设置Redis的连接密码
>
> bind：如果指定了bind，则说明只允许来自指定网卡的Redis请求。如果没有指定，就说明可以接受来自任意一个网卡的Redis请求。



**注意**：修改配置文件后需要重启Redis服务配置才能生效，并且启动Redis服务时需要显示的指定配置文件：

1）Linux中启动Redis服务

~~~
# 进入Redis安装目录
cd /usr/local/redis-4.0.0
# 启动Redis服务，指定使用的配置文件
./src/redis-server ./redis.conf
~~~

2）Windows中启动Redis服务

![image-20210927104929169](Redis-课堂笔记-61.assets/image-20210927104929169.png)



由于Redis配置文件中开启了认证校验，即客户端连接时需要提供密码，此时客户端连接方式变为：

![image-20210927105909600](Redis-课堂笔记-61.assets/image-20210927105909600.png)



#### 1.5.2 最简配置

- **最简配置**

  ```bash
  # 绑定Ip 指定可以通过redis服务端所在主机的哪个网口连接当前Redis实例  如果注释（删掉）则任意IP都可以连接（关闭保护模式的前提下）
  # 生产环境中，为了安全，需要指定。学习期间为了方便，注释掉
  # 可以在一行绑定本机的多个IP，中间使用空格分割
  # bind 127.0.0.1
  bind 127.0.0.1 192.168.115.130 
  
  # 指定Redis的端口
  port 6379
  
  # 当客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
  timeout 0
  
  # 是否以守护进程启动，守护进程表示后台启动，非守护进程表示前台启动
  daemonize no
  
  # 设置日志的级别  debug、verbose、notice、warning，默认为verbose
  loglevel verbose
  
  # 日志文件的名字，当指定为空字符串时，为标准输出，如果redis已守护进程模式运行，那么日志将会输出到  /dev/null 。
  # 如果这里配置了指定的日志文件，就算redis以非守护进程方式启动(前台启动)，日志也只是输入到日志文件，而不会在启动窗口展示
  # logfile ""
  ```


#### 1.5.3 完整配置

- 见中文注释版配置文件





## 2. `redis`命令操作

### 2.1 数据类型



数据类型指的是`key-value`中的`value的类型`，`key永远是字符串` 。

`Redis`的`value`支持五种数据类型：string（字符串），hash（哈希），list（列表），set（集合）及`zset`(sorted set：有序集合)。

- string   等效： `Map<String, String>`
- hash    等效： `Map<String,HashMap<String,String>>`
- list       等效： `Map<String,LinkedList<String>>`  有序 可重复
- set       等效： `Map<String,HashSet<String>>`  无序 不可重复
- `zset`（sorted set）  -- `Map<String,TreeSet<String>>`  可以指定序，不可重复，可以基于score实现排序

会根据数据不同，选择不同的数据类型，保证在不同场景下，存取处理的速度都是最快的。



### ==2.2 String==

最简单、最常用的单个数据存储的类型。

> 常用命令

<font color="red"  size=5>**set/get/setex/setnx**</font>

> 应用场景

验证码(`setex`)、分布式锁(`setnx`)、存储对象（不经常变化的）(`set`、`get`、`json`格式)



> 命令演示

```bash
# set key value				设置指定key的值，如果key存在，则覆盖原值。
# get key					获取指定key的值	
# setex key seconds value	设置指定key的值，并指定有效期
# setnx key value			设置指定key的值，如果存在则不设置

127.0.0.1:6379> ping
PONG 									#打招呼响应
127.0.0.1:6379> set name xiaoming
OK										# 设置成功
127.0.0.1:6379> get name
"xiaoming" 								# 根据key获取值
127.0.0.1:6379> set age 20
OK 										# 设置成功
127.0.0.1:6379> get age
"20"									# 根据key获取值
127.0.0.1:6379> set age 30
OK										# ke存在，则修改
127.0.0.1:6379> get age
"30"									# 根据key获取值，得到的是修改后的值
127.0.0.1:6379> setex city 10 beijing
OK										# 设置成功，并指定了有效期；过期后自动失效
127.0.0.1:6379> get city
"beijing"								# 根据key获取值
127.0.0.1:6379> ttl city				
"beijing"								# 查询指定key的过期时间，未过期显示剩余时间，过期显示-2
127.0.0.1:6379> get city
(nil)									# 根据key获取值，超时后获取不到
127.0.0.1:6379> setnx key1 value1
(integer) 1								# 设置成功，因为key不存在
127.0.0.1:6379> setnx key1 value2
(integer) 0								# 设置失败，因为key已经存在
127.0.0.1:6379> get key1
"value1"								# 根据key获取值
```





### ==2.3 `Hash`==

`Key`为`String`类型，值为`Hash`类型。

`Hash`类型是一个`string`类型的 field 和 value 的映射表，适合用于存储对象（经常变化的对象）：

![image-20210927113014567](Redis-课堂笔记-61.assets/image-20210927113014567.png)



命令多以`h`开头

> 常用命令：

<font color="red" size=5>**hset/hget/hdel/hexists/hkeys/hvals/hgetall**</font>

> 格式：

`redis的key` `field` `value`

> 应用场景：

购物车(`hset`、`hdel`)、存储对象（频繁变化的）(`hset`、`hdel`)

```bash
HSET key field value             #将哈希表 key 中的字段 field 的值设为 value
HGET key field                   #获取存储在哈希表中指定字段的值
HDEL key field                   #删除存储在哈希表中的指定字段
HKEYS key                        #获取哈希表中所有字段
HVALS key                        #获取哈希表中所有值
HGETALL key                      #获取在哈希表中指定 key 的所有字段和值


127.0.0.1:6379> hset 001 name xiaoming
(integer) 1									# 设置，需要指定key field value
127.0.0.1:6379> hset 001 age 20
(integer) 1									# 设置，需要指定key field value			
127.0.0.1:6379> hget 001
(error) ERR wrong number of arguments for 'hget' command #hget需要指定key和field
127.0.0.1:6379> hget 001 name
"xiaoming"									# 获取成功，需要指定key field
127.0.0.1:6379> hget 001 age
"20"										# 获取成功，需要指定key field
127.0.0.1:6379> hkeys 001
1) "name"
2) "age"									# 获取当前key下所有的field成功，需要指定key
127.0.0.1:6379> hvals 001
1) "xiaoming"
2) "20"										# 获取当前key下所有的value成功，需要指定key
127.0.0.1:6379> hgetall 001
1) "name"
2) "xiaoming"
3) "age"
4) "20"										# 获取当前key下所有的field+value成功，需要指定key
127.0.0.1:6379> HEXISTS 001 name
(integer) 1									# 判断当前key下是否存在指定的field，1表示存在
127.0.0.1:6379> HEXISTS 001 namex
(integer) 0									# 判断当前key下是否存在指定的field，0表示不存在
```





### 2.4 List

键为`String`，值为`list`。

Redis 列表是字符串列表，两端操作的队列，所以可以左右两端操作；

![image-20210927113312384](Redis-课堂笔记-61.assets/image-20210927113312384.png)



命令中涉及两端操作的，`l`表示 左，`r`表示右；其他情况下命令以`l`开头

没有索引越界异常，获取超出索引返回的是`nil`



> 常用命令：

<font color="red" size=5>**lpush/rpush/lpop/rpop/lrange/llen/brpop/blpop**</font>

> 应用场景：

消息队列、数据顺序添加并汇总，`eg`：

```bash
LPUSH key value1 [value2]        #将一个或多个值插入到列表头部
LRANGE key start stop            #获取列表指定范围内的元素
RPOP key                         #移除并获取列表最后一个元素
LLEN key                         #获取列表长度
BRPOP key1 [key2 ] timeout       #移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止


127.0.0.1:6379> lpush mylist a b c	# 可以一次性指定list列表中多个值
(integer) 3							# 返回是list列表的长度
127.0.0.1:6379> lrange mylist 0 -1  #索引从0开始。
1) "c"								# 在不明确list长度的前提下，慎用lrange key 0 -1
2) "b"								# 先用llen判断长度
3) "a"								# 获取该key的列表中所有的值。
127.0.0.1:6379> llen mylist			# 先用llen判断长度
(integer) 3
127.0.0.1:6379> lrange mylist 0 2	# 获取该key的列表中指定范围的值。
1) "c"
2) "b"
3) "a"
127.0.0.1:6379> lpush mylist itcast	# 左边添加到了头部位置
(integer) 4							# 返回是list列表的长度
127.0.0.1:6379> lrange mylist 0 -1  # 默认遍历从左往右
1) "itcast"
2) "c"
3) "b"
4) "a"
127.0.0.1:6379> lpush mylist a
(integer) 5							# 返回是list列表的长度
127.0.0.1:6379> lrange mylist 0 -1
1) "a"
2) "itcast"
3) "c"
4) "b"
5) "a"
127.0.0.1:6379> rpop mylist			# 从右侧获取并弹出
"a"
127.0.0.1:6379> lrange mylist 0 -1  # 查看最右侧的a已经消失
1) "a"
2) "itcast"
3) "c"
4) "b"
127.0.0.1:6379> rpop mylist			# 再弹
"b"
127.0.0.1:6379> lrange mylist 0 -1
1) "a"
2) "itcast"
3) "c"
127.0.0.1:6379> brpop mylist 10		# 阻塞，结束条件：超时或弹出成功
1) "mylist"							# 显示key的名字
2) "c"								# 显示弹出的值
127.0.0.1:6379> brpop mylist 10
1) "mylist"
2) "itcast"
127.0.0.1:6379> brpop mylist 10
1) "mylist"
2) "a"
127.0.0.1:6379> brpop mylist 10
(nil)								# 阻塞一段时候后，显示弹出nil
(10.05s)							# 时间稍有偏差
```





### 2.5 Set

键为`String`，值为`set`。

与`hash`的`field`结构相同，集合成员无序不重复。

![image-20210927113632472](Redis-课堂笔记-61.assets/image-20210927113632472.png)

命令多以`s`开头

> 常用命令

<font color="red" size=5>**sadd/smembers/scard/sinter/sunion/sdiff/srem/srandmember/sismember**</font>

> 应用场景

好友关注感兴趣的人的集合操作(`sinter/sunion/sdiff`)、随机展示(`srandmember`)、黑白名单(`sismember`)



> 演示代码

```bash
SADD key member1 [member2]            # 向集合添加一个或多个成员
SMEMBERS key                          # 返回集合中的所有成员
SCARD key                             # 获取集合的成员数
SINTER key1 [key2]                    # 返回给定所有集合的交集
SUNION key1 [key2]                    # 返回所有给定集合的并集
SDIFF key1 [key2]                     # 返回给定所有集合的差集
SREM key member1 [member2]            # 移除集合中一个或多个成员
srandmember key	[count]				  # 随机获取集合中某个成员
sismember key member				  # 判断是否是集合中成员


127.0.0.1:6379> sadd myset a b c d
(integer) 4							# 添加元素进集合。返回添加成功的个数
127.0.0.1:6379> SMEMBERS myset		# 查看集合中元素
1) "d"
2) "b"
3) "c"
4) "a"
127.0.0.1:6379> sadd myset a b		# 再次添加存在的元素，添加失败，返回成功的个数0
(integer) 0
127.0.0.1:6379> SMEMBERS myset		# 再次查看集合中元素
1) "c"
2) "a"
3) "b"
4) "d"
127.0.0.1:6379> scard myset			# 查看集合中元素个数
(integer) 4
127.0.0.1:6379> sismember myset a	# 查看a是否是myset元素，返回1表示是
(integer) 1
127.0.0.1:6379> sismember myset x	# 查看x是否是myset元素，返回0表示不是
(integer) 0
127.0.0.1:6379> sadd myset2 a b x y	# 添加元素进集合。返回添加成功的个数
(integer) 4
127.0.0.1:6379> SMEMBERS myset2
1) "b"
2) "x"
3) "y"
4) "a"
127.0.0.1:6379> sinter myset myset2 #求交集
1) "b"
2) "a"
127.0.0.1:6379> sunion myset myset2 #求并集
1) "b"
2) "d"
3) "a"
4) "y"
5) "c"
6) "x"
127.0.0.1:6379> sdiff myset myset2 # 求差集，在前者中存在，但是在后者中不存在的
1) "d"
2) "c"
127.0.0.1:6379> sdiff myset2 myset # 求差集，在前者中存在，但是在后者中不存在的
1) "x"
2) "y"
127.0.0.1:6379> SRANDMEMBER myset 2 # 随机从集合中取2个元素
1) "d"
2) "c"
127.0.0.1:6379> SRANDMEMBER myset 2 # 随机从集合中取2个元素
1) "b"
2) "c"
127.0.0.1:6379> SRANDMEMBER myset 3# 随机从集合中取3个元素
1) "d"
2) "b"
3) "a"
```





### 2.6 zset(SortedSet)

命令多以`z`开头，zset中元素是以分数从小到大排序的

> 常用命令

<font color="red" size=5>**zadd/zrange/zcard/zincryby/zrem**</font>



> 命令演示

```bash
27.0.0.1:6379> zadd myset3 10.0 a 9.0 b 	# 添加元素进集合，并携带分数，返回成功添加个数
(integer) 2
127.0.0.1:6379> zrange myset3 0 -1			# 查看集合中所有成员，慎用-1
1) "b"
2) "a"
127.0.0.1:6379> zadd myset3 9.9 c			# 再添加一个成员c，分数是9.9
(integer) 1
127.0.0.1:6379> zrange myset3 0 -1			# 查看集合中所有成员，慎用0 -1		
1) "b"
2) "c"
3) "a"
127.0.0.1:6379> zrange myset3 0 -1 withscores # 查看集合中所有成员，携带分数
1) "b"
2) "9"
3) "c"
4) "9.9000000000000004"
5) "a"
6) "10"
127.0.0.1:6379> ZINCRBY myset3 20 b			# 为某个成员增加分数，最注重影响 排名顺序
"29"
127.0.0.1:6379> zrange myset3 0 -1 withscores
1) "c"
2) "9.9000000000000004"
3) "a"
4) "10"
5) "b"
6) "29"
127.0.0.1:6379> ZREM myset3 b				# 删除某(多)个成员
(integer) 1
127.0.0.1:6379> zrange myset3 0 -1 withscores  # 查看集合中所有成员，携带分数
1) "c"
2) "9.9000000000000004"
3) "a"
4) "10"
```



### 2.7 通用命令



主要演示和`key`相关的命令

> 命令演示

```bash
keys pattern		# 按照正则pattern指定的规则查找key，
keys * 				# 查看所有的key，慎用
exists keyName		# 查看该key是否存在
type keyName		# 查看该key对应的值的类型
ttl keyName			# 查看该key的的剩余存活时间，-1表示永久
del keyName			# 删除对应的key

127.0.0.1:6379> keys *			# 查看当前库中所有的key
1) "myset2"
2) "myset"
3) "name"
4) "age"
5) "key1"
6) "myset3"
7) "001"
127.0.0.1:6379> keys my*		# 查看当前库中所有以my开头的key
1) "myset2"
2) "myset"
3) "myset3"
127.0.0.1:6379> exists myset4	# 判断该key是否存在，不存在返回0
(integer) 0
127.0.0.1:6379> exists myset3	# 判断该key是否存在，存在返回1
(integer) 1
127.0.0.1:6379> type myset3		# 查看对应key的值的类型
zset
127.0.0.1:6379> ttl myset3		# 查看该key的的存活时间，-1表示永久，-2表示已过期
(integer) -1
127.0.0.1:6379> setex gender 5 mele # 设置key&value，并指定有效期为5s
OK
127.0.0.1:6379> ttl gender		# 查看该key的的剩余存活时间
(integer) 3
127.0.0.1:6379> ttl gender		# 查看该key的的剩余存活时间
(integer) 2
127.0.0.1:6379> ttl gender		# 查看该key的的剩余存活时间
(integer) 1
127.0.0.1:6379> ttl gender		# 查看该key的的剩余存活时间
(integer) 0
127.0.0.1:6379> ttl gender		# 查看该key的的剩余存活时间，-2表示已过期
(integer) -2
127.0.0.1:6379> del 001 age		# 删除对应的key
(integer) 2
127.0.0.1:6379> keys *			# 再次查看所有的key
1) "myset2"
2) "myset"
3) "name"
4) "key1"
5) "myset3"
```





### 2.8 说明

命令不需要记忆背诵，看笔记或者查文档

更多命令可以参考

- Redis中文网：https://www.redis.net.cn/order/  /  https://www.redis.net.cn/tutorial/3501.html

- Redis参考命令(2019)：http://redisdoc.com/

- http://doc.redisfans.com/





## ==3. `JavaAPI`操作`Redis`==

`Redis`的`Java`客户端有很多，官方推荐三种

- `Jedis`：方法名与原生命令相似，最友好。
- `Lettuce`
- `Redisson`



另外：

Spring 对 Redis 客户端进行了整合，提供了 `Spring-Data-Redis`，在Spring Boot项目中还提供了对应的Starter，即 `spring-boot-starter-data-redis`。



所以，常用的有两个：

1. `Jedis`
2. `Spring-Data-Redis/spring-boot-starter-data-redis`



### 3.2 `Jedis`

`Redis`官方推荐的客户端之一，特点就是方法名和`redis`命令统一，易学习。

#### 3.2.1 使用步骤

1. 导入`jedis`依赖坐标
2. 创建连接对象
3. 通过方法调用命令操作`redis`
4. 释放资源



#### 3.2.2 演示

- 导入依赖

  ```xml
  <!-- 单元测试 -->
  <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
  </dependency>
  
  <!-- jedis -->
  <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>2.8.1</version>
  </dependency>
  ```

- 编码，创建连接并使用

  ```java
  package com.itheima.test;
  
  import org.junit.Test;
  import redis.clients.jedis.Jedis;
  
  import java.util.Set;
  
  /**
   * 使用Jedis操作Redis
   */
  public class JedisTest {
  
      @Test
      public void testRedis(){
          //1 获取连接
          Jedis jedis = new Jedis("localhost",6379);
          
          //2 执行具体的操作
          jedis.set("username","xiaoming");
  
          String value = jedis.get("username");
          System.out.println(value);
  
          //jedis.del("username");
  
          jedis.hset("myhash","addr","bj");
          String hValue = jedis.hget("myhash", "addr");
          System.out.println(hValue);
  
          Set<String> keys = jedis.keys("*");
          for (String key : keys) {
              System.out.println(key);
          }
  
          //3 关闭连接
          jedis.close();
      }
  }
  ```



### ==3.3 spring-data-redis==

#### 3.3.1 介绍

`Spring Data Redis` 是 Spring 的一部分，提供了一个高度封装的类`RedisTemplate`，该类对 `Jedis` 的`API`按照`value`的数据类型不同，封装出了一系列的对象/方法。

- `ValueOperations`：简单K-V操作（String类型）

- `SetOperations`：set类型数据操作

- `HashOperations`：针对hash类型的数据操作

- `ListOperations`：针对list类型的数据操作

- `ZSetOperations`：zset类型数据操作

  

在 Spring 项目中，可以使用Spring Data Redis来简化 Redis 操作



网址：https://spring.io/projects/spring-data-redis

![image-20210927143741458](Redis-课堂笔记-61.assets/image-20210927143741458.png)



#### 3.3.2 使用步骤

0. 新建`SpringBoot`模块

1. 导入起步依赖
2. 注入`redisTemplate`对象
3. 使用上述对象操作`redis`



#### 3.3.3  演示：环境搭建

1. 新建`SpringBoot`项目（勾选起步依赖、自动创建启动类）

2. 导入起步依赖

   ```xml
   <!-- 指定父模块 -->
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.3.7.RELEASE</version>
       <relativePath/>
   </parent>
   
   <!-- 定义本模块的坐标 -->
   <groupId>com.itheima</groupId>
   <artifactId>springdataredis_demo</artifactId>
   <version>1.0-SNAPSHOT</version>
   
   <!-- 指定本模块的jdk版本 -->
   <properties>
       <java.version>1.8</java.version>
   </properties>
   
   <!-- 依赖 -->
   <dependencies>
       <!-- 测试起步依赖 -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
           <scope>test</scope>
       </dependency>
   
       <!-- junit -->
       <dependency>
           <groupId>junit</groupId>
           <artifactId>junit</artifactId>
       </dependency>
   
       <!-- redis起步依赖 -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-redis</artifactId>
       </dependency>
   </dependencies>
   <!-- 打包插件 -->
   <build>
       <plugins>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
               <version>2.4.5</version>
           </plugin>
       </plugins>
   </build>
   ```

3. 编写启动类

   ```java
   @SpringBootApplication
   public class App {
   
       public static void main(String[] args) {
           SpringApplication.run(App.class,args);
       }
   
   }
   ```

   

4. 在`application.yml`中配置redis相关参数

   ```yaml
   spring:
     application:
       name: springdataredis_demo
     #Redis相关配置
     redis:
       host: localhost
       port: 6379
       #password: 123456
       database: 0 # 操作的是0号数据库，有0-15共16个数据库
       jedis:
         #Redis连接池配置
         pool:
           max-active: 8 #最大连接数
           max-wait: 1ms #连接池最大阻塞等待时间
           max-idle: 4 #连接池中的最大空闲连接
           min-idle: 0 #连接池中的最小空闲连接
   ```

   

5. 测试类中注入`RedisTemplate`对象并使用

   ```java
   @SpringBootTest
   @RunWith(SpringRunner.class)
   public class SpringDataRedisTest {
   
       // 注入RedisTemplate对象，可以通过该对象并识别配置文件中的连接参数连接到redis服务器
       @Autowired
       private RedisTemplate redisTemplate;
   }
   ```

   

1. 

#### 3.3.4 操作`String`类型数据

- 演示代码

  ```java
  /**
   * 操作String类型数据
   */
  @Test
  public void testString(){
      redisTemplate.opsForValue().set("city123","beijing");
  
      String value = (String) redisTemplate.opsForValue().get("city123");
      System.out.println(value);
  
      redisTemplate.opsForValue().set("key1","value1",10l, TimeUnit.SECONDS);
  
      Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
      System.out.println(aBoolean);
  }
  ```

  

- **问题展示**

  `redis`客户单查看效果

  ```bash
  127.0.0.1:6379> keys *
   1) "name"
   2) "myset"
   3) "myhash"
   4) "\xac\xed\x00\x05t\x00\acity123"   # key前面自动添加了一个前缀，不影响程序运行，但是影响程序员查看
  ```



- **产生原因**

  `RedisTemplate`在操作`redis`的时候，会使用`JdkSerializationRedisSerializer`在读写，对key&value进行序列化和反序列化，在key&value指定一个前缀。

  该前缀不影响程序的运行，但是影响感官。

- **解决方式1：**

  手动创建`RedisTemplate`并指定序列化器。

  ```java
  package com.itheima.config;
  
  import org.springframework.cache.annotation.CachingConfigurerSupport;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.data.redis.connection.RedisConnectionFactory;
  import org.springframework.data.redis.core.RedisTemplate;
  import org.springframework.data.redis.core.StringRedisTemplate;
  import org.springframework.data.redis.serializer.StringRedisSerializer;
  
  /**
   * Redis配置类
   */
  
  @Configuration
  public class RedisConfig extends CachingConfigurerSupport {
  
  
      @Bean
      public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
  
          RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
  
          //默认的Key序列化器为：JdkSerializationRedisSerializer
          redisTemplate.setKeySerializer(new StringRedisSerializer());
          redisTemplate.setHashKeySerializer(new StringRedisSerializer());
  
          redisTemplate.setConnectionFactory(connectionFactory);
  
          return redisTemplate;
      }
  }
  ```

  

- **解决方式2：**

  在操作string和hash类型的数据时，可以使用`StringRedisTemplate`替代`RedisTemplate`。

  这种方式不需要手动指定序列化器。（也就是不需要对应的配置类）

  ```java
  @SpringBootTest
  @RunWith(SpringRunner.class)
  public class SpringDataRedisTest {
  
      @Autowired
      private StringRedisTemplate redisTemplate;
  
      /**
       * 操作String类型数据
       */
      @Test
      public void testString(){
          redisTemplate.opsForValue().set("city123","beijing");
      
          String value = (String) redisTemplate.opsForValue().get("city123");
          System.out.println(value);
      
          redisTemplate.opsForValue().set("key1","value1",10l, TimeUnit.SECONDS);
      
          Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
          System.out.println(aBoolean);
      }
  }
  ```

  

#### 3.3.5 操作Hash类型数据

```java
/**
 * 操作Hash类型数据
 */
@Test
public void testHash(){
    HashOperations hashOperations = redisTemplate.opsForHash();

    //存值
    hashOperations.put("002","name","xiaoming");
    hashOperations.put("002","age","20");
    hashOperations.put("002","address","bj");

    //取值
    String age = (String) hashOperations.get("002", "age");
    System.out.println("age = " + age);

    //获得hash结构中的所有字段
    Set keys = hashOperations.keys("002");
    for (Object key : keys) {
        System.out.println("key = " + key);
    }

    //获得hash结构中的所有值
    List values = hashOperations.values("002");
    for (Object value : values) {
        System.out.println("value = " + value);
    }
}
```





#### 3.3.6 操作list类型数据

```java
/**
 * 操作List类型的数据
 */
@Test
public void testList(){
    ListOperations listOperations = redisTemplate.opsForList();

    //存值
    listOperations.leftPush("mylist","a");
    listOperations.leftPushAll("mylist","b","c","d");

    //取值
    List<String> mylist = listOperations.range("mylist", 0, -1);
    for (String value : mylist) {
        System.out.println(value);
    }

    //获得列表长度 llen
    Long size = listOperations.size("mylist");
    int lSize = size.intValue();


    for (int i = 0; i < lSize; i++) {
        //出队列
        String element = (String) listOperations.rightPop("mylist");
        System.out.println(element);
    }
}
```





#### 3.3.7 操作Set类型数据

```java
/**
 * 操作Set类型的数据
 */
@Test
public void testSet(){
    SetOperations setOperations = redisTemplate.opsForSet();

    //存值
    setOperations.add("myset","a","b","c","a");

    //取值
    Set<String> myset = setOperations.members("myset");
    for (String o : myset) {
        System.out.println(o);
    }

    //删除成员
    setOperations.remove("myset","a","b");

    //取值
    myset = setOperations.members("myset");
    for (String o : myset) {
        System.out.println(o);
    }

}
```





#### 3.3.8 操作Zset类型数据

```java
/**
 * 操作ZSet类型的数据
 */
@Test
public void testZset(){
    ZSetOperations zSetOperations = redisTemplate.opsForZSet();

    //存值
    zSetOperations.add("myZset","a",10.0);
    zSetOperations.add("myZset","b",11.0);
    zSetOperations.add("myZset","c",12.0);
    zSetOperations.add("myZset","a",13.0);

    //取值
    Set<String> myZset = zSetOperations.range("myZset", 0, -1);
    for (String s : myZset) {
        System.out.println(s);
    }

    //修改分数
    zSetOperations.incrementScore("myZset","b",20.0);

    //取值
    myZset = zSetOperations.range("myZset", 0, -1);
    for (String s : myZset) {
        System.out.println(s);
    }

    //删除成员
    zSetOperations.remove("myZset","a","b");

    //取值
    myZset = zSetOperations.range("myZset", 0, -1);
    for (String s : myZset) {
        System.out.println(s);
    }
}
```







#### 3.3.9 通用操作

```java
/**
 * 通用操作，针对不同的数据类型都可以操作
 */
@Test
public void testCommon(){
    //获取Redis中所有的key
    Set<String> keys = redisTemplate.keys("*");
    for (String key : keys) {
        System.out.println(key);
    }

    //判断某个key是否存在
    Boolean itcast = redisTemplate.hasKey("itcast");
    System.out.println(itcast);

    //删除指定key
    redisTemplate.delete("myZset");

    //获取指定key对应的value的数据类型
    DataType dataType = redisTemplate.type("myset");
    System.out.println(dataType.name());

}
```

