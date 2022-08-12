# ☁️中传放心传（CUC-cloud-disk-upload-management-system）技术报告

**一个基于Vue和springboot+mybatisplus实现的文件云盘管理系统**

[![Typing SVG](https://readme-typing-svg.herokuapp.com?size=30&duration=1497¢er=%E9%94%99%E8%AF%AF%E7%9A%84&vCenter=%E9%94%99%E8%AF%AF%E7%9A%84&multiline=true&width=620&height=70&lines=%E4%B8%AD%E4%BC%A0%E6%94%BE%E5%BF%83%E4%BC%A0;CUC-cloud-disk-upload-management-system)](https://git.io/typing-svg)

​**作者**：Lime-Cocoa :cocktail:

**负责工作**：后端平台搭建

**时间**：2022.8

## 前言

本次小学期的实践目标是实现一个结合本专业密码学和网络安全知识体系的一个文件管理云盘系统，要求做到有文件管理的基础功能（增删改查）以外还要融入网络空间安全专业所特有的专业知识，是一次十分有意义的并且能够挖掘自身能力和一个深度学习的过程。

这对于我们来说是一个非常困难和具有挑战性的任务，在经过小组讨论后，大家都愿意通过这次机会从0开始搭建开发，学习一些新的内容，经过搭建的讨论后我们决定采用目前较为流行的前后端分离开发，前端使用**Vue框架**，后端使用较为轻量化的**SpringBoot框架**。而在具体开发过程中结合密码学知识基础和要求去完成这样一个云盘文件管理系统——**中传放心传**

但是我对前后端的开发都不太清楚，所以只能从零开始学习，在后端开发中花费了相当大的精力，这中间出现了很多Bug和技术壁垒，非常感谢大家，尤其是组内的大佬们，遇到困难却从不退缩，在大佬们的带领下(通天代)，以我及其微弱的作用，最终真的从0到完整开发出成品！

## 开发内容回顾

**项目主体开发思路框架：**![](https://s6.jpg.cm/2022/08/03/PQMxgX.jpg)

### 后端开发

- **文件上传、下载、删除接口函数编写总结**

  - 文件的上传下载等借口，使用最多的两个类File以及MultipartFile类

  - **File类**

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
     - **MultipartFile类**

    该接口有多个方法属性

    | 参数                    | 描述     |
    | --------------------- | ------ |
    | getName()             | 文件格式   |
    | getOriginalFilename() | 文件名    |
    | getContentType        | 文件类型   |
    | isEmpty()             | 文件是否为空 |
    | getSize()             | 文件大小   |
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

- **后端接口**

  **File**表

  ![](https://s3.bmp.ovh/imgs/2022/08/12/5bce21594d4de3e7.png)  

  **Ucenter_member**表

  ![](https://s2.loli.net/2022/08/12/ypUxsr4ZjF7Dhqw.png)

  **User_dir**表

  ![](https://s2.loli.net/2022/08/12/H4yi6x8ugRfaXYJ.png)

  - **登录接口**

    - 基本信息

      **Path:**`8001/educenter/member/login`

      **Method:**`POST`

      **接口描述：**

      传入手机号和密码的参数

      返回接口格式，形如`return R.ok().data("token", token).data("mem",mem);`

      ![](https://s2.loli.net/2022/08/12/o3SkF5l694Rf2st.png) 

      **登陆成功后返回参数：**

      ![](https://s2.loli.net/2022/08/12/usezfiawGQqyVK5.png)

  - **注册接口**

    - 基本信息

      **Path:**`8001/educenter/member/register `

      **Method:**`POST`

      **接口描述：**

      传参接口格式，

      ```java
      {
          "avatar":"string"
          "code":"string"
          "mobile":"string"
          "nickname":"string"
          "password":"string"
      }
      
      //avatar:上传头像地址
      //code:验证码
      //mobile:手机号
      //nickname:昵称
      //password:密码
      ```

  - **用户信息更新接口**

    - 基本信息

      **Path:**`8001/educenter/member/updateMemberInfo `

      **Method:**`POST`

      **接口描述：**

      上传接口格式

      ```java
      {
          "avatar":"string"
          "id":"string"
          "nickname":"string"
      }
      
      //Avatar:修改头像地址
      //Id:用户id
      //Nickname:修改的用户名称根据id获取用户信息接口
      ```

  - **根据id获取用户信息接口**

    - 基本信息

      **Path:**`8001/educenter/member/updateMemberInfo/{id}`

      **Method:**`GET`

      传入用户`id`

      ![](https://s2.loli.net/2022/08/11/y1Pwni3tNG5COx8.png)

      **接口描述：**

      返回接口格式

      ![](https://s3.bmp.ovh/imgs/2022/08/11/812e309c6d522b59.png) 

  - **上传文件接口**

    - 基本信息

      **Path:**`8001/eduoss/fileoss/upload/{memid}`

      **Method:**`POST`

      传入文件`file`和用户`id`和文件夹目录路径 

      **接口描述：**

      ![](https://s3.bmp.ovh/imgs/2022/08/11/71abfc9f91325f65.png) 

      `File`:要传入的文件

      `Catalogue`:要传入的文件所属文件夹(这里根目录默认是`/root`)

  - **上传头像接口**

    - 基本信息

      **Path:**`8001/eduoss/fileoss/uploadFileAvatar `

      **Method:** `POST`

      **接口描述：**

      ![](https://s3.bmp.ovh/imgs/2022/08/11/44b22f9f3290b790.png) 

  - **删除文件接口**

    - 基本信息

      **Path:**`8001/eduoss/fileoss/removeAlyVideo/{memid} `

      **Method:**`DELETE`

      **接口描述：**

      ![](https://s3.bmp.ovh/imgs/2022/08/11/8e70feb2ab0f035f.png) 

      `Islist`:传入的文件`id`（多选删除可以传入对应的文件id集合） 

  - **添加文件到数据库接口**

    - 基本信息

      **Path:**`8001/educenter/file/addFile`

      **Method:** `POST`

      **接口描述：**

      传入参数接口格式 

      ![](https://s3.bmp.ovh/imgs/2022/08/11/ac41bde440cf721e.png) 

  - **文件收藏接口**

    - 基本信息

      **Path:**`8001/educenter/file/collectFile `

      **Method:** `POST`

      **接口描述：**

      传入参数接口格式 

      ![](https://s3.bmp.ovh/imgs/2022/08/11/71a8805565ef660a.png) 

      传入要收藏的`id`（多选收藏的话可传入集合） 

  - **取消文件收藏接口**

    - 基本信息

      **Path:**`8001/educenter/file/cancelCollection `

      **Method:** `POST`

      **接口描述：**

      与文件收藏接口类似，传入要取消收藏的`id`（多选取消收藏的话可传入集合） 

  - **登录后根据客户id查询当下所有的文件接口**

    - 基本信息

      **Path:**`8001/educenter/file/getAllFileInfo/{memId} ` 

      **Method:** `GET`

      **接口描述：**

      传入用户`id(memId)`，返回接口格式，形如

      `return R.ok().data("fileList", fileList);`

      可获得当前用户下所有的文件集合 

      ![](https://s3.bmp.ovh/imgs/2022/08/12/ada1b5c00ad07417.png) 

  - **登录后根据文件id查询当文件信息接口**

    - 基本信息

      **Path:**`8001/educenter/file/getFileInfo/{id} ` 

      **Method:** `GET`

      传入文件`id`

      **接口描述：**

      `return R.ok().data("file", file) `

      ![](https://s3.bmp.ovh/imgs/2022/08/12/d1e1c071fa8006ae.png) 

  - **多选文件移动接口**

    - 基本信息

      **Path:**`8001/educenter/file/fileMove` 

      **Method:** `POST`

      传入要移动的目标文件夹路径`movingPath`和对应的文件`id `

      **接口描述：**

      `MovingPath`:目标文件夹路径

      `Id`:文件`id`（多选的话可传入对应集合）

  - **根据名字模糊查询接口**

    - 基本信息

      **Path:**`8001/educenter/file/fileFile/{memid}/{name}` 

      **Method:** `POST`

      传入当前用户id和对应名字（模糊） 

      **接口描述：**

      `memid`:当前用户`id`

      `name`：名字

      ![](https://s3.bmp.ovh/imgs/2022/08/12/f10f40ca61be8251.png) 

      响应

      ![](https://s3.bmp.ovh/imgs/2022/08/12/8422a8d169879953.png) 

  - **文件重命名接口**

    - 基本信息

      **Path:**`8001/educenter/file/updateFile/{id}/{name}` 

      **Method:** `POST`

      传入文件`id`和更改的文件名字（`name`） 

      **接口描述：**

      `Id`：文件`id`

      `Name`:修改后的名字

      ![](https://s3.bmp.ovh/imgs/2022/08/12/27e662b9f7b2a5e7.png) 

      修改后文件的名称为`123`

  - **根据当前路径查询对应的所有文件接口**

    - 基本信息

      **Path:**`8001/educenter/file/getCurDirFiles/{id} ` 

      **Method:** `GET`

      传入用户`id`和当前文件夹路径 

      ![](https://s3.bmp.ovh/imgs/2022/08/12/cc3384e93e11d2d5.png) 

      **接口描述：**

      ![](https://s3.bmp.ovh/imgs/2022/08/12/860e6dac480085ce.png) 

  - **新建文件夹**

    - 基本信息

      **Path:**`8001/educenter/dir/setUserDir/{id}/{memid}/{name} ` 

      **Method:** `POST`

      **接口描述：**

      ![](https://s3.bmp.ovh/imgs/2022/08/12/d725747346d86496.png) 

      `Memid`:用户`id`

      `Id`:父文件夹`id`

      `Name`:新建文件夹名字

      ![](https://s3.bmp.ovh/imgs/2022/08/12/f3f4aab386afee51.png) 

  - **修改文件夹接口**

    - 基本信息

      **Path:**`8001/educenter/dir/updateDirStruct/{memid}/{name}/{id}` 

      **Method:** `POST`

      **接口描述：**

      ![](https://s3.bmp.ovh/imgs/2022/08/12/e4a2af626a01c784.png) 

      返回接口形式

      ![](https://s3.bmp.ovh/imgs/2022/08/12/87177048c20f8143.png) 

  - **删除文件夹接口**

    - 基本信息

      **Path:**`8001/educenter/fir/deleteDirStruct/{memid}/{id}` 

      **Method:** `POST`

      **接口描述：**

      传入格式

      ![](https://s3.bmp.ovh/imgs/2022/08/12/f0901f63bcacd0ee.png) 

      `Memid`:用户`id`

      `Id`:要删除的文件夹`id`

      `url`:删除的文件夹目录路径（方便数据库`f_dir`列对应文件的删除）

      返回接口格式

      ![](https://s3.bmp.ovh/imgs/2022/08/12/7a5d54db67a11c2d.png) 

## 总结反思

在这次暑假实践里真的学习了很多东西，也挑战了一下自己的知识盲区，对于后端框架自己之前真的是一无所知，通过这个暑假的不断学习，最终也慢慢掌握了一些开发方法以及流程，真的要感谢我的组员们，在他们的帮助下解决了很多我所遇到的问题。最后真的感谢大佬们带我，真心感谢大家！