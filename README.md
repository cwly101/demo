# Spring boot 帮助

## 自动配置注解说明

## @SpringBootApplication 
作用： spring boot 应用入口注解。内部包含@EnableAutoConfiguration 
## @EnableAutoConfiguration
作用： 启用自动配置。内部包含 @AutoConfigurationPackage
## @AutoConfigurationPackage
扫描@SpringBootApplication标注类所在包的下所有子包组件到容器中。  
说明：所以入口类不能乱放，否则将导致包扫描不到。
## @Import(AutoConfigurationImportSelector.class)
### AutoConfigurationImportSelector
#### 见类下的方法：public String[] selectImports(AnnotationMetadata annotationMetadata) 
导入哪些组件的选择器。将所有需要导入的组件以全类名方式返回；这些组件将会被加入到容器中；会给容器添加非常多的自动配置类；就是给容器中导入这个场景需要的所有组件，并配置好  
**总结：spring boot 在启动的时候从类路径下：META-INF/spring.factories中获取EnableAutoConfiguration指定值，将这些值作为自动配置类导入到容器中，自动配置类就会生效，帮我们进行自动配置工作**  
见maven依赖下的 spring-boot-autoconfigure-2.x.xRELEASE.jar下的MATA-INF/spring.factories文件  
  
J2EE的整体整合解决方案和自动配置都在spring-boot-autoconfigure-2.x.xRELEASE.jar包下的 org.springframwork.boot.autoconfigure包下

## YML配置文件语法
key: value (冒号后面必须有空格符)  
以空格缩进来控制层级关系。只要左对齐的一列数据，都属于同一层级关系。

### 值的写法
#### 字面量
key: value 直接来写  

    字符串默认不需要加单引号或双引号  
    双引号的含义：不会转义特殊字符，特殊字符会按照本身表达的意思输出。如：\n 就会输出为换行
    单引号含义：会转义特殊字符，最终输出为字符串

#### 对象、Map（属性和值）
key: value  
如对象:
```yml
# 正常与法
user:
  name: tom
  age: 18
# 行内写法
user: { name: tom,age: 18 }
```
### 数组（List、Set）
使用 - 表示数组中的一个元素，如：
```yml
numbers:
  - 33
  - 66
  - 7
# 行内写法
numbers: [33,66,7]
```
#### @ConfigurationProperties(prefix = "user")
读取yml配置文件中对应前缀为user的数据，映射到对应类对象中。注解标注在类对象上  
**注：对象必须作为组件注册到容器中，才能正常读取。读取时去容器中读取，如：@Autowried User user;**
#### @Vaule与@ConfigurationProperties获取值比较
||@ConfigurationProperties | @value
|---|:--:|---:
功能|批量注入配置文件中属性|一个一个指定
松散绑定（松散语法）|支持|不支持
SpEL|不支持|支持
JSR303数据检验|支持|不支持
复杂类型封闭|支持|不支持
   

松散绑定： last-name 和 lastName 及 last_name都被识别  
JSR303检验使用注解 @Validated， 以及各个属性的注解，如：@Email、@NotEmpty、@Range等
复杂类型封闭：如value为数组、集合、列表、对象时，@value解析不了。  
**使用场景：仅映射读取一个或几个值时，可以考虑用@value，如果是一组对象数据，并想实现快速自动化映射，推荐@ConfigurationProperties**

#### @PropertySource
加载指定的配置文件，即非全局的(application.yml或application.properties以外的)配置文件，如自定义的配置文件。  
**注：@ConfigurationProperties默认是从全局配置文件读取值**  
需要与@ConfigurationProperties或@value搭配使用，@PropertySource负责读取整个文件，@ConfigurationProperties指定读取指定前缀。  
@PropertySource具有两参数，如下：
@PropertySource(value = "classpath:user.yml", factory = ResourceFactory.class)  
参数1：读取路径，classpath表示项目的 src/main/resource 路径  
参数2：读取文件要使用工厂类  

#### @ImportResource
导入spring的配置文件，可以标注在程序启动类上，指定配置文件的路径，如：  
@ImportResource(value = "classpath:bean.xml")  
感觉在spring boot 中这个应该用的不多了，组件注册是不需要spring配置文件的，使用注解@Service或@bean即可。

#### 配置文件占位符
1. 可使用random随机数。
```yml
number: ${random.value}
number1: ${random.uuid}
number2: ${random.int}
number3: ${random.long}
number4: ${random.int(10)}
number4: ${random.int[10,100]}
```
2. 可获取之前设置的值，如之前定义了变量server.port，这里即可： ${server.port}。如果没有可以指定默认值,如：${fristname:ac}

### Profile (设置不同环境的配置文件)
```yml
# 激活不同环境下的配置文件，如：dev（开发环境）、pro（生产环境）
# 环境配置文件命令格式：application-{profile}.yml/properties
spring:
  profiles:
    active:
    - pro
```
也可以在启动时通过命令行参数设置，点击【运行】图标旁边的三角图标，选择 Run Configurations... 选项，在spring boot 选项卡下的 profile项中填写要设置的环境配置文件名，如：dev、pro ，点击运行。（前提是这些配置文件存在）  
命令行部署运行时的设置方式：
```cmd
java -jar demo.jar --spring.profiles.active=pro
```
### Spring boot 项目配置文件加载优先级
1. 项目根目录下创建 /config 文件夹，放置 application.yml/porperties
2. 项目根目录下 /
3. classpath: /config
4. classpath: /  
 
**说明：classpath就是 /src/main/resources 其中，高优先级覆盖低优先级配置。1最高，4最低。 几个优先级配置文件可以同时存在，会形成互补配置**  
使用场景：项目打包好后，我们可以通过命令行参数的形式指定配置文件的新位置，形成互补配置。运维更新时很有用，如：
```cmd
# 假设新上传的外部配置文件路径如： /opt/config/application.yml
java -jar demo.jar --spring.config.location=/opt/config/application.yml

# 如果外部配置文件和jar包位于同一级目录，可省略配置参数，如：
java -jar demo.jar
```
这样可以通过参数加载外部的配置文件用于项目，而不用重新编译打包上传运行等一系列操作  
**注：优先级3、4配置文件不参与打包，不会被打包到jar文件中**  
命令行修改配置，以端口举例
```cmd
java -jar demo.jar --server.port=80 --server.context-path=/demo
```
多个配置以空格分隔

### springboot配置文件支持所有配置属性
[springboot官方配置属性](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/appendix-application-properties.html#common-application-properties)  

### @Conditional注解
作用：判断指定条件是否成功。用于类注解，条件成立后，类组件才会加载到容器。  
几个扩展注解如下：  
@ConditionalOnJava() // 系统的java版本是否符合要求  
@ConditionalOnMissingBean()  // 不存在指定的bean，如果没有返回true，注入到容器  
@ConditionalOnBean() //存在指定的bean  
@ConditinalOnExpress() // 是否满足指定的SpEL  
@ConditionalOnClass()  // 判断项目中是否包含指定的类。如果包含，返回true  
@ConditionalOnWebApplication // 判断是否是一个web应用程序  
@ConditionalOnNotWebApplication  // 不是一个web应用程序 
@ConditionalOnProperty(prefix="spring.http.encoding",value="enabled",matchIfMissing=true) //判断配置文件中是否包含指定的前缀，如果不包含，即默认其包含，默认值：enabled  
@ConditionalOnResource()  //类路径下是否存在指定的资源文件  
... 还有很多  
**都是实现的@Conditional(OnClassCondition.class)接口, OnClassCondition类有一个match方法，匹配返回true,不匹配false。 可以自己定义**  
自动配置类多数必须在一定条件下才能生效；如需要某个类存在，而该类在某个jar包中，项目目前没导入该jar,那么该自动配置就不生效。  
**可以通过在yml/properties配置文件中声明 debug: ture,来开启自动配置生效的配置报告，打印到控制台**
```console
# 自动配置评估报告
============================
CONDITIONS EVALUATION REPORT
============================

# 自动配置类启用的组件
Positive matches:
-----------------

···


# 自动配置类没有启用的组件
Negative matches:
-----------------

···
```

### Springboot 日志配置
logging.file | logging.path | 示例 | 描述 
|---|:--:|:--:|---:
(none) | (none) |  | 只在控制台打印
指定文件名 | (none) | demo.log | 日志输出到demo.log(路径未指定，在程序根目录下生成demo.log)
(none) | 指定目录 | ./logs | 输出到程序根目录下的logs文件夹中，默认文件夹名spring.log  
  
  
**如果都指定了，file仅指定了文件名，path指定了目录，那么组合后生效。如果file同时指定了目录，path不生效，以file为准**  
logging.pattern.console 控制台输出的日志格式  
logging.pattern.file 日志文件中打印的日志格式  
%d 日期格式  如：%d{yyyy-MM-dd HH:mm:ss.SSS}  
%thread 线程名
%-5level 从右向右显示5个字符宽度
%logger{50} 表示logger名最多50个字符
%msg 日志信息
%n 换行


### springboot项目打成.jar包使用内置web窗口部署后静态资源访问  
1. "/**" 访问当前项目的任何资源，静态资源的文件夹
```txt
"classpath:/META-INF/resources",
"classpath:/resources",
"classpath:/static/",
"classpath:/public"
```
localhost/abc  如果所有控制器api都不处理该请求，那么去静态资源路径下找abc  
.js .css .html 均是一样，指定后缀名即可，如：abc.js、abc.js、abc.css  
2. "/**"，如：localhost/ 访问首页，如果未指定页面， index.html默认为首页。


### Thymeleaf 模板引擎
只需要将.html页面放置在template文件路径下即可动渲染。  
在html页面导入thymeleaf命名空间
```html
<html xmlns:th="http://www.thymeleaf.org">
```
#### 语法规则
th:text 替html换标签当前文本内容
th:utext  
th:if  
th:each  
...  
用法
```html
<div>
      <span th:text="${author}">未替换的静态数据</span>
   </div>
   <!-- *{} 读取对象中的属性，只能在对象包裹的作用域使用 -->
   <div th:object = "${user}"  style="border:1px solid red">      
      username：<span th:text ="*{username}"></span><br />
      age: <span th:text ="*{age}"></span><br />
      <ul th:each ="pet:*{pets}">
         <!-- 注：这里写成*{name}不行。因为是th:each，而非th:object。这是一个集合，不是一个对象，只能用th:each  -->
         <li th:text="${pet.name}"></li>
      </ul>
   </div>
   <div>
      <a th:href="@{http://localhost:8080/demo(author=${author})}">@{} 传递查询参数的示例</a> | 
      <a th:href="@{/(author=${author})}">@{} 主机名项目名可省略</a>
   </div>
```

### STS 类搜索 Shift + Ctrl + h

### WebMVC自动配置原理
**以下这些都会自动注册**
1. ContentNegotiatingViewResolver 组合所有的视图解析器，遍历，看哪个合适。如果没有，就解析不了。  
我们可以添加一个视图解析器，如Thymeleaf。   REST API是不需要解析视图的。
2. Converter 转换器。 页面提交的数据，转换成与之匹配的对象，这时就需要使用转换器。
3. Formatter 格式化器。如日期转换。
4. HttpMessageConverter http请求\响应消息转换器。如User对象以json格式数据返回。
5. HttpMessageConverters 从容器获取所有的HttpMessageConverter。 也可以向容器中自定义注入添加
6. MessageCodeResovler 定义错误代码生成规则
7. ConfigurableWebBindingInitilizer 初始化web数据绑定器。请求数据===JavaBean

### 如何修改Spring boot 的默认配置
模式：  
1. Spring boot在自动配置时，先看容器中有没有用户自己配置的（@Bean、@Component)，如果有，就用用户配置的，没有才自动配置。有些组件可以有多个，如ViewReslover，就将用户配置的和默认配置的组合起来。
2. 扩展SpringMVC，如为某个控制器添加拦截器. spring mvc传统格式如下：
```xml
<mvc:view-controller path="/hello" view-name="success" />
<mvc:interceptors>
   <mvc:intercetor>
      <mvc:mapping path="/hello">
      <bean></bean>
   </mvc:intercetor>
</mvc:intercetors>
```
springboot编写配置mvc类，使用@Configuration注解类，比如声明一个拦截器类，需要扩展自WebMvcConfigurerAdapter类(Spring5.0中已弃用，请使用WebMvcConfigurer接口)
### STS中查看某个接口或基类可重写的方法快捷键： ALT+SHIFT+S
### WebMvcAutoConfigurationAdapter 自动配置类
1. 这是spirngboot用于mvc的自动配置类，他实现了WebMvcConfigurer接口  
2. 在做其它配置时导入： @Import(EnableWebMvcConfiguration.class) 作用：使所有的mvc配置生效，包括自动配置的和开发者的扩展配置。如果有重叠，扩展配置会覆盖自动配置的重叠部分。
### @EnableWebMvc
作用：全部接管SpringMVC的所有配置，这将导致Springboot关于mvc的自动配置完全失效。如果仅想扩展部分的自定义配置，请不要在类上标记该注解。  
**为什么使用@EnableWebMvc会导入自动配置完全失效？**  
**原理：@EnableWebMvc会导入WebConfigrationSupport, 而springboot的MVC自动配置类有行一行注解@ConditionalOnMssingBean(WebConfigrationSupport)  只有容器中不存在时才导入，现在存在了。**

### Springboot自动配置好了国际化管理资源组件
MessageResourceAutoConfiguration
#### sts 资源编辑器安装步骤：
1. 去【Eclipse Marketplace...】下载ResourceBundle Editer插件
2. 新建--other--资源--ResourceBundle 资源集（属性文件）,创建各个国际化资源文件
3. 右键创建的.properties文件--open with--资源文本编辑器打开  

thymeleaf #{}取国际化值  
springboot 区域信息解析器：LocaleResolver,获取请求头携带的区域信息，进行区域语言的选择，没有对应语言显示默认。   
Request Headers(请求头语言)：Accept-Language: zh-CN,zh;q=0.9

### Thymeleaf 公共标签区域抽取
```html
<!-- 抽取公共标签。 标签所在页面叫：footer.html -->
<div th:fragment="copy">&copy; 2011 The Good Thymes Virtual Grocery</div>

<!-- 插入公共标签。 footer：表示公共标签所在的页面名称。 -->
<div th:insert="~{footer :: copy}"></div>
<!-- 简写 -->
<div th:insert="footer :: copy"></div>

<!-- 插入标签的3种不同方式 insert 、replace 、include ，如下： -->
<div th:insert="footer :: copy"></div>
<div th:replace="footer :: copy"></div>
<div th:include="footer :: copy"></div>

<!-- insert 实际效果 -->
<div>
  <footer>&copy; 2011 The Good Thymes Virtual Grocery</footer>
</div>

<!-- replace 实际效果。 通常这种是我们想要的。 -->
<footer>&copy; 2011 The Good Thymes Virtual Grocery</footer>

<!-- include 实际效果。 只保留标签内的内容，标签去掉 -->
<div>&copy; 2011 The Good Thymes Virtual Grocery</div>

<!-- id公共标签的抽取方式加# -->
<div id="copy">&copy; 2011 The Good Thymes Virtual Grocery</div>
<div th:replace="footer :: #copy"></div>

<!-- thymeleaf模板引擎 样式类引用示例 
(activeUri='emp') 变量及给变量赋值，下面根据变量值选择套用的样式-->
<div th:replace="header :: #sidebar(activeUri='emp')"></div>
<!-- 三元运算表达式放在大括号里面或外面均可，推荐放在外边。${}里边存放的是变量 -->
<li th:class="${activeUri=='home'?'active':''}"><a th:href="@{/thymeleaf}">首页</a></li>
<li th:class="${activeUri=='emp'?'active':''}"><a th:href="@{/employees}" >雇员列表</a></li>

<!-- 日期格式化 ${#date.format(date,'yyyy-MM-dd HH:mm:ss')} -->
<td th:text="${#date.format(emp.birth,'yyyy-MM-dd HH:mm:ss')}"></td>
```