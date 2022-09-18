# Mybatis

### HelloWorld

##### maven依赖

```xml
<!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>
```

##### mybatis配置文件 mybatis-config

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!--引入配置资源文件-->
    <properties resource="db.properties"></properties>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    
    <!--映射文件位置-->
    <mappers>
        <mapper resource="mappers/UserMapper.xml"/>
    </mappers>
</configuration>
```

##### db.properties

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/ssm_crud
username=root
password=123456
```

##### 创建表对应实体类

```java
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    private String username;

    private String password;

    private Integer age;

    private String sex;

    private String email;

}
```

##### 创建接口UserMapper.java

```java
public interface UserMapper {

    public int insertUser(User user);

    public int deleteById(@Param("id")Integer id);

    public int updateById(User user);

    public User getById(@Param("id")Integer id);

    public List<User> getAll();

}
```

##### 创建映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!-- namespace 和全类名一致-->
<mapper namespace="com.mapper.UserMapper">

<!--id和方法名一致-->
<!--    public int insertUser();-->
    <insert id="insertUser">
        insert into t_user values (null,#{username},#{password},#{age},#{sex},#{email})
    </insert>

<!--    public int deleteById(@Param("id")Integer id);-->
    <delete id="deleteById">
        delete from t_user where id=#{id}
    </delete>

<!--    public int updateById(User user);-->
    <update id="updateById">
        update t_user set username=#{username},password=#{password},age=#{age},sex=#{sex},email=#{email} where id=#{id}
    </update>

<!--    public User getById(@Param("id")Integer id);-->
    <select id="getById" resultType="User">
        select * from t_user where id=#{id}
    </select>

<!--    public List<User> getAll();-->
    <select id="getAll" resultType="User">
        select * from t_user
    </select>

</mapper>
```

##### 测试类

```java
public class MybatisTest {
    @Test
    public void testAdd() throws Exception {
        //根据全局配置文件，利用
        //SqlSessionFactoryBuilder创建SqlSessionFactory
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sessionFactory.openSession();

        //创建方式为JDBC 需要手动提交事务 openSession(true)则自动提交	自动提交为每个SQL都会提交，不是所有SQL一起提交
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(mapper.insertUser());
        sqlSession.commit();
        sqlSession.close();
    }
    
    
    @Test
    public void testAdd(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User(null, "user", "12345", 12, "男", "aa@qq.com");
        System.out.println(mapper.insertUser(user));
    }

    @Test
    public void testGet(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int id = 2;
        System.out.println(mapper.getById(id));
        mapper.getAll().forEach(user -> System.out.println(user));
    }

    @Test
    public void testUpdate(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User(12, "newname", "123456", 12, "男", "aa@qq.com");
        mapper.updateById(user);
    }

    @Test
    public void testDelete() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteById(12);
    }
}
```

##### 日志输出

引入日志依赖

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

mybatis-config.xml

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```

在类路径下创建log4j.properties (文件名不能变)

```properties
### 配置根/在什么地方会打印日志   ###
log4j.rootLogger = debug,console

### 设置输出sql的级别，其中logger后面的内容全部为jar包中所包含的包名 ###
log4j.logger.org.apache=off
log4j.logger.java.sql.Connection=dubug
log4j.logger.java.sql.Statement=dubug
log4j.logger.java.sql.PreparedStatement=dubug
log4j.logger.java.sql.ResultSet=dubug

### 配置输出到控制台 ###
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern =  %d{ABSOLUTE} %5p %c{1}:%L - %m%n

### 配置输出到文件 ###
log4j.appender.fileAppender = org.apache.log4j.FileAppender
log4j.appender.fileAppender.File = logs/log.log
log4j.appender.fileAppender.Append = true
log4j.appender.fileAppender.Threshold = DEBUG
log4j.appender.fileAppender.layout = org.apache.log4j.PatternLayout
log4j.appender.fileAppender.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```





### 配置文件

* enviroment

```xml
<!--配置多个数据库连接环境 default默认连接池的id-->
<environments default="development">
    <!--具体连接环境 id名称 唯一-->
    <environment id="development">
        <!--
		transactionManager设置事务管理方式
		type="JDBC"使用原生JDBC 事务提交需要手动
			MANAGED 被管理 如Spring
		-->
        <transactionManager type="JDBC"/>
        <!--dataSource配置数据源
			type=POOLED|UNPOOLED|JNDI
			使用数据库连接池|不使用数据库连接池|上下文数据源
		-->        
        <dataSource type="POOLED">
            <property name="driver" value="${driver}"/>
            <property name="url" value="${url}"/>
            <property name="username" value="${username}"/>
            <property name="password" value="${password}"/>
        </dataSource>
    </environment>
</environments>
```

* properties

```xml
<!--引入配置资源文件-->
    <properties resource="db.properties"></properties>
```

* typeAliases

```xml
 <typeAliases>
     <!--
        typeAlias设置某个类型别名,未设置时mapper映射文件要使用全类名
        type为全类名 alias为设置的别名且不区分大小写，缺省为类名且不区分大小写。
     -->
     <typeAlias type="com.pojo.User" alias="user"></typeAlias>
     <!-- 为包下所有的类设置默认别名-->
     <package name="com.pojo"/>
</typeAliases>
```

* mappers

```xml
 <mappers>
     <!--单个映射文件-->
     <mapper resource="com/mappers/UserMapper.xml"/>
     <!--以包为单位引入映射文件、
	要求1.mapper接口所在包要和映射文件所在包一致
	    2.mapper接口和映射文件名字一致
	-->
     <package name="com.mapper"/>
</mappers>
```

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220821142734870.png" alt="image-20220821142734870" style="zoom:80%;" align="left" />



### 获取参数和返回值

##### #{}和${}

#{}相当于占位符，会自动加上'  '

${}相当于字符串拼接，不会加上'  '

* 模糊查询

  * select * from t_user where username like '%{name}%'
  * select * from t_user where username like concat('%',#{name},'%')
  * select * from t_user where username like "%"{name}"%"

* in后面接批量删除

  delete from t_user where id in(1,2,3)	此时String ids="1,2,3"，获取参数只能用${}，因为#{}会加''

* 动态设置表名

  select * from table 此时table获取参数只能用${}，原因同上



##### 几种方法参数

* 单个字面量

  用#{}和${}获取参数值，以任意名称获取

* 多个参数

  默认arg0,arg1   ,param1,param1获取值

* Map

  以map的键获取值

* 对象类型

  直接用属性获取值

* @param注解

​		为注解赋value属性，用该值获取

```xml
public User login(String username,String password);
	<select id="login" resultType="user">
        select * FROM t_user where username=#{arg0} and password=#{arg1}
	</select>

public User login(Map<String,Object> map);
    <select id="login" resultType="user">
        select * FROM t_user where username=#{username} and password=#{password}
    </select>

public User login(User user);
	<select id="login" resultType="user">
       select * FROM t_user where username=#{username} and password=#{password}
	</select>
    
public User login(@Param("username")String username,@Param("password")String password);
	<select id="login" resultType="user">
        select * FROM t_user where username=#{username} and password=#{password}
	</select>
```



##### 几种返回类型

* 实体类对象

  设置resultType，若返回值为多个对象，则用集合接收返回值

* 单个值

  设置resultType，例如java.lang.Integer，mybatis为基本数据类型设置了默认的别名

* map集合

  若返回的结果只有一条则为一个属性＝值的集合

  若返回值为多条，需要设置@MapKey，返回的结果为以key为键，一条结果为值

```xml
<!--    public List<User> getAll();-->
    <select id="getAll" resultType="User">
        select * from t_user
    </select>

<!--    public Integer sum();-->
    <select id="sum" resultType="integer">
        select count(*) from t_user
    </select>

<!--    public Map<String,Object> getById(@Param("id")Integer id);>-->
    <select id="getById" resultType="map">
        select * from t_user where id=#{id}
    </select>
	<!--结果{password=1234, sex=男, id=2, age=12, email=email, username=zhang}-->

<!--    @MapKey("id")-->
<!--    public Map<Integer,User> getAll();-->
    <select id="getAll" resultType="map">
        select * from t_user
    </select>
```

##### 获取自增主键

 	useGeneratedKeys="true" keyProperty="id"

```xml
    <insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
        insert into t_user values (null,#{username},#{password},#{age},#{sex},#{email})
    </insert>
```



### 自定义映射resultMap

##### 字段名与属性名不一致

如字段名使用下划线_分隔，而属性名使用驼峰命名

* 使用字段别名

```xml
<!--public List<Emp>getAll();-->
    <select id="getAll" resultType="emp">
        select eid,emp_name empName,age,sex,email,did from t_emp
    </select>
```

* 配置mapUnderscoreToCamelCase

 ```xml
 <settings>
     <setting name="mapUnderscoreToCamelCase" value="true"/>
 </settings>
 ```

* 自定义映射resultMap

```xml
<resultMap id="empResultMap" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="sex" column="sex"></result>
        <result property="email" column="email"></result>
</resultMap>

    <!--public List<Emp>getAll();-->
    <select id="getAll" resultMap="empResultMap">
        select * from t_emp
    </select>
```

##### 多对一映射

* 级联赋值

```xml
<resultMap id="empResultMap" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="sex" column="sex"></result>
        <result property="email" column="email"></result>
        <result property="dept.did" column="did"></result>
        <result property="dept.deptName" column="dept_name"></result>
    </resultMap>

    <!--public List<Emp>getAll();-->
    <select id="getAll" resultMap="empResultMap">
        select * from t_emp left join t_dept on t_emp.did=t_dept.did
    </select>
```

* association 注意为javatype

```xml
<resultMap id="empResultMap" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="sex" column="sex"></result>
        <result property="email" column="email"></result>
        <association property="dept" javaType="com.entity.Dept">
            <id property="did" column="did"></id>
            <result property="deptName" column="dept_name"></result>
        </association>
    </resultMap>

	<!--public List<Emp>getAll();-->
    <select id="getAll" resultMap="empResultMap">
        select * from t_emp left join t_dept on t_emp.did=t_dept.did
    </select>
```

* 分步查询

```xml
<!--第一步 查询员工信息 关键association语句 
	property为赋值的属性，select为下一步的查询方法，column为传入的参数值/查询条件-->
<resultMap id="empResultMap" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="sex" column="sex"></result>
        <result property="email" column="email"></result>
        <association property="dept" select="com.mapper.DeptMapper.getById" column="did"></association>
    </resultMap>

    <!--public List<Emp>getAll();-->
    <select id="getAll" resultMap="empResultMap">
        select * from t_emp
    </select>

<!--第一步 用did查询部门信息-->
<resultMap id="deptResultMap" type="dept">
        <id property="did" column="did"></id>
        <result property="deptName" column="dept_name"></result>
	</resultMap>

	<!--public Dept getById(@Param("did")String did);-->
    <select id="getById" resultMap="deptResultMap">
        select * from t_dept where did=#{did}
    </select>
```



##### 一对多映射

* collection 注意为oftype

```xml
    <resultMap id="deptResultMap" type="dept">
        <id property="did" column="did"></id>
        <result property="deptName" column="dept_name"></result>
        <collection property="emps" ofType="Emp">
            <id property="eid" column="eid"></id>
            <result property="empName" column="emp_name"></result>
            <result property="age" column="age"></result>
            <result property="sex" column="sex"></result>
            <result property="email" column="email"></result>
        </collection>
    </resultMap>

<!--    public Dept getById(@Param("did")String did);-->
    <select id="getById" resultMap="deptResultMap">
        select * from t_dept left join t_emp on t_dept.did=t_emp.did where t_dept.did=#{did}
    </select>
```

* 分步查询

```xml
<!--第一步-->
	<resultMap id="deptResultMap" type="dept">
        <id property="did" column="did"></id>
        <result property="deptName" column="dept_name"></result>
        <collection property="emps" select="com.mapper.EmpMapper.getByDid" column="did"></collection>
    </resultMap>

	<!--public Dept getById(@Param("did")String did);-->
    <select id="getById" resultMap="deptResultMap">
        select * from t_dept where did=#{did}
    </select>

<!--第二步-->
<!--    public Emp getByDid(@Param("did")Integer did);-->
    <select id="getByDid" resultMap="empResultMap2">
        select * from t_emp where did=#{did}
    </select>
```



##### 延迟加载

```xml
<settings>
        <setting name="logImpl" value="LOG4J"/>
        <setting name="lazyLoadingEnabled" value="true"/>
    </settings>

<association property="dept" select="com.mapper.DeptMapper.getById" column="did" fetchType="eager"></association>
```

默认关闭，开启后分步查询会按需加载，需要对应属性时才会开启第二次步查询。

fetchType(eager|lazy)会覆盖从而对具体实现是否延迟加载



### 动态SQL

##### test标签

判断条件决定是否拼接到sql中

注意拼接时可能会存在where直接跟and的情况，此时可加一个恒等式如1=1

```xml
<!--    public List<Emp>getByCOndition(Emp emp);-->
    <select id="getByCOndition" resultType="emp">
        select * from t_emp
       
        <if test="empName!=null and empName!=''">
            emp_name=#{empName}
        </if>
        <if test="age!=null and age!=''">
            and age=#{age}
        </if>
        <if test="sex!=null and sex!=''">
            and sex=#{sex}
        </if>
        <if test="email!=null and email!=''">
            and email=#{email}
        </if>
    </select>
```

##### where标签

会根据是否有内容从而**动态生成**where标签，并且可以将内容前多余的or和and去掉，而不能将结尾的or和and去掉

```xml
<!--    public List<Emp>getByCOndition(Emp emp);-->
    <select id="getByCOndition" resultType="emp">
        select * from t_emp
        <where>
        <if test="empName!=null and empName!=''">
            emp_name=#{empName}
        </if>
        <if test="age!=null and age!=''">
            and age=#{age}
        </if>
        <if test="sex!=null and sex!=''">
            and sex=#{sex}
        </if>
        <if test="email!=null and email!=''">
            and email=#{email}
        </if>
        </where>
    </select>
```

##### trim标签

* prefix="" 补充前缀

* prefixOverrides=""删除前缀
*  suffix="" 补充后缀
* suffixOverrides=""删除后缀

```xml
<!--    public List<Emp>getByCOndition(Emp emp);-->
    <select id="getByCOndition" resultType="emp">
        select * from t_emp
        <trim prefix="where" prefixOverrides="and">
        <if test="empName!=null and empName!=''">
            emp_name=#{empName}
        </if>
        <if test="age!=null and age!=''">
            and age=#{age}
        </if>
        <if test="sex!=null and sex!=''">
            and sex=#{sex}
        </if>
        <if test="email!=null and email!=''">
            and email=#{email}
        </if>
        </trim>
    </select>
```

##### choose when otherwise标签

相当于if   else if   else	只命中一个

```xml
<!--    public List<Emp>getByCOndition(Emp emp);-->
    <select id="getByCOndition" resultType="emp">
        select * from t_emp
            <where>
                <choose>
                    <when test="empName!=null and empName!=''">
                        emp_name=#{empName}
                    </when>
                    <when test="age!=null and age!=''">
                        age=#{age}
                    </when>
                    <when test="sex!=null and sex!=''">
                        sex=#{sex}
                    </when>
                    <when test="email!=null and email!=''">
                        email=#{email}
                    </when>
                    <otherwise>
                        eid=#{eid}
                    </otherwise>
                </choose>
            </where>
    </select>
</mapper>
```

##### foreach标签



##### sql标签

将固定内容写入sql标签，在其他地方则可引用。

```xml
<sql id="colum">emp_name,sex,age,email,did</sql>

<select id="getById" resultMap="deptResultMap">
        select <include refid="colum"></include> from t_dept where did=#{did}
    </select>
```



### 缓存

##### 一级缓存

sqlSession级别的，sqlSession查询时会被缓存，同一个sqlSession第二次查询相同数据时会从缓存中取出，而不会查询数据库。

失效情况

* 不同的sqlSession
* 查询条件不同
* 两次查询中做了增删改操作（另一个sqlSession如果做了增删改操作，但本sqlSession没用，仍会从缓存中读取数据，**此时会存在问题**）
* 手动清空了缓存：sqlSession.clearCache()

##### 二级缓存

开启条件

* 全局配置settings标签中cacheEnabled="true"
* 映射文件中设置标签\<cache/>
* 二级缓存必须在SqlSession关闭或者提交后有效
* 缓存的数据的实体类对象必须实现序列化接口

```java
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        mapper.getByDid(3).forEach(emp-> System.out.println(emp));
        sqlSession.close();
        EmpMapper deptMapper = sqlSessionFactory.openSession(true).getMapper(EmpMapper.class);
        deptMapper.getByDid(3).forEach(emp-> System.out.println(emp));

//发现日志只输出一句SQL语句
//16:44:16,459 DEBUG EmpMapper:60 - Cache Hit Ratio [com.mapper.EmpMapper]: 0.5 命中率
```

失效情况

* 两次查询之间执行了增删改查

缓存配置

* `eviction`：缓存回收策略

  * LRU - 最近最少回收，移除最长时间不被使用的对象

    - FIFO - 先进先出，按照缓存进入的顺序来移除它们

    - SOFT - 软引用，移除基于垃圾回收器状态和软引用规则的对象

    - WEAK - 弱引用，更积极的移除基于垃圾收集器和弱引用规则的对象
	  
	    默认是 LRU 最近最少回收策略
	

- `flushinterval` 缓存刷新间隔，缓存多长时间刷新一次，默认不清空，设置一个毫秒值
- `readOnly`: 是否只读；**true 只读**，MyBatis 认为所有从缓存中获取数据的操作都是只读操作，不会修改数据。MyBatis 为了加快获取数据，直接就会将数据在缓存中的引用交给用户。不安全，速度快。**读写(默认)**：MyBatis 觉得数据可能会被修改
- `size` : 缓存存放多少个元素
- `type`: 指定自定义缓存的全类名(实现Cache 接口即可)
- `blocking`： 若缓存中找不到对应的key，是否会一直blocking，直到有对应的数据进入缓存。

##### 查询顺序

​	先查询二级缓存->再查询一级缓存->最后查询数据库->sqlsession关闭后会将其写入二级缓存

```
探究更新对一级缓存失效的影响： 由上面的分析结论可知，我们每次执行 update 方法时，都会先刷新一级缓存，因为是同一个 SqlSession, 所以是由同一个 Map 进行存储的，所以此时一级缓存会失效
探究不同的 SqlSession 对一级缓存的影响： 这个也就比较好理解了，因为不同的 SqlSession 会有不同的Map 存储一级缓存，然而 SqlSession 之间也不会共享，所以此时也就不存在相同的一级缓存
同一个 SqlSession 使用不同的查询操作： 这个论点就需要从缓存的构成角度来讲了，我们通过 cacheKey 可知，一级缓存命中的必要条件是两个 cacheKey 相同，要使得 cacheKey 相同，就需要使 cacheKey 里面的值相同。

那么究竟应该不应该使用二级缓存呢？先来看一下二级缓存的注意事项：
缓存是以namespace为单位的，不同namespace下的操作互不影响。
insert,update,delete操作会清空所在namespace下的全部缓存。
通常使用MyBatis Generator生成的代码中，都是各个表独立的，每个表都有自己的namespace。
多表操作一定不要使用二级缓存，因为多表操作进行更新操作，一定会产生脏数据
```

### 逆向工程

**由表生成实体类， mapper接口和映射文件**

* maven依赖

```xml
<!-- 依赖MyBatis核心包 -->
<dependencies>
	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis</artifactId>
		<version>3.5.7</version>
	</dependency>
</dependencies>

<!-- 控制Maven在构建过程中相关配置 -->
<build>
	<!-- 构建过程中用到的插件 -->
	<plugins>
	<!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
		<plugin>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.0</version>
	<!-- 插件的依赖 -->
            <dependencies>
                <!-- 逆向工程的核心依赖 -->
                <dependency>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-core</artifactId>
                    <version>1.3.2</version>
                </dependency>
                <!-- 数据库连接池 -->
                <dependency>
                    <groupId>com.mchange</groupId>
                    <artifactId>c3p0</artifactId>
                    <version>0.9.2</version>
                    </dependency>
                <!-- MySQL驱动 -->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.8</version>
                </dependency>
            </dependencies>
		</plugin>
	</plugins>
</build>
```

* 创建逆向工程的配置文件 文件名必须是：generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
        <generatorConfiguration>
        <!--
targetRuntime: 执行生成的逆向工程的版本
MyBatis3Simple: 生成基本的CRUD（清新简洁版）
MyBatis3: 生成带条件的CRUD（奢华尊享版）
-->
        <context id="DB2Tables" targetRuntime="MyBatis3Simple">
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
        connectionURL="jdbc:mysql://localhost:3306/ssm_crud"
        userId="root"
        password="123456">
        </jdbcConnection>
        <!-- javaBean的生成策略-->
        <javaModelGenerator targetPackage="com.entity"
        targetProject=".\src\main\java">
        <property name="enableSubPackages" value="true" />
        <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="com.mapper"
        targetProject=".\src\main\resources">
        <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER"
        targetPackage="com.mapper" targetProject=".\src\main\java">
        <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_dept" domainObjectName="Dept"/>
        </context>
        </generatorConfiguration>
```

* QBC查询

```java
@Test
    public void testMBG() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSession sqlSession = new
                SqlSessionFactoryBuilder().build(is).openSession(true);
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        EmpExample empExample = new EmpExample();
//创建条件对象，通过andXXX方法为SQL添加查询添加，每个条件之间是and关系
        empExample.createCriteria().andEnameLike("a").andAgeGreaterThan(20).andDidIsNot
        Null();
//将之前添加的条件通过or拼接其他条件
        empExample.or().andSexEqualTo("男");
        List<Emp> list = mapper.selectByExample(empExample);
        for (Emp emp : list) {
            System.out.println(emp);
        }
    }
```



### 分页插件

* maven依赖

```xml
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
<dependency>
<groupId>com.github.pagehelper</groupId>
<artifactId>pagehelper</artifactId>
<version>5.2.0</version>
</dependency>

<plugins>
<!--设置分页插件-->
<plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```

* 使用
  * 在查询功能之前使用PageHelper.startPage(int pageNum, int pageSize)开启分页功能 pageNum：当前页的页码 pageSize：每页显示的条数 
  * 在查询获取list集合之后，使用PageInfo pageInfo = new PageInfo<>(List list, int navigatePages)获取分页相关数据 list：分页之后的数据 navigatePages：导航分页的页码数 
  * 分页相关数据:pageNum：当前页的页码 pageSize：每页显示的条数 size：当前页显示的真实条数 total：总记录数 pages：总页数 prePage：上一页的页码 nextPage：下一页的页码  com.github.pagehelper pagehelper 5.2.0  isFirstPage/isLastPage：是否为第一页/最后一页 hasPreviousPage/hasNextPage：是否存在上一页/下一页 navigatePages：导航分页的页码数 navigatepageNums：导航分页的页码，[1,2,3,4,5]





**逆向工程和分页插件**

