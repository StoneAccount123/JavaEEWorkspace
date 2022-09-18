# Maven

### ①POM

​	Project Object Model：项目对象模型。将 Java 工程的相关信息封装为对象作为便于操作和管理的模型。 Maven 工程的核心配置。可以说学习 Maven 就是学习 pom.xml 文件中的配置。

### ②约定的目录结构 

![image-20220811151748728](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220811151748728.png)

### ③坐标 

​	Maven 的坐标 使用如下三个向量在 Maven 的仓库中唯一的确定一个 Maven 工程。 

​	[1]groupid：公司或组织的域名倒序+当前项目名称 

​	[2]artifactId：当前项目的模块名称 

​	[3]version：当前模块的版本

​	如何通过坐标到仓库中查找 jar 包？ 

​	[1]将 gav 三个向量连起来 com.atguigu.maven+Hello+0.0.1-SNAPSHOT 

​	[2]以连起来的字符串作为目录结构到仓库中查找 com/atguigu/maven/Hello/0.0.1-SNAPSHOT/Hello-0.0.1-SNAPSHOT.jar ※注意：我们自己的 Maven 工程必须执行安装操作才会进入仓库。安装的命令是：mvn install

### ④依赖管理 

​	①依赖的目的是什么 当 A jar 包用到了 B jar 包中的某些类时，A 就对 B 产生了依赖，这是概念上的描述。那么如何在项目 中以依赖的方式引入一个我们需要的 jar 包呢？ 答案非常简单，就是使用 dependency 标签指定被依赖 jar 包的坐标就可以了。

​	②依赖的范围 大家注意到上面的依赖信息中除了目标 jar 包的坐标还有一个 scope 设置，这是依赖的范围。依赖的范 围有几个可选值，我们用得到的是：compile、test、provided 三个。

|          | compile | test | provided |
| -------- | ------- | ---- | -------- |
| 主程序   | √       | ×    | √        |
| 测试程序 | √       | √    | √        |
| 参与部署 | √       | ×    | ×        |

​	③依赖的传递性

![image-20220811153649340](C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220811153649340.png)

​	④依赖的排除 如果我们在当前工程中引入了一个依赖是 A，而 A 又依赖了 B，那么 Maven 会自动将 A 依赖的 B 引入当 前工程，但是个别情况下 B 有可能是一个不稳定版，或对当前工程有不良影响。这时我们可以在引入 A 的时 候将 B 排除。

``` xml
<dependency>
	<groupId>com.atguigu.maven</groupId>
 	<artifactId>HelloFriend</artifactId>
	<version>0.0.1-SNAPSHOT</version>
 	<type>jar</type>
 	<scope>compile</scope>
 	<exclusions> 
 		<exclusion> 
 			<groupId>commons-logging</groupId> 
 			<artifactId>commons-logging</artifactId> 
 		</exclusion> 
 	</exclusions> 
</dependency>
```

​	⑤统一管理所依赖 jar 包的版本

```xml
<properties>
	<atguigu.spring.version>4.1.1.RELEASE</atguigu.spring.version>
</properties>

<dependencies>
	<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
	<version>${atguigu.spring.version}</version>
 </dependency>
	……
</dependencies>
```

⑥依赖的原则：解决 jar 包冲突

<img src="C:\Users\myAdministrator\AppData\Roaming\Typora\typora-user-images\image-20220811154209216.png" alt="image-20220811154209216" style="zoom:67%;" />



### ⑤仓库管理 

**分类**

​	[1]本地仓库：为当前本机电脑上的所有 Maven 工程服务。

​	[2]远程仓库

​		 (1)私服：架设在当前局域网环境下，为当前局域网范围内的所有 Maven 工程服务。

​		(2)中央仓库：架设在 Internet 上，为全世界所有 Maven 工程服务。

​		(3)中央仓库的镜像：架设在各个大洲，为中央仓库分担流量。减轻中央仓库的压力，同时更快的响应用户请求。

**仓库中的文件**

​	[1]Maven 的插件 

​	[2]我们自己开发的项目的模块

​	[3]第三方框架或工具的 jar 包 

​	※不管是什么样的 jar 包，在仓库中都是按照坐标生成目录结构，所以可以通过统一的方式查询或依赖。

​	

### ⑥生命周期 

- maven的生命周期就是为了对所有的构建过程进行抽象和统一。

- 包含了项目的`清理`、`初始化`、`编译`、`测试`、`打包`、`集成测试`、`验证`、`部署`和`站点生成`等几乎所有构建步骤。

- 几乎所有的项目构建，都能映射到这样一个生命周期上。

  ①清理：删除以前的编译结果，为重新编译做好准备。

  ②编译：将 Java 源程序编译为字节码文件。 

  ③测试：针对项目中的关键点进行测试，确保项目在迭代开发过程中关键点的正确性。

  ④报告：在每一次测试后以标准的格式记录和展示测试结果。 

  ⑤打包：将一个包含诸多文件的工程封装为一个压缩文件用于安装或部署。Java 工程对应 jar 包，Web 工程对应 war 包。 

  ⑥安装：在 Maven 环境下特指将打包的结果——jar 包或 war 包安装到本地仓库中。 ⑦部署：将打包的结果部署到远程仓库或将 war 包部署到服务器上运行。

1. 三套生命周期
   - maven包含三套相互独立的生命周期。
   - `clean`生命周期：用于清理项目。
   - `default`生命周期：用于构建项目。
   - `site`生命周期：用于建立项目站点。
2. 生命周期内部的阶段
   - 每个生命周期包含`多个阶段`(phase)。
   - 这些阶段是`有序`的。
   - `后面`的阶段`依赖`于`前面`的阶段。当执行某个阶段的时候，会先执行它前面的阶段。
   - 用户和maven最直接的交互就是调用这些生命周期阶段。
3. **clean生命周期**

| clean生命周期阶段 | 说明                          |
| ----------------- | ----------------------------- |
| pre-clean         | 执行一些clean前需要完成的工作 |
| clean             | 清理上一次构建生成的文件      |
| post-clean        | 执行一些clean后需要完成的工作 |

**default生命周期**
default生命周期的目的是构建项目，它定义了真正构建时所需要完成的所有步骤，是所有生命周期中最核心的部分。
包含23个阶段：

| default生命周期阶段     | 说明                                                       |
| ----------------------- | ---------------------------------------------------------- |
| validate                | 验证项目是否正确并且所有必要信息都可用                     |
| initialize              | 初始化构建状态，比如设置属性值、创建目录                   |
| generate-sources        | 生成包含在编译阶段中的任何源代码                           |
| process-sources         | 处理源代码，比如说，过滤任意值                             |
| generate-resources      | 生成将会包含在项目包中的资源文件                           |
| process-resources       | 复制和处理资源到目标目录，为打包阶段最好准备               |
| `compile`               | 编译项目的源代码                                           |
| process-classes         | 处理编译生成的文件，比如说对Java class文件做字节码改善优化 |
| generate-test-sources   | 生成包含在编译阶段中的任何测试源代码                       |
| process-test-sources    | 处理测试源代码，比如说，过滤任意值                         |
| generate-test-resources | 为测试创建资源文件                                         |
| process-test-resources  | 复制和处理测试资源到目标目录                               |
| test-compile            | 编译测试源代码到测试目标目录                               |
| process-test-classes    | 处理测试源码编译生成的文件                                 |
| `test`                  | 使用合适的单元测试框架运行测试 , 测试代码不会被打包或部署  |
| prepare-package         | 在实际打包之前，执行任何的必要的操作为打包做准备           |
| `package`               | 将编译后的代码打包成可分发的格式，比如JAR                  |
| pre-integration-test    | 在执行集成测试前进行必要的动作。比如说，搭建需要的环境     |
| integration-test        | 如有必要，将程序包处理并部署到可以运行集成测试的环境中     |
| post-integration-test   | 执行集成测试完成后进行必要的动作。比如说，清理集成测试环境 |
| verify                  | 运行任何检查以验证包是否有效并符合质量标准                 |
| `install`               | 安装项目包到maven本地仓库，供本地其他maven项目使用         |
| `deploy`                | 将最终包复制到远程仓库，供其他开发人员和maven项目使用      |

1. **site生命周期**
   site生命周期的目的是建立和发布项目站点。
   Maven能够基于pom.xml所包含的信息，自动生成一个友好的站点，方便团队交流和发布项目信息。
   包含以下4个阶段：

   | site生命周期阶段 | 说明                                     |
   | ---------------- | ---------------------------------------- |
   | pre-site         | 执行一些在生成项目站点之前需要完成的工作 |
   | site             | 生成项目站点文档                         |
   | post-site        | 执行一些在生成项目站点之后需要完成的工作 |
   | site-deploy      | 将生成的项目站点发布到服务器上           |

2. mvn命令和生命周期
   从命令行执行maven任务的最主要方式就是调用maven的生命周期阶段。
   需要注意的是，每套生命周期是相互独立的，但是每套生命周期的阶段是有前后依赖关系的。

   - 格式： `mvn 阶段 [阶段2] ...[阶段n]`
   - `mvn clean`：该命令调用clean生命周期的clean阶段。
     - 实际执行的阶段为clean生命周期中的pre-clean和clean阶段。
   - `mvn test`：该命令调用default生命周期的test阶段。
     - 实际执行的阶段为default生命周期的从validate到test的所有阶段。
     - 这也就解释了为什么执行测试的时候，项目的代码能够自动编译。
   - `mvn clean install`：该命令调用clean生命周期的clean阶段和default生命周期的install阶段。
     - 实际执行的阶段为clean生命周期的pre-clean、clean阶段，以及default生命周期的从validate到install的所有阶段。
   - `mvn clean deploy`：该命令调用clean生命周期的clean阶段和default生命周期的deploy阶段。
     - 实际执行的阶段为clean生命周期的pre-clean、clean阶段，以及default生命周期的所有阶段。
     - 包含了清理上次构建的结果、编译代码、运行单元测试、打包、将打好的包安装到本地仓库、将打好的包发布到远程仓库等所有操作。



### ⑦插件和目标 

- maven的核心仅仅定义了抽象的生命周期，具体的任务交给插件完成。每套生命周期包含多个阶段，每个阶段执行什么操作，都由插件完成。
- **插件中的每个功能就叫做插件的目标（Plugin Goal），每个插件中可能包含一个或者多个插件目标（Plugin Goal）**。



### ⑧继承 

​	**使用继承机制就可以将这样的依赖信息统一提取到父工程模块中进行统一管理。**

​	子工程

```xml
<parent>
	<!-- 父工程坐标 -->
	<groupId>...</groupId>
	<artifactId>...</artifactId>
	<version>...</version>
	<relativePath>从当前目录到父项目的 pom.xml 文件的相对路径</relativePath>
</parent>

<!--子工程不需要版本声明-->
<dependencies>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
	</dependency>
</dependencies>
```

父工程

``` xml
<dependencyManagement>
	<dependencies>
		<dependency>
    		<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.9</version>
			<scope>test</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```



### ⑨聚合

将多个工程拆分为模块后，需要手动逐个安装到仓库后依赖才能够生效。修改源码后也需要逐个手动进 行 clean 操作。**而使用了聚合之后就可以批量进行 Maven 工程的安装、清理工作。**

```xml
<!--在总的聚合工程中使用 modules/module 标签组合，指定模块工程的相对路径即可-->
<modules>
	<module>../Hello</module>
	<module>../HelloFriend</module>
	<module>../MakeFriends</module>
</modules>
```





# Gradle