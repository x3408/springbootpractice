# 一、Spring Boot初探究

### 1.Pom文件探究



#### 1. 父项目

> ```xml
> <parent>
>    <groupId>org.springframework.boot</groupId>
>    <artifactId>spring-boot-starter-parent</artifactId>
>    <version>2.0.4.RELEASE</version>
>    <relativePath/> <!-- lookup parent from repository -->
> </parent>
> ```

其父项目是真正管理spring boot的依赖版本(版本仲裁中心);

所以以后默认是不需要写版本的(除非没有写在版本管理里面的);

#### 2.导入的依赖

> ```xml
> <dependency>
>    <groupId>org.springframework.boot</groupId>
>    <artifactId>spring-boot-starter-web</artifactId>
> </dependency>
> ```

改依赖帮我们导入了web模块正常运行的依赖组件

将所有的功能场景都抽取出来做成一个个starters(启动器),版本且又spring管理



### 2.主程序类

> ```java
> @SpringBootApplication
> public class SpringbootpracticeApplication {
> 
>    public static void main(String[] args) {
>       SpringApplication.run(SpringbootpracticeApplication.class, args);
>    }
> }
> ```

通过这个注解启动spring boot程序

> ```java
> @Target({ElementType.TYPE})
> @Retention(RetentionPolicy.RUNTIME)
> @Documented
> @Inherited
> @SpringBootConfiguration
> @EnableAutoConfiguration
> @ComponentScan(
>     excludeFilters = {@Filter(
>     type = FilterType.CUSTOM,
>     classes = {TypeExcludeFilter.class}
> ), @Filter(
>     type = FilterType.CUSTOM,
>     classes = {AutoConfigurationExcludeFilter.class}
> )}
> )
> ```

**@SpringBootConfiguration**:Spring boot的配置类:标志着这是个配置类;

**@EnableAutoConfiguration**:开启自动配置功能;

	以前需要配置的内动由spring boot帮我们配置
	
	底层将主程序类所在的包以及下面所有子包中的组件扫描到spring容器中

spring boot启动时 从类路径的META-INF/spring.factories中获取EnableAutoConfiguration中需要加载的配置

## 2. 使用Spring Initializer快速创建Spring Boot项目

* resource目录结构:保存所有的静态资源如css、js、image等；
* templates:保存所有的模板页面（不支持 jsp页面）；可以使用模板引擎（freemaker、themeleaf）；
* application.properties:spring boot的配置文件;可以修改一些默认的配置

## 3.配置文件

spring boot使用全局配置文件,配置的文件命名是固定的

* application.properties
* application.yml

### 1. YAML基本语法

key value:表示一对键值对(空格必须有)

以空格的缩进表示层级关系;左对齐表示一个层级;

属性和值也是大小写敏感

### 2. 值的写法

字面量:普通的值(数字 字符串 布尔):

	k: v
	
	字面值直接写 且冒号后有空格，字符串不用加双引号
	
	“”：不会转义字符串里面的特殊字符
	
		eg：
	
			name: "zhangsan\n"			不转义
	
			输出shangsan换行
	
	‘’：会转义



**对象 Map**

> firends:
>
> 		lastname: chen
>		
> 		age: 20

> friends: {lastname: chen,age: 18}

 **数组(List Set)**

``` yml
pets:
- cat
- dog
- pig
```



### 3.配置文件值的注入

#### @ConfigurationProperties 和 Value

在spring中使用@Value("") 填入相关的属性

|                    | @ConfigurationProperties | @Value     |
| ------------------ | ------------------------ | ---------- |
| 功能               | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定(松散语法) | 支持                     | 不支持     |
| EL表达式           | 不支持                   | 支持       |
| JSR303数据校验     | 支持                     | 不支持     |
| 复杂类型封装       | 支持                     | 不支持     |

配置文件无论是yml还是properties都能获取到值



#### @PropertitySource

> ```java
> @PropertySource(value = {"classpath:person.properties"})
> ```

加载指定的配置文件



#### @ImportSource

导入spring配置文件，并让其生效。标注在一个配置类上

而SpringBoot中推荐给容器添加组件的方式



## 4.profile

### 1.多Profile文件

### 2.yml支持多文档块方式

>```yml
>server:
>  port: 8080
>spring:
>  profiles:
>    active: dev
>---
>server:
>  port: 8081
>spring:
>  profiles: prod
>---
>server:
>  port: 8082
>spring:
>  profiles: dev
>```

### 3.激活指定的profile

1.在配置文件中指定 spring.profiles.active=dev

2.命令行:

	--spring.profiles.active=dev

3.虚拟机参数

	-Dspring.profiles.active=dev

## 4.配置文件的加载位置

优先级由高到低

-file:./config/

-file:./

-classpath:/config/

-classpath:/

# 二、web开发

## 1、简介

使用SpringBoot;

1)、创建SpringBoot应用，选中我们需要的模块；

2)、SpringBoot已经默认将这些场景配置好了，只需要在配置文件中指定少量配置就可以运行起来

3)、自己编写业务代码

**自动配置原理？**

这个场景的SpringBoot帮我们配置了什么？能不能修改？能修改那些配置？能不能扩展？xxx

```
xxxAutoConfiguration:帮我们给容器中自动配置组件
xxxProperties:配置类来封装配置文件的内容
```

## 2、静态资源文件映射规则

```
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties implements ResourceLoaderAware, InitializingBean {
    //可以设置和静态资源相关的参数，缓存时间等
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
   if (!this.resourceProperties.isAddMappings()) {
      logger.debug("Default resource handling disabled");
      return;
   }
   Integer cachePeriod = this.resourceProperties.getCachePeriod();
   if (!registry.hasMappingForPattern("/webjars/**")) {
      customizeResourceHandlerRegistration(registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
            .setCachePeriod(cachePeriod));
   }
   String staticPathPattern = this.mvcProperties.getStaticPathPattern();
   if (!registry.hasMappingForPattern(staticPathPattern)) {
      customizeResourceHandlerRegistration(
            registry.addResourceHandler(staticPathPattern)
                  .addResourceLocations(
                        this.resourceProperties.getStaticLocations())
                  .setCachePeriod(cachePeriod));
   }
}
```

### 1、webjar

1)、所有的/webjars/**，都去classpath:/META-INF/resources/webjars/找资源；

​	webjars：以jar包的方式引入静态资源

<http://www.webjars.org/>

[![12.jquery]()](https://github.com/lxy-go/SpringBoot/blob/master/SpringBoot%E5%85%A5%E9%97%A8%E7%AE%80%E4%BB%8B.md)

localhost:8080/webjars/jquery/3.3.1/jquery.js

### 2、本地资源

```
private String staticPathPattern = "/**";
```

访问任何资源

2、会在这几文件夹下去找静态路径（静态资源文件夹）

```
"classpath:/META-INF/resources/", 
"classpath:/resources/",
"classpath:/static/", 
"classpath:/public/",
"/";当前项目的根路径
```

[![13.static]()](https://github.com/lxy-go/SpringBoot/blob/master/SpringBoot%E5%85%A5%E9%97%A8%E7%AE%80%E4%BB%8B.md)

localhost:8080/abc ==>去静态资源文件夹中找abc

[![14.static-css]()](https://github.com/lxy-go/SpringBoot/blob/master/SpringBoot%E5%85%A5%E9%97%A8%E7%AE%80%E4%BB%8B.md)

3、index页面欢迎页，静态资源文件夹下所有的index.html页面；被“/**”映射；

localhost:8080/ -->index页面

```
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(
      ResourceProperties resourceProperties) {
   return new WelcomePageHandlerMapping(resourceProperties.getWelcomePage(),
         this.mvcProperties.getStaticPathPattern());
}
```

4、喜欢的图标，即网站title的图标favicon

```
@Configuration
@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
public static class FaviconConfiguration {

   private final ResourceProperties resourceProperties;

   public FaviconConfiguration(ResourceProperties resourceProperties) {
      this.resourceProperties = resourceProperties;
   }

   @Bean
   public SimpleUrlHandlerMapping faviconHandlerMapping() {
      SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
      mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
       //把任何favicon的图标都在静态文件夹下找
      mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
            faviconRequestHandler()));
      return mapping;
   }

   @Bean
   public ResourceHttpRequestHandler faviconRequestHandler() {
      ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
      requestHandler
            .setLocations(this.resourceProperties.getFaviconLocations());
      return requestHandler;
   }

}
```

可以在配置文件配置静态资源文件夹

```
spring.resources.static-locations=classpath:xxxx
```

## 3、模板引擎

将html和数据 结合到一起 输出组装处理好的新文件

SpringBoot推荐Thymeleaf;语法简单，功能强大

### 1、引入thymeleaf 3

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

默认导入thymeleaf2，版本太低 所以使用thymeleaf3.

[官方导入办法](https://docs.spring.io/spring-boot/docs/1.5.12.RELEASE/reference/htmlsingle/#howto-use-thymeleaf-3)

```
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <!--thymeleaf 3的导入-->
    <thymeleaf.version>3.0.9.RELEASE</thymeleaf.version>
    <!--布局功能支持 同时支持thymeleaf3主程序 layout2.0以上版本  -->
    <!--布局功能支持 同时支持thymeleaf2主程序 layout1.0以上版本  -->
    <thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
</properties>
```

### 2、Thymeleaf使用和语法

```
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

   private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

   private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");

   public static final String DEFAULT_PREFIX = "classpath:/templates/";

   public static final String DEFAULT_SUFFIX = ".html";
   //只要把HTML文件方法类路径下的template文件夹下，就会自动导入
```

只要把HTML页面放到classpath:/templates/,thymeleaf就能自动渲染；

使用：

1、导入thymeleaf的名称空间

```
<html xmlns:th="http://www.thymeleaf.org">    
```

2、使用thymeleaf语法；

```
<!DOCTYPE html>
<html lang="en"  xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" />
    <title>success</title>
</head>
<body>
<h1>success</h1>
<!--th:text 将div里面的文本内容设置为-->
<div th:text="${Lion}">
前端数据
</div>
</body>
</html>
```

3、语法规则

1）、th:text="${hello}"可以使用任意标签 替换原生的任何属性

**在SpringBoot的环境下**

```
<div id="testid" class="testcalss" th:id="${Lion}" th:class="${Lion}" th:text="${Lion}">
	前端数据
</div>
```

[![15.thtmeleaf-th01]()](https://github.com/lxy-go/SpringBoot/blob/master/SpringBoot%E5%85%A5%E9%97%A8%E7%AE%80%E4%BB%8B.md)

**直接访问HTML页面**

[![15.thtmeleaf-th02]()](https://github.com/lxy-go/SpringBoot/blob/master/SpringBoot%E5%85%A5%E9%97%A8%E7%AE%80%E4%BB%8B.md)

**2)、内联写法注意需要在body上加上 th:inline="text"敲黑板**

不然不起作用

```
<body class="text-center" th:inline="text"></body>
```

th标签的访问优先级

Order Feature Attributes

### 3、语法规则

|      | 功能                            | 标签                                 | 功能和jsp对比                         |
| ---- | ------------------------------- | ------------------------------------ | ------------------------------------- |
| 1    | Fragment inclusion              | th:insert th:replace                 | include(片段包含)                     |
| 2    | Fragment iteration              | th:each                              | c:forEach(遍历)                       |
| 3    | Conditional evaluation          | th:if th:unless th:switch th:case    | c:if(条件判断)                        |
| 4    | Local variable definition       | th:object th:with                    | c:set(声明变量)                       |
| 5    | General attribute modification  | th:attr th:attrprepend th:attrappend | 属性修改支持前面和后面追加内容        |
| 6    | Specific attribute modification | th:value th:href th:src ...          | 修改任意属性值                        |
| 7    | Text (tag body modification)    | th:text th:utext                     | 修改标签体内容utext：不转义字符大标题 |
| 8    | Fragment specification          | th:fragment                          | 声明片段                              |
| 9    | Fragment removal                | th:remove                            |                                       |

```
Simple expressions:(表达式语法)
    Variable Expressions: ${...}
    	1、获取对象属性、调用方法
    	2、使用内置基本对象：
    	    #ctx : the context object.
            #vars: the context variables.
            #locale : the context locale.
            #request : (only in Web Contexts) the HttpServletRequest object.
            #response : (only in Web Contexts) the HttpServletResponse object.
            #session : (only in Web Contexts) the HttpSession object.
            #servletContext : (only in Web Contexts) the ServletContext object.
         3、内置一些工具对象
        	#execInfo : information about the template being processed.
        	#messages : methods for obtaining externalized messages inside variables expressions, in the same way as they
            would be obtained using #{…} syntax.
            #uris : methods for escaping parts of URLs/URIs
            #conversions : methods for executing the configured conversion service (if any).
            #dates : methods for java.util.Date objects: formatting, component extraction, etc.
            #calendars : analogous to #dates , but for java.util.Calendar objects.
            #numbers : methods for formatting numeric objects.
            #strings : methods for String objects: contains, startsWith, prepending/appending, etc.
            #objects : methods for objects in general.
            #bools : methods for boolean evaluation.
            #arrays : methods for arrays.
            #lists : methods for lists.
            #sets : methods for sets.
            #maps : methods for maps.
            #aggregates : methods for creating aggregates on arrays or collections.
            #ids : methods for dealing with id attributes that might be repeated (for example, as a result of an iteration).
    Selection Variable Expressions: *{...} //选择表达式：和${}功能一样，补充功能
   # 配合th:object使用，object=${object} 以后获取就可以使用*{a}  相当于${object.a}
  	    <div th:object="${session.user}">
            <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
            <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
            <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
		</div>
    Message Expressions: #{...} //获取国际化内容
    Link URL Expressions: @{...} //定义URL链接
    	#<a href="details.html" th:href="@{/order/details(orderId=${o.id})}">view</a>
    Fragment Expressions: ~{...}//片段文档
    
Literals（字面量）
    Text literals: 'one text' , 'Another one!' ,…
    Number literals: 0 , 34 , 3.0 , 12.3 ,…
    Boolean literals: true , false
    Null literal: null
    Literal tokens: one , sometext , main ,…
Text operations:(文本操作)
    String concatenation: +
    Literal substitutions: |The name is ${name}|
Arithmetic operations:（数学运算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -
Boolean operations:（布尔运算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not
Comparisons and equality:（比较运算）
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )
Conditional operators:（条件运算）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
Special tokens:（空操作）
	No-Operation: _
```

inline写法

```
[[]] -->th:text
[()] -->th:utext
```

## 4、SpringMVC自动配置

### 1、SpringMVC的自动导入

[Spring框架](https://docs.spring.io/spring-boot/docs/1.5.12.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

自动配置好了mvc：

以下是SpringBoot对SpringMVC的默认

Spring Boot provides auto-configuration for Spring MVC that works well with most applications.

The auto-configuration adds the following features on top of Spring’s defaults:

- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.

  - 自动配置了ViewResolver(视图解析器：根据方法的返回值得到视图对象（View）,视图对象决定如何渲染（转发？重定向？）)
  - `ContentNegotiatingViewResolver`组合所有视图解析器
  - 如何定制：我们可以自己给容器中添加一个视图解析器；自动将其整合进来

- Support for serving static resources, including support for WebJars (see below).静态资源

- Static `index.html` support.

- Custom `Favicon` support (see below).

- 自动注册 了`Converter`, `GenericConverter`, `Formatter` beans.

  - `Converter`：类型转换 文本转为字面量

  - `Formatter` ：格式化器 转换后格式转换

    ```
    @Bean
    @ConditionalOnProperty(prefix = "spring.mvc", name = "date-format")//在文件配置入职格式化的规则
    public Formatter<Date> dateFormatter() {
       return new DateFormatter(this.mvcProperties.getDateFormat());//日期格式化组件
    }
    ```

    自己添加的格式化转换器，只需要放在容器中即可

- Support for `HttpMessageConverters` (see below).

  - `HttpMessageConverters` ：转换HTTP转换和响应：User - json

  - `HttpMessageConverters` ：是从容器中确定；获取所有的`HttpMessageConverters` ，将自己的组件注册在容器中@Bean

  - If you need to add or customize converters you can use Spring Boot’s `HttpMessageConverters` class:

    ```
    import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
    import org.springframework.context.annotation.*;
    import org.springframework.http.converter.*;
    
    @Configuration
    public class MyConfiguration {
    
        @Bean
        public HttpMessageConverters customConverters() {
            HttpMessageConverter<?> additional = ...
            HttpMessageConverter<?> another = ...
            return new HttpMessageConverters(additional, another);
        }
    
    }
    ```

- Automatic registration of `MessageCodesResolver` (see below).

  - 定义错误代码生成规则

- Automatic use of a `ConfigurableWebBindingInitializer` bean (see below).

  - ```
    @Override
    protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
       try {
          return this.beanFactory.getBean(ConfigurableWebBindingInitializer.class);
       }
       catch (NoSuchBeanDefinitionException ex) {
          return super.getConfigurableWebBindingInitializer();
       }
    }
    ```

    在beanFactory：中可以自己创建一个，初始化webDataBinder

    请求数据 ==》javaBean

If you want to keep Spring Boot MVC features, and you just want to add additional [MVC configuration](https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle#mvc) (interceptors, formatters, view controllers etc.) you can add your own `@Configuration` class of type `WebMvcConfigurerAdapter`, but **without**`@EnableWebMvc`. If you wish to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter` or `ExceptionHandlerExceptionResolver` you can declare a `WebMvcRegistrationsAdapter` instance providing such components.

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.

思想：修改默认配置

### 2、扩展SpringMVC

编写一个配置类，类型是WebMvcConfigurerAdapter(继承)，使用WebMvcConfigurerAdapter可以扩展，不能标注@EnableWebMvc;既保留了配置，也能拓展我们自己的应用

```
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        super.addViewControllers(registry);
        //浏览器发送wdjr请求，也来到success页面
        registry.addViewController("/wdjr").setViewName("success");
    }
}
```

原理：

1）、WebMvcAutoConfiguration是SpringMVC的自动配置

2）、在做其他自动配置时会导入；@Import(EnableWebMvcConfiguration.class)

```
@Configuration
public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration {
    private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

	//从容器中获取所有webMVCconfigurer
	@Autowired(required = false)
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addWebMvcConfigurers(configurers);
            
            	@Override
                protected void addViewControllers(ViewControllerRegistry registry) {
                    this.configurers.addViewControllers(registry);
                }
            //一个参考实现,将所有的webMVCconfigurer相关配置一起调用（包括自己的配置类）
            	@Override
               // public void addViewControllers(ViewControllerRegistry registry) {
                   // for (WebMvcConfigurer delegate : this.delegates) {
				 //delegate.addViewControllers(registry);
                    //}
                }
		}
	}
    
```

3）、自己的配置被调用

效果：SpringMVC的自动配置和我们的扩展配置都会起作用

### 3、全面接管mvc

不需要SpringBoot对SpringMVC的自动配置。

```
@EnableWebMvc
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

@Override
public void addViewControllers(ViewControllerRegistry registry) {


//        super.addViewControllers(registry);
        //浏览器发送wdjr请求，也来到success页面
        registry.addViewController("/wdjr").setViewName("success");
    }
}
```

例如静态资源访问，不推荐全面接管

原理：

为什么@EnableWebMvc注解，SpringBoot对SpringMVC的控制就失效了

1）、核心配置

```
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
```

2）、DelegatingWebMvcConfiguration

```
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
```

3）、WebMvcAutoConfiguration

```
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class,
      WebMvcConfigurerAdapter.class })
//容器没有这个组件的时候，这个自动配置类才生效
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
      ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
```

4）、@EnableWebMvc将WebMvcConfigurationSupport导入进来了；

5）、导入的WebMvcConfigurationSupport只是SpringMVC最基本的功能

## 5、修改SpringMVC默认配置

模式:

	1）、SpringBoot在自动配置很多组件的时候，先看容器中有没有用户自己配置的（@Bean、@Component）如果有就用用户配置的，如果没有，才自动配置；如果有些组件可以有多个（ViewResolver）将用户配置的和自己默认的组合起来；

​	2）、在SpringBoot中会有 xxxConfigurer帮助我们扩展配置。

## SpringMvc全面接管



#### 在SpringBoot2.0及Spring 5.0 WebMvcConfigurerAdapter已被废弃，目前找到解决方案就有两种

#### 1 直接实现WebMvcConfigurer （官方推荐）

例如：

```java
@Configuration
public class WebMvcConfg implements WebMvcConfigurer {

    //todo

}123456
```

#### 2 直接继承WebMvcConfigurationSupport

例如：

```java
@Configuration
public class WebMvcConfg extends WebMvcConfigurationSupport {

        //todo

}
```

==若使用第二种 则相当于启用了@EnableWebMvc注解功能（即全面接管springmvc）==