https://www.yht7.com/news/192281

##### 目录

- JWT简介

- - 1.什么是JWT

  - 2.为什么要用JWT

  - - 2.1 传统Session认证的弊端
    - 2.2 JWT认证的优势

- JWT结构

- - 1.Header
  - 
  - 2.Payload
  - 
  - 3.Signature

- JWT的种类

- - 1.nonsecure JWT
  - 
  - 2.JWS

- Java中使用JWT

- - 1.java-jwt

  - - 1.1 对称签名
    - 1.2 非对称签名

  - 2.jjwt-root

  - - 2.1 对称签名
    - 2.2 非对称签名

- 实际开发中的应用




## JWT简介



### 1.什么是JWT

在介绍JWT之前，我们先来回顾一下利用`token`进行用户身份验证的流程：

- 客户端使用用户名和密码请求登录
- 服务端收到请求，验证用户名和密码
- 验证成功后，服务端会签发一个`token`，再把这个token返回给客户端
- 客户端收到token后可以把它存储起来，比如放到`cookie`中
- 客户端每次向服务端请求资源时需要携带服务端签发的`token`，可以在cookie或者header中携带
- 服务端收到请求，然后去验证客户端请求里面带着的`token`，如果验证成功，就向客户端返回请求数据

这种基于`token`的认证方式相比传统的`session`认证方式更节约服务器资源，并且对移动端和分布式更加友好。其优点如下：

- **支持跨域访问**：`cookie`是无法跨域的，而`token`由于没有用到`cookie`(前提是将`token`放到请求头中)，所以跨域后不会存在信息丢失问题
- **无状态**：`token`机制在服务端不需要存储`session`信息，因为token自身包含了所有登录用户的信息，所以可以减轻服务端压力
- **更适用CDN**：可以通过内容分发网络请求服务端的所有资料**更**
- **适用于移动端**：当客户端是非浏览器平台时，`cookie`是不被支持的，此时采用`token`认证方式会简单很多
- **无需考虑CSRF**：由于不再依赖`cookie`，所以采用token认证方式不会发生CSRF，所以也就无需考虑CSRF的防御

而`JWT`就是上述流程当中`token`的一种具体实现方式，其全称是`JSON Web Token`，官网地址：https://jwt.io/

通俗地说，**JWT的本质就是一个字符串**，它是将用户信息保存到一个Json字符串中，然后进行编码后得到一个`JWT token`，**并且这个`JWT token`带有签名信息，接收后可以校验是否被篡改**，所以可以用于在各方之间安全地将信息作为Json对象传输。JWT的认证流程如下：

- 首先，前端通过Web表单将自己的用户名和密码发送到后端的接口，这个过程一般是一个`POST`请求。建议的方式是通过SSL加密的传输(HTTPS)，从而避免敏感信息被嗅探
- 后端核对用户名和密码成功后，**将包含用户信息的数据作为JWT的Payload，将其与JWT Header分别进行Base64编码拼接后签名**，形成一个`JWT Token`，形成的`JWT Token`就是一个如同`lll.zzz.xxx`的字符串
- 后端将`JWT Token`字符串作为登录成功的结果返回给前端。前端可以将返回的结果保存在浏览器中，退出登录时删除保存的`JWT Token`即可
- **前端在每次请求时将`JWT Token`放入HTTP请求头中的`Authorization`属性中**(解决XSS和XSRF问题)
- 后端检查前端传过来的`JWT Token`，验证其有效性，比如检查签名是否正确、是否过期、token的接收方是否是自己等等
- 验证通过后，后端解析出`JWT Token`中包含的用户信息，进行其他逻辑操作(一般是根据用户信息得到权限等)，返回结果

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221557.png)



### 2.为什么要用JWT



#### 2.1 传统Session认证的弊端

我们知道**HTTP本身是一种无状态的协议**，这就意味着如果用户向我们的应用提供了用户名和密码来进行用户认证，认证通过后HTTP协议不会记录下认证后的状态，那么下一次请求时，用户还要再一次进行认证，因为根据HTTP协议，我们并不知道是哪个用户发出的请求，所以为了让我们的应用能识别是哪个用户发出的请求，我们只能在用户首次登录成功后，在服务器存储一份用户登录的信息，这份登录信息会在响应时传递给浏览器，告诉其保存为`cookie`，以便下次请求时发送给我们的应用，这样我们的应用就能识别请求来自哪个用户了，这是传统的基于`session`认证的过程

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221558.png)

然而，传统的`session`认证有如下的问题：

- 每个用户的登录信息都会保存到服务器的session中，随着用户的增多，服务器开销会明显增大
- 由于session是存在与服务器的物理内存中，所以在分布式系统中，这种方式将会失效。虽然可以将session统一保存到Redis中，但是这样做无疑增加了系统的复杂性，对于不需要redis的应用也会白白多引入一个缓存中间件
- 对于非浏览器的客户端、手机移动端等不适用，因为session依赖于cookie，而移动端经常没有cookie
- 因为session认证本质基于cookie，所以如果cookie被截获，用户很容易收到跨站请求伪造攻击。并且如果浏览器禁用了cookie，这种方式也会失效
- 前后端分离系统中更加不适用，后端部署复杂，前端发送的请求往往经过多个中间件到达后端，cookie中关于session的信息会转发多次
- 由于基于Cookie，而cookie无法跨域，所以session的认证也无法跨域，对单点登录不适用



#### 2.2 JWT认证的优势

对比传统的`session`认证方式，JWT的优势是：

- 简洁：`JWT Token`数据量小，传输速度也很快
- 因为JWT Token是以JSON加密形式保存在客户端的，所以JWT是跨语言的，原则上任何web形式都支持
- 不需要在服务端保存会话信息，也就是说不依赖于cookie和session，所以没有了传统session认证的弊端，特别适用于分布式微服务
- 单点登录友好：使用Session进行身份认证的话，由于cookie无法跨域，难以实现单点登录。但是，使用token进行认证的话， token可以被保存在客户端的任意位置的内存中，不一定是cookie，所以不依赖cookie，不会存在这些问题
- 适合移动端应用：使用Session进行身份认证的话，需要保存一份信息在服务器端，而且这种方式会依赖到Cookie（需要 Cookie 保存 SessionId），所以不适合移动端

> 因为这些优势，目前无论单体应用还是分布式应用，都更加**推荐用JWT token的方式进行用户认证**



## JWT结构

JWT由3部分组成：标头(Header)、有效载荷(Payload)和签名(Signature)。在传输的时候，会将JWT的3部分分别进行Base64编码后用`.`进行连接形成最终传输的字符串

```
JWTString=Base64(Header).Base64(Payload).HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
```

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221559.png)



### 1.Header

**JWT头**是一个描述JWT元数据的JSON对象，alg属性表示签名使用的算法，默认为HMAC SHA256（写为HS256）；typ属性表示令牌的类型，JWT令牌统一写为JWT。最后，使用Base64 URL算法将上述JSON对象转换为字符串保存

```
{
  "alg": "HS256",
  "typ": "JWT"
}
```



### 2.Payload

**有效载荷**部分，是JWT的主体内容部分，也是一个**JSON对象**，包含需要传递的数据。 JWT指定七个默认字段供选择

```
iss：发行人
exp：到期时间
sub：主题
aud：用户
nbf：在此之前不可用
iat：发布时间
jti：JWT ID用于标识该JWT
```

这些预定义的字段并不要求强制使用。除以上默认字段外，我们还可以自定义私有字段，**一般会把包含用户信息的数据放到payload中**，如下例：

```
{
  "sub": "1234567890",
  "name": "Helen",
  "admin": true
}
```

> 请注意，**默认情况下JWT是未加密的，因为只是采用base64算法，拿到JWT字符串后可以转换回原本的JSON数据，任何人都可以解读其内容，因此不要构建隐私信息字段，比如用户的密码一定不能保存到JWT中**，以防止信息泄露。**JWT只是适合在网络中传输一些非敏感的信息**



### 3.Signature

**签名哈希**部分是对上面两部分数据签名，需要使用base64编码后的header和payload数据，通过指定的算法生成哈希，以**确保数据不会被篡改**。首先，需要指定一个密钥（secret）。该密码仅仅为保存在服务器中，并且不能向用户公开。然后，使用header中指定的签名算法（默认情况下为HMAC SHA256）根据以下公式生成签名

> HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)

在计算出签名哈希后，JWT头，有效载荷和签名哈希的三个部分组合成一个字符串，每个部分用`.`分隔，就构成整个JWT对象

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221660.jpg)

> 注意JWT每部分的作用，在服务端接收到客户端发送过来的JWT token之后：
>
> ```
> header
> ```
>
> 和
>
> ```
> payload
> ```
>
> 可以直接利用base64解码出原文，从
>
> ```
> header
> ```
>
> 中获取哈希签名的算法，从
>
> ```
> payload
> ```
>
> 中获取有效数据

> `signature`由于使用了不可逆的加密算法，无法解码出原文，它的作用是**校验token有没有被篡改**。服务端获取`header`中的加密算法之后，利用该算法加上`secretKey`对`header`、`payload`进行加密，比对加密后的数据和客户端发送过来的是否一致。注意`secretKey`只能保存在服务端，而且对于不同的加密算法其含义有所不同，一般对于MD5类型的摘要加密算法，`secretKey`实际上代表的是盐值



## JWT的种类

其实JWT(JSON Web Token)指的是一种规范，这种规范允许我们使用JWT在两个组织之间传递安全可靠的信息，JWT的具体实现可以分为以下几种：

- `nonsecure JWT`：未经过签名，不安全的JWT
- `JWS`：经过签名的JWT
- `JWE`：`payload`部分经过加密的JWT



### 1.nonsecure JWT

未经过签名，不安全的JWT。其`header`部分没有指定签名算法

```
{
  "alg": "none",
  "typ": "JWT"
}
```

并且也没有`Signature`部分



### 2.JWS

JWS ，也就是JWT Signature，其结构就是在之前nonsecure JWT的基础上，在头部声明签名算法，并在最后添加上签名。**创建签名，是保证jwt不能被他人随意篡改**。我们通常使用的JWT一般都是JWS

为了完成签名，除了用到header信息和payload信息外，还需要算法的密钥，也就是`secretKey`。加密的算法一般有2类：

- 对称加密：`secretKey`指加密密钥，可以生成签名与验签
- 非对称加密：`secretKey`指私钥，只用来生成签名，不能用来验签(验签用的是公钥)

JWT的密钥或者密钥对，一般统一称为JSON Web Key，也就是`JWK`

到目前为止，jwt的签名算法有三种：

- HMAC【哈希消息验证码(对称)】：HS256/HS384/HS512
- RSASSA【RSA签名算法(非对称)】（RS256/RS384/RS512）
- ECDSA【椭圆曲线数据签名算法(非对称)】（ES256/ES384/ES512）



## Java中使用JWT

官网推荐了6个Java使用JWT的开源库，其中比较推荐使用的是`java-jwt`和`jjwt-root`

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221661.jpg)



### 1.java-jwt



#### 1.1 对称签名

首先引入依赖

```
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.10.3</version>
</dependency>
```

生成JWT的token

```
public class JWTTest {
    @Test
    public void testGenerateToken(){
        // 指定token过期时间为10秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);

        String token = JWT.create()
                .withHeader(new HashMap<>())  // Header
                .withClaim("userId", 21)  // Payload
                .withClaim("userName", "baobao")
                .withExpiresAt(calendar.getTime())  // 过期时间
                .sign(Algorithm.HMAC256("!34ADAS"));  // 签名用的secret

        System.out.println(token);
    }
}
```

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221662.png)

> 注意**多次运行方法生成的token字符串内容是不一样的**，尽管我们的payload信息没有变动。因为**JWT中携带了超时时间**，所以每次生成的token会不一样，我们利用base64解密工具可以发现payload确实携带了超时时间
>
> ![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221663.png)

解析JWT字符串

```
@Test
public void testResolveToken(){
    // 创建解析对象，使用的算法和secret要与创建token时保持一致
    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!34ADAS")).build();
    // 解析指定的token
    DecodedJWT decodedJWT = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6ImJhb2JhbyIsImV4cCI6MTU5OTkyMjUyOCwidXNlcklkIjoyMX0.YhA3kh9KZOAb7om1C7o3vBhYp0f61mhQWWOoCrrhqvo");
    // 获取解析后的token中的payload信息
    Claim userId = decodedJWT.getClaim("userId");
    Claim userName = decodedJWT.getClaim("userName");
    System.out.println(userId.asInt());
    System.out.println(userName.asString());
    // 输出超时时间
    System.out.println(decodedJWT.getExpiresAt());
}
```

运行后发现报异常，原因是之前生成的token已经过期

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221664.png)

再运行一次生成token的方法，然后在过期时间10秒之内将生成的字符串拷贝到解析方法中，运行解析方法即可成功

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221665.png)

我们可以将上述方法封装成工具类

```
public class JWTUtils {
    // 签名密钥
    private static final String SECRET = "!DAR$";

    /**
     * 生成token
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String getToken(Map<String,String> payload){
        // 指定token过期时间为7天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);

        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach((k,v) -> builder.withClaim(k,v));
        // 指定过期时间和签名算法
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SECRET));
        return token;
    }
    /**
     * 解析token
     * @param token token字符串
     * @return 解析后的token
     */
    public static DecodedJWT decode(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }
}
```



#### 1.2 非对称签名

生成jwt串的时候需要指定私钥，解析jwt串的时候需要指定公钥

```
private static final String RSA_PRIVATE_KEY = "...";
private static final String RSA_PUBLIC_KEY = "...";

/**
     * 生成token
     * @param payload token携带的信息
     * @return token字符串
     */
public static String getTokenRsa(Map<String,String> payload){
    // 指定token过期时间为7天
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, 7);

    JWTCreator.Builder builder = JWT.create();
    // 构建payload
    payload.forEach((k,v) -> builder.withClaim(k,v));

    // 利用hutool创建RSA
    RSA rsa = new RSA(RSA_PRIVATE_KEY, null);
    // 获取私钥
    RSAPrivateKey privateKey = (RSAPrivateKey) rsa.getPrivateKey();
    // 签名时传入私钥
    String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.RSA256(null, privateKey));
    return token;
}

/**
     * 解析token
     * @param token token字符串
     * @return 解析后的token
     */
public static DecodedJWT decodeRsa(String token){
    // 利用hutool创建RSA
    RSA rsa = new RSA(null, RSA_PUBLIC_KEY);
    // 获取RSA公钥
    RSAPublicKey publicKey = (RSAPublicKey) rsa.getPublicKey();
    // 验签时传入公钥
    JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
    DecodedJWT decodedJWT = jwtVerifier.verify(token);
    return decodedJWT;
}
```



### 2.jjwt-root



#### 2.1 对称签名

引入依赖

```
<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

使用方法类似，可参考下列代码

```
public class JwtUtils {
    // token时效：24小时
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    // 签名哈希的密钥，对于不同的加密算法来说含义不同
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 根据用户id和昵称生成token
     * @param id  用户id
     * @param nickname 用户昵称
     * @return JWT规则生成的token
     */
    public static String getJwtToken(String id, String nickname){
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("baobao-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("nickname", nickname)
            	// HS256算法实际上就是MD5加盐值，此时APP_SECRET就代表盐值
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request Http请求对象
     * @return 如果token有效返回true，否则返回false
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            // 从http请求头中获取token字符串
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @param request Http请求对象
     * @return 解析token后获得的用户id
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }
}
```

> 注意：
>
> jjwt在0.10版本以后发生了较大变化，pom依赖要引入多个

```
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.2</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
```

标准规范中对各种加密算法的`secretKey`的长度有如下要求：

- `HS256`：要求至少 256 bits (32 bytes)
- `HS384`：要求至少384 bits (48 bytes)
- `HS512`：要求至少512 bits (64 bytes)
- `RS256` and `PS256`：至少2048 bits
- `RS384` and `PS384`：至少3072 bits
- `RS512` and `PS512`：至少4096 bits
- `ES256`：至少256 bits (32 bytes)
- `ES384`：至少384 bits (48 bytes)
- `ES512`：至少512 bits (64 bytes)

在jjwt0.10版本之前，没有强制要求，`secretKey`长度不满足要求时也可以签名成功。但是0.10版本后强制要求`secretKey`满足规范中的长度要求，否则生成jws时会抛出异常

![img](https://www.yht7.com/upload/image/2022/06/06/2022060614221666.png)

新版本的jjwt中，之前的签名和验签方法都是传入密钥的字符串，已经过时。最新的方法需要传入`Key`对象

```
public class JwtUtils {
    // token时效：24小时
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    // 签名哈希的密钥，对于不同的加密算法来说含义不同
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHOsdadasdasfdssfeweee";

    /**
     * 根据用户id和昵称生成token
     * @param id  用户id
     * @param nickname 用户昵称
     * @return JWT规则生成的token
     */
    public static String getJwtToken(String id, String nickname){
        String JwtToken = Jwts.builder()
                .setSubject("baobao-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("nickname", nickname)
                // 传入Key对象
                .signWith(Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public static Jws<Claims> decode(String jwtToken) {
        // 传入Key对象
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(jwtToken);
        return claimsJws;
    }
}
```



#### 2.2 非对称签名

生成jwt串的时候需要指定私钥，解析jwt串的时候需要指定公钥

```
private static final String RSA_PRIVATE_KEY = "...";
private static final String RSA_PUBLIC_KEY = "...";

/**
     * 根据用户id和昵称生成token
     * @param id  用户id
     * @param nickname 用户昵称
     * @return JWT规则生成的token
     */
public static String getJwtTokenRsa(String id, String nickname){
    // 利用hutool创建RSA
    RSA rsa = new RSA(RSA_PRIVATE_KEY, null);
    RSAPrivateKey privateKey = (RSAPrivateKey) rsa.getPrivateKey();
    String JwtToken = Jwts.builder()
        .setSubject("baobao-user")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
        .claim("id", id)
        .claim("nickname", nickname)
        // 签名指定私钥
        .signWith(privateKey, SignatureAlgorithm.RS256)
        .compact();
    return JwtToken;
}

/**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
public static Jws<Claims> decodeRsa(String jwtToken) {
    RSA rsa = new RSA(null, RSA_PUBLIC_KEY);
    RSAPublicKey publicKey = (RSAPublicKey) rsa.getPublicKey();
    // 验签指定公钥
    Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwtToken);
    return claimsJws;
}
```



## 实际开发中的应用

在实际的`SpringBoot`项目中，一般我们可以用如下流程做登录：

- 在登录验证通过后，给用户生成一个对应的随机token(注意这个token不是指jwt，可以用uuid等算法生成)，然后将这个token作为key的一部分，用户信息作为value存入Redis，并设置过期时间，这个过期时间就是登录失效的时间
- 将第1步中生成的随机token作为JWT的payload生成JWT字符串返回给前端
- 前端之后每次请求都在请求头中的Authorization字段中携带JWT字符串
- 后端定义一个拦截器，每次收到前端请求时，都先从请求头中的Authorization字段中取出JWT字符串并进行验证，验证通过后解析出payload中的随机token，然后再用这个随机token得到key，从Redis中获取用户信息，如果能获取到就说明用户已经登录

```
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String JWT = request.getHeader("Authorization");
        try {
            // 1.校验JWT字符串
            DecodedJWT decodedJWT = JWTUtils.decode(JWT);
            // 2.取出JWT字符串载荷中的随机token，从Redis中获取用户信息
            ...
            return true;
        }catch (SignatureVerificationException e){
            System.out.println("无效签名");
            e.printStackTrace();
        }catch (TokenExpiredException e){
            System.out.println("token已经过期");
            e.printStackTrace();
        }catch (AlgorithmMismatchException e){
            System.out.println("算法不一致");
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("token无效");
            e.printStackTrace();
        }
        return false;
    }
}
```

> 在实际开发中需要用下列手段来增加JWT的安全性：
>
> 因为JWT是在请求头中传递的，所以为了避免网络劫持，推荐使用
>
> ```
> HTTPS
> ```
>
> 来传输，更加安全

> JWT的哈希签名的密钥是存放在服务端的，所以只要服务器不被攻破，理论上JWT是安全的。因此要保证服务器的安全

> JWT可以使用暴力穷举来破解，所以为了应对这种破解方式，可以定期更换服务端的哈希签名密钥(相当于盐值)。这样可以保证等破解结果出来了，你的密钥也已经换了
