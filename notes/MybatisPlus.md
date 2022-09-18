# MybatisPlus

### HelloWorld

* ##### 创建springboot项目

* ##### 引入依赖

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.baomidou</groupId>
		<artifactId>mybatis-plus-boot-starter</artifactId>
		<version>3.5.1</version>
	</dependency>
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<optional>true</optional>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<scope>runtime</scope>
	</dependency>
</dependencies>
```

* ##### application.yaml

```yaml
spring:
# 配置数据源信息
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false
    username: root
    password: 123456
# 配置MyBatis日志
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

注意：MySQL5.7版本的url： jdbc:mysql://localhost:3306/mybatis_plus?characterEncoding=utf-8&useSSL=false 

​			MySQL8.0版本的url： jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false

* ##### 启动类	添加mapperscan

```java
@MapperScan("com.atguigu.mapper")
@SpringBootApplication
public class MybatisplusApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisplusApplication.class, args);
    }
}
```

* ##### 实体类

```java
@Data //lombok注解
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

* ##### mapper层

```java
@Repository//此注解不是必须的，若没有自动装配时idea会标红
public interface UserMapper extends BaseMapper<User> {
}
```

* ##### 测试

```java
@SpringBootTest
class MybatisplusApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        userMapper.selectList(null).forEach(System.out::println);
    }
}
```



### 基本CRUD

* ##### 添加

```java
@Test
    void contextLoads() {
        User user = new User();
        user.setName("Mary");
        user.setAge(17);
        user.setEmail("zhangsan@qq.com");
        userMapper.insert(user);
        System.out.println(user.getId)
    }
```

​	参数为实体类对象，有id属性则为user.getId()，否则默认id为**雪花算法得到的id**

* ##### 删除

  * deleteById()	可以通过Id删除
  * deleteByMap()  以map的键和值为删除的条件
  * deleteBatchIds()  按Id批量删除 底层SQL语句为 DELETE FROM user WHERE id IN ( ? , ? , ? )

```java
@Test
    void contextLoads() {
        userMapper.deleteById(1);
        User user = new User();
        user.setId(1561909209207939074L);
        userMapper.deleteById(user);
        HashMap<String, Object> map = new HashMap<>();
        map.put("age",18);
        map.put("email","wened@qq.com");
        userMapper.deleteByMap(map);
        userMapper.deleteBatchIds(Arrays.asList(12,35,1561910997667561473L));
    }
```

* ##### 修改

  通过id修改

```java
void contextLoads() {
        User newuser = new User();
        newuser.setId(1561913479370207233L);
        newuser.setName("newuser");
        newuser.setAge(19);
        newuser.setEmail("qwer@qq.com");
        userMapper.updateById(newuser);
    }
```

* ##### 查询

  * 根据id查询
  * 根据id批量查询  SELECT id,name,age,email FROM user WHERE id IN ( ? , ? , ? )
  * 根据map查询
  * 查询所有数据

```java
@Test
    void contextLoads() {
        System.out.println(userMapper.selectById(2));
        System.out.println(userMapper.selectBatchIds(Arrays.asList(2, 3, 4)));
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","Tom");
        System.out.println(userMapper.selectByMap(map));
        System.out.println(userMapper.selectList(null));
    }
```

* ##### 使用MyBatis的方式 自定义功能

在application.yml中配置：mybatis-plus.mapper-locations: 	默认为"classpath*:/mapper/**/*.xml" 即mapper包下的所有xml文件




### 条件构造器

* ##### 组装查询条件

```java
//查询用户名包含a，年龄在20到30之间，并且邮箱不为null的用户信息	注意查询条件为表的列名而不是属性名
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.like("user_name","a")
    .between("age",20,30)
    .isNotNull("email");
userMapper.selectList(wrapper).forEach(System.out::println);
```

* ##### 组装排序条件

```java
//按年龄降序查询用户，如果年龄相同则按id升序排列
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.orderByDesc("age").orderByAsc("did");
userMapper.selectList(wrapper).forEach(System.out::println);
```

* ##### 组装删除条件

```java
 //删除email为空的用户
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.isNull("email");
userMapper.delete(wrapper);
```

* ##### 条件的优先级

```java
//and比or优先级高

//将（年龄大于20并且用户名中包含有a）或邮箱为null的用户信息修改 
//方法拼接默认就是and,or需要用or()连接
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.gt("age",20)
                        .like("user_name","a")
                                .or().isNull("email");
        User user = new User();
        user.setAge(18);
        user.setEmail("user@atguigu.com");
        userMapper.update(user,wrapper);

//将用户名中包含有a并且（年龄大于20或邮箱为null）的用户信息修改
//and后接一个lambda表达式，and后的查询条件为一个整体
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("user_name","a").and(i->i.gt("age",20).or().isNull("email"));
        userMapper.update(user,wrapper);
```

注：update()中的user为null的属性值对应的表的字段不会更改

* ##### 组装select子句

```java
//查询用户信息的username和age字段
//SELECT username,age FROM t_user
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.select("username", "age");
//selectMaps()返回Map集合列表，通常配合select()使用，避免User对象中没有被查询到的列值为null
	List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
	maps.forEach(System.out::println);
```

* ##### 实现子查询

```java
//查询id小于等于3的用户信息
//SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE (id IN(select id from t_user where id <= 3))
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
queryWrapper.inSql("id", "select id from t_user where id <= 3");
List<User> list = userMapper.selectList(queryWrapper);
list.forEach(System.out::println);
```

* ##### UpdateWrapper

```java
//将（年龄大于20或邮箱为null）并且用户名中包含有a的用户信息修改	注意此时条件优先级 and&or set语句位置无关紧要
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.like("user_name",'a').and(i->i.gt("age",20).isNull("email"))
                        .set("age",18).set("email","20203@qq.com");
//没用只有wrapper的方法，这里必须要创建User对象或设置为null
        userMapper.update(null,wrapper);
```

* ##### 组装条件

  在真正开发的过程中，组装条件是常见的功能，而这些条件数据来源于用户输入，是可选的，因 此我们在组装这些条件时，必须先判断用户是否选择了这些条件，若选择则需要组装该条件，若 没有选择则一定不能组装，以免影响SQL执行的结果。

  * 可以通过if判断决定是否组装
  * 可以通过wrapper的带condition的方法

```java
@Test
public void test08() {
	//定义查询条件，有可能为null（用户未输入或未选择）
	String username = null;
	Integer ageBegin = 10;
	Integer ageEnd = 24;
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	//StringUtils.isNotBlank()判断某字符串是否不为空且长度不为0且不由空白符(whitespace)
	构成
	if(StringUtils.isNotBlank(username)){
		queryWrapper.like("username","a");
	}
	if(ageBegin != null){
		queryWrapper.ge("age", ageBegin);
	}
	if(ageEnd != null){
		queryWrapper.le("age", ageEnd);
	}
	//SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE (age >=? AND age <= ?)
	List<User> users = userMapper.selectList(queryWrapper);
	users.forEach(System.out::println);
}
```


​	wrapper中查询方法都有一个带bool condition的重载方法

```java
//定义查询条件，有可能为null（用户未输入或未选择）
String username = null;
Integer ageBegin = 10;
Integer ageEnd = 24;
QueryWrapper<User> wrapper = new QueryWrapper<>();
//StringUtils.isNotBlank()判断某字符串是否不为空且长度不为0且不由空白符(whitespace)构成
wrapper.like(!StringUtils.isBlank(username),"user_name",username)
    .ge(ageBegin!=null,"age",ageBegin)
    .lt(ageEnd!=null,"age",ageEnd);
System.out.println(userMapper.selectList(wrapper));
```

* ##### LambdaQueryWrapper

```java
//方法中的字段用lambda表达式的方式 类::get属性
//定义查询条件，有可能为null（用户未输入或未选择）
        String username = null;
        Integer ageBegin = 10;
        Integer ageEnd = 24;
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //StringUtils.isNotBlank()判断某字符串是否不为空且长度不为0且不由空白符(whitespace)构成
        wrapper.like(!StringUtils.isBlank(username),User::getUsername,username)
                .ge(ageBegin!=null, User::getAge,ageBegin)
                .lt(ageEnd!=null,User::getAge,ageEnd);
        System.out.println(userMapper.selectList(wrapper));
```

* ##### LambdaUpdateWrapper

```java
@Test
public void test10() {
    //组装set子句
    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
    .set(User::getAge, 18)
    .set(User::getEmail, "user@atguigu.com")
    .like(User::getName, "a")
    .and(i -> i.lt(User::getAge, 24).or().isNull(User::getEmail)); 
    User user = new User();
    int result = userMapper.update(user, updateWrapper);
    System.out.println("受影响的行数：" + result);
}
```



### Service

​	通用 Service CRUD 封装IService接口，进一步封装 CRUD

* ##### 创建Service接口

```java
public interface UserService extends IService<User> {
}
```

* ##### 创建实现类

```java
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService{
}
```

* ##### 测试

```java
@SpringBootTest
class MybatisplusApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        //批量添加	底层不是一整条SQL 而是一条执行多次 因为SQL有长度限制
        LinkedList<User> users = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setEmail(i+"@ww.com");
            user.setName(i+"hua");
            user.setAge(i+10);
            users.add(user);
        }
        userService.saveBatch(users);
        //查询总记录数
        System.out.println(userService.count());
    }
}
```



### 常用注解

* ##### @TableName

​		在使用MyBatis-Plus实现基本的CRUD时，我们并没有指定要操作的表，只是在 Mapper接口继承BaseMapper时，设置了泛型User，而操作的表为user表 由此得出结论，MyBatis-Plus在确定操作的表时，由BaseMapper的泛型决定，即实体类型决定，且**默认操作的表名和实体类型的类名一致**

​	方法一：@TableName

```java
//在实体类上添加注解TableName
@TableName("t_user")
```

​	方法二：在开发的过程中，我们经常遇到以上的问题，即实体类所对应的表都有固定的前缀，例如t\_或tbl_ 此时，可以使用MyBatis-Plus提供的全局配置，为实体类所对应的表名设置默认的前缀，那么就 不需要在每个实体类上通过@TableName标识实体类对应的表

```yaml
mybatis-plus:
  global-config:
    db-config:
      table-prefix: t_
```

​	注：如果两个同时都有配置，则以@TableName的value属性为表名

* ##### @TableId

​	若实体类和表中表示主键的不是id，而是其他字段，例如did，MyBatis-Plus不会自动识别did为主键列。则无法使用雪花算法以及和id有关的方法。

```java
@TableId(value = "did",type = IdType.AUTO)
    private Long did;
```

​	@TableId表示为该属性对应的列为主键，value表示数据库中的主键名是什么（默认值为属性名），type为id的生成策略。

​	可以在全局配置中配置生成策略globalconfig.dbconfig.id-type值

* ##### @TableField

  当属性名和表中的字段名不一致时，如果为驼峰和下划线风格则可以自动映射，如果不行，则可以使用@TableField

```java
@TableField("user_name")
    private String username;
```

* ##### @TableLogic

  逻辑删除：假删除，将对应数据中代表是否被删除字段的状态修改为“被删除状态”，之后在数据库中仍旧能看到此条数据记录。

```java
//在表中添加逻辑删除字段is_deleted并设置默认值0
//在实体类中添加对应属性并加上注解
@TableLogic()
    private Integer isDeleted;
```

注：测试删除功能，真正执行的是修改 UPDATE t_user SET is_deleted=1 WHERE id=? AND is_deleted=0 

​		测试查询功能，被逻辑删除的数据默认不会被查询 SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0

### 插件

##### 分页插件

* 配置插件

```java
//新建一个配置类
@Configuration
@MapperScan(basePackages = "com.atguigu.mapper")
public class MybatisplusConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

* 使用插件

```java
@Test
    void contextLoads() {
        //第二页 一页5条记录
        Page<User>page = new Page<>(2,5);
        //用selectPage方法，第二个参数为Wrapper 此处表示没用有条件
        userMapper.selectPage(page,null);
        //page中有分页后的信息 此处选择几个做实例
        page.getRecords().forEach(i-> System.out.println(i));
        System.out.println(page.getPages());
        System.out.println(page.getCurrent());
        System.out.println(page.getTotal());
        System.out.println(page.getSize());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }
```

* ##### xml自定义方法实现分页

```java
//自定义方法 返回值为Page，参数有Page
Page<User> selectPageVo(@Param("page") Page<User> page, @Param("age")Integer age);
```

```xml
<!--SQL片段，记录基础字段-->
<sql id="BaseColumns">id,username,age,email</sql>
<!--IPage<User> selectPageVo(Page<User> page, Integer age);-->
<select id="selectPageVo" resultType="User">
	SELECT <include refid="BaseColumns"></include> FROM t_user WHERE age > #{age}
</select>
```

```java
@Test
public void testSelectPageVo(){
    //设置分页参数
    Page<User> page = new Page<>(1, 5);
    userMapper.selectPageVo(page, 20);
    //获取分页数据
    List<User> list = page.getRecords();
    list.forEach(System.out::println);
    System.out.println("当前页："+page.getCurrent());
    System.out.println("每页显示的条数："+page.getSize());
    System.out.println("总记录数："+page.getTotal());
    System.out.println("总页数："+page.getPages());
    System.out.println("是否有上一页："+page.hasPrevious());
    System.out.println("是否有下一页："+page.hasNext());
}
```

##### 乐观锁插件

* 在表中添加表示version的字段
* 在配置类中添加乐观锁插件

```java
@Configuration
@MapperScan(basePackages = "com.atguigu.mapper")
public class MybatisplusConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
```

* 在实体类的version字段前增加注解

```java
@Data
@TableName("t_product")
public class Product {
    private Long id;
    private String name;
    private Integer price;
    @Version
    private Integer version;
}
```

结果：如果执行update方法时，Product对象的version与表中的version不一致，则不进行更改。如果version相同则更改成功并且			version字段自增。

### 通用枚举

对于表中特殊的列如性别，实体类中的属性使用枚举类来表示，存在问题：会使用枚举类的tostring()填充字段，使用通用枚举可以指定字段填充时的值。

* sex字段对应的枚举类 **@EnumValue注解表明和数据库值对应的枚举类属性**

```java
@Getter
public enum SexEnum {
    Male(1,"男"),Female(2,"女");
    @EnumValue
    private Integer sex;
    private String sexname;

    SexEnum(Integer sex, String sexname) {
        this.sex = sex;
        this.sexname = sexname;
    }
}
```

* 配置扫描通用枚举

```yml
mybatis-plus:
  type-enums-package: com.atguigu.enums
```

* 测试(此处测试添加方法，查询方法同理，得到的值也会转化为对应的枚举类型)

```java
@Test
public void testSexEnum(){
    User user = new User();
    user.setName("Enum");
    user.setAge(20);
    //设置性别信息为枚举项，会将@EnumValue注解所标识的属性值存储到数据库
    user.setSex(SexEnum.MALE);
    //INSERT INTO t_user ( username, age, sex ) VALUES ( ?, ?, ? )
    //Parameters: Enum(String), 20(Integer), 1(Integer)
    userMapper.insert(user);
}
```



### 代码生成器

##### 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.1</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>3.5.1</version>
    </dependency>
    <dependency>
        <groupId>org.freemarker</groupId>
        <artifactId>freemarker</artifactId>
        <version>2.3.31</version>
    </dependency>
</dependencies>
```

##### 配置数据源

```yml
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8&characterEncoding=utf8&useSSL=false
    username: root
    password: 123456
```

##### 生成文件

方法一：

```java
@SpringBootTest
class MbgApplicationTests {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mybatis_plus?" +
                        "serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false", "root", "123456")
                        .globalConfig(builder -> {
                            builder.author("chen") // 设置作者
                                    // .enableSwagger() // 开启 swagger 模式
                                    .fileOverride() // 覆盖已生成文件
                                    .outputDir("D://mybatis_plus"); // 指定输出目录
                        })
                        .packageConfig(builder -> {
                            builder.parent("com.exzample") // 设置父包名
                                    .moduleName("mybatisplus") // 设置父包模块名
                                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://mybatis_plus"));// 设置mapperXml生成路径
                        })
                        .strategyConfig(builder -> {
                            builder.addInclude("t_user","t_product") // 设置需要生成的表名
                                    .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                        })
                        .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker 引擎模板，默认的是Velocity引擎模板
                        .execute();
    }
}
```

方法二（不需要yml配置）：

```java
public class CodeGenerator {

    @Test
    public void run() {
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        // 此处建议写项目/src/main/java源代码的绝对路径
        gc.setOutputDir("C:\\idea\\onlinedu-parent\\service\\service-edu" + "/src/main/java");
        // 生成注释时的作者
        gc.setAuthor("scorpios");
        //生成后是否打开资源管理器
        gc.setOpen(false);
        gc.setFileOverride(false); //重新生成时文件是否覆盖
        gc.setServiceName("%sService");	//去掉Service接口的首字母I
        gc.setIdType(IdType.ID_WORKER_STR); //主键策略
        gc.setDateType(DateType.ONLY_DATE); //定义生成的实体类中日期类型
        // 如果开启Swagger,要引入相应的包
        gc.setSwagger2(true); //开启Swagger2模式

        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis_plus?serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
        // 此处要注意：parent + moduleName 为包的名字，在这个包下，创建对应的controller...
        pc.setParent("com.scorpios");
        pc.setModuleName("eduservice"); //模块名
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 数据库中表的名字，表示要对哪些表进行自动生成controller service、mapper...
        strategy.setInclude("edu_course","edu_course_description",
        	"edu_chapter","edu_video");
        // 数据库表映射到实体的命名策略,驼峰命名法
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 生成实体时去掉表前缀，比如edu_course，如果不加下面这句，生成的实体类名字就是：EduCourse
        strategy.setTablePrefix("edu_"); 
        //生成实体时去掉表前缀
		// strategy.setTablePrefix(pc.getModuleName() + "_"); 

		//数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符

        mpg.setStrategy(strategy);

        // 6、执行
        mpg.execute();
    }
}
```

运行则可以生成基本项目结构和CRUD方法



### 多数据源

适用于多种场景：纯粹多库、 读写分离、 一主多从、 混合模式等

* 模拟多库 mybatis_plus和mybatis_plus_1

```sql
CREATE DATABASE `mybatis_plus_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `mybatis_plus_1`;
CREATE TABLE product
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '商品名称',
    price INT(11) DEFAULT 0 COMMENT '价格',
    version INT(11) DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (id)
);
```

* 引入依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>3.5.0</version>
</dependency>
```

* 多数数据源全局配置

```yml
spring:
# 配置数据源信息
  datasource:
    dynamic:
    # 设置默认的数据源或者数据源组,默认值即为master
      primary: master
    #严格匹配数据源,默认false.true未匹配到指定数据源时抛异常,false使用默认数据源
      strict: false
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT&characterEncoding=utf-8&useSSL=false
          driver-class-name: com.mysql.cj.jdbc.Driver
          username: root
          password: 123456
        slave_1:
          url: jdbc:mysql://localhost:3306/mybatis_plus_1?serverTimezone=GMT&characterEncoding=utf-8&useSSL=false
          driver-class-name: com.mysql.cj.jdbc.Driver
          username: root
          password: 123456
```

* 添加@DS注解	在Serivice层Mapper接口等要执行sql的类或者方法上均可使用该注解指定数据源

```java
@DS("master")
public interface UserService extends IService<User> {
}
```

```java
@DS("slave_1")
public interface ProductMapper extends BaseMapper<Product> {
}
```



### MybatisX插件

* 对应的映射文件和抽象类有小鸟图标，点击可以实现映射文件和抽象类的切换。如果mapper接口的方法没有映射文件会标红，点击接口方法名可以与进入对应映射文件的方法。

* 代码生成器	生成实体类，mapper接口，service以及映射文件
  * Idea连接数据库

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220824194005839.png" alt="image-20220824194005839" style="zoom:50%;" align="left" />

* 按需生成代码

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220824194159538.png" alt="image-20220824194159538" style="zoom:50%;" align="left"/>

* 快速生成CRUD	按需求写方法名

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220824194937209.png" alt="image-20220824194937209" style="zoom:50%;" align="left" />

可以进行条件拼接

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220824195025358.png" alt="image-20220824195025358" style="zoom:50%;" align="left"/>

alt+enter 选择Generate Mybatis Sql

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220824195040073.png" alt="image-20220824195040073" style="zoom:50%;" align="left" />
