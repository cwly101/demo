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
 
**说明：classpath就是 /src/main/resources 其中，高优先级覆盖低优先级配置。1最高，4最低**
