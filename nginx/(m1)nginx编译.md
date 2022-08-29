#### 报错处理

https://github.com/openresty/lua-resty-core/issues/248

#### Nginx添加Lua模块和优化配置

https://blog.csdn.net/budongfengqing/article/details/117925430



#### 环境变量的坑

https://blog.csdn.net/weixin_39730587/article/details/111281943

最后无奈，我将这两个变量直接写死在lua-nginx-module的config中，即：

https://www.bbsmax.com/A/RnJWrm7Ezq/



# OpenResty

https://github.com/openresty

下载release版本

<img src="/Users/hcbigboss/Library/Application Support/typora-user-images/image-20220828173814680.png" alt="image-20220828173814680" style="zoom:50%;" />

```sh

 ./configure --prefix=/Users/hcbigboss/nginx \
--sbin-path=/Users/hcbigboss/nginx/nginx/nginx \
--conf-path=/Users/hcbigboss/nginx/conf/nginx.conf \
--pid-path=/Users/hcbigboss/nginx/nginx/nginx.pid \
--with-ld-opt="-Wl,-rpath,/usr/local/LuaJIT/lib" \
--with-http_ssl_module \
--with-pcre=/Users/hcbigboss/work/pcre-8.40 \
--with-zlib=/Users/hcbigboss/work/zlib-1.2.12 \
--with-openssl=/Users/hcbigboss/work/openssl-3.0.5 \
--add-module=/Users/hcbigboss/work/ngx-devel-kit-0.3.1 \
--add-module=/Users/hcbigboss/work/lua-nginx-module-0.10.14


export LUAJIT_LIB=/usr/local/lib \
export LUAJIT_INC=/usr/local/include/luajit-2.1



// -------------推荐------------- OpenResty 
 ./configure \
 --with-pcre=/Users/hcbigboss/work/pcre-8.40 \
--with-zlib=/Users/hcbigboss/work/zlib-1.2.12 \
--with-openssl=/Users/hcbigboss/work/openssl-3.0.5
```

