# ☁️中传放心传（CUC-cloud-disk-upload-management-system）技术报告

**一个基于Vue和springboot+mybatisplus实现的文件云盘管理系统**

[![Typing SVG](https://readme-typing-svg.herokuapp.com?size=30&duration=1497&center=%E9%94%99%E8%AF%AF%E7%9A%84&vCenter=%E9%94%99%E8%AF%AF%E7%9A%84&multiline=true&width=620&height=70&lines=%E4%B8%AD%E4%BC%A0%E6%94%BE%E5%BF%83%E4%BC%A0;CUC-cloud-disk-upload-management-system)](https://git.io/typing-svg)

**作者**：:cherries:子亦:cherries:

**负责工作**：项目整体规划、具体功能测试

**时间**：2022.8.5


## 前言

小学期的任务是实现一个结合密码学和网络安全知识体系的文件管理云盘系统，除了文件管理的基础功能以外还要融入网络空间安全专业所学的密码学内容，我认为是一个非常有难度和挑战的任务。因为对于我而言，除了之前帮助朋友做过微信小程序的页面设计之外，对网页前后端的搭建可以说几乎没有任何基础。

在小组讨论后，我们决定放弃PHP搭建而去学习使用**SpringBoot框架**，再结合**Vue框架**的相关知识完成本次实践。

在前言的最后我必须要感谢我的组员:pray:，特别特别特别是组里的两位也有可能是三位大佬，在我的微微微微微微微微微微弱的协助下，以二or三带我，出色的完成了本次实践。对于他们优秀的学习和运用能力，我佩服的五体投地，且自愧不如，万分羡慕:pray:。日后我希望自己能“改过自新”，以他们为学习的目标，遨游在知识的海洋中。

## 开发内容回顾

**项目主体开发思路框架：**![](https://s6.jpg.cm/2022/08/03/PQMxgX.jpg)

### 前端开发

由于我之前只用**WXML**做过微信小程序的页面设计，对**Vue框架**并不是很熟悉，所以在前端开发之前，我特意去了解了这两者框架的区别：

**1**.对于**事件处理**方面，
* vue：使用v-on:event绑定事件，或者使用@event绑定事件，如
```typescript
<button v-on:click="counter += 1">Add 1</button>
```
* 小程序：用bindtap(bind+event)，或catchtap(catch+event)绑定事件，如
```typescript
<button bindtap="noWork">xxx</button>
<button catchtap="noWork">xxx</button> 
```

**2**.对于**数据双向绑定**方面，

（1） 设置值
* vue：在表单元素上加上v-model，绑定data中对应的一个值，当表单元素内容发生变化时，data中对应的值也会相应改变
```typescript
<div id="app">
    <input v-model="xxx" placeholder="1" class='xxx'/>
</div>

new Vue({
  el: '#app',
  data: {
   xxx:''
  }
})
```
* 小程序：当表单内容发生变化时，会触发表单元素上绑定的方法，然后在该方法中，通过this.setData({key:value})来将表单上的值赋值给data中的对应值
```typescript
<input bindinput="bindxxx" placeholder="1" class='xxx' value='{{xxx}}' name="xxx" />

Page({
data:{
    xxx:''
},
bindxxx(e) {
    this.setData({
      xxx: e.detail.value
    })
  }
})
```

（2） 取值
* vue：通过this.reason取值
* 小程序：通过this.data.reason取值

**3**.对于**绑定事件传参**方面，
* vue：在触发事件的方法中把需要传递的数据作为形参传入
```typescript
<button @click="say('xxx')"></button>

new Vue({
  el: '#app',
  methods:{
    say(arg){
    consloe.log(arg)
    }
  }
})
```
* 小程序：需要将参数作为属性值，绑定到元素上的data-属性上，然后在方法中，通过e.currentTarget.dataset.*的方式获取
```typescript
<view class='tr' bindtap='toApprove' data-id="{{item.id}}"></view>
Page({
data:{
    xxx:''
},
toApprove(e) {
    let id = e.currentTarget.dataset.id;
  }
})
```

**4**.对于**父子组件通信**方面，

（1）子组件的使用
* vue：编写子组件:heavy_minus_sign:在需要使用的父组件中通过import引入:heavy_minus_sign:在vue的components中注册:heavy_minus_sign:在模板中使用
```typescript
//子组件 bar.vue
<template>
  <div class="search-box">
    <div @click="say" :title="title" class="icon-dismiss"></div>
  </div>
</template>
<script>
export default{
props:{
    title:{
       type:String,
       default:''
      }
    }
},
methods:{
    say(){
       console.log('emm');
       this.$emit('helloWorld')
    }
}
</script>

// 父组件 foo.vue
<template>
  <div class="container">
    <bar :title="title" @helloWorld="helloWorld"></bar>
  </div>
</template>

<script>
import Bar from './bar.vue'
export default{
data(){
    return{
        title:"标题"
    }
},
methods:{
    helloWorld(){
        console.log('接收到子组件传递的事件')
    }
},
components:{
    Bar
}
</script>
```
* 小程序：编写子组件:heavy_minus_sign:在子组件的json文件中将该文件声明为组件:heavy_minus_sign:在需要引入的父组件的json文件中，用usingComponents填写引入组件的组件名以及路径:heavy_minus_sign:在父组件中直接引入

（2）父子组件间通信
* vue：父组件向子组件传递数据，在父组件通过v-bind传入一个值；在子组件中，通过props接收；子组件和父组件通信可以通过this.$emit将方法和数据传递给父组件
```typescript
// 父组件 foo.vue
<template>
  <div class="container">
    <bar :title="title"></bar>
  </div>
</template>
<script>
import Bar from './bar.vue'
export default{
data(){
    return{        
        title:"标题"
    }
},
components:{
    Bar
}
</script>

// 子组件bar.vue
<template>
  <div class="search-box">
    <div :title="title" ></div>
  </div>
</template>
<script>
export default{
props:{
    title:{
       type:String,
       default:''
      }
    }
}
</script>
```
* 小程序:父组件向子组件通信和vue类似，但是小程序没有v-bind，而是直接将值赋值给一个变量:
```typescript
<tab-bar currentpage="index"></tab-bar>
//“index”为向子组件传递的值
``` 

（3）父组件调用子组件
* vue：给子组件添加ref属性，通过this.$refs.ref的值获取到该子组件，可以调用子组件中的任意方法
```typescript
//子组件
<bar ref="bar"></bar>

//父组件
this.$ref.bar.子组件的方法
```
* 小程序：给子组件添加id或者class，通过this.selectComponent找到子组件，再调用子组件的方法
```typescript
//子组件
<bar id="bar"></bar>

// 父组件
this.selectComponent('#id').syaHello()
```
<br>

虽然最后对于本次作业的前端开发我并没有参与很多:sweat_drops:，但是我通过阅读学习组员编写的前端代码对**Vue框架**的使用有了更深入的了解。

- **[vue与小程序的不同](https://blog.csdn.net/weixin_45503079/article/details/108972874)**

### 后端开发

因为是从零开始学习**SpringBoot框架**，并且自身的学习能力没有组长他们那么强:skull:，所以只是学习了最基本的功能。

**1**.文件上传、下载、删除接口函数编写总结

文件的上传下载等借口，使用最多的两个类是File以及MultipartFile类
* File类
```java
// 文件系统对象
// 后面很多方法也用到了文件系统类的方法，比如系统创建文件的时间等，这里简称fs，后面多处都用到了
private static final FileSystem fs = DefaultFileSystem.getFileSystem();
    
// 路径名
private final String path;
    
// 枚举类型
// file类对象的地址是否合法通过枚举类判定
private static enum PathStatus { INVALID, CHECKED };
    
// 文件路径是否有效
private transient PathStatus status = null;
// 检查路径是否有效 但只涉及nul字符，true绝对无效，false不一定有效
final boolean isInvalid() {
    if (status == null) {
        status = (this.path.indexOf('\u0000') < 0) ? PathStatus.CHECKED
                                                   : PathStatus.INVALID;
        }
    return status == PathStatus.INVALID;
    }
    
// 路径名前缀长度
private final transient int prefixLength;
int getPrefixLength() {
    return prefixLength;
}
    
// 依赖系统分隔符
public static final char separatorChar = fs.getSeparator();
// 将其转换为字符串表示
public static final String separator = "" + separatorChar;
```
| 函数                                     | 描述                               |
| ---------------------------------------- | ---------------------------------- |
| public File(String pathname)             | 创建路径名实例                     |
| public File(String parent, String child) | 父+子路径 创建实例                 |
| public File(URI uri)                     | 根据URI 路径创建一个新的 File 实例 |
* MultipartFile类
 ```java
  @Override
      public List<File> getCurFiles(String dir,String id) {
          QueryWrapper<File> wrapper=new QueryWrapper<>();
          wrapper.eq("f_dir",dir);
          wrapper.eq("mem_id",id);
          //File files = baseMapper.selectById(wrapper);
          List<File> files = baseMapper.selectList(wrapper);
          return files;
      }
  
      @Override
      public File getFiles(String id) {
          QueryWrapper<File> wrapper=new QueryWrapper<>();
          wrapper.eq("id",id);
          File file = baseMapper.selectOne(wrapper);
          return file;
      }
  
      @Override
      public List<File> getFindFile(String memid,String name) {
          QueryWrapper<File> wrapper=new QueryWrapper<>();
          wrapper.eq("mem_id",memid);
          wrapper.like("name",name);
          List<File> fileList = baseMapper.selectList(wrapper);
          return fileList;
      }
  ```
| 参数                  | 描述         |
| --------------------- | ------------ |
| getName()             | 文件格式     |
| getOriginalFilename() | 文件名       |
| getContentType        | 文件类型     |
| isEmpty()             | 文件是否为空 |
| getSize()             | 文件大小     |

**2**.文件加密

在搜索资料的过程中发现已经有大佬组装好了在SpringBoot中用于加密的组件（虽然可能这不太符合老师要求的加密方式:disappointed_relieved:），但是我觉得依然学到了学到了…:persevere:

（1）.xml中导入依赖
```java
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter -->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

（2） 测试类
```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class JasyptTestUtil {
 
    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
 
        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("secrect");                        // 加密的密钥，随便自己填写，很重要千万不要告诉别人
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "123456";         //自己的密码
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println("密码：" + encryptedText);
    }
 
    @Test
    public void testDe() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
 
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("secrect");
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "oOuQJhZQF9JcHz0dba78cw==";   //加密后的密码
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }
 
}
```

（3） yml中配置
```java
jasypt:
  encryptor:
    password: xxx(口令)
```

（4）yml中用生成的密文替换
```java
spring:
  ...
    username: 
    password: ENC(密文)
```

（5）启动类加上注解
```java
 @EnableEncryptableProperties
```
- **[jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot)**

**3**.限制文件大小

springboot使用MultipartFile上传，可以对文件上传大小的限制进行直接配置
```java
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

## 总结反思

我们这次的实验运用了**SpringBoot框架**，在学习运用的过程中，我对于SpringBoot有了基本的认识，首先它的基本流程可分为以下几个步骤：
1.new一个SpringApplication，构造方法里初始化一些属性
2.在实例化之后执行run方法主体，run执行流程是基于观察者模式的，整个SpringBoot的启动流程就是各种事件的发布，同时获取并启用监听器ApplicationListener，并发布应用启动事件SpringApplicationRunListener
3.准备环境变量，包含系统属性和用户配置的属性以及maven变量等，执行的代码块在 prepareEnvironment 方法内
4.创建应用程序的上下文createApplicationContext()
5.去spring.factories文件加载异常报告器SpringBootExceptionReporter
6.准备上下文环境prepareContext(执行实现了ApplicationContextInitializer 接口的类)
7.刷新上下文refreshContext，主要是自动装配和启动 tomcat
8.刷新上下文后置处理afterRefresh，空方法
9.发布上下文准备就绪事件
10.执行自定义的run方法。ApplicationRunner和CommandLineRunner

我也发现了该技术较于其他框架的优点：首先就是它可以自动配置SpringMVC等其它开源框架，同时简化了maven依赖与配置；然后就是它可以以jar包的形式独立运行，对于后端的测试较为方便。

在整个开发流程中，我们还存在部分技术细节和问题没有解决，比如说短信的验证和数字签名的验证，所以我们需要进一步去学习和实践，毕竟学无止境，不能只满足于已经实现的功能。

最后再次再次由衷地、发自内心地、饱含诚意地感谢我的组员:pray:，他们真的太强太厉害了:pray:，没有他们这个作业真的不在我能想象到的以我的能力能完成的范围之内:pray:感谢各位大佬带我:pray:。


