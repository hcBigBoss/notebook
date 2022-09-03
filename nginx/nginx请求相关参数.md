# nginx请求相关参数

 原创

[kq1983](https://blog.51cto.com/u_330478)2021-08-25 09:49:05博主文章分类：[nginx](https://blog.51cto.com/u_330478/category43)©著作权

***文章标签\*[lua](https://blog.51cto.com/topic/lua.html)[显式](https://blog.51cto.com/topic/xianshi6.html)[json](https://blog.51cto.com/topic/json.html)[html](https://blog.51cto.com/topic/html.html)[客户端](https://blog.51cto.com/topic/kehuduan.html)*****文章分类\*[Nginx](https://blog.51cto.com/nav/nginx)[服务器](https://blog.51cto.com/nav/server)*****阅读数\**\*689\****

\1. 获取param参数

```has
local arg = ngx.req.get_uri_args();

for k,v in pairs(arg) do
    ngx.log(ngx.WARN,"key=",k," val=",v);
end1.2.3.4.5.
```

\2. 读取body

```has
-- 解析 body 参数之前一定要先读取 body
ngx.req.read_body()
local arg = ngx.req.get_post_args();

for k,v in pairs(arg) do
    ngx.log(ngx.WARN,"bkey=",k," bval=",v);
end1.2.3.4.5.6.7.
-- 解析 body 参数之前一定要先读取 body ngx.req.read_body()
-- 这个读取application/json
local data = ngx.req.get_body_data()
ngx.log(ngx.WARN,"data=",data)1.2.3.4.
```

\3. 下载

> ```html
> location /download {
>       root   html;
> #      content_by_lua_file luademo/download.lua;
>       access_by_lua_block {
>         ngx.var.limit_rate = 10000  #10K
>       }
>     }1.2.3.4.5.6.7.
> ```

 

wget http://192.168.6.170:10000/download/a.zip

sleep

> ```html
> location /testsleep {
>   content_by_lua_block {
>     ngx.say("hello")
>     ngx.sleep(3)
>     ngx.say("the world")
>   }
> }
> 
> location /test2 {
>   content_by_lua_block {
>     ngx.say("hello")
>     ngx.flush() -- 显式的向客户端刷新先响应输出
>     ngx.sleep(3)
>     ngx.say("the world")
>   }
> }
> ```
