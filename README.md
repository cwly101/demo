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

### 视图控制器转发请求
1. return "redirect:/requestPath"  // 重定向请求到指定路由
2. return "forward:/requestPath"  // 转发请求到指定路由  
如果直接 return "/requestPath",那么将由thymeleaf视图解析器接手，去template下寻找对应视图，然后渲染返回，是不会进入相应的controller的。

### springboot 错误页面404 原理
如果是浏览器访问，springboot自动生成404错误页面  
如果是其它客户端访问，springboot自动生成json数据返回给相应客户端  
springboot自动配置会智能识别   
自动配置类：ErrorMvcAutoConfiguration 这给容器中添加了如下组件：  
1. DefaultErrorAttributes
```java
// 共享页面的属性值
@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		errorAttributes.put("timestamp", new Date());
		addStatus(errorAttributes, webRequest);
		addErrorDetails(errorAttributes, webRequest, includeStackTrace);
		addPath(errorAttributes, webRequest);
		return errorAttributes;
	}
```
2. BasicErrorController (处理默认的/error请求)
```java
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {

// 产生html类型的数据 （浏览器端访问）
// public static final String TEXT_HTML_VALUE = "text/html";
// 浏览器请求头必然包含 text/html
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections
				.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
	}
// 产生json类型的数据 （某种客户端访问）
@RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		if (status == HttpStatus.NO_CONTENT) {
			return new ResponseEntity<Map<String, Object>>(status);
		}
		Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
		return new ResponseEntity<>(body, status);
	}	
```
3. ErrorPageCustomizer (自定义错误页配置)
```java
   /**
	 * Path of the error controller.
	 */
	@Value("${error.path:/error}")
	private String path = "/error";   // 系统发生错误后，来到error请求进行处理。
```
4. DefaultErrorViewResolver
```java
@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		ModelAndView modelAndView = resolve(String.valueOf(status.value()), model);
		if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
			modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
		}
		return modelAndView;
	}

	private ModelAndView resolve(String viewName, Map<String, Object> model) {
      // 默认springboot去找一个页面： /error/404
		String errorViewName = "error/" + viewName;
      // 如果模板引擎可以解析就用模板引擎
		TemplateAvailabilityProvider provider = this.templateAvailabilityProviders.getProvider(errorViewName,
				this.applicationContext);
      // 如果模板引擎可用
		if (provider != null) {
			return new ModelAndView(errorViewName, model);
		}
      // 不可用，就在静态资源文件夹下找 errorViewName对应的页面
		return resolveResource(errorViewName, model);
	}
```

原理步骤：一旦系统出现4xx和5xx错误时，ErrorPageCustomizer就会生效（定制错误的响应规则）;就会来到/error请求；就会被BasicErrorController处理

1. 如何定制错误页面  
  1.1. 有模板引擎：  
    在templates文件夹下，创建一个error文件夹，创建404.html 页面  
    **可以使用4xx.html和5xx.html表示以4开头（或5开头）的错误，在没有找到精确状态码所对应的页面时，来到笼统4xx或5xx的页面。**  
    页面能获取的信息：  
    timestamp、status、error、exception（springboot2.x后此为空，trace包含此信息）、message、trace、errors(JSR303数据校验错误)  
  1.2. 没有模板引擎：   
  无法渲染获取thymeleaf变量  
  1.3. 静态资源文件夹和模板引擎都没有：  
  默认情况。Springboot的默认错误页面,源码如下：
  ```java
  @Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(prefix = "server.error.whitelabel", name = "enabled", matchIfMissing = true)
	@Conditional(ErrorTemplateMissingCondition.class)
	protected static class WhitelabelErrorViewConfiguration {
      // new StaticView();就是默认的视图，以后台拼串形式返回。
		private final StaticView defaultErrorView = new StaticView();

		@Bean(name = "error")
		@ConditionalOnMissingBean(name = "error")
		public View defaultErrorView() {
			return this.defaultErrorView;
		}
  ```
  
2. 如何定制错误json响应数据
```java
/**
 * 全局异常处理器
 * @author cwly1
 *
 */
@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {

	@ResponseBody
	@ExceptionHandler(CustomException.class)
	public Map<String, Object> handleException(Exception e) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "100");
		map.put("msg", e.getMessage());
		log.info(e.getMessage());
		return map;
	}
}
// 不具备自适应效果，无法对浏览器或其它客户端访问进行区别对待，返回不同数据格式
```
自适应定制：  
步骤1：  
```java
/*======== 自适应效果并携带自己的数据，浏览器或其它客户端访问进行区别对待，返回不同数据格式 =========*/
	
	@ExceptionHandler(CustomException.class)
	public String handleException(
			Exception e,
			HttpServletRequest request
			) {
		Map<String, Object> map = new HashMap<>();
		// 传入自己的错误状态码。4xx或5xx，否则不会进行错误页面。 状态码key，参见源码，如下：
		// Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		request.setAttribute("javax.servlet.error.status_code", 500);
		map.put("code", "100");
		map.put("message", e.getMessage().toString()+"aaa");  
		request.setAttribute("ext", map);  // 将自定义的额外扩展错误提示属性添加到请求属性中
		log.info(e.getMessage());
		// 转发到/error， 这里springboot进行了自适应配置
		return "forward:/error";
	}
```
步骤2：
```java
package com.cw.common;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * 注入自定义的错误属性
 * 说明：自适应效果并携带自己的数据，浏览器或其它客户端访问进行区别对待，返回不同数据格式
 * @author cwly1
 *
 */
@Component
public class DemoErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		errorAttributes.put("company", "cw");   // 添加自定义错误标识 
//		errorAttributes.remove("trace");   //移除系统提交的不想要的错误属性，如trace
		
		/**
		 * 源码：
		 * Object getAttribute(String name, int scope);
		 * int SCOPE_REQUEST = 0;
		 * int SCOPE_SESSION = 1;
		 * 这里我们使用的是 request域的数据
		 */
		Map<String, Object> ext = (Map<String, Object>) webRequest.getAttribute("ext", 0);  // 读取在全局异常处理器中设置的错误提示扩展属性。
		errorAttributes.put("ext", ext);
		return errorAttributes;
	}

}

```
### Springboot 嵌入式servlet容器
1. 如何定制和配置servlet容器配置。在ServerProperties类（org.springframework.boot.autoconfigure.web包下）,支持servlet容器如下：
```java
private final Servlet servlet = new Servlet();

	private final Tomcat tomcat = new Tomcat();

	private final Jetty jetty = new Jetty();

	private final Netty netty = new Netty();

	private final Undertow undertow = new Undertow();
```
.yml配置见：[springboot官方Server Properties配置文档](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/appendix-application-properties.html#server-properties)  
我使用的Undertow  
#### 嵌入式servlet容器自动配置原理：  
ServletWebServerFactoryConfiguration（package org.springframework.boot.autoconfigure.web.servlet;）
```java
@Configuration(proxyBeanMethods = false)
class ServletWebServerFactoryConfiguration {

	@Configuration(proxyBeanMethods = false)
	// 条件：必须存在的类
	@ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
	// 条件：必须没有的Bean
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat { 
		@Bean
		public TomcatServletWebServerFactory tomcatServletWebServerFactory(
				ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
				ObjectProvider<TomcatContextCustomizer> contextCustomizers,
				ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {
				...
				}
	 }

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ Servlet.class, Server.class, Loader.class, WebAppContext.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedJetty { ... }

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedUndertow { ... }
}
```
ServletWebServerFactory (servlet容器工厂)
```java
@FunctionalInterface
public interface ServletWebServerFactory {

	// 获取web server容器
	WebServer getWebServer(ServletContextInitializer... initializers);

}
```
web server （servlet容器，即web服务器）  
```java
public interface WebServer {

	/**
	 * Starts the web server. Calling this method on an already started server has no
	 * effect.
	 * @throws WebServerException if the server cannot be started
	 */
	void start() throws WebServerException;

	/**
	 * Stops the web server. Calling this method on an already stopped server has no
	 * effect.
	 * @throws WebServerException if the server cannot be stopped
	 */
	void stop() throws WebServerException;

	int getPort();

}
```
**STS查看接口的实现类：Crtl+T**  
WebServer的实现类：JettyWebServer、NettyWebServer、TomcatWebServer、UndertowWebServer、UndertowServletWebServer  

是如何实现的自动配置呢？  
ServletWebServerFactoryConfiguration检索看存在哪个WebServer的实现类，就配置哪个servlet容器。在pom.xml中依赖了哪个嵌入式web容器，就会检索到哪个。  

以TomcatServletWebServerFactory（package org.springframework.boot.web.embedded.tomcat; 注：这已经不在springboot自动配置jar包下，而在springboot-starter-web的jar下）为例：
```java
public class TomcatServletWebServerFactory extends AbstractServletWebServerFactory
		implements ConfigurableTomcatWebServerFactory, ResourceLoaderAware {
			...
		@Override
	public WebServer getWebServer(ServletContextInitializer... initializers) { 
		Tomcat tomcat = new Tomcat();  // 创建一个tomact
		// 配置tomcat的基本环节
		File baseDir = (this.baseDirectory != null) ? this.baseDirectory : createTempDir("tomcat");
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		Connector connector = new Connector(this.protocol);
		connector.setThrowOnFailure(true);
		tomcat.getService().addConnector(connector);
		customizeConnector(connector);
		tomcat.setConnector(connector);
		tomcat.getHost().setAutoDeploy(false);
		configureEngine(tomcat.getEngine());
		for (Connector additionalConnector : this.additionalTomcatConnectors) {
			tomcat.getService().addConnector(additionalConnector);
		}
		prepareContext(tomcat.getHost(), initializers);
		return getTomcatWebServer(tomcat);   
		// 返回一个嵌入式web容器，同时启动。详见：getTomcatWebServer函数内部源码
	}
}
```
#### 对嵌入式容器配置的修改如何生效
1. yml配置文件修改，在ServerProperties类,见： [springboot官方Server Properties配置文档](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/appendix-application-properties.html#server-properties)    

2. 在2.x版本改为实现org.springframework.boot.web.server.WebServerFactoryCustomizer接口的customize方法。
```java

@Component
public class EmbeddedTomcatConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
 
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        ((TomcatServletWebServerFactory)factory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                protocol.setMaxConnections(200);
                protocol.setMaxThreads(200);
                protocol.setSelectorTimeout(3000);
                protocol.setSessionTimeout(3000);
                protocol.setConnectionTimeout(3000);
            }
        });
    }
}

``` 
后置处理器概念：[BeanPostProcessor简介](https://www.jianshu.com/p/369a54201943)

---

### Servlet容器启动原理
4. onRefresh(); web的ioc容器重写了onRefresh()方法
5. 由web ioc容器创建嵌入式的servlet容器
6. 获取嵌入式的servlet容器工厂
7. 使用容器工厂获取嵌入式的servlet容器
8. 嵌入式的servlet容器创建对象并启动web容器  
**先启动web容器，再将ioc容器中剩下没有创建出的对象获取出来。这些对象就是：controller、service等**

#### 内置Web容器项目 与 外置Web容器项目
##### 内置web容器项目：  
1. .jar 项目 
2. 直接运行程序启动类（DemoApplication）的main方法，并不需要编写一个SpringBootServletInitializer的子类
##### 外置web容器项目：
1. .war 项目
2. 打包时要将内置web容器排除，在pom.xml使用，如:
```xml
<!-- <scope>provided</scope> 目标环境已经存在该容器，打包时不打包含该容器， -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-undertow</artifactId>
	<scope>provided</scope>
</dependency>
```
3. 必须编写一个SpringBootServletInitializer的子类，实现configure方法：
```java
package com.cw;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// 向servlet初始化器传入springboot主程序类
		return application.sources(DemoApplication.class);
	}

}

```
**注：这将导致以外部web容器配置文件配置的信息为准，开发时内部web容器配置的信息将失效。**  

#### 原理
jar包： 执行springboot主程序类的main方法，启动ioc容器，创建嵌入式的servlet容器。  
war包： 启动服务器，服务器启动springboot应用，启动ioc容器; 服务器就是通过SpringBootServletInitializer的子类实现的configure方法，启动的springboot主程序。

# 在Linux(CentOS7)上安装Docker步骤
1. 使用远程客户端（如SmarTTY）连接上CentOS7系统。  
  1.1. 获取远程主机IP，确保其连接上了网络。 查看服务器：ip addr  
  1.2. SmarTTY客户端输入用户名密码登录
2. 查看CentOS7 内核版本，命令：uname -r  内核版本必须高于3.10才能安装Docker 
3. 安装：yum install docker
4. 完成后，查询软件是否成功安装：rpm -qa | grep docker , 如下表示成功
```shell
root@192.168.98.128:~# rpm -qa | grep docker
docker-1.13.1-103.git7f2769b.el7.centos.x86_64
docker-common-1.13.1-103.git7f2769b.el7.centos.x86_64
docker-client-1.13.1-103.git7f2769b.el7.centos.x86_64
```
5. 启动docker: systemctl start docker
6. 查看运行状态： systemctl status docker
```shell
root@192.168.98.128:~# systemctl status docker
● docker.service - Docker Application Container Engine
   Loaded: loaded (/usr/lib/systemd/system/docker.service; disabled; vendor preset: disabled)
   Active: active (running) since 日 2019-11-03 11:02:55 CST; 1min 33s ago
     Docs: http://docs.docker.com
 Main PID: 4525 (dockerd-current)
    Tasks: 27
   CGroup: /system.slice/docker.service
           ├─4525 /usr/bin/dockerd-current --add-runtime docker-runc=/usr/libexec/docker/docker-runc-current --default-runtime=docker-runc --exec-opt native.cgroupdriver=sys...
           └─4533 /usr/bin/docker-containerd-current -l unix:///var/run/docker/libcontainerd/docker-containerd.sock --metrics-interval=0 --start-timeout 2m --state-dir /var/...

```
7. 将docker设置为开机启动。 systemctl enable docker
```shell
root@192.168.98.128:~# systemctl enable docker
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
```

## docker 附录
1. 停止docker命令：  systemctl stop docker  
2. 开机禁用某个服务，如防火墙：systemctl disable firewalld

## 使用docker search命令错误解决方法
问题如下：
```shell
root@192.168.31.142:~# docker search mysql
Error response from daemon: Get https://index.docker.io/v1/search?q=mysql&n=25: dial tcp: lookup index.docker.io on [::1]:53: read udp [::1]:43884->[::1]:53: read: connection refused
```
[解决方案](https://blog.csdn.net/heliu3/article/details/92587849)  
查看服务器DNS网络配置

vi /etc/resolv.conf 把里面的内容清除，并改为：

nameserver 192.168.31.1  (查询自己的当前的DNS，以它为准)  

重启网络服务
systemctl restart network

[Docker国内镜像源配置方法](https://www.jianshu.com/p/18bb54978bc0)  
[网易云镜像中心](https://c.163yun.com/hub#/library)  
出现如下表示安装完成
```shell
root@192.168.31.142:~# docker pull hub.c.163.com/public/mongodb:3.2.0
Trying to pull repository hub.c.163.com/public/mongodb ... 
3.2.0: Pulling from hub.c.163.com/public/mongodb
46c1c8bc0bb3: Pull complete 
a3ed95caeb02: Pull complete 
9bf4fa82dc06: Pull complete 
a83bf6dbf3fb: Pull complete 
ced51798fca6: Pull complete 
65edf32fac16: Pull complete 
c83896179f6e: Pull complete 
536e2325b257: Pull complete 
24d9aee54d10: Pull complete 
79ea2280bdff: Pull complete 
a5545b572972: Pull complete 
5bc663d1f626: Pull complete 
78dc0139f16f: Pull complete 
e36375d8bd5b: Pull complete 
e59e45f7e850: Pull complete 
bb8b955587a9: Pull complete 
Digest: sha256:c14907551b81b8a3d64dd983eab0fc448ce110a2e85c802d60d84a481b23b5c9
Status: Downloaded newer image for hub.c.163.com/public/mongodb:3.2.0
```
docker images # 查看所有本地的镜像列表  
```shell
root@192.168.31.142:~# docker images
REPOSITORY                     TAG                 IMAGE ID            CREATED             SIZE
hub.c.163.com/public/mongodb   3.2.0               ed27e79af407        3 years ago         378 MB
```
docker rmi  # 删除指定的镜像ID对应的镜像文件
```shell
docker rmi ed27e79af407
```
## Docker容器操作
运行镜像---产生一个容器（正在运行的软件）  
### 运行docker容器
--name（可选）:表示给容器起个名字.  
-d:表示后台运行.  
-d后面跟镜像的名字:标签，如：hub.c.163.com/library/mongo    先使用docker images 查看本地存在的镜像)  
-p:表示主机端口要映射到的容器端口号,即主机端口：容器端口
```shell
docker run --name mongodb-docker -d hub.c.163.com/library/mongo
docker run --name mongodb-docker -d -p 27017:27017 hub.c.163.com/library/mongo 
docker run --name mysql-docker -e MYSQL_ROOT_PASSWORD=123456 -d -p 3306:3306 hub.c.163.com/library/mysql
# 注意：新增加的容器要运行，请使用docker start 容器ID 命令
```
出现如下表示运行成功：
```shell
root@192.168.31.142:~# docker run --name mongodb-docker -d hub.c.163.com/library/mongo
8d43ee5341d8dd609a6be03f3012827542f8252260fe2a0b5130529d88f07222
```
### 查看运行中的容器
docker ps # 查看哪些容器在运行中
```shell
# 查看所有的容器，包括曾经运行过的容器
docker ps -a 
```
### 停止运行中的容器
```shell
# stop后面跟容器ID或容器名字
docker stop mongodb-docker
```
### 运行过的容器重新启动
```shell
# 容器ID或容器名字
docker start mongodb-docker
```
### 删除容器
rm 删除容器  
rmi 删除镜像文件
```shell
# 容器ID或容器名字
docker rm mongodb-docker
```
### 查看容器日志
docker logs 容器ID或容器名字

### 进入到docker容器的mongo环境中
容器ID或容器名称  
docker exec -it 80c25b29df67 bash、  
docker exec -it 05ad3849eed4 redis-cli
```shell
root@192.168.31.142:~# docker exec -it 80c25b29df67 bash
root@80c25b29df67:/# mongo
MongoDB shell version v3.4.7
connecting to: mongodb://127.0.0.1:27017
MongoDB server version: 3.4.7
Welcome to the MongoDB shell.
For interactive help, type "help".
For more comprehensive documentation, see
        http://docs.mongodb.org/
Questions? Try the support group
        http://groups.google.com/group/mongodb-user
Server has startup warnings: 
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] 
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] ** WARNING: Access control is not enabled for the database.
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] **          Read and write access to data and configuration is unrestricted.
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] 
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] 
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] ** WARNING: /sys/kernel/mm/transparent_hugepage/enabled is 'always'.
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] **        We suggest setting it to 'never'
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] 
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] ** WARNING: /sys/kernel/mm/transparent_hugepage/defrag is 'always'.
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] **        We suggest setting it to 'never'
2019-11-04T02:13:34.599+0000 I CONTROL  [initandlisten] 
> show dbs
admin  0.000GB
local  0.000GB
> exit;
root@80c25b29df67:/# exit;
exit
root@192.168.31.142:~#
```