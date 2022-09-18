# 设计模式

### 八大设计原则

##### 单一职责原则



##### 接口隔离原则

##### 依赖倒转(倒置)原则

##### 里氏替换原则

##### 开闭原则

##### 迪米特法则

##### 合成复用原则





### if-else重构

​	if-else两种场景：

* 异常逻辑处理：异常逻辑处理说明只能一个分支是正常流程
* 不同状态处理：不同状态处理都所有分支都是正常流程

##### 异常逻辑处理

​	总的原则：尽可能地维持正常流程代码在最外层。写 if-else 语句时一定要尽量保持主干代码是正常流程，避免嵌套过深。

​	实现的手段有：减少嵌套、移除临时变量、条件取反判断、合并条件表达式等。

* 合并条件表达式

```java
double getPayAmount(){
 2    double result;
 3    if(_isDead) {
 4        result = deadAmount();
 5    }else{
 6        if(_isSeparated){
 7            result = separatedAmount();
 8        }
 9        else{
10            if(_isRetired){
11                result = retiredAmount();
12            else{
13                result = normalPayAmount();
14            }
15        }
16    }
17    return result;
18}
```

















