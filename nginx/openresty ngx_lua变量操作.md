#### openresty ngx_lua变量操作

​        

​                

https://blog.csdn.net/weixin_43931625/article/details/125605357                              

变量操作
         

##### nginx 自定义变量：ngx.var.name

location / {
    set $a "";

    rewrite_by_lua_block {
        ngx.var.a = "1";
    }
}
             

##### http 请求头中的变量：ngx.var.http_name

location / {
    set $b "";

    rewrite_by_lua_block {
        ngx.var.b = ngx.var.http_user_agent;  #读取请求头中的User_agent，将其赋值给b
    }
}
          

##### cookie中的变量：ngx.var.cookie_name

location / {
    set $b "";

    rewrite_by_lua_block {
        ngx.var.b = ngx.var.cookie_age;  #读取cookie中的age，将其赋值给b
    }
}
            

##### http 路径中的参数：ngx.var.arg_name

location / {
    set $c "";

    rewrite_by_lua_block {
        ngx.var.c = ngx.var.arg_age;  #读取路径参数中的age参数，将其赋值给c
    }
}
           

正则表达式捕获的变量：ngx.var[n]，n从1开始

location /([a-z]+)/test {
    set $d "";
    echo $1;   # nginx使用$1读取正则表达式捕获的变量

    rewrite_by_lua_block {
        ngx.var.d = ngx.var[1];  #读取正则表达式捕获的第一个变量，将其赋值给d
    }
}
             

​                 

​                              

使用示例
         

default.conf

server {
    listen       80;
    server_name  localhost;

    location / {
        root   /usr/local/openresty/nginx/html;
        index  index.html index.htm;
    }
     
    location ~ /test/([a-z]+) {
        set $a "";
        set $b "";
        set $c "";
        set $d "";
        set $e "";
     
        rewrite_by_lua_block {
            local ngx = require "ngx";
            ngx.var.a = "1";
            ngx.var.b = ngx.var.http_name;
            ngx.var.c = ngx.var.arg_name;
            ngx.var.d = ngx.var[1];
            ngx.var.e = ngx.var.cookie_age;
        }
     
        echo $a $b $c $d $e;
    }
     
    #error_page  404              /404.html;
     
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/local/openresty/nginx/html;
    }

}
       

创建容器

docker run -it -d -p 2000:80 \
-v /Users/huli/lua/openresty/conf7/default.conf:/etc/nginx/conf.d/default.conf \
--name open3 lihu12344/openresty
          

使用测试

