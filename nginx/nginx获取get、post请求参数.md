https://blog.csdn.net/liuxiao723846/article/details/117755966

#### 1、获取get请求的参数

可以通过以下几种方法：

在nginx配置中，通过$arg_XXX获得单个参数XXX的值
在 ngx_lua 中，通过ngx.var.arg_XXX获得单个参数XXX的值
在 ngx_lua 中，通过ngx.req.get_uri_args()获取所有GET请求的参数和值，返回值是一个table结构（key是参数名，value是参数值）
说明：在openresty的lua中使用原生nginx的变量，前面需要添加ngx.var，例如获取$args变量值的方法是ngx.var.args

注意：ngx.var.arg_xx与ngx.req.get_uri_args["xx"]的区别：当请求uri中有多个同名参数时，ngx.var.arg_xx返回第一个出现的值，ngx.req_get_uri_args["xx"]返回一个table，里存放了该参数的所有值。因此，ngx.req.get_uri_args属于ngx.var.arg_的增强。

看一个例子：

location  /ztest3 {
      root    html;
      index   index.html index.htm index.php;
      proxy_redirect      off;
      proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header    X-Real-IP $remote_addr;
      proxy_set_header    Host $http_host;
      proxy_http_version  1.1;
      proxy_set_header    Connection "";

      access_by_lua_block {
          if ngx.var.request_method == "GET" then
              local _a = ngx.var.arg_a
              ngx.log(ngx.ERR,"args:a".._a)
     
              local g_args = ngx.req.get_uri_args()
              local _args = {}
              for k,v in pairs(g_args) do
                  if type(v) == "table" then
                      table.insert(_args,k.."="..table.concat(v,"|"))
                  else
                      table.insert(_args,k.."="..v)
                  end
              end
          end
          ngx.log(ngx.ERR,"args:"..table.concat(_args,"&"))
      }
      proxy_pass          http://z_worker;
}

输入：http://.../ztest3?a=1&b=2&b=3&a=0

输出：

args:a=1

args:b=2|3&a=1|0

#### 2、获取post请求的参数

通过nginx内建变量$request_body获取post请求体数据（get的请求体是空），即所有请求参数和值。但需要注意：The variable’s value is made available in locations processed by the proxy_pass, fastcgi_pass, uwsgi_pass, and scgi_pass directives when the request body was read to a memory buffer.（意思是只有location中用到proxy_pass,fastcgi_pass,scgi_pass命令时，该变量才有值）

也可以在lua中使用ngx.req.get_body_data()、ngx.req.get_post_args()来获取post请求体数据（同样get的为空），二者的区别：前者返回字符串，后者返回table结构（key是参数名，value是参数值）。

注：对于post的请求，我们无法通过$arg_XXX这种方式获取参数值；

注：对于post请求，get_body_data()和get_post_args()这两个方法都无法获取到url后面跟的参数；要获取post请求的url后面参数，只能通过ngx.var.args变量来获取。

注：在使用ngx.req.get_body_data()、ngx.req.get_post_args()前需要先调用ngx.req.read_body()，或者通过配置lua_need_request_body on 指令，否则会报如下错误，no request body found; maybe you should turn on lua_need_request_body

1）get_body_data例子：

location  /ztest3 {
      root    html;
      index   index.html index.htm index.php;
      proxy_redirect      off;
      proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header    X-Real-IP $remote_addr;
      proxy_set_header    Host $http_host;
      proxy_http_version  1.1;
      proxy_set_header    Connection "";

      access_by_lua_block {
          local req_body = ngx.req.get_body_data()
          ngx.log(ngx.ERR,"body:"..req_body) --a=1&b=2
      }
      proxy_pass          http://z_worker;
}

说明：get_body_data返回的是一个字符串。

2）get_post_args示例：

location  /ztest3 {
      root    html;
      index   index.html index.htm index.php;
      proxy_redirect      off;
      proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header    X-Real-IP $remote_addr;
      proxy_set_header    Host $http_host;
      proxy_http_version  1.1;
      proxy_set_header    Connection "";

      access_by_lua_block {
          if ngx.var.request_method == "POST" then
              ngx.req.read_body()
     
              local p_args = ngx.req.get_post_args()
              local _args = {}
              for k,v in pairs(p_args) do
                  if type(v) == "table" then
                      table.insert(_args,k.."="..table.concat(v,"|"))
                  else
                      table.insert(_args,k.."="..v)
                  end
              end
          end
          ngx.log(ngx.ERR,"args:"..table.concat(_args,"&"))
      }
      proxy_pass          http://z_worker;
}
3）对于post请求，如何获取url后的参数？

location  /ztest3 {
      root    html;
      index   index.html index.htm index.php;
      proxy_redirect      off;
      proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header    X-Real-IP $remote_addr;
      proxy_set_header    Host $http_host;
      proxy_http_version  1.1;
      proxy_set_header    Connection "";

      access_by_lua_block {
          function mysplit (inputstr, sep)
             if sep == nil then
                sep ="%s"
             end
             local t={}
             for str in string.gmatch(inputstr,"([^"..sep.."]+)") do
                table.insert(t, str)
             end
             return t
          end
     
          if ngx.var.request_method == "POST" then
              ngx.req.read_body()
              --post args
              local p_args = ngx.req.get_post_args()
              
              --url args
              local g_args = ngx.var.args
              local gg_args = {} --将url args放到table中，key是参数名，val是参数值
              if g_args and #g_args > 0 then
                  local g_args_tab = mysplit(g_args,"&")
                  for k,v in pairs(g_args_tab) do
                    local kv = mysplit(v,"=")
                    gg_args[kv[1]]=kv[2]
                  end
              end
              local _args = {}
              for k,v in pairs(gg_args) do
                  p_args[k] = v
              end
     
              local _args = {}
              for k,v in pairs(p_args) do
                  if type(v) == "table" then
                      table.insert(_args,k.."="..table.concat(v,"|"))
                  else
                      table.insert(_args,k.."="..v)
                  end
              end
          end
          ngx.log(ngx.ERR,"args:"..table.concat(_args,"&"))
      }
      proxy_pass          http://z_worker;
}
4）一点说明：

通过ngx.req.get_uri_args()和ngx.req.get_post_args()方法获取get、post请求的参数时，返回的都是一个table结构，key是参数名，value是参数值，相当于一个map结构，对于这种table，无法使用table.concat方法来输出，所以，通常是定义一个table数组，然后是用for遍历参数table，拼接好key和value放到数组中，最后再通过table.concat方法输出数组；例如：

local g_args = ngx.req.get_uri_args()
    local _args = {}
    for k,v in pairs(g_args) do
        if type(v) == "table" then
            table.insert(_args,k.."="..table.concat(v,"|"))
        else
            table.insert(_args,k.."="..v)
        end
    end
end
ngx.log(ngx.ERR,"args:"..table.concat(_args,"&"))
