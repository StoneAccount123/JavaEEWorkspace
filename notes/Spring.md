[TOC]



# Spring

### HelloWorld

#### xml方式

实体类

```java
public class Hello {
    public void sayHello(){
        System.out.println("Hello World!");
    }
}
```

xml配置文件

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="hello" class="Hello"></bean>
</beans>
```

测试类

```java
public class Test {
    @org.junit.Test
    public void testHello(){
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        Hello hello = context.getBean("hello", Hello.class);
        hello.sayHello();
    }
}
```

**注意点**：1.xml中的class为全类名

​				2.ClassPathXmlApplicationContext相当于从src或者resource开始，还有FileSystemXmlApplicationContext则是计算盘符位置。



#### 注解方式

``` java
@component
class xxx{
}

@Configuration//配置类
@ComponentScan(basePackages = "hello")
	public class Config {
	}

@org.junit.Test
    public void matestHello() throws SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
        userDao.add();
    }
```





### IOC

#### 原理

​	xml 解析、工厂模式、反射

​	IOC容器底层就是一个对象工厂，通过xml解析拿到全类名，然后通过反射创建对象。

#### 两种实现方式

* BeanFactory：IOC 容器基本实现，是 Spring 内部的使用接口，不提供开发人员进行使用 ，加载配置文件时候不会创建对象，在获取对象（使用）才去创建对象 

* ApplicationContext：BeanFactory 接口的子接口，提供更多更强大的功能，一般由开发人员进行使用加载配置文件时候就会把在配置文件对象进行创建。



#### 基于 xml 配置文件方式实现bean管理

###### 创建对象

```xml
<bean id="hello" class="com.demo.pojo.Hello"></bean>
```

* 在 spring 配置文件中，使用 bean 标签，标签里面添加对应属性，就可以实现对象创建 

* 在 bean 标签有很多属性，常用的属性 
  * id 属性：唯一标识 
  * class 属性：类全路径（包类路径） 
* 创建对象时候，默认也是执行无参数构造方法完成对象创建

###### set实现依赖注入

``` xml
bean id="book" class="com.atguigu.spring5.Book">
 <!--使用 property 完成属性注入
 name：类里面属性名称
 value：向属性注入的值
 -->
 <property name="bname" value="易筋经"></property>
 <property name="bauthor" value="达摩老祖"></property>
</bean>
```

* 这种方式是利用类本身的set方法实现的

###### 有参构造器实现依赖注入

```xml
<bean id="orders" class="com.atguigu.spring5.Orders">
 <constructor-arg name="oname" value="电脑"></constructor-arg>
 <constructor-arg name="address" value="China"></constructor-arg>
</bean>
```

###### p 名称空间注入

```xml
<!--引入命名空间-->
<beans ...
       xmlns:p="http://www.springframework.org/schema/p">
<!--用p名称空间注入-->
    <bean id="book" class="pojo.Book" p:name="chen" p:price="123"></bean>
</beans>
```

* 这种方式也是利用类本身的set方法实现的

###### xml注入字面量

```xml
<!--null 值-->
<property name="address">
	<null/>
</property>

<!--属性值包含特殊符号
	把带特殊符号内容写到 CDATA
-->
<property name="address">
 <value><![CDATA[<<南京>>]]></value>
</property>
```

###### xml注入外部bean

``` xml
<bean id="userService" class="com.atguigu.spring5.service.UserService">
 	<!--注入 userDao 对象
 	name 属性：类里面属性名称
 	ref 属性：创建 userDao 对象 bean 标签 id 值
 	-->
 	<property name="userDao" ref="userDaoImpl"></property>
</bean>
<bean id="userDaoImpl" class="com.atguigu.spring5.dao.UserDaoImpl"></bean>
```

​	用ref指向外部bean的id

###### xml注入内部bean

```xml
<bean id="person" class="pojo.Person">
    <property name="name" value="zhang"></property>
    <property name="age" value="16"></property>
    <property name="book" >
        <bean class="pojo.Book" id="book2" p:name="bname" p:price="112"></bean>
    </property>
</bean>
```

​	在bean的内部配置bean

###### xml级联赋值

```xml
<bean id="person" class="pojo.Person">
        <property name="name" value="zhang"></property>
        <property name="age" value="16"></property>
        <property name="book" >
            <bean class="pojo.Book" id="book2">
            </bean>
        </property>
        <property name="book.price" value="12"></property>
        <property name="book.name" value="banme"></property>
 </bean>
```

​	注入对象值后可以通过对象.属性的方式实现级联赋值	**需要有对应对象的get方法**。

###### xml集合属性赋值

```xml
<bean id="stu" class="com.atguigu.spring5.collectiontype.Stu">
<!--数组类型属性注入-->
	<property name="courses">
 		<array>
 			<value>java 课程</value>
 			<value>数据库课程</value>
 		</array>
 	</property>
 <!--list 类型属性注入-->
 	<property name="list">
 		<list>
 			<value>张三</value>
 			<value>小三</value>
 		</list>
 	</property>
 <!--map 类型属性注入-->
 	<property name="maps">
 		<map>
 			<entry key="JAVA" value="java"></entry>
 			<entry key="PHP" value="php"></entry>
 		</map>
 	</property>
 <!--set 类型属性注入-->
 	<property name="sets">
        <set>
 			<value>MySQL</value>
 			<value>Redis</value>
 		</set>
 	</property>
</bean>


<!--注入 list 集合类型，值是对象-->
<bean id="course1" class="com.atguigu.spring5.collectiontype.Course">
 	<property name="cname" value="Spring5 框架"></property>
</bean>
<bean id="course2" class="com.atguigu.spring5.collectiontype.Course">
 	<property name="cname" value="MyBatis 框架"></property>
</bean>
<property name="courseList">
 	<list>
 		<ref bean="course1"></ref>
 		<ref bean="course2"></ref>
 	</list>
</property>

<!--1 提取 list 集合类型属性注入-->
<util:list id="bookList">
	<value>易筋经</value>
 	<value>九阴真经</value>
 	<value>九阳神功</value>
</util:list>
<!--2 提取 list 集合类型属性注入使用-->
<bean id="book" class="com.atguigu.spring5.collectiontype.Book">
 	<property name="list" ref="bookList"></property>
</bean>
```



##### 工厂bean

Spring 有两种类型 bean

* 普通 bean：在配置文件中定义 bean 类型就是返回类型 
* 工厂 bean：在配置文件定义 bean 类型可以和返回类型不一样 
  * 创建类，让这个类作为工厂 bean，实现接口 FactoryBean 
  * 写泛型，实现接口里面的方法，在实现的方法中定义返回的 bean 类型和属性。

``` java
public class MyBean implements FactoryBean<Course> {
 	//定义返回 bean
 	@Override
	 public Course getObject() throws Exception {
 		Course course = new Course();
	 	course.setCname("abc");
	 	return course;
 	}
    //返回bean的类型
	@Override
 	public Class<?> getObjectType() {
 		return Course.class;
 	}
    //是否为单例
	@Override
	public boolean isSingleton() {
 		return false;
 	}
}

<bean id="myBean" class="com.atguigu.spring5.factorybean.MyBean"></bean>
    
@Test
public void test3() {
 	ApplicationContext context =
 	new ClassPathXmlApplicationContext("bean3.xml");
 	Course course = context.getBean("myBean", Course.class);
 	System.out.println(course);
}
```

##### bean的作用域

* 在 Spring 里面，默认情况下，bean 是单实例对象 

* 在 spring 配置文件 bean 标签里面有属性（scope）：singleton表示是单实例对象，prototype表示是多实例对象，默认为单实例

* 单实例**加载 spring 配置文件时候**就会创建单实例对象，多实例**在调用 getBean 方法时候**创建多实例对象。

  

##### bean的生命周期

* 通过构造器创建 bean 实例（无参数构造）
* 为 bean 的属性设置值和对其他 bean 引用（调用 set 方法） 
* 调用 bean 的初始化的方法（需要进行配置初始化的方法 initMethod）
* bean 可以使用了（对象获取到了）
* 当容器关闭时候，调用 bean 的销毁的方法（需要进行配置销毁的方法 destroyMethod）

``` java
public class Thing {
    private String attribute;

    public Thing() {
        System.out.println("构造器");
    }

    public void setAttribute(String attribute) {
        System.out.println("设置属性");
        this.attribute = attribute;
    }

    public void initMethod(){
        System.out.println("初始化方法");
    }

    public void destroyMethod(){
        System.out.println("销毁方法");
    }
}

//将初始化和销毁的方法与类方法绑定
<bean class="pojo.Thing" id="thing" p:attribute="value" init-method="initMethod" destroy-method="destroyMethod"></bean>


@org.junit.Test
    public void matestHello(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Springconfig/bean.xml");
        Thing thing = context.getBean("thing", Thing.class);
        System.out.println("得到了对象");
    	//手动销毁
        context.close();
}
```



bean 的后置处理器，需要实现接口BeanPostProcessor，bean 生命周期有七步 

* 通过构造器创建 bean 实例（无参数构造）
* 为 bean 的属性设置值和对其他 bean 引用（调用 set 方法）
* 把 bean 实例传递 bean 后置处理器的方法 postProcessBeforeInitialization  
* 调用 bean 的初始化的方法（需要进行配置初始化的方法）
* 把 bean 实例传递 bean 后置处理器的方法 postProcessAfterInitialization 
* bean 可以使用了（对象获取到了） 
* 当容器关闭时候，调用 bean 的销毁的方法（需要进行配置销毁的方法）

``` java
//创建处理器类
public class MyProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化之前处理");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化之后处理");
        return bean;
    }
}
//在配置文件中配置
<bean class="pojo.MyProcessor" id="myProcessor"></bean>

```



##### xml自动装配

``` xml
<!--实现自动装配
 bean 标签属性 autowire，配置自动装配
 autowire 属性常用两个值：
 byName 根据属性名称注入 ，注入值 bean 的 id 值和类属性名称一样
 byType 根据属性类型注入
-->
<bean id="emp" class="com.atguigu.spring5.autowire.Emp" autowire="byType">
 <!--<property name="dept" ref="dept"></property>-->
</bean>
<bean id="dept" class="com.atguigu.spring5.autowire.Dept"></bean>
```



##### 外部属性文件（配置文件）

``` xml
<!--引入 context 名称空间-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/context 
			http://www.springframework.org/schema/context/spring-context.xsd">
	<!--引入外部属性文件-->
    <context:property-placeholder location="classpath:jdbc.properties"/>
    
    <!--用${}-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
 		<property name="driverClassName" value="${prop.driverClass}"></property>
 		<property name="url" value="${prop.url}"></property>
 		<property name="username" value="${prop.userName}"></property>
 		<property name="password" value="${prop.password}"></property>
	</bean>
</beans>
```

```properties
jdbc.username=root
jdbc.password=123456
jdbc.url=jdbc:mysql://localhost:3306/ssm_crud
jdbc.driverClass=com.mysql.jdbc.Driver
```





#### 基于注解方式实现实现bean管理

##### 组件注解

* @Component
* @Service
* @Controller
* @Repository

​	注解的value属性对应组件的id值，不指定默认为类名首字母小写

##### 开启组件扫描

* xml方式

```xml
<context:component-scan base-package="com.atguigu"></context:component-scan>


<!--自定义组件扫描-->
<!--只扫描@Controller-->
<context:component-scan base-package="com.atguigu" use-defaultfilters="false">
 <context:include-filter type="annotation" 
expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
<!--不扫描@Controller-->
<context:component-scan base-package="com.atguigu">
 <context:exclude-filter type="annotation" 
expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

* 注解方式

```java
@Configuration//配置类
@ComponentScan(basePackages = "dao")
	public class Config {
	}

@org.junit.Test
    public void matestHello() throws SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
        userDao.add();
    }
```



##### 属性注入

* @Autowired：根据属性类型进行自动装配

* @Qualifier：根据名称进行注入，需要和@Autowired一起使用 

  ​			例：	@Autowired

  ​						@Qualifier(value = "userDaoImpl1") 

* @Resource：可以根据类型注入，也可以根据名称注入。看有无设置value属性

* @Value：注入普通类型属性

  

### AOP

##### 底层原理

AOP 底层使用动态代理 

*  有接口情况，使用 JDK 动态代理，创建接口实现类代理对象，增强类的方法
* 没有接口情况，使用 CGLIB 动态代理

##### 术语

* 连接点：那些方法可以被增强
* 切入点：实际真正被增强的方法
* 通知（增强）：实际被增强的逻辑部分，分为
  * 前置通知
  * 后置通知
  * 环绕通知
  * 异常通知
  * 最终通知
* 切面：把通知应用到切入点的过程

##### 切入点表达式

​	execution([权限修饰符] [返回类型] [类全路径] \[方法名称]([参数列表]) )

* 对 com.atguigu.dao.BookDao 类里面的 add 进行增强 execution(* com.atguigu.dao.BookDao.add(..)) 
* 对 com.atguigu.dao.BookDao 类里面的所有的方法进行增强 execution(* com.atguigu.dao.BookDao.* (..)) 
* 对 com.atguigu.dao 包里面所有类，类里面所有方法进行增强 execution(* com.atguigu.dao.\*.\* (..))



##### 注解实现AOP

```xml
<!--引入依赖-->
<!--AspectJ 开始-->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjtools</artifactId>
    <version>1.9.5</version>
</dependency>

<dependency>
    <groupId>aopalliance</groupId>
    <artifactId>aopalliance</artifactId>
    <version>1.0</version>
</dependency>

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.0</version>
</dependency>

<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.3.0</version>
</dependency>
<!--AspectJ 结束-->
```



```java
//配置类
@Configuration
@ComponentScan(basePackages = "dao")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Config {
}
```

``` java
//被增强的类
@Component
public class User {
    public void add(){
        System.out.println("add");
    }
}
```

```java
//代理类
@Component
@Aspect
public class UserProxy {

    @Before(value = "execution(* dao.*.*(..))")
    public void before(){
        System.out.println("前置通知.........");
    }

    @AfterReturning(value = "execution(* dao.*.*(..))")
    public void afterReturning() {
        System.out.println("后置通知.........");
    }

    @After(value = "execution(* dao.*.*(..))")
    public void after() {
        System.out.println("最终通知.........");
    }

    @AfterThrowing(value = "execution(* dao.*.*(..))")
    public void afterThrowing() {
        System.out.println("异常通知.........");
    }

    @Around(value = "execution(* dao.*.*(..))")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕之前.........");
        //被增强的方法执行
        proceedingJoinPoint.proceed();
        System.out.println("环绕之后.........");
    }
}
```

```java
//测试方法
@org.junit.Test
    public void matestHello() throws SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        User user = applicationContext.getBean("user", User.class);
        user.add();
   }
```



##### 通知类别

* @Before前置通知
* @AfterReturning后置通知/返回通知
* @After最终通知
* @AfterThrowing异常通知
* @Around环绕通知

执行次序：

​	没有异常时：

​		环绕之前.........
​		前置通知.........
​		方法执行........
​		后置通知.........
​		最终通知.........

​		环绕之后........

​	有异常时：

​		环绕之前.........
​		前置通知.........
​		方法执行........
​		异常通知.........
​		最终通知.........

##### 切入点抽取

```java
@Pointcut(value = "execution(* dao.*.*(..))")
public void pointcut(){
}

@Before(value = "pointcut()")
public void before(){
    System.out.println("前置通知.........");
}
```

##### 设置增强类优先级

在增强类上面添加注解 @Order(数字类型值)，数字类型值越小优先级越高，默认为32为2进制数最大值。

在切入点前的通知优先级越高越先执行，在嵌入点后的通知优先级越高越后执行。



##### xml方式

仅xml

```xml
<!--创建对象-->
<bean id="book" class="com.atguigu.spring5.aopxml.Book"></bean>
<bean id="bookProxy" class="com.atguigu.spring5.aopxml.BookProxy"></bean>
3、在 spring 配置文件中配置切入点
<!--配置 aop 增强-->
<aop:config>
 <!--切入点-->
 <aop:pointcut id="p" expression="execution(* 
com.atguigu.spring5.aopxml.Book.buy(..))"/>
 <!--配置切面-->
 <aop:aspect ref="bookProxy">
 <!--增强作用在具体的方法上-->
 <aop:before method="before" pointcut-ref="p"/>
 </aop:aspect>
</aop:config>
```



xml和注解混用

```xml
<!-- 开启注解扫描 -->
 <context:component-scan basepackage="com.atguigu.spring5.aopanno"></context:component-scan>

<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```



### JDBC

##### 配置

maven依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>${spring.version}</version>
</dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework/spring-orm -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
    <version>${spring.version}</version>
</dependency>
```

属性配置

```java
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssm_crud
jdbc.username=root
jdbc.password=123456
```

数据库配置类

```java
@PropertySource("classpath:jdbc.properties")
public class JdbcConfig {

    @Value("${jdbc.driverClass}")
    private String driverClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean()
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name="jdbcTemplate")
    public JdbcTemplate createJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
```

Spring配置类

```java
@Configuration
@ComponentScan(basePackages = "com")
@Import(JdbcConfig.class)
public class Config {
}
```

##### 实现增删改查

* 实体类

```java
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;

    private String username;

    private String ustatus;
}
```

* service层

```java
@Service
public class Userservice {

    //抽象类注入其实现子类
    @Autowired
    UserDao userDao;

    public int add(User user){
        return userDao.add(user);
    }

    public int delete(int id){
        return userDao.delete(id);
    }

    public int update(User user){
        return userDao.update(user);
    }

    public User getById(int id){
        return userDao.getById(id);
    }

    public List<User> getAll(){
        return userDao.getAll();
    }
}
```

* dao层

```java
public interface UserDao {
    public int add(User user);

    public int delete(int id);

    public int update(User user);

    public User getById(int id);

    public List<User> getAll();
}
```

```java
@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(User user) {
        String sql = "insert into t_user values (?,?,?)";
        int update = jdbcTemplate.update(sql, user.getUserId(), user.getUsername(), user.getUstatus());
        return update;
    }

    @Override
    public int delete(int id) {
        String sql = "delete from t_user where user_id=?";
        return jdbcTemplate.update(sql,id);
    }

    @Override
    public int update(User user) {
        String sql = "update t_user set username=?,ustatus=? where user_id=?";
        return jdbcTemplate.update(sql,user.getUsername(),user.getUstatus(),user.getUserId());
    }

    @Override
    public User getById(int id) {
        String sql = "select * from t_user where user_id=?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),id);
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from t_user";
        List<User> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return query;
    }
    
    //补充 查询结果为一个值
	@Override
	public int selectCount() {
 		String sql = "select count(*) from t_book";
 		Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
 		return count;
	}
}
```

* 测试类

```java
@org.junit.Test
    public void matestHello() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        Userservice userservice = applicationContext.getBean("userservice", Userservice.class);
        userservice.add(new User(2,"李","在线"));
        userservice.add(new User(3,"牛","离开"));
        System.out.println(userservice.getById(1));
        userservice.update(new User(1,"李","在线"));
        System.out.println(userservice.getAll());
        userservice.delete(1);
        System.out.println(userservice.getAll());
    }
```



##### 批量操作

利用batchUpdate(String sql,Object[]batchArgs)

```java
//批量添加
@Override
public void batchAddBook(List<Object[]> batchArgs) {
 String sql = "insert into t_book values(?,?,?)";
 int[] ints = jdbcTemplate.batchUpdate(sql, batchArgs);
 System.out.println(Arrays.toString(ints));
}
//批量添加测试
List<Object[]> batchArgs = new ArrayList<>();
Object[] o1 = {"3","java","a"};
Object[] o2 = {"4","c++","b"};
Object[] o3 = {"5","MySQL","c"};
batchArgs.add(o1);
batchArgs.add(o2);
batchArgs.add(o3);
//调用批量添加
bookService.batchAdd(batchArgs);
```

```java
//批量修改
@Override
public void batchUpdateBook(List<Object[]> batchArgs) {
 String sql = "update t_book set username=?,ustatus=? where user_id=?";
 int[] ints = jdbcTemplate.batchUpdate(sql, batchArgs);
 System.out.println(Arrays.toString(ints));
}
//批量修改
List<Object[]> batchArgs = new ArrayList<>();
Object[] o1 = {"java0909","a3","3"};
Object[] o2 = {"c++1010","b4","4"};
Object[] o3 = {"MySQL1111","c5","5"};
batchArgs.add(o1);
batchArgs.add(o2);
batchArgs.add(o3);
//调用方法实现批量修改
bookService.batchUpdate(batchArgs);
```

```java
//批量删除
@Override
public void batchDeleteBook(List<Object[]> batchArgs) {
 String sql = "delete from t_book where user_id=?";
 int[] ints = jdbcTemplate.batchUpdate(sql, batchArgs);
 System.out.println(Arrays.toString(ints));
}
//批量删除
List<Object[]> batchArgs = new ArrayList<>();
Object[] o1 = {"3"};
Object[] o2 = {"4"};
batchArgs.add(o1);
batchArgs.add(o2);
//调用方法实现批量删除
bookService.batchDelete(batchArgs);
```

##### 事务

**Spring事务操作** 

* 事务添加到 JavaEE 三层结构里面 Service 层（业务逻辑层） 
* 在 Spring 进行事务管理操作有两种方式：编程式事务管理和声明式事务管理（使用）
* 声明式事务管理 （1）基于注解方式（使用） （2）基于 xml 配置文件方式 
* 在 Spring 进行声明式事务管理，底层使用 AOP 原理 
* Spring 事务管理 API 提供一个接口，代表事务管理器，这个接口针对不同的框架提供不同的实现类

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220818095518086.png" alt="image-20220818095518086" style="zoom:70%;" align="left"/>



**操作**

```java
//在数据库配置类中加入注解 并配置TransactionManager
@EnableTransactionManagement

@Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
```

```java
//在service方法上加@Transactional注解  
@Transactional
    public boolean inde(){
        dao.add();
        int a = 0/0;
        dao.decr();
        return true;
    }
```

##### 事务操作（声明式事务管理参数配置）

注解@Transactional可以配置事务相关参数

* propagation：事务传播行为：多事务方法直接进行调用，这个过程中事务 是如何进行管理的 

  ![preview](https://pic2.zhimg.com/v2-bb91a9b8464e6c3838a340d1a822e475_r.jpg)

  * PROPAGATION_REQURIED所有方法公用一个事务，要么一起成功提交，要么一起失败回滚。如果嵌套执行的方法要求一起执行成功或者一起回滚，则选择该事物传播级别。
  * PROPAGATION_REQURIED_NEW所有方法使用各自的事务，各自提交或者回滚各自的事务，相互之间不会造成影响。如果嵌套执行的方法要求各自事务独立，不能进行相互影响，则选择本事务传播级别。
  * PROPAGATION_SUPPORTS如果存着事务就加入和PROPAGATION_REQUIRED传播级别一致，如果当前不存在事务，则不会创建新的事务，以非事务的方式执行。如果嵌套执行的方法要求一起执行成功或者一起回滚，单独执行时候以非事务方式执行，则选择该事物传播级别。

  

* ioslation：事务隔离级别 

  有三个读问题

  * 脏读：一个未提交事务读取到另一个未提交事务的数据 
  * 不可重复读：一个未提交事务读取到另一提交事务修改数据 
  * 虚读/幻读：一个未提交事务读取到另一提交事务添加数据 

  解决：通过设置事务隔离级别，解决读问题 

  |                  | 脏读 | 不可重复读 | 幻读 |
  | ---------------- | ---- | ---------- | ---- |
  | Read uncommitted | ×    | ×          | ×    |
  | Read committed   | √    | ×          | ×    |
  | Repeatable read  | √    | √          | ×    |
  | Serializable     | √    | √          | √    |

* timeout：超时时间 ：事务需要在一定时间内进行提交，如果不提交进行回滚 ，默认值是 -1 ，设置时间以秒单位进行计算 
* readOnly：是否只读，默认值 false，表示可以查询，可以添加修改删除操作 ，设置成 true 之后，只能查询 
* rollbackFor：回滚：设置出现哪些异常进行事务回滚 
* noRollbackFor：不回滚 ：设置出现哪些异常不进行事务回滚

##### xml方式实现

```xml
<!--在 spring 配置文件引入名称空间 tx-->
<xmlns:tx="http://www.springframework.org/schema/tx" 

http://www.springframework.org/schema/tx 
http://www.springframework.org/schema/tx/spring-tx.xsd">
                                                       
    <!--开启事务注解-->
<tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
                                                              

<!--1 创建事务管理器-->
    <bean id="transactionManager" 
    class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
     <!--注入数据源-->
     <property name="dataSource" ref="dataSource"></property>
    </bean>
<!--2 配置通知-->
    <tx:advice id="txadvice">
     <!--配置事务参数-->
     <tx:attributes>
     <!--指定哪种规则的方法上面添加事务-->
     <tx:method name="accountMoney" propagation="REQUIRED"/>
     <!--<tx:method name="account*"/>-->
     </tx:attributes>
    </tx:advice>
<!--3 配置切入点和切面-->
<aop:config>
     <!--配置切入点-->
     <aop:pointcut id="pt" expression="execution(* 
    com.atguigu.spring5.service.UserService.*(..))"/>
     <!--配置切面-->
     <aop:advisor advice-ref="txadvice" pointcut-ref="pt"/>
</aop:config>
```

### Spring5新特性

##### log4j2日志框架

```xml
<!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>${log4j.version}</version>
            <scope>test</scope>
        </dependency>


        <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.30</version>
        </dependency>
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
<!--Configuration 后面的 status 用于设置 log4j2 自身内部的信息输出，可以不设置，
当设置成 trace 时，可以看到 log4j2 内部各种详细输出-->
<configuration status="INFO">
 	<!--先定义所有的 appender-->
 	<appenders>
 	<!--输出日志信息到控制台-->
 	<console name="Console" target="SYSTEM_OUT">
 	<!--控制日志输出的格式-->
 	<PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
 	</console>
 	</appenders>
 	<!--然后定义 logger，只有定义 logger 并引入的 appender，appender 才会生效-->
 	<!--root：用于指定项目的根日志，如果没有单独指定 Logger，则会使用 root 作为默认的日志输出-->
 	<loggers>
 		<root level="info">
 		<appender-ref ref="Console"/>
 		</root>
 	</loggers>
</configuration>
```

控制台则可以得到输出结果

```java
LoggerFactory.getLogger(Test.class).info("手动输出");
```

##### @Nullable

* 注解用在方法上面，方法返回值可以为空 
* 注解使用在方法参数里面，方法参数可以为空
* 注解使用在属性上面，属性值可以为空

##### 核心容器支持函数式风格 GenericApplicationContext

```java
@Test
public void testGenericApplicationContext() {
     //1 创建 GenericApplicationContext 对象
     GenericApplicationContext context = new GenericApplicationContext();
     //2 调用 context 的方法对象注册
     context.refresh();
     context.registerBean("user1",User.class,() -> new User());
     //3 获取在 spring 注册的对象
     // User user = (User)context.getBean("com.atguigu.spring5.test.User");
     User user = (User)context.getBean("user1");
     System.out.println(user);
}
```

#####  支持整合 JUnit5

* Junit4

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${spring.version}</version>
    <scope>test</scope>
</dependency>
```

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class Test {
    @Autowired
    Service service;
    
    @org.junit.Test
    public void add(){
        service.inde();
    }
}
```

* Junit5

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:bean1.xml")
public class JTest5 {
     @Autowired
     private UserService userService;
     @Test
     public void test1() {
     userService.accountMoney();
     }
}
```

```java
使用一个复合注解替代上面两个注解完成整合
@SpringJUnitConfig(locations = "classpath:bean1.xml")
public class JTest5 {
     @Autowired
     private UserService userService;
     @Test
     public void test1() {
     userService.accountMoney();
     }
}
```





### Webflux响应式编程



Spring IOC AOP实现/源码	Spring5新特性



