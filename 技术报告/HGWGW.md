# ☁️中传放心传（CUC-cloud-disk-upload-management-system）技术报告

**一个基于Vue和springboot+mybatisplus实现的文件云盘管理系统**

[![Typing SVG](https://readme-typing-svg.herokuapp.com?size=30&duration=1497¢er=%E9%94%99%E8%AF%AF%E7%9A%84&vCenter=%E9%94%99%E8%AF%AF%E7%9A%84&multiline=true&width=620&height=70&lines=%E4%B8%AD%E4%BC%A0%E6%94%BE%E5%BF%83%E4%BC%A0;CUC-cloud-disk-upload-management-system)](https://git.io/typing-svg)

​**作者**：HGWGW :volcano:

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

我学习了一些基本用法：

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

#### 文件的上传和下载

##### 文件上传

对于上传文件，要将`MultipartFile`用作请求参数，此API应使用多部分表单数据值。

```java
@RestController
public class FileUploadController {
   @RequestMapping(value = "/upload", method = RequestMethod.POST, 
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

   public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
      File convertFile = new File("/var/tmp/"+file.getOriginalFilename());
      convertFile.createNewFile();
      FileOutputStream fout = new FileOutputStream(convertFile);
      fout.write(file.getBytes());
      fout.close();
      return "File is upload successfully";
   }
}
```

##### 文件下载

对于文件下载，应该使用`InputStreamResource`下载文件。需要在`Response`中设置`HttpHeader Content-Disposition`，并且需要指定应用程序的响应`Media Type`。

```java
@RequestMapping(value = "/download", method = RequestMethod.GET) 
public ResponseEntity<Object> downloadFile() throws IOException  {
   String filename = "/var/tmp/mysql.png";
   File file = new File(filename);
   InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
   HttpHeaders headers = new HttpHeaders();

   headers.add("Content-Disposition", String.format("attachment; filename=\\"%s\\"", file.getName()));
   headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
   headers.add("Pragma", "no-cache");
   headers.add("Expires", "0");

   ResponseEntity<Object> 
   responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(
      MediaType.parseMediaType("application/txt")).body(resource);

   return responseEntity;
}
```

### 视频播放器

这个文件管理云盘系统有一个播放视频的功能，我之前找到一个用python写的视频播放器，本项目中的视频播放器是用Java写的，我跟组长问了代码，对比一下二者的区别：

- 组长的Java视频播放器

```java
@ApiOperation(value = "根据视频vodioId获取播放地址")
    @PostMapping("getPlayAuth")
    public R getPlayAuth(@RequestParam("isList") List<String> isList) {

        //List<String>isList =Arrays.asList(list);
        System.out.println(isList);

        ArrayList urlList = new ArrayList();
//        File file = new File();
//         创建SubmitMediaInfoJob实例并初始化
//        DefaultProfile profile = DefaultProfile.getProfile(
//                "cn-Shanghai",                // // 点播服务所在的地域ID，中国大陆地域请填cn-shanghai
//                "LTAI5tNE97urNKXTDZaXCL48",        // 您的AccessKey ID
//                "qfkZet3UlBHc3l1VnUFXVIJ9PmjXPn");    // 您的AccessKey Secret
//        IAcsClient client = new DefaultAcsClient(profile);
//        GetPlayInfoRequest request = new GetPlayInfoRequest();
        // 视频ID。
        for (int i = 0; i < isList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println(isList.get(i));
//            file.setVideoId(isList.get(i));
//            map.put("videoId", isList.get(i));
//            request.setVideoId(isList.get(i));
            // request.setVideoId(id);
            File fileDb = fileService.getAllFileVedioId(isList.get(i));
            try {
                fileDb.setUrl(new String(DES.decrypt(Base64.getDecoder().decode(fileDb.getUrl()), password)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            try {
//                GetPlayInfoResponse response = client.getAcsResponse(request);
                // System.out.println(response.getPlayInfoList().get(3));
//                response.getPlayInfoList().
                //response.getVideoBase().getCoverURL();
                //System.out.println(new Gson().toJson(response));
//                for (GetPlayInfoResponse.PlayInfo playInfo : response.getPlayInfoList()) {
                    // 播放地址
//                    System.out.println("PlayInfo.PlayURL = " + playInfo.getPlayURL());
//                    file.setUrl(playInfo.getPlay
```

- 我的python的视频播放器

```python
import os
import sys
from PyQt5.QtGui import *
from PyQt5.QtCore import *
from PyQt5.QtWidgets import *
from PyQt5.QtMultimedia import *
from PyQt5.QtMultimediaWidgets import *


'''视频播放器'''
class VideoPlayer(QWidget):
    def __init__(self, parent=None, **kwargs):
        super(VideoPlayer, self).__init__(parent)
        # 初始化窗口
        self.setWindowTitle('VideoPlayer')
        self.setWindowIcon(QIcon(os.path.join(os.getcwd(), 'images/icon.png')))
        self.setGeometry(300, 50, 810, 600)
        self.setWindowFlags(Qt.WindowCloseButtonHint | Qt.WindowMinimizeButtonHint)
        palette = QPalette()  
        palette.setColor(QPalette.Background, Qt.gray)
        self.setPalette(palette)
        # 定义组件
        # --视频播放插件
        self.video_widget = QVideoWidget(self)
        self.video_widget.setGeometry(QRect(5, 5, 800, 520))
        palette = QPalette()
        palette.setColor(QPalette.Background, Qt.black)
        self.video_widget.setPalette(palette)
        self.video_widget.setStyleSheet('background-color:#000000')
        self.player = QMediaPlayer(self)
        self.player.setVideoOutput(self.video_widget)
        self.player.setVolume(50)
        # --当前的视频路径
        self.video_line_edit = QLineEdit('')
        # --选择视频按钮
        self.select_video_btn = QPushButton('选择')
        # --播放按钮
        self.play_btn = QPushButton(self)
        self.play_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/play.png')))
        self.play_btn.setIconSize(QSize(25, 25))
        self.play_btn.setStyleSheet('''QPushButton{border:none;}QPushButton:hover{border:none;border-radius:35px;}''')
        self.play_btn.setCursor(QCursor(Qt.PointingHandCursor))
        self.play_btn.setToolTip('播放')
        self.play_btn.setFlat(True)
        # --暂停按钮
        self.pause_btn = QPushButton('')
        self.pause_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/pause.png')))
        self.pause_btn.setIconSize(QSize(25, 25))
        self.pause_btn.setStyleSheet('''QPushButton{border:none;}QPushButton:hover{border:none;}''')
        self.pause_btn.setCursor(QCursor(Qt.PointingHandCursor))
        self.pause_btn.setToolTip('暂停')
        self.pause_btn.setFlat(True)
        self.pause_btn.hide()
        # --播放进度
        self.play_progress_label = QLabel('00:00 / 00: 00')
        self.play_progress_slider = QSlider(Qt.Horizontal, self)
        self.play_progress_slider.setMinimum(0)
        self.play_progress_slider.setSingleStep(1)
        self.play_progress_slider.setGeometry(QRect(0, 0, 200, 10))
        # --音量控制
        self.volume_slider = QSlider(Qt.Horizontal)
        self.volume_slider.setMinimum(0)
        self.volume_slider.setMaximum(100)
        self.volume_slider.setValue(50)
        self.mute_btn = QPushButton('')
        self.mute_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/sound.png')))
        self.mute_btn.setIconSize(QSize(25, 25))
        self.mute_btn.setStyleSheet('''QPushButton{border:none;}QPushButton:hover{border:none;}''')
        self.mute_btn.setCursor(QCursor(Qt.PointingHandCursor))
        self.mute_btn.setToolTip('禁音')
        self.mute_btn.setFlat(True)
        self.volume_label = QLabel('50')
        # --布局
        v_layout = QVBoxLayout()
        v_layout.setSpacing(0)
        v_layout.addStretch()
        h_layout = QHBoxLayout()
        h_layout.setSpacing(15)
        h_layout.addWidget(self.video_line_edit, 2, Qt.AlignVCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.select_video_btn, 0, Qt.AlignCenter | Qt.AlignVCenter)
        v_layout.addLayout(h_layout)
        h_layout = QHBoxLayout()
        h_layout.setSpacing(2)
        h_layout.addWidget(self.play_btn, 0, Qt.AlignCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.pause_btn, 0, Qt.AlignCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.play_progress_label, 0, Qt.AlignCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.play_progress_slider, 15, Qt.AlignVCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.mute_btn, 0, Qt.AlignCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.volume_slider, 0, Qt.AlignCenter | Qt.AlignVCenter)
        h_layout.addWidget(self.volume_label, 0, Qt.AlignCenter | Qt.AlignVCenter)
        v_layout.addLayout(h_layout)
        self.setLayout(v_layout)
        # 事件绑定
        self.player.durationChanged.connect(self.setVideoLength)
        self.player.positionChanged.connect(self.setPlayProgress)
        self.select_video_btn.clicked.connect(self.openvideo)
        self.play_btn.clicked.connect(self.playvideo)
        self.pause_btn.clicked.connect(self.pausevideo)
        self.mute_btn.clicked.connect(self.mute)
        self.volume_slider.valueChanged.connect(self.setVolume)
        self.play_progress_slider.sliderPressed.connect(self.playProgressSliderPressed)
        self.play_progress_slider.sliderReleased.connect(self.playProgressSliderReleased)
    '''播放进度条按下ing事件'''
    def playProgressSliderPressed(self):
        if self.player.state() != 0: self.player.pause()
    '''播放进度条按下释放事件'''
    def playProgressSliderReleased(self):
        if self.player.state() != 0:
            self.player.setPosition(self.play_progress_slider.value())
            self.player.play()
    '''播放视频'''
    def playvideo(self):
        if self.player.duration() == 0: return
        self.play_btn.hide()
        self.pause_btn.show()
        self.player.play()
    '''暂停视频'''
    def pausevideo(self):
        if self.player.duration() == 0: return
        self.play_btn.show()
        self.pause_btn.hide()
        self.player.pause()
    '''禁音'''
    def mute(self):
        if self.player.isMuted():
            self.mute_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/sound.png')))
            self.player.setMuted(False)
            self.volume_label.setText('50')
            self.volume_slider.setValue(50)
            self.player.setVolume(50)
        else:
            self.player.setMuted(True)
            self.volume_label.setText('0')
            self.volume_slider.setValue(0)
            self.mute_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/mute.png')))
    '''打开视频文件'''
    def openvideo(self):
        # 打开并显示视频路径
        filepath = QFileDialog.getOpenFileName(self, '请选择视频', '.')
        if filepath[0]:
            self.video_line_edit.setText(filepath[0])
        # 将视频路径初始化进视频播放插件
        filepath = self.video_line_edit.text()
        if not os.path.exists(filepath): return
        fileurl = QUrl.fromLocalFile(filepath)
        if fileurl.isValid():
            self.player.setMedia(QMediaContent(fileurl))
            self.player.setVolume(50)
    '''设置音量'''
    def setVolume(self):
        value = self.volume_slider.value()
        if value:
            self.player.setMuted(False)
            self.player.setVolume(value)
            self.volume_label.setText(str(value))
            self.volume_slider.setValue(value)
            self.mute_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/sound.png')))
        else:
            self.player.setMuted(True)
            self.volume_label.setText('0')
            self.volume_slider.setValue(0)
            self.mute_btn.setIcon(QIcon(os.path.join(os.getcwd(), 'images/mute.png')))
    '''播放进度设置'''
    def setPlayProgress(self):
        _, right = self.play_progress_label.text().split('/')
        position = self.player.position() + 1
        second = int(position / 1000 % 60)
        minute = int(position / 1000 / 60)
        left = str(minute).zfill(2) + ':' + str(second).zfill(2)
        self.play_progress_label.setText(left + ' /' + right)
        self.play_progress_slider.setValue(position)
    '''视频时长显示更改'''
    def setVideoLength(self):
        left, _ = self.play_progress_label.text().split('/')
        duration = self.player.duration()
        self.play_progress_slider.setMaximum(duration)
        second = int(duration / 1000 % 60)
        minute = int(duration / 1000 / 60)
        right = str(minute).zfill(2) + ':' + str(second).zfill(2)
        self.play_progress_label.setText(left + '/ ' + right)
    '''关闭窗口'''
    def closeEvent(self, event):
        self.player.stop()
    '''改变窗口大小'''
    def resizeEvent(self, event):
        size = event.size()
        self.video_widget.setGeometry(5, 5, size.width() - 5, size.height() - 80)


'''run'''
if __name__ == '__main__':
    app = QApplication(sys.argv)
    client = VideoPlayer()
    client.show()
    sys.exit(app.exec_())
```

- 两者的共同点：
  - 可以控制音量
  - 托拉进度条
- 两者的区别：
  - 组长的Java播放器：
    - 放大缩小屏幕
    - 画中画
    - 控制播放速度等功能
    - 可以下载视频
  - 我的Python播放器
    - 可以自主选择本地的视频来播放
    - 需要另外下载解码器
    - 由于功能还不完善，我的这个python播放器不能放大屏幕 :sob:

组长的Java视频播放器功能很强大！而且不需要另外下载解码器，很方便，膜拜大佬！:fist:


## 总结反思

这次小学期实践任务很难，尤其对于后端的知识，从最开始的一无所知，到现在慢慢掌握了一些方法，非常感谢我的大佬组长和组员们，他们真的好厉害！让我深刻认识到自己的不足，在今后的学习过程中我将向各位大佬看齐！感谢各位大佬愿意带我！ :sob: