#### MySql 

```yml
server:
  port: 80
spring:
  application:
    #应用的名称，可选
    name: reggie_take_out

  datasource:
    druid:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql:///reggie?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
      username: root
      password: root
```



#### 时区配置

serverTimezone

成长中的码农Mr.Yellow

于 2020-09-12 23:14:31 发布


今天第一次写springboot的时候遇到了这个问题，页面一直刷新不出来，显示url有问题，后来发现在url后面加上

##### serverTimezone = GMT即可

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/XXX?useUnicode=true&characterEncoding=utf8&serverTimezone = GMT

##### 1、概念： serverTimezone连接mysql数据库时指定了时差

##### 2、时区示例：

//北京时间东八区
serverTimezone=GMT%2B8 
//上海时间
serverTimezone=Asia/Shanghai

##### 3、重要性

（1）时差会导致插入的date数据发生变化（自动更换时差）
（2）UTC是全球标准时间，北京地区早标准时间8小时
（3）注意使用useSSL=false