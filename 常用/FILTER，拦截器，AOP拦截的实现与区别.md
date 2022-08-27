#### FILTER，拦截器，AOP拦截的实现与区别



https://www.freesion.com/article/2175174683/

**效果**
![这里写图片描述](https://www.freesion.com/images/513/a62b8245d43c4a891015411c0d0f9ba9.png)
**实现**
**1 Filter**
直接实现**Filter**接口，重写拦截方法，再到**@WebFilter**注解上配置拦截规则即可实现

```
@Component
@WebFilter(urlPatterns = { "/**" }, filterName = "tokenAuthorFilter")
public class ConfigurationFilter implements Filter{
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
         System.out.println("我是Filter，我拦截到了请求");
         chain.doFilter(request, response);//到下一个链
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }
}1234567891011121314151617181920
```

**拦截器**
和filter类似 实现**HandlerInterceptor**接口并重写拦截方法

```
public class MyInterceptor implements HandlerInterceptor{

    //在请求处理之前进行调用（Controller方法调用之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.printf("我是拦截器：preHandle被调用");

        HandlerMethod handlerMethod = (HandlerMethod) o;
        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(interceptorLog.class) != null) {
            return true;
        }else{
             return false;
        }
    }

    //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("我是拦截器：postHandle被调用");
    }

    //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("我是拦截器：afterCompletion被调用");
    }

}1234567891011121314151617181920212223242526272829
```

像spring容器内将创建的拦截器注册进去

```
@Configuration
public class interceptorConfig extends WebMvcConfigurerAdapter{
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MyInterceptor())    //指定拦截器类
                .addPathPatterns("/**");        //指定该类拦截的url
    }
}
12345678
```

自定义一个注解用于拦截标识

```
@Retention(RetentionPolicy.RUNTIME) // 表示注解在运行时依然存在
@Target(ElementType.METHOD)  
@Documented
public @interface interceptorLog {

}
1234567
```

**aop拦截**
pom

```
  <!--AOP--> 
     <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
       <!--druid  -->123456
```

配置aop

```
@Component
@Aspect 
public class HttpAspect {
   @Pointcut("execution(* spring.controller..*.*(..)) && @annotation(spring.annotation.aopLog)")
  // @Pointcut("public * *(..)")
    public void log(){
    }

    @Around("log()")
    public void  aroundLogCalls(JoinPoint joinPoint) throws Throwable {

        System.out.println("我是aop:方法环绕start.....");


        System.out.println("我是aop:方法环绕end.....");

    }
    /**
     * 打印请求的ur l method ip 类方法  参数
     * @param joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =attributes.getRequest();

        System.out.println("我是aop:方法开始");

    }


   // @After("execution(public * com.tg.controller.GirlController.*(..))")
    @After("log()")
    public void doAfter(){
        System.out.println("我是aop:方法结束");
    }

    /**
     * 打印请求的具体内容
     * @param objects
     */
    @AfterReturning(returning = "objects",pointcut = "log()")
    public void  doAfterReturning(Objects objects){

    }


      @AfterThrowing("log()")
    public void cathInfo(){
         System.out.println("异常信息");
     }

}  1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253
```

自定义注解用于拦截标识

```
@Retention(RetentionPolicy.RUNTIME) // 表示注解在运行时依然存在
@Target(ElementType.METHOD)  
@Documented
public @interface aopLog {

}
1234567
```

**被请求方法**

```
@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ApiOperation("登录")
    @ResponseBody
    @aopLog
    @interceptorLog
    public Map<String, Object> login(
            @RequestParam(value="useName", required=true) String useName,
            @RequestParam(value="passWord", required=true) String passWord
            ){
        Map<String,Object> result=new HashMap<String,Object>();
        try{
            //查询
            userInfo selectedUser=  dao.selectUserInfo(useName);
            //更新
            dao.updateFrequency(useName);
            if(selectedUser.getPassWord().equals(passWord)){
                result.put("userInfo", selectedUser);
                result.put("code", "200");
            }else{
                result.put("msg", "账号密码错误");
            }
        }catch(Exception e){
            result.put("code", "404");
        }
        return result;
    }1234567891011121314151617181920212223242526
```

**总结：**
![这里写图片描述](https://www.freesion.com/images/348/ceae80830979ed9dc32036b317d61124.png)
这是swagger连接到服务器所触发的拦截信息，可以看到所有的请求都会被**filter**首先拦截，并可以预处理request,
而拦截器可以调用IOC容器中的各种依赖，filter就不能获取注解信息并拦截，因为它和框架无关，但拦截器不能修改request,filter基于回调函数，我们需要实现的filter接口中doFilter方法就是回调函数，而interceptor则基于java本身的反射机制，而@Aspect与Interceptor的都是基于spring aop的实现，@Aspect粒度更细
**拦截顺序：filter—>Interceptor—->@Aspect**
![这里写图片描述](https://www.freesion.com/images/209/813719ebb0f6407d30dfcaeca7067ae1.png)
![这里写图片描述](https://www.freesion.com/images/813/bb11622387dc3bf6ccf06819ef3f1cd5.png)
