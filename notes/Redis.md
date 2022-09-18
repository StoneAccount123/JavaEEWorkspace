[TOC]

# Redis

### 数据结构

​	exists	key

​	keys	*

​	del key	不支持通配符

​	type	key	数据结构



##### String字符串

​	赋值取值，递增递减，追加值，获取长度，获取设置多个键，位操作

​	set key value

​	setnx key value key不存在则复制

​	get key

​	incr key

​	incrby key increment

​	decr	key

​	decrby key decrement

​	incrbyfloat key increment

​	append key value

​	strlen key	unicode编码长度

​	mset key1 value1 key2 value2...

​	mget key1 key2...

​	getbit key offset	从左到右(高位到低位)	从0开始计数 超过范围显示0

​	setbit key offset

​	bitcount key [start] [end] 都包括

​	bitop operation destkey key key 操作符有and or xor not



##### List列表

​	向两端添加元素，从两端弹出元素，获取元素个数，获取列表片段，删除指定值，获取/设置索引元素值，保留指定片段，向列表插入元素，元素从一个列表转到另一个列表

​	lpush/rpush key value value 可同时加多个

​	lpop/rpop key

​	llen key

​	lrange key start stop	从0开始 包括两端的元素的切片

​	lrem key count val	count<0从右到左删count个val，count=0删除所有val，count>0从左向右删count个

​	lindex key index	lset key index value 索引

​	ltrim key start end 保留范围内的元素

​	linsert key before|after pivot value 在值为pivot的前/后插入元素

​	rpoplpush source destination 从source右弹出带destination左

​	

##### Hash哈希

​	赋值取值，判断字段是否存在，字段不存在时赋值，增加数字，删除字段，只获取字段名和字段值，获取字段数量。

​	hset key field value

​	hget key field

​	hgetall key	返回字段和值	其他语言可能会优化封装为对象

​	hmset key field value key field value

​	hmget hget key field key field

​	hexists	key field

​	hsetnx	key field value

​	hincrby key field increment	

​	hdel key filed field	可以删除多个

​	hkeys key

​	hvals key

​	hlen key



##### Set集合

增加删除元素，获得所有元素，判断是否有元素，集合运算，元素个数，集合运算结果储存，随机获取元素，弹出一个元素

​	sadd key member..

​	srem key member..

​	smembers key

​	sismember key member

​	sdiff/sinter/sunion key key	差集交集并集 可以加多个集合 一步步计算

​	scard key

​	sdiffstore/sinterstore/sunionstore key key	des中原有内容会被删除

​	srandmember key [count]

​	spop key



##### Zset有序集合

​	相当于哈希＋跳跃表	哈希：由名称到分数	跳跃表做集合的排序

​	![image-20220807115946734](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220807115946734.png)

​	

增加元素，获得元素分数，获取排名/分数在某个范围的元素列表，增加元素分数，获取元素个数，获取分数范围的元素个数，删除一个/多个元素，按排名/分数范围删除元素，获取元素排名

​	zadd   \<key>\<score1>\<member1>\<score2>\<member2>…

​	zscore key member

​	zrange/zrerange \<key>\<start>\<stop> [WITHSCORES]

​	zrangebyscore/zrerangebyscore key min max [withscores] [limit offset count]

​	zincrby \<key>\<increment>\<member>  

​	zcard key

​	zcount \<key>\<min>\<max>

​	zrem \<key>\<member>

​	zremrangebyrank/zremrangebyscore key start stop

​	zrank \<key>\<member>



##### Bitmaps

​	设置/获取比特位的值，计数1的位数，比特位运算。

​	setbit key offset value

​	getbit key offset 

​	bitcount key [start end] 负数表示从后数，没有则表示整个范围

​	bitop and(or/not/xor)  destkey  [key…]



##### HyperLogLog

​	解决基数问题，如UV独立访客统计。比set/bitmaps更省内存

​	添加元素，计数基数，合并HLL

​	pfadd  key  element 

​	pfcount key

​	pfmerge destkey sourcekey sourcekey 



##### Geospatial

​	提供了经纬度设置，查询，范围查询，距离查询

​	geoadd key longitude latitude member

​	geopos key member

​	geodist key member1 member2

​	georadius key longitude latitude radius

 

### 配置文件

​	bind 127.0.0.1 限制只能本地访问，不能远程访问

​	protected-mode	保护模式yes改为no	即可远程登陆

​	port 6379	端口号

​	tcp-backlog	未完成和已完成三次握手的队列总和	高并发需要调大

​	timeout

​	tcp-keepalive	

​	daemonize yes 是否为守护线程	用于后台启动 

​	pidfile	pid的存储文件位置

​	loglevel	日志级别

​	logfile	输出文件路径

​	requirepass	设置密码



### 事务和锁

​	Redis事务的主要作用就是串联多个命令防止别的命令插队。

​	Multi、Exec、discard三个命令

​	从输入Multi命令开始，输入的命令都会依次进入命令队列中，但不会执行，直到输入Exec后，Redis会将之前的命令队列中的命令依次执行。组队的过程中可以通过discard来放弃组队。

​	<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220809102935486.png" alt="image-20220809102935486" style="zoom: 80%;" />

​	错误处理：组队中某个命令出现了报告错误，执行时整个的所有队列都会被取消。如果执行阶段某个命令报出了错误，则只有报错的命令不会被执行，而其他的命令都会执行，不会回滚。

​	

锁：

​	**悲观锁(Pessimistic Lock)**, 顾名思义，就是很悲观，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会block直到它拿到锁。**传统的关系型数据库里边就用到了很多这种锁机制**，比如**行锁**，**表锁**等，**读锁**，**写锁**等，都是在做操作之前先上锁。

​	**乐观锁(Optimistic Lock),** 顾名思义，就是很乐观，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用版本号等机制。**乐观锁适用于多读的应用类型，这样可以提高吞吐量**。Redis就是利用这种check-and-set机制实现事务的。Redis默认乐观锁，悲观锁的实现可用Lua脚本。

​	在执行multi之前，先执行watch key1 [key2],可以监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。取消 WATCH 命令对所有 key 的监视。如果在执行 WATCH 命令之后，EXEC 命令或DISCARD 命令先被执行了的话，那么就不需要再执行UNWATCH 了。

​	

 **Redis事务三特性**

​	**单独的隔离操作**：事务中的所有命令都会序列化、按顺序地执行。事务在执行的过程中，不会被其他客户端发送来的命令请求所打断。

​	**没有隔离级别的概念** ：队列中的命令没有提交之前都不会实际被执行，因为事务提交前任何指令都不会被实际执行

​	**不保证原子性** ： 事务中如果有一条命令执行失败，其后的命令仍然会被执行，没有回滚 



​	**秒杀案例：**





### 持久化

##### RDB

​	**原理**

​	在指定的时间间隔内将内存中的数据集快照写入磁盘， 也就是行话讲的Snapshot快照，它恢复时是将快照文件直接读到内存里。

​	Redis会单独创建（fork：写时复制技术）一个子进程来进行持久化，会先将数据写入到 一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。 整个过程中，主进程是不进行任何IO操作的，这就确保了极高的性能 如果需要进行大规模数据的恢复，且对于数据恢复的完整性不是非常敏感，那RDB方式要比AOF方式更加的高效。RDB的缺点是最后一次持久化后的数据可能丢失。

![image-20220810154724037](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220810154724037.png)



​	**配置文件**

​	dbfilename dbfilename dump.rdb	rdb文件名

​	dir ./	rdb文件路径

​	save 秒钟 写操作次数：RDB是整个内存的压缩过的Snapshot，RDB的数据结构，可以配置复合的快照触发条件，默认是1分钟内改了1万次，或5分钟内改了10次，或15分钟内改了1次。

​	stop-writes-on-bgsave-error	：当Redis无法写入磁盘的话，直接关掉Redis的写操作。推荐yes

​	rdbcompression 对于存储到磁盘中的快照，可以设置是否进行压缩存储。如果是的话，redis会采用LZF算法进行压缩。如果你不想消耗CPU来进行压缩的话，可以设置为关闭此功能。推荐yes。

​	rdbchecksum  检查完整性

​	**命令**

​	save ：save时只管保存，其它不管，全部阻塞。手动保存。不建议。

​	bgsave：Redis会在后台异步进行快照操作，快照同时还可以响应客户端请求。

​	lastsave 命令获取最后一次成功执行快照的时间

​	**优点**

* 适合大规模的数据恢复
* 对数据完整性和一致性要求不高更适合使用
* 节省磁盘空间
* 恢复速度快

​	**缺点**

 * Fork的时候，内存中的数据被克隆了一份，大致2倍的膨胀性需要考虑
 *  虽然Redis在fork时使用了写时拷贝技术,但是如果数据庞大时还是比较消耗性能。
 * 在备份周期在一定间隔时间做一次备份，所以如果Redis意外down掉的话，就会丢失最后一次快照后的所有修改。

​	**禁用**

​	redis-cli config set save ""#save后给空值，表示禁用保存策略

##### AOF

​	**原理**

* 客户端的请求写命令会被append追加到AOF缓冲区内；

* AOF缓冲区根据AOF持久化策略[always,everysec,no]将操作syn

  同步到磁盘的AOF文件中；

* AOF文件大小超过重写策略或手动重写时，会对AOF文件rewrite重写，压缩AOF文件容量；

* Redis服务重启时，会重新load加载AOF文件中的写操作达到数据恢复的目的；

![](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220811085433183.png)

​	**配置**

​	可以在redis.conf中配置文件名称，默认为 appendonly.aof，AOF文件的保存路径，同RDB的路径一致。AOF和RDB同时开启，系统默认取AOF的数据（数据不会存在丢失）

​	appendonly no

​	如遇到AOF文件损坏，通过/usr/local/bin/redis-check-aof--fix appendonly.aof进行恢复



​	appendfsync always

​	始终同步，每次Redis的写入都会立刻记入日志；性能较差但数据完整性比较好

​	appendfsync everysec

​	每秒同步，每秒记入日志一次，如果宕机，本秒的数据可能丢失。

​	appendfsync no

​	redis不主动进行同步，把同步时机交给操作系统。



 **优势**                               

* 备份机制更稳健，丢失数据概率更低。

* 可读的日志文本，通过操作AOF稳健，可以处理误操作。

**劣势**

* 比起RDB占用更多的磁盘空间。

* 恢复备份速度要慢。

* 每次读写都同步的话，有一定的性能压力。

* 存在个别Bug，造成恢复不能。



**用哪个？**

官方推荐两个都启用。

如果对数据不敏感，可以选单独用RDB。

不建议单独用 AOF，因为可能会出现Bug。

如果只是做纯内存缓存，可以都不用。





### 发布订阅

​	通过频道发送和订阅

​	subscribe channel	订阅

​	publish channel message	发布



### Jedis与Spring boot整合Redis

``` xml
<!---Jedis的jar包--->
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
	<version>3.2.0</version>
</dependency>
```

注意：

禁用Linux的防火墙：Linux(CentOS7)里执行命令

**systemctl stop/disable firewalld.service**  

redis.conf中注释掉bind 127.0.0.1 ,然后 protected-mode no



Jedis-Demo

``` Java
//联通测试
public class Demo01 {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.137.3",6379);
        String pong = jedis.ping();
		System.out.println("连接成功："+pong);
		jedis.close();
	}
}


//数据类型测试
//key
jedis.set("k1", "v1");
jedis.set("k2", "v2");
jedis.set("k3", "v3");
Set<String> keys = jedis.keys("*");
System.out.println(keys.size());
for (String key : keys) {
	System.out.println(key);
}
System.out.println(jedis.exists("k1"));
System.out.println(jedis.ttl("k1"));                
System.out.println(jedis.get("k1"));

//list
List<String> list = jedis.lrange("mylist",0,-1);
for (String element : list) {
	System.out.println(element);
}
//set
jedis.sadd("orders", "order01");
jedis.sadd("orders", "order02");
jedis.sadd("orders", "order03");
jedis.sadd("orders", "order04");
Set<String> smembers = jedis.smembers("orders");
for (String order : smembers) {
	System.out.println(order);
}
jedis.srem("orders", "order02");

//....其他类似 将指令变为方法
```



Springboot配置

redis依赖

```xml
<!-- redis -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- spring2.X集成redis所需common-pool2-->
<dependency>
<groupId>org.apache.commons</groupId>
<artifactId>commons-pool2</artifactId>
<version>2.6.0</version>
</dependency>
```



application.properties配置redis配置

``` properties
#Redis服务器地址
spring.redis.host=192.168.140.136
#Redis服务器连接端口
spring.redis.port=6379
#Redis数据库索引（默认为0）
spring.redis.database= 0
#连接超时时间（毫秒）
spring.redis.timeout=1800000
#连接池最大连接数（使用负值表示没有限制）
spring.redis.lettuce.pool.max-active=20
#最大阻塞等待时间(负数表示没限制)
spring.redis.lettuce.pool.max-wait=-1
#连接池中的最大空闲连接
spring.redis.lettuce.pool.max-idle=5
#连接池中的最小空闲连接
spring.redis.lettuce.pool.min-idle=0
```



redis配置类

```java
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(factory);
//key序列化方式
        template.setKeySerializer(redisSerializer);
//value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
//value hashmap序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
// 配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }
}

```

 测试

``` java
@RestController
@RequestMapping("/redisTest")
public class RedisTestController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public String testRedis() {
        //设置值到redis
        redisTemplate.opsForValue().set("name","lucy");
        //从redis获取值
        String name = (String)redisTemplate.opsForValue().get("name");
        return name;
    }
}

```

 

### 主从复制

##### 简介

​	机数据更新后根据配置和策略， 自动同步到备机的master/slaver机制，**Master**以写为主，**Slave**以读为主。在主机上写，在从机上可以读取数据。在从机上写数据报错。主机挂掉，重启就行，一切如初。从机重启需重设：slaveof 127.0.0.1 6379，且在重设前没有数据。

![image-20220809170147133](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220809170147133.png)



​	优点：1. 读写分离，性能扩展	2.容灾快速恢复

​	

##### 操作

​		拷贝多个redis.conf文件include(写绝对路径)，开启daemonize yes，Pid文件名字pidfile，指定端口port，Log文件名字

dump.rdb名字dbfilename，Appendonly 关掉或者换名字。（如下图）

replica-priority 设置从机的优先级，值越小，优先级越高，用于选举主机时使用。默认100

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220809170403648.png" alt="image-20220809170403648" style="zoom:100%;" align="left"/>

​	启动三台redis服务器

​	info replication	打印主从复制的相关信息

​	slaveof ip port 设置主从关系



##### 复制原理

* Slave启动成功连接到master后会发送一个sync命令，Master接到命令启动后台的存盘进程，同时收集所有接收到的用于修改数据集命令， 在后台进程执行完毕之后，master将传送整个数据文件到slave,以完成一次完全同步

	* 全量复制：而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中。

	* 增量复制：Master继续将新的所有收集到的修改命令依次传给slave,完成同步

 * 但是只要是重新连接master,一次完全同步（全量复制)将被自动执行



##### 一主二仆

* 切入点问题？slave1、slave2是从头开始复制还是从切入点开始复制?比如从k4进来，那之前的k1,k2,k3是否也可以复制？

​		从复制原理分析，从头开始复制，所有内容都有。

 * 主机shutdown后情况如何？从机是上位还是原地待命？

   原地待命。从机仍有数据。

 * 主机又回来了后，主机新增记录，从机还能否顺利复制？ 

   可以。

 * 其中一台从机down后情况如何？依照原有它能跟上大部队吗？

   down后没有任何数据，重新加入后有全部数据。

 *  从机是否可以写？set可否？ 

​		不可写。



##### 薪火相传

​	上一个Slave可以是下一个slave的Master，Slave同样可以接收其他 slaves的连接和同步请求，那么该slave作为了链条中下一个的master, 可以有效减轻master的写压力,去中心化降低风险。

![image-20220810084622970](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220810084622970.png)



##### 反客为主

​	 slaveof no one 将从机变为主机。



##### 哨兵模式

​	搭建主从后可以实现自动的反客为主

​	能够后台监控主机是否故障，如果故障了根据投票数自动将从库转换为主库。

![image-20220810091929920](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220810091929920.png)

​	自定义的/myredis目录下新建sentinel.conf文件，名字绝不能错，填写内容：sentinel monitor mymaster 127.0.0.1 6379 1其中mymaster为监控对象起的服务器名称， 1 为至少有多少个哨兵同意迁移的数量。

执行redis-sentinel /myredis/sentinel.conf启动哨兵 



故障恢复说明：

![image-20220810092803123](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220810092803123.png)

	* 优先级在redis.conf中默认：replica-priority 100，值越小优先级越高
	* 偏移量是指获得原主机数据最全的
	* 每个redis实例启动后都会随机生成一个40位的runid	





### 集群

​	实现了扩容和写压力的分摊。Redis 集群实现了对Redis的水平扩容，即启动N个redis节点，将整个数据库分布存储在这N个节点中，每个节点存储总数据的1/NRedis 集群通过分区（partition）来提供一定程度的可用性（availability）： 即使集群中有一部分节点失效或者无法进行通讯， 集群也可以继续处理命令请求。



**配置**

``` 
include /myredis/redis.conf
pidfile /var/run/redis_6379.pid
port 6379
dbfilename dump6379.rdb
#开启集群
cluster-enabled yes
#节点配置文件名
cluster-config-file nodes-6379.conf
#设定节点失联时间，超过该时间（毫秒），集群自动进行主从切换。
cluster-node-timeout 15000
```

拷贝新建6个配置文件，删除持久化文件，运行6个服务。



组合之前，请确保所有redis实例启动后，nodes-xxxx.conf文件都生成正常。

cd /opt/redis-6.2.1/src

redis-cli --cluster create --cluster-replicas 1 192.168.11.101:6379 192.168.11.101:6380 192.168.11.101:6381 192.168.11.101:6389 192.168.11.101:6390 192.168.11.101:6391#使用自己的真实ip

redis-cli -c -p 6379  采用集群策略连接，设置数据会自动切换到相应的写主机

cluster nodes 命令查看集群信息



**插槽**

一个 Redis 集群包含 16384 个插槽（hash slot）， 数据库中的每个键都属于这 16384 个插槽的其中一个， 

集群使用公式 CRC16(key) % 16384 来计算键 key 属于哪个槽， 其中 CRC16(key) 语句用于计算键 key 的 CRC16 校验和 。

集群中的每个节点负责处理一部分插槽。 举个例子， 如果一个集群可以有主节点， 其中：

节点 A 负责处理 0 号至 5460 号插槽。

节点 B 负责处理 5461 号至 10922 号插槽。

节点 C 负责处理 10923 号至 16383 号插槽。

不在一个slot下的键值，是不能使用mget,mset等多键操作，可以通过{}来定义组的概念，从而使key中{}内相同内容的键值对放到一个slot中去。mset k1{group} v1 k2{group} v2

CLUSTER GETKEYSINSLOT slot count 返回 count 个 slot 槽中的键



**故障**

如果主节点下线，从节点能自动升为主节点。注意：15秒超时                            

主节点恢复后，主从关系会如何？主节点回来变成从机。

如果所有某一段插槽的主从节点都宕掉，redis服务是否还能继续?

如果某一段插槽的主从都挂掉，而cluster-require-full-coverage 为yes ，那么 ，整个集群都挂掉

如果某一段插槽的主从都挂掉，而cluster-require-full-coverage 为no ，那么，该插槽数据全都不能使用，也无法存储。

redis.conf中的参数 cluster-require-full-coverage



### 应用问题





**秒杀案例	应用问题	集群搭建**

