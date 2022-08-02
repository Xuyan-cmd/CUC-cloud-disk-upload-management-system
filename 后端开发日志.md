# 中传放心传后端开发日志

### **2022.7.11 创建项目仓库，设置工作区**

### 2022.7.12创建项目初始化框架，生成demo文件

![](https://s6.jpg.cm/2022/08/01/PQNUuz.png)

- 项目结构介绍 

![](https://ask.qcloudimg.com/http-save/yehe-2492432/mig6y89iw4.png)

如上图所示，Spring Boot 项目结构如下

- `src/main/java` 主程序入口和项目开发
- `src/resources` 项目配置文件
- `src/test/java` 测试程序

此外，建议在包名下分别新建 controller、domain、service、mapper，这些分别表示

- controller：页面访问控制，也就是 api，
- domain：主要用于实体类和数据访问层（mapper）
- service：业务处理
- SpringBootProjctApplication.java：主程序，创建项目时会自动创建，一般为项目名称+Application.java

最后，启动 `SpringBootProjctApplication` 主程序。这样就完成 Java 项目配置了。

注：controller、domain、service、mapper 包，需要放在 SpringBootProjctApplication.java 主程序同包名或放在主程序下，否则主程序会扫描不到，导致报错。

- **Web 模块** 

在配置之前，先说明一下 pom.xml 文件。此文件包含 Spring Boot 版本、项目基本信息、第三方 Jar 包 Maven 引用。

所以，我们引用 Web 模块时，需要在 pom.xml 的 dependencies 添加以下代码:

```java
<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

其中，pom.xml 有两个默认的 模块

- spring-boot-starter：Spring Boot的核心启动器，包含了自动配置、日志和YAML。
- spring-boot-starter-test：支持常规的测试依赖，包括JUnit、Hamcrest、Mockito以及spring-test模块。

**编写 Controller api 内容**

在 controller 包下新建 `TestController.java`，内容如下:

```java
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "你好，我是 Spring Boot 项目测试！";
    }
}
```

这时，我们启动主程序，在浏览器输入localhost:8080/test，就可以看到返回的结果。是不是很简单，只需要很少的配置，就可以创建一个 Java Web 项目了。

![](https://ask.qcloudimg.com/http-save/yehe-2492432/8uvatzfmon.png?imageView2/2/w/1620)

### 2022.7.15建立数据库，设置好对应的表单名称、数据属性

- **建立数据库的Sql**

```mysql
/*
 Navicat Premium Data Transfer

 Source Server         : 11
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : 47.106.217.172:3306
 Source Schema         : space

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 19/07/2021 17:39:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `mem_id` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户ID',
  `name` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名字',
  `type` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  `collection` tinyint(9) NULL DEFAULT 0 COMMENT '是否收藏',
  `f_dir` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/root',
  `filetype` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'file',
  `video_id` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` bigint(100) NULL DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ucenter_member
-- ----------------------------
DROP TABLE IF EXISTS `ucenter_member`;
CREATE TABLE `ucenter_member`  (
  `id` char(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员id',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `neicun` bigint(100) NULL DEFAULT NULL COMMENT '内存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_dir
-- ----------------------------
DROP TABLE IF EXISTS `user_dir`;
CREATE TABLE `user_dir`  (
  `mem_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mem_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户目录结构',
  PRIMARY KEY (`mem_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Triggers structure for table ucenter_member
-- ----------------------------
DROP TRIGGER IF EXISTS `addUserDir`;
delimiter ;;
CREATE DEFINER = `space`@`%` TRIGGER `addUserDir` AFTER INSERT ON `ucenter_member` FOR EACH ROW BEGIN

       insert into user_dir values(NEW.id,'{"childrenList":[],"id":1,"name":"root/","parentId":0}');
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table ucenter_member
-- ----------------------------
DROP TRIGGER IF EXISTS `delUserDir`;
delimiter ;;
CREATE DEFINER = `space`@`%` TRIGGER `delUserDir` AFTER DELETE ON `ucenter_member` FOR EACH ROW BEGIN
    DELETE from user_dir WHERE mem_id =OLD.id;
      
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

```

- 初始化数据库内容：

![](https://s6.jpg.cm/2022/08/01/PQSFh5.png)

### 2022.7.16编写对应的数据接口，测试接口功能是否正常

```java
try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            File file1=new File();
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件类型
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String name=originalFilename.substring(0, originalFilename.indexOf("."));
            String type = fileType.substring(1);
            if(type.equals("bmp")||type.equals("mp3")||type.equals("jpg")||type.equals("jpeg")||type.equals("png")||type.equals("mp3")||type.equals("tif")||type.equals("gif")
                    ||type.equals("pcx")||type.equals("tga")||type.equals("exif")||type.equals("fpx")||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                    ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                    ||type.equals("WMF")||type.equals("webp")||type.equals("mp3")||type.equals("avif")){
                file1.setFiletype("image");
            }

            //2 把文件按照日期进行分类
            //获取当前日期
            //   2019/11/12
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            originalFilename = datePath + "/" + originalFilename;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, originalFilename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;
            file1.setName(name);
            file1.setType(type);
            file1.setUrl(url);
            file1.setFDir(catalogue);
            file1.setSize(file.getSize());
            return file1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
```

### 2022.7.19修复文件上传接口Bug，新增文件状态值检验

```java
public File upload(MultipartFile file,String catalogue) {
        // 工具类获取值
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            File file1=new File();
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件类型
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String name=originalFilename.substring(0, originalFilename.indexOf("."));
            String type = fileType.substring(1);
            if(type.equals("bmp")||type.equals("mp3")||type.equals("jpg")||type.equals("jpeg")||type.equals("png")||type.equals("mp3")||type.equals("tif")||type.equals("gif")
                    ||type.equals("pcx")||type.equals("tga")||type.equals("exif")||type.equals("fpx")||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                    ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                    ||type.equals("WMF")||type.equals("webp")||type.equals("mp3")||type.equals("avif")){
                file1.setFiletype("image");
            }

            //2 把文件按照日期进行分类
            //获取当前日期
            //   2019/11/12
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            originalFilename = datePath + "/" + originalFilename;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, originalFilename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
```

### 2022.7.20实现对用户口令进行加密和强度检验

**消息摘要(数据的指纹)**

**定义**

- 对不固定的消息（字符串，一段文本，一个文件），通过一种特定的算法，得到一个固定长度的文本，固定长度的文本叫做消息摘要
- 比如`我是程序员`经过特定的算法之后，得到了消息摘要为：`adaf02515dfds7885csdfcdsc`

**作用**

- 数据完整性的检验技术，我们将文本转换为消息摘要，然后比较消息摘要的值是否相等，如果相等那么表示两种文本相同

**特性**

- 不可逆的，不能从消息摘要再得到原来的文本

**特定的算法**

1. MD5
2. SHA

**实现步骤**

- **添加依赖jar包**
  - `commons-codec`

```java
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.10</version>
</dependency>
```

- 测试MD5Hex
  - 得到的是32位的16进制的字符串

```java
@Test
	public void test1(){
		String str1="你们好，未来的程序员!";
		String strMessageString=DigestUtils.md5Hex(str1);
		System.out.println(strMessageString);
	}
	
//读取文件
	@Test
	public void test2() throws IOException{
		InputStream inputStream=new FileInputStream(new File("/home/chenjiabing/Documents/Blog/AOP.md"));
		String message=DigestUtils.md5Hex(inputStream);
		System.out.println(message);
	}
```

**密码加密**

- 避免在数据库中明文保存密码，通过消息摘要技术对密码进行加密

**明文**

- 没有加密的文字(字符串)，能看懂的文字

**密文**

- 经过加密后的文字(字符串)，看不出来明文的意思

*加盐处理　salt*

- 为了提高密码的安全性
- 就是在用户的密码之后随便添加一个字符串，然后连接在一起生成摘要，那么即使获取摘要，也不会被破解。

```java
@Test
public void test3(){
	String str1="123456";
	String salt="这个是加盐处理";  //需要加盐，随便定义一个字符串
	String message=DigestUtils.md5Hex(str1+salt);  //获取加盐之后的消息摘要
	System.out.println(message);
}
```

**实现**

- 涉及到密码： 登录，注册，修改密码
- 创建一个`MD5Password工具`类，用于加密密码

```java
/**
 * 密码加密的类
 * @author chenjiabing
 */
public class MD5Password {
	private final static String SALT="中传放心传";  //加盐处理
	
	/**
	 * 获取加密之后的密码
	 * @param password 用户输入的密码
	 * @return  加密之后的密码
	 */
	public static  String getMd5Password(String password){
		return DigestUtils.md5Hex(password+SALT);  //使用了加盐处理
	}
}
```

- 在注册的时候对输入的密码进行加密存储到数据库中

```java
/**
	 * 注册
	 * 1. 调用selectUserByUserName(User user)方法判断用户名是否存在，返回对象u
	 * 2. 判断u是否为null，
	 * 3. 如果为null，调用insertUser(user)方法添加
	 * 4. 如果不为null，抛出异常提示controller用户名存在(UserNameAlreadyExistException)
	 */
	public void register(User user) throws UserNameAlreadyExistException {
		User u=userMapper.selectUserByUserName(user.getUsername());  //调用usermapper中的方法
		if (u!=null) {  //如果u不为null，表示用户名已经存在与数据库中，不可以再次注册了，因此抛出异常
			throw new UserNameAlreadyExistException("用户名已经存在，请重新输入!!!");
		}else {   //如果u==null，表示用户名不存在，可以添加
			//获取加密之后的密码
			String md5Password=MD5Password.getMd5Password(user.getPassword());
			//将加密之后的密码设置到user中，保存到数据库中
			user.setPassword(md5Password); 
			userMapper.insertUser(user);  //直接调用持久层方法插入数据即可
		}
	}
```

- 在登录的时候，将用户输入的密码进行加密获取到加密之后的密码，然后和数据库中的密码比较

```java
/**
	 * 登录方法
	 * 1. 通过selectUserByUserName返回user对象
	 * 2.判断user是否为null
	 * 3.如果user=null，抛出UserNotFoundException异常
	 * 4.如果user！=null，那么验证其中的密码是否正确
	 * 5.如果密码不匹配，抛出PassWordNotMatchException异常
	 * 6. 如果密码匹配，那么返回user对象
	 * @throws UserNotFoundException 
	 * @throws PassWordNotMatchException 
	 */
	public User login(String userName, String passWord) throws UserNotFoundException, PassWordNotMatchException {
		
		User user=userMapper.selectUserByUserName(userName);  //根据用户名查询，返回user对象
		if (user==null) {   //user为null，表示用户名不存在
			throw new UserNotFoundException("用户名不存在");
		}else {  //如果用户名存在，验证密码
			
			//获取加密之后的密码，实际是一个消息摘要
			String md5Password=MD5Password.getMd5Password(passWord);
			
			//使用加密之后获取的消息摘要和数据库中对应的密码比较
			if (md5Password.equals(user.getPassword())) {  //如果密码匹配
				return user;   //返回user对象即可
			}else {   //如果密码不相同，那么直接抛出密码不匹配的异常即可
				throw new PassWordNotMatchException("密码不匹配");
			}
		}
	}
```

- 在修改中，将旧密码加密后和数据库中的密码比较，并且将新密码加密更新到数据库中

```java
/**
	 * 修改密码
	 * 1. 根据id查询用户信息，返回user
	 * 2. 如果user=null,抛出用户不存在的异常
	 * 3. 如果user！=null，比较user中的密码和用户输入的旧密码oldPassword是否相同
	 * 4. 如果密码不相同，抛出密码不匹配的异常
	 * 5. 如果密码相同，表示用户输入的旧密码是正确的，那么更新密码即可 
	 */
	public void updatePassword(Integer id, String oldPassword,
			String newPassword) throws UserNotFoundException, PassWordNotMatchException {
		User user=userMapper.seletUserById(id);  //根据id查询，返回user对象
		if (user==null) {  //如果用户不存在
			throw new UserNotFoundException("当前登录的用户不存在");  //抛出用户不存在的异常
		}else {  //如果当前登录的用户存在
			//获取旧密码的加密之后的密码
			String oldMd5Password=MD5Password.getMd5Password(oldPassword);
			
			//使用加密之后的密码和数据库中的密码比较
			if (!user.getPassword().equals(oldMd5Password)) { //如果返回的user对象中的密码和用户输入的旧密码不匹配
				throw new PassWordNotMatchException("输入的旧密码不匹配");
			}else {  //如果输出的旧密码正确
				User u1=new User();   //创建User对象，封装修改所需的参数
				//获取加密之后的新密码
				String newMd5Password=MD5Password.getMd5Password(newPassword);
				
				u1.setPassword(newMd5Password);  //封装新密码，其中是加密之后的密码
				u1.setId(id);  //封装id
				userMapper.update(u1);  //调用修改的方法
			}
		}	
	}
```

### 2022.7.22新增文件接口参数加密，调整文件存储状态的返回值

我们先来定义一个加密工具类备用，加密这块有多种方案可以选择，对称加密、非对称加密，其中对称加密又可以使用 AES、DES、3DES 等不同算法，这里我们使用 Java 自带的 Cipher 来实现对称加密，使用 AES 算法：

```java
public class AESUtils {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    // 获取 cipher
    private static Cipher getCipher(byte[] key, int model) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(model, secretKeySpec);
        return cipher;
    }

    // AES加密
    public static String encrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    // AES解密
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(Base64.getDecoder().decode(data));
    }
}
```

这个工具类比较简单，不需要多解释。需要说明的是，加密后的数据可能不具备可读性，因此我们一般需要对加密后的数据再使用 Base64 算法进行编码，获取可读字符串。换言之，上面的 AES 加密方法的返回值是一个 Base64 编码之后的字符串，AES 解密方法的参数也是一个 Base64 编码之后的字符串，先对该字符串进行解码，然后再解密。

```java
public class RespBean {
    private Integer status;
    private String msg;
    private Object obj;

    public static RespBean build() {
        return new RespBean();
    }

    public static RespBean ok(String msg) {
        return new RespBean(200, msg, null);
    }

    public static RespBean ok(String msg, Object obj) {
        return new RespBean(200, msg, obj);
    }

    public static RespBean error(String msg) {
        return new RespBean(500, msg, null);
    }

    public static RespBean error(String msg, Object obj) {
        return new RespBean(500, msg, obj);
    }

    private RespBean() {
    }

    private RespBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public RespBean setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}
```

接下来我们定义两个注解 `@Decrypt` 和 `@Encrypt`：

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
public @interface Decrypt {
}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Encrypt {
}
```

这两个注解就是两个标记，在以后使用的过程中，哪个接口方法添加了 @Encrypt 注解就对哪个接口的[数据加密](https://cloud.tencent.com/solution/domesticencryption?from=10680)返回，哪个接口/参数添加了 @Decrypt 注解就对哪个接口/参数进行解密。这个定义也比较简单，没啥好说的，需要注意的是 `@Decrypt` 比 `@Encrypt` 多了一个使用场景就是 `@Decrypt` 可以用在参数上。

考虑到用户可能会自己配置加密的 key，因此我们再来定义一个 `EncryptProperties` 类来读取用户配置的 key：

```java
@ConfigurationProperties(prefix = "spring.encrypt")
public class EncryptProperties {
    private final static String DEFAULT_KEY = "www.itboyhub.com";
    private String key = DEFAULT_KEY;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
```

另外还有一点需要注意，`ResponseBodyAdvice` 在你使用了 `@ResponseBody` 注解的时候才会生效，`RequestBodyAdvice` 在你使用了 `@RequestBody` 注解的时候才会生效，换言之，前后端都是 JSON 交互的时候，这两个才有用。不过一般来说接口加解密的场景也都是前后端分离的时候才可能有的事。

先来看接口加密：

```java
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class EncryptResponse implements ResponseBodyAdvice<RespBean> {
    private ObjectMapper om = new ObjectMapper();
    @Autowired
    EncryptProperties encryptProperties;
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(Encrypt.class);
    }

    @Override
    public RespBean beforeBodyWrite(RespBean body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        byte[] keyBytes = encryptProperties.getKey().getBytes();
        try {
            if (body.getMsg()!=null) {
                body.setMsg(AESUtils.encrypt(body.getMsg().getBytes(),keyBytes));
            }
            if (body.getObj() != null) {
                body.setObj(AESUtils.encrypt(om.writeValueAsBytes(body.getObj()), keyBytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
}
```

我们自定义 EncryptResponse 类实现 ResponseBodyAdvice接口，泛型表示接口的返回类型，这里一共要实现两个方法：

1. supports：这个方法用来判断什么样的接口需要加密，参数 returnType 表示返回类型，我们这里的判断逻辑就是方法是否含有 `@Encrypt` 注解，如果有，表示该接口需要加密处理，如果没有，表示该接口不需要加密处理。
2. beforeBodyWrite：这个方法会在数据响应之前执行，也就是我们先对响应数据进行二次处理，处理完成后，才会转成 json 返回。我们这里的处理方式很简单，RespBean 中的 status 是状态码就不用加密了，另外两个字段重新加密后重新设置值即可。
3. 另外需要注意，自定义的 ResponseBodyAdvice 需要用 `@ControllerAdvice` 注解来标记。

再来看接口解密：

```java
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class DecryptRequest extends RequestBodyAdviceAdapter {
    @Autowired
    EncryptProperties encryptProperties;
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(final HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body);
        try {
            byte[] decrypt = AESUtils.decrypt(body, encryptProperties.getKey().getBytes());
            final ByteArrayInputStream bais = new ByteArrayInputStream(decrypt);
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return bais;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
```

1. supports：该方法用来判断哪些接口需要处理接口解密，我们这里的判断逻辑是方法上或者参数上含有 `@Decrypt` 注解的接口，处理解密问题。
2. beforeBodyRead：这个方法会在参数转换成具体的对象之前执行，我们先从流中加载到数据，然后对数据进行解密，解密完成后再重新构造 HttpInputMessage 对象返回。

接下来，我们再来定义一个自动化配置类，如下：

```java
@Configuration
@ComponentScan("org.javaboy.encrypt.starter")
public class EncryptAutoConfiguration {

}
```

最后，resources 目录下定义 META-INF，然后再定义 `spring.factories` 文件，内容如下：

```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=org.javaboy.encrypt.starter.autoconfig.EncryptAutoConfiguration
```

### 2022.7.26创建文件删除、修改接口并实现功能，修复用户登录接口由于用户ID重复造成的状态回显异常

- **文件删除、修改接口**

```java
@ApiOperation(value = "根据传入用户memid、目录路径url、和父文件夹id的删除文件夹")
    @PostMapping("deleteDirStruct/{memid}/{id}")
    public R deleteDirStruct(@PathVariable String memid, @PathVariable long id,@RequestBody String url) {
        UserDir userDir = userDirService.getUserDir(memid);
        TreeNode treeNode = JSON.parseObject(userDir.getMemDir(), new TypeReference<TreeNode>() {
        });
        boolean i=userDirService.deleteStruct(memid,url);
        System.out.println(i);
        if (i){
            StringBuffer sb=new StringBuffer();
            delete(treeNode, id,sb);
            //System.out.println(sb);
            String s = JSONObject.toJSONString(treeNode);
            userDir.setMemDir(s);
            userDirService.setUserDir(userDir);
            return R.ok();
        }else {
            return R.error();
        }
    }

    public static void insert(TreeNode treeNode, long id, TreeNode newNode) {
        List<TreeNode> list = treeNode.getChildrenList();
        List arrayList=new ArrayList();
        for (int i=0;i<list.size();i++){
            arrayList.add(list.get(i).getName());
        }
        arrayList.add(newNode.getName());
        System.out.println(arrayList);
        HashSet set=new HashSet<>(arrayList);
        Boolean result=set.size()==arrayList.size()?true:false;
        if (result==false){
            System.out.println("名字不可重复");
            throw new SpaceException(20001, "名字不可重复");
        }
        if (id==treeNode.getId()) {
                treeNode.getChildrenList().add(newNode);
                return;
        }
        //System.out.println(list.size());
        if (list == null || list.isEmpty()) {
            return;                            //若该结点 的子结点集合为空 返回
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (result==true){
                    insert(list.get(i), id, newNode);
                }
            }
        }
    }
```

- **用户登录接口查询检查**

```java
//登录
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        //member对象封装手机号和密码
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token= memberService.login(member);
        UcenterMember mem=memberService.login1(member);
        //System.out.println(mem);
        return R.ok().data("token", token).data("mem",mem);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    //查询用户信息
    @ApiOperation(value = "根据用户表id查询用户信息")
    @GetMapping("getMemberInfo/{id}")
    public R getMemberInfo(@PathVariable String id){
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        UcenterMember ucenterMember = memberService.getOne(wrapper);
        return R.ok().data("member",ucenterMember);
    }

    //修改用户信息
    @ApiOperation(value = "更新用户信息")
    @PostMapping("updateMemberInfo")
    public R updateMemberInfo(@RequestBody UcenterMember ucenterMember){
        String id = ucenterMember.getId();
        QueryWrapper<UcenterMember> w=new QueryWrapper<>();
        w.eq("id",id);
        UcenterMember one = memberService.getOne(w);
        UcenterMember member=new UcenterMember();
        member.setId(ucenterMember.getId());
        member.setNeicun(one.getNeicun());
        member.setAvatar(ucenterMember.getAvatar());
        member.setNickname(ucenterMember.getNickname());
        boolean b = memberService.updateById(member);
        if (b){
            return R.ok();
        }else{
            return R.error();
        }
    }
}
```

### 2022.7.30修改文件删除中的实现逻辑，使得在OSS的源文件能够备份，而前端不再拉取文件数据

- 为了能够对用户的上传文件进行身份验证，故不对数据库的源文件进行删除，而是在后端返回的data字段中不返回对应url值，即前端不再拉取对应的文件数据

```java
 public String delete(String id) {
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;
        // 日期目录
        // 注意，这里虽然写成这种固定获取日期目录的形式，逻辑上确实存在问题，但是实际上，filePath的日期目录应该是从数据库查询的
        QueryWrapper<File> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        File fileServiceOne = fileService.getOne(wrapper);
        String name = fileServiceOne.getName();
        //System.out.println(fileServiceOne);
        boolean remove = fileService.remove(wrapper);
        if (remove==true){
            System.out.println("删除成功");
        }
        else{
            System.out.println("删除失败");
        }
        SimpleDateFormat data = new SimpleDateFormat("yyyy/MM/dd");
        Date gmtCreate = fileServiceOne.getGmtCreate();
        data.format(gmtCreate);
        //String filePath = new DateTime().toString("yyyy/MM/dd");
        String filePath = data.toString();
        //System.out.println(filePath);
        try {
            /**
             * 注意：在实际项目中，不需要删除OSS文件服务器中的文件，
             * 只需要删除数据库存储的文件路径即可！
             */
            // 建议在方法中创建OSSClient 而不是使用@Bean注入，不然容易出现Connection pool shut down
            OSSClient ossClient = new OSSClient(endpoint,
                    accessKeyId, accessKeySecret);
            // 根据BucketName,filetName删除文件
            // 删除目录中的文件，如果是最后一个文件fileoath目录会被删除。
            String fileKey =filePath + "/" + name;
            ossClient.deleteObject(bucketName, fileKey);
            try {
            } finally {
                ossClient.shutdown();
            }
            System.out.println("文件删除！");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

```

### 2022.7.31实现对文件按大小、时间日期排序，优化显示效果

```java
@Override
public File upload(MultipartFile file,String catalogue) {
    // 工具类获取值
    String endpoint = ConstanPropertiesUtils.END_POIND;
    String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
    String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
    String bucketName = ConstanPropertiesUtils.BUCKET_NAME;

    try {
        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        File file1=new File();
        //获取上传文件输入流
        InputStream inputStream = file.getInputStream();
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String name=originalFilename.substring(0, originalFilename.indexOf("."));
        String type = fileType.substring(1);
        if(type.equals("bmp")||type.equals("mp3")||type.equals("jpg")||type.equals("jpeg")||type.equals("png")||type.equals("mp3")||type.equals("tif")||type.equals("gif")
                ||type.equals("pcx")||type.equals("tga")||type.equals("exif")||type.equals("fpx")||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                ||type.equals("WMF")||type.equals("webp")||type.equals("mp3")||type.equals("avif")){
            file1.setFiletype("image");
        }

        //2 把文件按照日期进行分类
        //获取当前日期
        //   2019/11/12
        String datePath = new DateTime().toString("yyyy/MM/dd");
        //拼接
        //  2019/11/12/ewtqr313401.jpg
        originalFilename = datePath + "/" + originalFilename;

        //调用oss方法实现上传
        //第一个参数  Bucket名称
        //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
        //第三个参数  上传文件输入流
        ossClient.putObject(bucketName, originalFilename, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //把上传之后文件路径返回
        //需要把上传到阿里云oss路径手动拼接出来
        //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
        String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;
        file1.setName(name);
        file1.setType(type);
        file1.setUrl(url);
        file1.setFDir(catalogue);
        file1.setSize(file.getSize());
        return file1;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
```









