# ☁️中传放心传（CUC-cloud-disk-upload-management-system）技术报告

**一个基于Vue和springboot+mybatisplus实现的文件云盘管理系统**

[![Typing SVG](https://readme-typing-svg.herokuapp.com?size=30&duration=1497¢er=%E9%94%99%E8%AF%AF%E7%9A%84&vCenter=%E9%94%99%E8%AF%AF%E7%9A%84&multiline=true&width=620&height=70&lines=%E4%B8%AD%E4%BC%A0%E6%94%BE%E5%BF%83%E4%BC%A0;CUC-cloud-disk-upload-management-system)](https://git.io/typing-svg)

​**作者**：HGWGW

**负责工作**：项目规划讨论、部分功能测试

**时间**：2022.8

## 前言

本次小学期的任务是实现一个结合密码学和网络安全知识体系的文件管理云盘系统，经过讨论，大家决定前端使用**Vue框架**，后端使用**SpringBoot框架**，是一次非常难的任务，感谢组内大佬带我飞起！


## 开发内容回顾

**项目主体开发思路框架：**![](https://s6.jpg.cm/2022/08/03/PQMxgX.jpg)

### 前端开发

我之前只用vue框架做过一个静态的登陆页面，所以对于vue框架只知皮毛，在此次实践中有了更深入的了解。

- **vue的两个核心功能**

  - 声明式渲染：Vue 基于标准 HTML 拓展了一套模板语法，使得我们可以声明式地描述最终输出的 HTML 和 JavaScript 状态之间的关系。
  - 响应性：Vue 会自动跟踪 JavaScript 状态变化并在改变发生时响应式地更新 DOM

- 一个基本实例：
  - js：
  ```js
  import { createApp } from 'vue'

  createApp({
    data() {
      return {
        count: 0
      }
    }
  }).mount('#app')
  ```

  - template：
  ```html
  <div id="app">
    <button @click="count++">
      Count is: {{ count }}
    </button>
  </div>
  ```

  - 最终实现效果为一个计数器：点击按钮可以逐次增加数目

- 创建一个vue应用 

  - 应用实例

  每个 Vue 应用都是通过 `createApp` 函数创建一个新的 `应用实例`，示例代码如下：

  ```js
  import { createApp } from 'vue'

  const app = createApp({
    /* 根组件选项 */
  })
  ```

  - 根组件

  我们传入 `createApp` 的对象实际上是一个组件，每个应用都需要一个“根组件”，其他组件将作为其子组件。

  如果使用的是单文件组件，我们可以直接从另一个文件中导入根组件。

  ```js
  import { createApp } from 'vue'
  // 从一个单文件组件中导入根组件
  import App from './App.vue'

  const app = createApp(App)
  ```

  - 挂载一个应用

  应用实例必须在调用了 `.mount()` 方法后才会渲染出来。该方法接收一个“容器”参数，可以是一个实际的 DOM 元素或是一个 CSS 选择器字符串：

  ```html
  <div id="app"></div>
  ```

  ```js
  <div id="app"></div>
  ```

  应用根组件的内容将会被渲染在容器元素里面。容器元素自己将不会被视为应用的一部分。`.mount()` 方法应该始终在整个应用配置和资源注册完成后被调用。

  > DOM 中的根组件模板

  ```html
  <div id="app">
  <button @click="count++">{{ count }}</button>
  </div>
  ```

  ```js
  import { createApp } from 'vue'

  const app = createApp({
    data() {
      return {
        count: 0
      }
    }
  })

  app.mount('#app')
  ```

  当根组件没有设置 `template` 选项时，Vue 将自动使用容器的 `innerHTML` 作为模板。

  - 应用配置

  应用实例会暴露一个 `.config` 对象允许我们配置一些应用级的选项，例如定义一个应用级的错误处理器，它将捕获所有由子组件上抛而未被处理的错误：

  ```js
  app.config.errorHandler = (err) => {
  /* 处理错误 */
  }
  ```

### 后端开发

前端留的一些接口需要通过后端来实现跳转页面。

后端开发主要使用了SpringBoot框架，这是一个从无到有的过程，组长分享了一些学习资料，带领我们攻坚克难！

- 使用 IDEA 创建一个 Spring Boot 项目，首先要配置相关环境
  - 需具备Java环境，在 Java 官方网站下载 JDK 8.0 及以上版本
  - 下载安装及配置 Maven 3.x

#### 实现文件上传接口

**1.首先定义一个文件上传的服务service及其实现类：**

```java
public interface FileService {
  public FileReturn uploadFile(MultipartFile multipartFile);
}
```

  ```java
  @Service
  public class FileServiceImpl implements FileService {
      private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
      @Override
      public FileReturn uploadFile(MultipartFile multipartFile) {
  //        文件保存路径
          String filePath = "F:\\filepath";
  //        文件名
          String fileName = String.valueOf(System.currentTimeMillis());
          File file = new File(filePath +File.separator + fileName);
          FileOutputStream fileOutputStream = null;
          try {
              fileOutputStream = new FileOutputStream(file);
              IOUtils.copy(multipartFile.getInputStream(),fileOutputStream);
              System.out.println("===========file upload success=======");
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }finally {
              try {
  //                关闭
                  fileOutputStream.close();
              } catch (IOException e) {
                  e.printStackTrace();
                  logger.error("文件关闭错误",e);
              }
          }
          return new FileReturn<>(1,"文件上传成功",file);
      }
  }
  ```

**2.定义一个统一返回结果的实体类**

  ```java
  public class FileReturn<T> implements Serializable {
    private static final long serialVersionUID = -1959544190118740608L;
    private int resultCode;
    private String msg;
    private T data;
    public FileReturn() {
    }
    public FileReturn(int resultCode, String msg, T data) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.data = data;
    }
    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "FileReturn{" +
                "resultCode=" + resultCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
  }
  ```

**3.再定义一个工具类处理返回的结果：**

  ```java
  public class ReturnValue<T> implements Serializable {
    private static final long serialVersionUID = -1959544190118740608L;
    private int ret;
    private String msg;
    private T data;
    public ReturnValue() {
        this.ret = 0;
        this.msg = "";
        this.data = null;
    }
    public ReturnValue(int retCode, String msg, T data) {
        this.ret = 0;
        this.msg = "";
        this.data = null;
        this.ret = retCode;
        this.data = data;
        this.msg = msg;
    }
    public ReturnValue(int retCode, String msg) {
        this.ret = 0;
        this.msg = "";
        this.data = null;
        this.ret = retCode;
        this.msg = msg;
    }
    public ReturnValue(ReturnCodeAndMsgEnum codeAndMsg) {
        this(codeAndMsg.getCode(), codeAndMsg.getMsg(), null);
    }
    public ReturnValue(ReturnCodeAndMsgEnum codeAndMsg, T data) {
        this(codeAndMsg.getCode(), codeAndMsg.getMsg(), data);
    }
    public int getRet() {
        return this.ret;
    }
    public void setRet(int ret) {
        this.ret = ret;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return this.data;
    }
    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "ReturnValue{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
  }
  ```

**4.最后就是实现一个Controller，处理文件上传的请求：**

  ```java
  @RestController
  @RequestMapping(value="/file")
  public class FileController {
      @Autowired
      private FileService fileService;
      @RequestMapping("/upload")
      public FileReturn uploadFile(@RequestParam("uploadFile") MultipartFile multipartFile){
          return fileService.uploadFile(multipartFile);
      }
  }
  ```

**5.可以在application.yml中加入以下配置，限制大小：**

  ```java
  spring:
  servlet:
    multipart:
  //单个文件最大限制 类型是datasize，单位kb
      max-file-size: 1024
  //单次请求最大限制
      max-request-size: 2048
  ```

## 总结反思

这次小学期实践任务很难，尤其对于后端的知识，从最开始的一无所知，到现在慢慢掌握了一些方法，非常感谢我的大佬组长和组员们，他们真的好厉害！让我深刻认识到自己的不足，在今后的学习过程中我将向各位大佬看齐！感谢各位大佬愿意带我！