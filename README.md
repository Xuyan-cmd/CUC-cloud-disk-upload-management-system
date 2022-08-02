# ☁️中传放心传（CUC-cloud-disk-upload-management-system）

**一个基于Vue和springboot+mybatisplus实现的文件云盘管理系统**

<a href="https://git.io/typing-svg"><img src="https://readme-typing-svg.herokuapp.com?font=Montserrat&size=50&duration=1500&color=37BFF7&center=%E9%94%99%E8%AF%AF%E7%9A%84&vCenter=%E9%94%99%E8%AF%AF%E7%9A%84&multiline=true&width=900&height=150&lines=%E4%B8%AD%E4%BC%A0%E6%94%BE%E5%BF%83%E4%BC%A0;CUC-cloud-disk-upload-management-system" alt="Typing SVG" /></a>

### 📔任务清单

- 基于网页的用户注册与登录系统（60分）
  - 使用https绑定证书到域名而非IP地址 【 *PKI* *X.509* 】
  - 允许用户注册到系统
    - 用户名的合法字符集范围：中文、英文字母、数字
      - 类似：-、_、.等合法字符集范围之外的字符不允许使用
    - 用户口令长度限制在36个字符之内
    - 对用户输入的口令进行强度校验，禁止使用弱口令
  - 使用合法用户名和口令登录系统
  - 禁止使用明文存储用户口令 【PBKDF2、散列算法、慢速散列、针对散列算法（如MD5、SHA1等）的攻击方法】
    - 存储的口令即使被公开，也无法还原/解码出原始明文口令
  - （可选）安全的忘记口令 / 找回密码功能
  - （可选）微信/微博/支付宝的OAuth授权登录 / 注册绑定
  - （可选）双因素认证
    - OTP: Google Authenticator
    - Email
    - SMS
    - 扫码登录
- 基于网页的文件上传加密与数字签名系统（20分）
  - 已完成《基于网页的用户注册与登录系统》所有要求
  - 限制文件大小：小于 10MB
  - 限制文件类型：office文档、常见图片类型
  - 匿名用户禁止上传文件
  - 对文件进行对称加密存储到文件系统，禁止明文存储文件 【 *对称加密* *密钥管理（如何安全存储对称加密密钥）* *对称加密密文的PADDING问题* 】
  - 系统对加密后文件进行数字签名 【 *数字签名（多种签名工作模式差异）* 】
  - （可选）文件秒传：服务器上已有的文件，客户端可以不必再重复上传了
- 基于网页的加密文件下载与解密（20分）
  - 已完成《基于网页的文件上传加密与数字签名系统》所有要求
  - 提供匿名用户加密后文件和关联的数字签名文件的下载
    - 客户端对下载后的文件进行数字签名验证 【 *非对称（公钥）加密* *数字签名* 】
    - 客户端对下载后的文件可以解密还原到原始文件 【 *对称解密* *密钥管理* 】
  - 提供已登录用户解密后文件下载
  - 下载URL设置有效期（限制时间或限制下载次数），过期后禁止访问 【 *数字签名* *消息认证码* *Hash Extension Length Attack* *Hash算法与HMAC算法的区别与联系* 】
  - 提供静态文件的散列值下载，供下载文件完成后本地校验文件完整性 【 *散列算法* 】

**项目开发日志请查看：**[项目进度](https://www.xuyanblog.cn/%E5%AF%86%E7%A0%81%E5%AD%A6%E5%B0%8F%E5%AD%A6%E6%9C%9F%E5%AE%9E%E8%B7%B5)

### 💻功能概览

- 用户登录注册功能
  - 包括对于用户口令的强度检验、口令长度、口令内容信息限制
  - ![](https://s6.jpg.cm/2022/08/02/PQkdgf.png)

- 用户信息修改、头像上传、昵称修改、存储空间分配
  - <img src="https://s6.jpg.cm/2022/08/02/PQklGU.png" style="zoom:50%;" />

- 文件上传、重命名、删除功能完整实现
  - 对于支持文件类型包括：.txt .pdf .doc .mp3 .mp4 .zip .pdf .rar等，大小限制在了30MB
  - ![](https://s6.jpg.cm/2022/08/02/PQkVvw.png)

- 支持文件的下载和分享
  - 同时对于音频和视频文件的下载分享链接的设定有效时间为1小时
  - ![](https://s6.jpg.cm/2022/08/02/PQkXei.png)

- 支持对于文件在平台端的移动、文件夹的创建和属性信息的查看
  - ![](https://s6.jpg.cm/2022/08/02/PQkcsk.png)

- 支持多种文件排序方式，可以根据文件大小、创建时间进行排序
  - ![](https://s6.jpg.cm/2022/08/02/PQkUhe.png)

- 能够对文件进行收藏标记、同时支持对于jpg、png、mp4格式文件收录于相册
  - ![](https://s6.jpg.cm/2022/08/02/PQk1Jr.png)

- 已配置音乐播放器组件、视频播放组件，支持音乐、视频在线播放
  - ![](https://s6.jpg.cm/2022/08/02/PQkSwR.png)
  - ![](https://s6.jpg.cm/2022/08/02/PQkYiz.png)

- 实现了对于用户上传的文件数据进行加密，加密完成后，包括对于文件的下载、查看、删除都需要进行对应的口令校验
  - ![](https://s6.jpg.cm/2022/08/02/PQkFh4.png)

- 计算文件散列值，并与数据库中的原始文件进行对比校验，验证文件身份标识和是否被篡改
  - ![](https://s6.jpg.cm/2022/08/02/PQkmnX.png)

- 支持对于多个文件进行全局设定
  - ![](https://s6.jpg.cm/2022/08/02/PQkpJD.png)

### 🔏密码学理论与技术要点分析

- 对用户口令进行加密存储

  - Spring Security 支持多种不同的数据源，这些不同的数据源最终都将被封装成 UserDetailsService 的实例，我们是自己来创建一个类实现 UserDetailsService 接口，除了自己封装，我们也可以使用系统默认提供的 UserDetailsService 实例

  - 我们来看下 UserDetailsService 都有哪些实现类：

  - ![](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYmb6x5Teic0aJ1mApcZ96eibLhVBfSV1FNGuGKZIYyTxyr6KhP0OKNGfWofickqy0hjkyko1rTGGX0wQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

  - 可以看到，在几个能直接使用的实现类中，除了 InMemoryUserDetailsManager 之外，还有一个 JdbcUserDetailsManager，使用 JdbcUserDetailsManager 可以让我们通过 JDBC 的方式将数据库和 Spring Security 连接起来。

  - 这里需要数据库支持，所以我们在项目中添加如下两个依赖：

  - ```typescript
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    ```

  - 然后再在 application.properties 中配置一下数据库连接，配置完成后，就可以启动项目。
  - ![](https://s6.jpg.cm/2022/08/02/PQkvzp.png)

- 对用户口令的强度校验和规则设定

  - Vue框架下，可以通过前端页面对于用户注册输入的口令进行强度限制和规则设定，其中输入口令强度检验在js代码的编写中设定好规则，本项目规定密码只能为阿拉伯数字、英文字母（不区分大小写）、除".-\"以外的特殊符号进行设定，当用户采用其中不同组合时，密码强度的设定也为随之提高，本项目用颜色进行区分。

  - 而在特殊符号的筛选当中，使用正则表达式去进行筛选鉴别即可，确保用户的口令不会过于简单。

  - 同时在input的输入框也设置规则，包括用户的用户名、口令长度都进行限制，本项目用户名长度限制为11位的阿拉伯数字，口令长度限制为8-36

  - 实现代码如下：

  - ```typescript
    
    </el-form-item>
       <el-form-item>
          <el-input
            v-model="registered.password"
           @input="checkPassLever"
            type="password"
            placeholder="请输入密码"
            onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\-\_\.]/g,'')"
             maxlength="36"
             show-word-limit
             ></el-input>
             <table
               border="0"
               align="center"
               style="width: 200px;margin-left: 0px"
               >
              <tr>
               <td width="60px">
                <el-progress
                 :percentage="100"
                  :color="tr1"
                   :format="format"
                    ></el-progress>
                     </td>
           <td width="60px">
                <el-progress
                   :percentage="100"
                    :color="tr2"
                    :format="format"
                     ></el-progress>
                     </td>
      <td width="60px">
       <el-progress
         :percentage="100"
         :color="tr3"
         :format="format"
         ></el-progress>
         </td>
         <td width="20px">
          <div
          class="strength"
           :style="{ color: fontColor }"
            v-if="
            registered.password !== '' &&
            registered.password !== undefined
            "
            >
             {{ strength }}
            </div>
                </td>
                </tr>
                </table>
                </el-form-item>
    export default {
      name: "Login",
      data() {
        const checkPawd = (rule, value, callback) => {
          checkPasswd(value).then((res) => {
            if (res.msg !== "notCheck") {
              this.notCheck = false;
              if (res.msg === "低") {
                this.fontColor = "red";
                this.strength = this.indicator["red"];
              }
              if (res.msg === "中") {
                this.fontColor = "orange";
                this.strength = this.indicator["orange"];
              }
              if (res.msg === "高") {
                this.fontColor = "blue";
                this.strength = this.indicator["blue"];
      methods: {
        format(percentage) {
          return percentage === 100 ? "" : `${percentage}%`;
        },
        // 验证面膜等级
        checkPassLever(value) {
          // 红 蓝 绿
          if (value.length == 1) {
            this.tr2 = "#FFFFFF";
            this.tr3 = "#FFFFFF";
          }
          if (value.length == 2) {
            this.tr3 = "#FFFFFF";
          }
          if (value.length > 1) {
            var pd1 = /[._-]/;
            var pd2 = /[a-zA-Z]/;
            var pd3 = /[0-9]/;
            var x1 = pd1.test(value) && pd2.test(value);
            var x2 = pd3.test(value) && pd2.test(value);
            var x3 = pd1.test(value) && pd3.test(value);
            if (x1 || x2 || x3) {
              this.tr2 = "blue";
            } else {
              this.tr2 = "#FFFFFF";
            }
          }
          if (value.length > 2) {
            var zg2 = /^(?![^a-zA-Z]+$)(?!\D+$)(?![._-]+$)/;
            var zg3 = /[._-]/;
            if (zg2.test(value) && zg3.test(value)) {
              this.tr3 = "green";
            } else {
              this.tr3 = "#FFFFFF";
            }
          }
        },
    ```

- 对文件路由进行加密

  - 加密需要依赖 `crypto-js` 库

  - 在创建路由的时候，添加两个方法

  - stringifyQuery: 序列化传入的query参数，方法可以接收一个对象参数

    在`new Router`的时候传递这个属性，在序列化`query`参数的就执行这个方法，不会执行默认的方法，序列化后在地址栏显示序列化之后的参数

  - parseQuery: 解析地址栏参数，方法接收一个字符串参数

    在`new Router`的时候传递这个属性，在解析`query`参数的时候，回执行这个方法，不会在执行默认的方法。

    **注：** 这个方法只解析`path`中的参数，或者浏览器刷新的时候的地址栏的参数，不会对在`query`参数对处理，如：

    ```typescript
    this.$router.push({
      path: "foo?a=123",
      query: {
        b: 345
      }
    })
    ```

    - 在执行这段代码的时候，`parseQuery`方法不会对`query:{b: 345}`进行解析,会解析`path:"foo?a=123"`中的`a=123`的字符串

    - 序列化

      vue-router在执行`createRoute`的时候，获取`fullPath`会执行`getFullPath`方法

      createRouter 方法 会获取在 `new VueRouter`的时候传递的`stringifyQuery`方法，如果没有这个方法，就会在`getFullPath`的时候，使用默认的方法

    - 反序列化

      在调用`push`的时候，会执行`this.router.match`方法，`match`方法会执行`normalizeLocation`

      `normalizeLocation`通过`resolveQuery`方法解析path中的query，传入的三个参数（path中的?之后的参数数据字符串，使用push或replace方法传递的query参数，反序列化参数的方法）

      反序列化方法会通过`router && router.options.parseQuery`获取，如果在`new VueRouter`的时候传递了`parseQuery`方法，就是用该方法，如果没有就在`resolveQuery`方法中使用默认的方法

    - 最后用采用md5算法进行封装加密即可得到加密不可逆转的路由

    - ![](https://s6.jpg.cm/2022/08/02/PQwAbr.png)

- 对文件本身支持用户自定义口令进行加密

  - 前端使用jsencrypt.js。

    后端需要一个RSA工具类，包含生成密钥对、加解密等功能。

    流程：

    1.后端生成密钥对，并把公钥传递给前端，后端保存私钥

    2.前端用公钥加密数据后，传递给后端

    3.后端用私钥解密，获取数据

    注意：

    1.后端把公钥用base64编码为字符串传输。

    ```java
    import org.apache.commons.codec.binary.Base64;
     private static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
     }
    ```

    2.前端加密后的数据也是以base64编码传输的，后端需要base64解码。

- 获取文件散列值，并进行校验文件是否遭到篡改

  - MD5的加密方式是一种哈希加密。一些主流的编程语言都已经实现了MD5的加密，所以如果你的程序或是系统涉及到在多种语言之间的校验，那么MD5可以是备选之一。不过因为MD5是采用哈希函数来进行的加密，所以它无关密钥，也就是说在确定了明文的情况下，MD5就可以加密。不过MD5是不可逆的，只能加密，不能解密。

  - ```java
    public final class MD5 {
    
        public static String encrypt(String strSrc) {
            try {
                char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                byte[] bytes = strSrc.getBytes();
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(bytes);
                bytes = md.digest();
                int j = bytes.length;
                char[] chars = new char[j * 2];
                int k = 0;
                for (int i = 0; i < bytes.length; i++) {
                    byte b = bytes[i];
                    chars[k++] = hexChars[b >>> 4 & 0xf];
                    chars[k++] = hexChars[b & 0xf];
                }
                return new String(chars);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException("MD5加密出错！！+" + e);
            }
        }
    
        public static void main(String[] args) {
            System.out.println(MD5.encrypt("111111"));
        }
    
    }
    ```

### 🚀快速安装与使用方法说明

#### **🌐前端vue框架环境搭建**：

**1.安装node.js**

可以从官网下载各种系统下的安装包，地址：[node.js官网](https://nodejs.org/zh-cn/)

1）如果你是window系统，那简单了，直接下载.msi安装文件安装即可愉快的使用

2）如果你是linux系统，那安装方式就很多了，具体可以参考：[node.js下载地址](https://nodejs.org/en/download/package-manager/)

查看是否安装成功：

```bash
$node -v
```

**2.安装npm**

目前nodejs版本已经集成安装npm的包管理工具，所以无需额外再安装,查看安装是否成功：

```bash
$npm -v                          
```

注意：国内网络情况，直接npm安装包可能难以实现，可以通过国内npm源进行安装，如：淘宝https://npm.taobao.org 方便起见，可安装他们定制的cnpm命令行工具来代替npm

**同时推荐使用yarn管理工具**

Yarn 是 Facebook, Google, Exponent 和 Tilde 开发的一款新的 JavaScript 包管理工具。就像我们可以从官方文档了解那样，它的目的是解决这些团队使用 npm 面临的少数问题，即：

安装的时候无法保证速度/一致性 安全问题，因为 npm 安装时允许运行代码 我们可以用npm 下载yarn

```
npm install -g yarn
```

**3.安装vue-cli脚手架**

```bash
npm install -g @vue/cli
# 或者
yarn global add @vue/cli
```

检查vue-cli版本号命令： `vue --version`，不确定是否安装成功，可以输入检查版本号命令，如果没报错返回版本号，则说明安装成功。

**4.安装项目所需的依赖包**

将本项目git clone到本地后，通过`git bash`切换到`front_end`分支，在根目录下运行以下命令进行依赖安装：

```bash
npm install
```

**5.运行前端vue项目**

安装依赖完毕后，执行以下语句：

```bash
npm run serve
```

即可运行前端项目（**具体实现功能若要本地运行需要后端项目在本地同时运行**）

具体可供参考文档：[Vue.js官方文档](https://github.com/vuejs/vue)

#### **🌐后端Springboot框架搭建：**

spring-boot是由Pivotal团队提供的全新框架，其设计目的是用来简化新Spring应用的初始搭建以及开发过程。该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。通过这种方式，Boot致力于在蓬勃发展的快速应用开发领域（rapid application development）成为领导者。

**1.配置对应的java环境**

首先我们需要下载 java 开发工具包 JDK，下载地址：https://www.oracle.com/java/technologies/downloads/

并在主机配置好对应的环境变量，具体可参考：[java官方安装配置说明](https://docs.oracle.com/en/java/javase/18/install/overview-jdk-installation.html)

**2.在本地数据库创建本项目需要的表单（本项目数据库编写采用Mysql）**

执行以下sql文件内容：

[中传放心传数据库构建sql文件](https://github.com/Xuyan-cmd/CUC-cloud-disk-upload-management-system/blob/rear_end/space-master/space.sql)

根据本地数据库设定端口在后端文件的配置文件`application.properties`中写入对应的数据库账号、密码、接口，请确保端口能够正常调用。

**3.运行springboot框架**

推荐安装IntelliJ IDEA，将本项目克隆至本地后，切换到rear_end分支，并在IDEA中打开，直接运行即可。

### 🔮课程结题报告与系统功能操作视频演示地址
