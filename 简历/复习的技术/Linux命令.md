# Linux命令:

### 基础命令:

clear:清屏(置顶,并不是真正的清屏,滚轮可以看到之前的历史记录)

ctrl+d:退出当前用户

cd .. :返回上一级目录



### 账号相关:

1.创建用户:useradd (选项) 用户名

注意点:需要root权限

2.用户口令:passwd 用户名

注意点:

1. root用户可以更该所用用户密码,其余用户只能更改自己密码
2. 简单密码无法一次设置成功(回文,过短,过简单等),需要再次输入

3.修改用户: usermod 选项 用户名

选项:usermod可以查看选项,如 -l 表示修改登录名,-L锁定用户账号等

注意点:

修改用户名时如果该用户正在使用无法修改需要先退出当前用户

4.删除用户:userdel (选项) 用户名

注意点:

若用户正在使用无法直接删除需要先退出,可以用-r(删除主目录和邮件池)-m(强制删除) 用户名 强制删除

### 用户组相关:

1.创建用户组: groupadd (选项) 用户组名

注意点:需要root权限

2.修改用户组: groupmod (选项) 用户组名

![1634714980371](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634714980371.png)

groupmod -n 旧组名 新组名:更改组名

3.查询用户所属组: groups 用户名

注意点:如果未添加组,查询组名与用户名一致

4.删除用户组: groupdel 用户组名

### 用户组管理相关:

![1634715149293](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634715149293.png)

示例:添加/删除/指定管理员/..用户:gpasswd -a/-d/-A/.. 用户名 组名

### 系统管理相关:

##### 日期:

![1634715303685](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634715303685.png)

示例:

date:没有参数显示系统当前时间,以年月日星期时分秒时区展示

date -d "2020-12-12 11:11:11":展示字符串时间(格式与无参一致)

date -s "2020-12-12 11:11:11":设置系统时间

注意点:需要root权限

![1634715584646](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634715584646.png)

需要查看其他命令时可通过 date --help查看

##### 显示用户:

```
logname[--help][--vesion] 显示登录账号信息
--help:帮助
--vesion:显示版本信息
```

##### 切换用户:

```java
su [选项] 用户名
-c 命令:切换用户执行-c 之后的命令再切换回原用户
```

![1634715867813](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634715867813.png)

##### id命令:

```java
id:直接使用,查看用户所有的详细信息
```

![1634715998628](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634715998628.png)

##### sudo命令:

```java
sudo -u root 命令
使用root用户执行命令
```

![1634716051217](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634716051217.png)

### 进程相关:

###### top命令:监控进程

```jav
pid:进程id
user:进程所属用户
pr:进程优先级
ni:进程优先级(负数高优先级)
virt:虚拟内存消耗量
res:
s:进程状态,s睡眠r运行
command:进程名字
```

注意点:实时监控,按Q退出

![1634716230626](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634716230626.png)

top -c:实时显示所有进程信息(显示完整命令)

top -p 进程pid:监控特定线程

###### ps命令:显示进程

ps:显示当前时刻正在运行的进程信息

ps -A:显示所有进程的简单信息

ps -ef:显示所有进程的详细信息

ps -u 用户名:显示指定用户的进程信息

###### kill命令:

![1634716818012](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634716818012.png)

kill 进程PID：直接杀死进程

kill -9 进程PID:强制杀死进程

kill -9 $(ps -ef |grep 用户名):过滤指定用户的进程并全部强制杀死

killall -u 用户名:杀死指定用户全部进程

### 系统管理其他命令:

##### 关机和重启:

shutdown:立即或者1分钟后关机(根据OS版本)

shutdown -c:取消关机计划

shutdown -h now:立即关机

shutdown +n "警告信息":显示警告信息,并在n分钟后关机

shutdown -r +n "警告信息":显示警告信息,并在n分钟后重启

reboot:立即重启

## 目录管理命令:

##### ls命令:

![1634717419835](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634717419835.png)

ls -al:显示所有文件的详细信息

![1634717561875](C:\Users\gongm\AppData\Roaming\Typora\typora-user-images\1634717561875.png)

##### pwd命令

psw:查看当前所在目录

##### cd命令:

cd (相对路径或绝对路径) :切换目录

相对路径:相对当前目录而言(../表示上一层目录)

绝对路径:由根目录/开始

### 文件管理命令:

### mkdir命令:

mkdir [-p] 文件夹名称:在当前创建单层目录(-p确保目录名称存在,不存在的就建一个,用于直接创建多层文件夹)

### rmdir命令:

rmdir 文件夹名称:删除指定文件夹,只能删除非空文件夹

rmdir -p bbb/ccc:删除ccc,如果删完之后bbb也是空的,bbb也一起删除

### rm命令:

touch 文件名:创建文件

rm [选项] 文件 :删除文件(无法删除目录)

rm -r 目录:删除目录和目录中的所有内容

### 目录管理命令:

##### 复制cp命令:

cp [选项] 数据源 目的地:文件复制(只能拷贝文件)

![1634719166930](C:/Users/gongm/Desktop/img/1634719166930.png)

cp -r 数据源 目的地:文件及目录复制(cp -r a/* b)将a文件夹下全部拷贝到b文件夹中

##### 剪切mv命令:

mv [选项] 数据源 目的地   (同目录改名),移动到目的地

![1634719502659](C:/Users/gongm/Desktop/img/1634719502659.png)

![1634719530453](C:/Users/gongm/Desktop/img/1634719530453.png)

### 文件属性相关:

第一位描述文件:d目录,-文件,I链接文档;

2-10位权限描述:r可读,w可写,x可执行,-没有权限

2-4位属主权限,5-7位属组权限,8-10位:其他用户权限

![1634719942148](C:/Users/gongm/Desktop/img/1634719942148.png)

##### 更改属主,数组命令:

-chgrp [选项参数 -v:带提示语句] [所属群组] [文件或目录]:更改属主(需要root权限)

###### chown命令:

chown 属主名 文件名 :更改属主

chown [参数选项 -R处理目录下所有] 属主名:属组名 文件名 : 更改属主和属组

###### chmod命令:

更改权限:chmod [参数选项 -R] 数字/符号权限 文件或目录

1.数组修改: r-4,w-2,x-1,-:0;rwxrwxrwx:777

2.符号修改:u-属主 g-属组 o-其他全选 a-全部

![1634720712204](C:/Users/gongm/Desktop/img/1634720712204.png)

示例:chmod u=rwx,g=rx,o=r 文件

## 文件管理:

##### touch命令:

touch [参数选项] 文件名 (如果文件不存在,创建空文件,如果存在,修改最后修改时间属性)

批量创建:touch a{1..10}.txt 创建a1,a2..a10文件

stat 文件名:查看文件的详细信息

##### vi/vim(文本)编辑器:

![1634721589982](C:/Users/gongm/Desktop/img/1634721589982.png)

打开新建文件:

vim 文件名:如果文件已存在,会直接打开如果不存在会打开临时文件,保存退出后新建文件

编辑模式进入命令模式:

![1634721686380](C:/Users/gongm/Desktop/img/1634721686380.png)

命令模式:进入末行模式

![1634721744959](C:/Users/gongm/Desktop/img/1634721744959.png)

vim定位行:

vim 文件名 +n:打开文件,并将光标定位在n行



## 文本文件操作相关命令:

##### 查看命令:

cat [参数选项] 文件名:查看小文件内容，查看大文件无法显示全部

-n:显示行号

less [参数选项] 文件名:显示大文件内容，上下键查看未显示,q退出

-N:显示行号

head-n 文件名:查看文件的前一部分(与tail参数基本一致)

tail [参数选项]  文件名: 查看文件的最后十行

-n:显示最后n行,-f:动态显示最后十行(实时刷新,ctrl+c退出)

-n+a,从a行开始显示至最后一行,-c n ：显示最后n个字符

grep [关键词] 关键字 文件名 : 根据关键字搜索文本文件内容或进程

-n:显示行号,-i:忽略大小写,-v展示不包含结果

**ps -ef | grep 关键字 | grep -v  "grep"**

**展示包含关键字进程并不展示查找进程(自己)**

**ps -ef | grep -c 关键字 :统计包含关键字的进程数目**

##### 文件管理echo:

echo 字符串:展示文本

echo 字符串 > 文件名:将字符串写到文件中(覆盖文件中内容)

echo 字符串 >> 文件名:将字符串写到文件中(不覆盖文件中内容)

cat 不存在的目录 &>> 文件名 将命令的失败结果追加到指定文件后面

##### 软连接(快捷方式):

ln -s 目标文件路径  快捷方式路径

##### 查找find命令:

find [参数选项]<指定目录><指定条件><指定内容> 在指定目录下查找文件

. 当前文件，**适配符 : find . -name "*.*txt"查找当前目录的txt文件

<指定条件>:

-name 文件名 根据文件名查找

-ctime -n或+n :按时间来查找文件,-n指n天以内,+n指n天以前

### 备份压缩-gzip,gunzip命令:

##### gzip命令:(压缩不保留源文件,解压不保留解压文件)

gzip 文件名 压缩文件(不保留源文件)，*目录下文件全部单独压缩

注意点:压缩后文件无法二次压缩

gzip -dv * 解压文件并列出详细信息

##### gunzip命令:(压缩不保留源文件,解压不保留解压文件)

gunzip [参数] [文件]  解压文件(不保留压缩文件)

##### tar命令:

tar本身不具有压缩功能,调用压缩功能实现

tar [必要参数] [选择参数] [压缩/源文件] [源/压缩文件] :打包,压缩和解压(文件/文件夹)

![1634822408330](C:/Users/gongm/Desktop/img/1634822408330.png)

常用组合:

![1634822661354](C:/Users/gongm/Desktop/img/1634822661354.png)

##### zip,unzip命令:

zip [必要参数]  [选择参数] [文件] : 压缩

压缩后会**另外产生**.zip后缀的压缩文件

-q 不显示执行过程,-r递归:将指定目录下的所有文件及子目录一并处理

unzip [必要参数]  [选择参数] [文件] : 解压

只能解压.zip文件

-l:显示压缩文件内所包含的文件

-d<目录> 解压文件并存储到目录

### 网络命令:

##### ifconfig:展示网络设置信息;

ifconfig 网卡名 down/up:关闭/开启指定网卡(需要root权限)

ifconfig 网卡名 ip地址:更改指定网卡的ip地址

ifconfig 网卡名 ip地址 netmask 255.255.255.0:更改指定网卡的ip地址和子网掩码

ping [参数选项] ip地址或网址:测试是否连通

-c n:指定n次测试

netstat [参数选项]:显示网络状态

-a:显示系统中详细的连接情况

-i:显示网卡列表

##### 安装插件:

yum:需要root权限

![1635045314360](img/1635045314360.png)

更改yum源:

yum -y install wget:下载下载工具

cd /etc/yum.repos.d/

mv CentOS-Base.rep CentOS-Base.repo.back :备份yum设置

wget -O CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo:更改下载源为阿里云

yum clean all:清除原yum缓存

yum makecache:加载当前yum缓存

