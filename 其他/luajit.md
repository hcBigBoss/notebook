# Mac安装LuaJIT



https://www.cnblogs.com/helios-fz/p/15697785.html

到 LuaJIT 官网 http://luajit.org/download.html，查看当前最新开发版本。

顺序执行如下命令安装 LuaJIT：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
# wget http://luajit.org/download/LuaJIT-2.1.0-beta1.tar.gz
# tar -xvf LuaJIT-2.1.0-beta1.tar.gz
# cd LuaJIT-2.1.0-beta1
# make
# sudo make install
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

[![img](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141309485-1939352359.png)](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141309485-1939352359.png)

执行到 make 的时候，我的机器上报了一个错：

[![img](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141405351-1030320795.png)](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141405351-1030320795.png)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
  "__Unwind_DeleteException", referenced from:
      _lj_err_unwind_dwarf in libluajit.a(lj_err.o)
  "__Unwind_GetCFA", referenced from:
      _lj_err_unwind_dwarf in libluajit.a(lj_err.o)
  "__Unwind_RaiseException", referenced from:
      _lj_err_throw in libluajit.a(lj_err.o)
  "__Unwind_SetGR", referenced from:
      _lj_err_unwind_dwarf in libluajit.a(lj_err.o)
  "__Unwind_SetIP", referenced from:
      _lj_err_unwind_dwarf in libluajit.a(lj_err.o)
ld: symbol(s) not found for architecture x86_64
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这里需要手动设置一个安装变量 MACOSX_DEPLOYMENT_TARGET ，值为当前Mac操作系统版本

[![img](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141610960-1976174386.png)](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141610960-1976174386.png) 

```
export MACOSX_DEPLOYMENT_TARGET=12.0.1
```

之后再执行 make 命令就可以通过了。

[![img](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141703118-481001171.png)](https://img2020.cnblogs.com/blog/727602/202112/727602-20211216141703118-481001171.png)

由于LuaJIT 2.1 目前还是beta版本，所以在make install后，并没有进行luajit的符号连接，可以执行下面的指令将luajit-2.1.0-beta1和luajit进行软连接，从而可以直接使用luajit命令

```
ln -sf luajit-2.1.0-beta1 /usr/local/bin/luajit
```

验证是否安装成功

```
luajit -v
```

 
