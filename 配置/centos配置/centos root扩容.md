树莓派安装centos7之后发现根目录只有2G的容量，UP主的SD卡是127G。下面演示调整到127G（最大）的方法。

#### 第一步：

查看一下系统挂载信息：

[root@bogon ~]# df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/root       2.0G  1.4G  621M  69% /
devtmpfs        424M     0  424M   0% /dev
tmpfs           457M     0  457M   0% /dev/shm
tmpfs           457M   12M  445M   3% /run
tmpfs           457M     0  457M   0% /sys/fs/cgroup
/dev/mmcblk0p1  286M   54M  233M  19% /boot
tmpfs            92M     0   92M   0% /run/user/0

查看一下磁盘分区情况，总容量为15.9G。

[root@bogon ~]# fdisk -l
磁盘 /dev/mmcblk0：127.9 GB, 127865454592 字节，127865454592 个扇区
Units = 扇区 of 1 * 512 = 512 bytes
扇区大小(逻辑/物理)：512 字节 / 512 字节
I/O 大小(最小/最佳)：512 字节 / 512 字节
磁盘标签类型：dos
磁盘标识符：0x00024e4f

        设备 Boot      Start         End      Blocks   Id  System
/dev/mmcblk0p1   *        8192      593919      292864    c  W95 FAT32 (LBA)
/dev/mmcblk0p2          593920     1593343      499712   82  Linux swap / Solaris
/dev/mmcblk0p3         1593344     249737182    124071919   83  Linux

#### 第二步：扩展容量，使用的命令是 rootfs-expand

看下命令的路径

[root@bogon ~]# which rootfs-expand
/usr/bin/rootfs-expand

命令行输入 rootfs-expand 回车

[root@bogon ~]# rootfs-expand

/dev/mmcblk0p3 /dev/mmcblk0 3
Extending partition 3 to max size ....
CHANGED: partition=3 start=1593344 old: size=4296704 end=5890048 new: size=29522911 end=31116255
Resizing ext4 filesystem ...
resize2fs 1.42.9 (28-Dec-2013)
Filesystem at /dev/mmcblk0p3 is mounted on /; on-line resizing required
old_desc_blocks = 1, new_desc_blocks = 2
The filesystem on /dev/mmcblk0p3 is now 3690363 blocks long.

Done.

再次查看磁盘分区情况

[root@bogon ~]# fdisk -l
Disk /dev/mmcblk0: 127.9 GB, 127865454592 bytes, 249737216 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x00024e4f

        Device Boot      Start         End      Blocks   Id  System
/dev/mmcblk0p1   *        8192      593919      292864    c  W95 FAT32 (LBA)
/dev/mmcblk0p2          593920     1593343      499712   82  Linux swap / Solaris
/dev/mmcblk0p3         1593344    249737182    124071919+  83  Linux

查看挂载信息

[root@bogon ~]# df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/root        14G  1.4G   13G  10% /
devtmpfs        424M     0  424M   0% /dev
tmpfs           457M     0  457M   0% /dev/shm
tmpfs           457M   12M  445M   3% /run
tmpfs           457M     0  457M   0% /sys/fs/cgroup
/dev/mmcblk0p1  286M   54M  233M  19% /boot
tmpfs            92M     0   92M   0% /run/user/0

可以看到容量已经从原来的2G扩展到了129G

如果输入 rootfs-expand命令后出现以下情况，没有成功扩展分区

/dev/mmcblk0p3 /dev/mmcblk0 3
Extending partition 3 to max size ....
unexpected output in sfdisk --version [sfdisk，来自 util-linux 2.23.2]
Resizing ext4 filesystem ...
resize2fs 1.42.9 (28-Dec-2013)
The filesystem is already 537088 blocks long.  Nothing to do!

可能是因为系统设置了中文导致，将 LANG 改为 en_US.UTF-8 就可以

#### 执行LANG=en_US.UTF-8 命令后再执行一遍 rootfs-expand 就能扩展成功

————————————————
版权声明：本文为CSDN博主「相貌平平李天尊」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_24088107/article/details/124441388