# openResty中ngx_lua模块提供的指令

ngx_lua模块的原理：

1. 每个worker（工作进程）创建一个Lua VM，worker内所有协程共享VM；
2. 将Nginx I/O原语封装后注入 Lua VM，允许Lua代码直接访问；
3. 每个外部请求都由一个Lua协程处理，协程之间数据隔离；
4. Lua代码调用I/O操作等异步接口时，会挂起当前协程（并保护上下文数据），而不阻塞worker；
5. I/O等异步操作完成时还原相关协程上下文数据，并继续运行

**系列文章：**
指令：[openResty中ngx_lua模块提供的指令
](http://www.hangdaowangluo.com/archives/2686)常量：[openResty中ngx_lua模块提供的常量
](http://www.hangdaowangluo.com/archives/2692)API：[openResty中ngx_lua模块提供的API](http://www.hangdaowangluo.com/archives/2694)



# 要学会openResty必须要熟记的一张图，熟记！熟记！！熟记 ！！！

![77d1c09e-1a37-11e6-97ef-d9767035fc3e](./assert/77d1c09e-1a37-11e6-97ef-d9767035fc3e.png)

# 一、指令

### **1.1 \*lua_capture_error_log\* <未知>**

**语法:** *lua_capture_error_log size
***默认:** *none
***模块:** *http*

### 1.2 lua_use_default_type

**语法:** *lua_use_default_type on | off
***默认:** *lua_use_default_type on
***模块:** *http, server, location, location if*

用来启用/禁止,ngx_lua使用default_type指定的Content-Type的值。

### 1.3 lua_malloc_trim **<未知>**

**语法:** *lua_malloc_trim <request-count>
***默认:** *lua_malloc_trim 1000
***模块:** *http*

### 1.4 lua_code_cache

**语法:** *lua_code_cache on | off
***默认:** *lua_code_cache on
***模块:** *http, server, location, location if*

启用/禁止缓存，在`*_by_lua_file指令中的lua代码；例如（set_by_lua_file , content_by_lua_file, rewrite_by_lua_file, access_by_lua_fil）`缓存开启时修改LUA代码需要重启nginx,不开启时则不用。开发阶段一般关闭缓存。生产环境必需开启，对性能的影响较大。

### 1.5 lua_regex_cache_max_entries

**语法:** *lua_regex_cache_max_entries <num>
***默认:** *lua_regex_cache_max_entries 1024
***模块:** *http*

用于限制 缓存 ngx.re.match, ngx.re.gmatch, ngx.re.sub 和 ngx.re.gsub中带 -o 的正则表达式的数量。如果缓存的正则表达式超过该数量，则在日志中有warn的日志报警。

### 1.6 lua_regex_match_limit **<未知>**

**语法:** *lua_regex_match_limit <num>
***默认:** *lua_regex_match_limit 0
***模块:** *http*

### 1.7 lua_package_path

**语法:** *lua_package_path <lua-style-path-str>
***默认:** *The content of LUA_PATH environment variable or Lua’s compiled-in defaults.
***模块:** *http*

用Lua写的lua外部库路径（.lua文件）

例如：

```
lua_package_path "/data/www/openresty/resty/?.lua;/data/www/openresty/lor/?.lua;;";
```

**tips 01 :此处定义了lua require搜索.lua文件的跟目录。**

### 1.8 lua_package_cpath

**语法:** *lua_package_cpath <lua-style-cpath-str>
***默认:** *The content of LUA_CPATH environment variable or Lua’s compiled-in defaults.
***模块:** *http*

用C写的lua外部库路径（.so文件）

例如：

```
lua_package_cpath "/data/www/openresty/resty/?.so;/data/www/openresty/lor/?.so;;";
```

**tips 01 :此处定义了lua require搜索.so文件的跟目录。**

### 1.9 init_by_lua_block、init_by_lua_file

**语法:** *init_by_lua_block { lua-script }
***语法:** **init_by_lua_file <path-to-lua-script-file>\*
***模块:** *http
***运行阶段:** *loading-config*

master进程启动时挂载的lua代码。

### 1.10 init_worker_by_lua_block、init_worker_by_lua_file

**语法:** *init_worker_by_lua_block { lua-script }
***语法:** **init_worker_by_lua_file <lua-file-path>\*
***模块:** *http
***运行阶段:** *starting-worker*

worker进程启动时挂载的lua代码。

### 1.11 set_by_lua_block、set_by_lua_file

**语法:** *set_by_lua_block $res { lua-script }
***语法:** *set_by_lua_file $res <path-to-lua-script-file> [$arg1 $arg2 …]**
***模块:** *server, server if, location, location if*

设置变量。

### 1.12 content_by_lua_block、content_by_lua_file

**语法:** *content_by_lua_block { lua-script }***
语法:** *content_by_lua_file <path-to-lua-script-file>***
模块:** *location, location if*

内容处理器，接收请求处理并输出响应，content_by_lua_block直接在nginx配置文件里编写较短Lua代码后者使用lua文件.

### 1.13 rewrite_by_lua_block、rewrite_by_lua_file **<未知>**

**语法:** *rewrite_by_lua_block { lua-script }*
**语法:** *rewrite_by_lua_file <path-to-lua-script-file>*
**模块:** *http, server, location, location if*
**执行阶段:** *rewrite tail*

执行内部URL重写或者外部重定向，典型的如伪静态化的URL重写。其默认执行在rewrite处理阶段的最后.
注意，在使用rewrite_by_lua_*时，开启rewrite_log on;后也看不到相应的rewrite log。

### **1.14** access_by_lua_block、access_by_lua_file

**语法:** *access_by_lua_block { lua-script }
***语法:** *access_by_lua_file <path-to-lua-script-file>**
***模块:** *http, server, location, location if
***执行阶段:** *access tail*

用于访问控制。

### 1.15 header_filter_by_lua_block、header_filter_by_lua_file

**语法:** *header_filter_by_lua_block { lua-script }
***语法:** *header_filter_by_lua_file <path-to-lua-script-file>**
***模块:** *http, server, location, location if
***执行阶段:** *output-header-filter*

### 1.16 body_filter_by_lua_block、body_filter_by_lua_file

**语法:** *body_filter_by_lua_block { lua-script-str }
***语法:** *body_filter_by_lua_file <path-to-lua-script-file>**
***模块:** *http, server, location, location if
***执行阶段:** *output-body-filter*

### 1.17 log_by_lua_block、log_by_lua_file

**语法:** *log_by_lua_block { lua-script }***
语法:** *log_by_lua_file <path-to-lua-script-file>**
***模块:** *http, server, location, location if***
执行阶段:** *log*

### 1.18 balancer_by_lua_block、balancer_by_lua_file

**语法:** *balancer_by_lua_block { lua-script }
***语法:** *balancer_by_lua_file <path-to-lua-script-file>**
***模块:** *upstream
***执行阶段:** *content*

### 1.19 lua_need_request_body

**语法:** *lua_need_request_body <on|off>
***默认:** *off
***模块:** *http, server, location, location if
***执行阶段:** *depends on usage*

### 1.20 ssl_certificate_by_lua_block、ssl_certificate_by_lua_file

**语法:** *ssl_certificate_by_lua_block { lua-script }
***语法:** *ssl_certificate_by_lua_file <path-to-lua-script-file>**
***模块:** *server
***执行阶段:** *right-before-SSL-handshake*

### 1.21 ssl_session_fetch_by_lua_block、ssl_session_fetch_by_lua_file

**语法:** *ssl_session_fetch_by_lua_block { lua-script }
***语法:** *ssl_session_fetch_by_lua_file <path-to-lua-script-file>**
***模块:** *http
***执行阶段:** *right-before-SSL-handshake*

### 1.22 ssl_session_store_by_lua_block、ssl_session_store_by_lua_file

**语法:** *ssl_session_store_by_lua_block { lua-script }
***语法:** *ssl_session_store_by_lua_file <path-to-lua-script-file>**
***模块:** *http
***执行阶段:** *right-after-SSL-handshake*

### 1.23 lua_shared_dict

**语法:** *lua_shared_dict <name> <size>
***默认:** *no
***模块:** *http
***执行阶段:** *depends on usage*

### 1.24 lua_socket_connect_timeout、lua_socket_send_timeout、lua_socket_read_timeout

**语法:** *lua_socket_connect_timeout <time>***
语法:** *lua_socket_send_timeout <time>***
语法:** *lua_socket_read_timeout <time>
***默认:** *lua_socket_connect_timeout 60s***
默认:** *lua_socket_send_timeout 60s***
默认:** *lua_socket_read_timeout 60s
***模块:** *http, server, location*

### 1.25 lua_socket_send_lowat

**语法:** *lua_socket_send_lowat <size>
***默认:** *lua_socket_send_lowat 0
***模块:** *http, server, location*

### 1.26 lua_socket_buffer_size

**语法:** *lua_socket_buffer_size <size>
***默认:** *lua_socket_buffer_size 4k/8k
***模块:** *http, server, location*

### 1.27 lua_socket_pool_size

**语法:** *lua_socket_pool_size <size>
***默认:** *lua_socket_pool_size 30
***模块:** *http, server, location*

### 1.28 lua_socket_keepalive_timeout

**语法:** *lua_socket_keepalive_timeout <time>
***默认:** *lua_socket_keepalive_timeout 60s
***模块:** *http, server, location*

### 1.29 lua_socket_log_errors

**语法:** *lua_socket_log_errors on|off
***默认:** *lua_socket_log_errors on
***模块:** *http, server, location*

### 1.30 lua_ssl_ciphers

**语法:** *lua_ssl_ciphers <ciphers>
***默认:** *lua_ssl_ciphers DEFAULT
***模块:** *http, server, location*

### 1.31 lua_ssl_crl

**语法:** *lua_ssl_crl <file>
***默认:** *no
***模块:** *http, server, location*

### 1.32 lua_ssl_protocols

**语法:** *lua_ssl_protocols [SSLv2] [SSLv3] [TLSv1] [TLSv1.1] [TLSv1.2]
***默认:** *lua_ssl_protocols SSLv3 TLSv1 TLSv1.1 TLSv1.2
***模块:** *http, server, location*

### 1.33 lua_ssl_trusted_certificate

**语法:** *lua_ssl_trusted_certificate <file>
***默认:** *no
***模块:** *http, server, location*

### 1.34 lua_ssl_verify_depth

**语法:** *lua_ssl_verify_depth <number>
***默认:** *lua_ssl_verify_depth 1
***模块:** *http, server, location*

### 1.35 lua_http10_buffering

**语法:** *lua_http10_buffering on|off
***默认:** *lua_http10_buffering on
***模块:** *http, server, location, location-if*

### 1.36 rewrite_by_lua_no_postpone

**语法:** *rewrite_by_lua_no_postpone on|off
***默认:** *rewrite_by_lua_no_postpone off
***模块:** *http*

### 1.37 access_by_lua_no_postpone

**语法:** *access_by_lua_no_postpone on|off
***默认:** *access_by_lua_no_postpone off
***模块:** *http*

### 1.38 lua_transform_underscores_in_response_headers

**语法:** *lua_transform_underscores_in_response_headers on|off
***默认:** *lua_transform_underscores_in_response_headers on
***模块:** *http, server, location, location-if*

### 1.39 lua_check_client_abort

**语法:** *lua_check_client_abort on|off
***默认:** *lua_check_client_abort off
***模块:** *http, server, location, location-if*

### 1.40 lua_max_pending_timers

**语法:** *lua_max_pending_timers <count>
***默认:** *lua_max_pending_timers 1024
***模块:** *http*

### 1.41 lua_max_running_timers

**语法:** *lua_max_running_timers <count>
***默认:** *lua_max_running_timers 256
***模块:** *http*
