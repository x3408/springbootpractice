## 1. Spring Boot初探究

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

## 5.自动配置原理