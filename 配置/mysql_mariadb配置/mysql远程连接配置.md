### Mysql

##### 方式一

#授权

###### mysql>CREATE USER 'root'@'%' IDENTIFIED BY 'root';

##### mysql>GRANT ALL ON *.* TO 'root'@'%';

#刷新权限

###### mysql> flush privileges;

#修改root用户密码

###### mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';

#刷新权限

###### mysql> flush privileges;

###### magician



##### 方式二

##### 1.4.3.2 登录MySQL

获取到root用户的临时密码之后，我们就可以登录mysql数据库，修改root的密码，为root设置一个新的密码。并且我们还需要开启root用户远程访问该数据库的权限，这样的话，我们就可以在windows上来访问这台MySQL数据库。

执行如下指令： 

```
①. 登录mysql（复制日志中的临时密码登录）
	mysql -uroot -p								

②. 修改密码
    set global validate_password_length=4;			设置密码长度最低位数
    set global validate_password_policy=LOW;		设置密码安全等级低，便于密码可以修改成root
    set password = password('root');				设置密码为root
    
③. 开启访问权限
    grant all on *.* to 'root'@'%' identified by 'root';
    flush privileges;
```







//修改密码 8.0

###### ALTER USER USER() IDENTIFIED BY 'Admin2022!'; 



### MariaDB

###### 1、给数据库用户授权，允许远程访问（这1步与设置MySQL相同）。

```dart
mysql> use mysql;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A
Database changed

mysql>  grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
Query OK, 0 rows affected, 1 warning (0,00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0,00 sec）
```



###### 2、修改配置文件，开启MariaDB的远程服务。可以通过下面两步来实现：

 （1）注释掉skip-networking选项，来开启远程访问。
 （2）注释掉bind-address项，允许所有IP远程连接。
 MariaDB 与MySQL的一个不同在于它的配置文件不止一个，可使用grep在/etc/mysql/目录，递归查找特征字符串所在的配置文件。如：

```bash
grep -rn "bind-address" *
```

