1.路径配置的分类
在nginx中，一共有4种不同的路径配置方法

```
= - Exact match
^~ - Preferential match
~ && ~* - Regex match
no modifier - Prefix match
```



#路径完全一样则匹配

```
location = path {

}

#路径开头一样则匹配
location ^~ path{

}

#正则匹配，大小写敏感
location ~ path{

}

#正则匹配，大小写不敏感
location ~* path{

}

#前缀匹配
location path{

}
```



上面的执行顺序是，优先查看Exact match,若存在，则停止。如不存在，则进入Preferential match。之后在进入Regex match,先看大小写敏感的规则，再看大小写不敏感的规则.最后进入Prefix match.

= --> ^~ --> ~ --> ~* --> no modifier

在每一个同类型的匹配规则中，按照他们出现在配置文件中的先后，一一对比。

2.例子

```
location /match {  
  return 200 'Prefix match: will match everything that starting with /match';  
}  

location ~* /match[0-9] {  
  return 200 'Case insensitive regex match';  
}  

location ~ /MATCH[0-9] {  
  return 200 'Case sensitive regex match';  
}  

location ^~ /match0 {  
  return 200 'Preferential match';  
}  

location = /match {  
  return 200 'Exact match';  
}  

/match     # => 'Exact match'  
/match0    # => 'Preferential match'  
/match1    # => 'Case insensitive regex match'  
/MATCH1    # => 'Case sensitive regex match'  
/match-abc # => 'Prefix match: matches everything that starting with /match'  
```

————————————————
版权声明：本文为CSDN博主「我是Oliver啊」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/lihaotong10/article/details/122798797
