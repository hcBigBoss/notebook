Nginx中，set $para $1，$1表示路径中正则表达式匹配的第一个参数。

https://blog.csdn.net/cbmljs/article/details/86573248

以下是一个示例，用以实验$1,$2。如：

location ~/abc/(.*)/(.*) {

    set $para1 $1
    set $para2 $2
    content_by_lua_block {
    ngx.say(ngx.var.para1)
    ngx.say(ngx.var.para2)
    }
}

此时，若访问路径为localhost:8080/abc/qwe/asd时，则浏览器会输出

qwe

asd





今天在公司做伪静态的时候，遇到了一些疑惑，特别是针对$1,2这个含义

rewrite ^/(news_\d)/(\d).html$ https://$host/?$1 permanent;
上面是我写的重写规则，先说$代表的是参数，所以一定是（）包含的

$1就是 news_\d

$2就是 \d

举个例子

https://www.zhubanxian.com/news_1/2.html
z这里$1 就是news_1

$2就是2
————————————————
版权声明：本文为CSDN博主「zhuwei_clark」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/zhuwei_clark/article/details/105417047
