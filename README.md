# 中传放心传前端开发日志

------

### **2022.7.11 创建项目仓库，设置工作区**

### **2022.7.12 创建前端Vue项目，并进行初始化**

**总文件名称**：`Front-end-project`

![](https://s6.jpg.cm/2022/07/12/PnygZu.png)

**完成进度：**配置对应的登录初始界面，设置基本路由，前端总体开发采用Vue+Element ui

**更新内容：**

- **vue中路由的配置与使用**

引入路由:

```vue
import VueRouter from 'vue-router'1
```

路由实例化：

```vue
Vue.use(VueRouter)1
```

定义路由中的内容：

```vue
let router = new VueRouter({
    mode: 'history',
    routes: [//这里定义路由指向的页面
        {
            path: '/',//默认时指向的页面 根目录
            component: IndexPage
        },
        {
            path: '/detail/analysis',
            component: DetailPage
        },
        {   
            //假设'/detail'与'/detail/analysis'请求的内容相同时,
            //不能直接写成以下方式，必须写成重定向方式
            //重定向的内容是该组件的真正请求路径
            /**
                   {
                        path: '/detail',
                        component: DetailPage
                    }
            */
            path: '/detail',
            component: DetailPage,
            redirect: '/detail/analysis',
            //嵌套子路由
            children: [
                {
                    path: 'analysis',
                    component: DetailAnaPage
                },
                {
                    path: 'count',
                    component: DetailCouPage
                },
            ]
        }
    ]
})
```

将路由注入Vue对象：

```vue
new Vue({
  el: '#app',
  router,
})
```

Element UI技术文档：[Element UI技术文档](https://element.eleme.cn/#/zh-CN/component/installation)

### **2022.7.13 编写首页登陆界面，确定接口传递数据方式**

**更新内容：**

- 编写`login.vue`界面，确定用户向后端传递口令和账号信息用`ajax`传递

- 主页面初步呈现效果如下：

![](https://s6.jpg.cm/2022/07/13/P5GzNW.png)

![](https://s6.jpg.cm/2022/07/13/P5Gg5S.png)

- 修改页面的路由配置，初步建立文件上传、文件删除的页面跳转规则

```vue
import Vue from 'vue'
import VueRouter from 'vue-router'
import vuexIndex from '@/store/index.js'


const Index = () => import('views/index.vue')
const Files = () => import('views/files/Files.vue')
```

- 修复登陆跳转bug
- 登录成功能够进入初始页面

![](https://s6.jpg.cm/2022/07/13/P5G3LH.png)

### 2022.7.14进行前端文件管理页面优化，添加了多个小组件显示效果，优化了对应的界面显示

- 编写音乐播放组件`MusicPlayer.vue`，显示效果如下：

<img src="https://s1.ax1x.com/2022/07/15/jhfgFx.png" title="" alt="" data-align="center">

- 编写视频播放组件以及上传进度条组件，分别为`VideoPlayer.vue`和`ProgressDialog.vue`

- 组件使用规范如下：

```javascript
import VideoPlayer from "components/videoPlayer/VideoPlayer.vue";
import MusicPlayer from "components/musicPlayer/MusicPlayer.vue";
import ProgressDialog from "components/progressDialog/ProgressDialog.vue";

export default {
  components: {
    AsideBar,
    UserInfoCard,
    VideoPlayer,
    MusicPlayer,
    ProgressDialog,
  },}
```

- 编写登录后的系统主页`index.vue`

- 系统主页预期实现效果如下：

<img title="" src="https://s1.ax1x.com/2022/07/15/jh7gDU.png" alt="" data-align="center">

- 编写相册页`Albums.vue`

<img title="" src="https://s1.ax1x.com/2022/07/15/jh7xPA.png" alt="" data-align="left">

- 编写收藏夹页面`Collectes.vue`

<img title="" src="https://s1.ax1x.com/2022/07/15/jh7bDO.png" alt="" data-align="center">

- 页面跳转功能实现：

```javascript
data() {
    return {
      asideBarData: [
        {
          path: "/files",
          name: "文件",
          icon: "wenjian",
          componentName: "files",
          params: { path: "/root" },
        },
        { path: "/albums", name: "相册", icon: "xiangce1" },
        { path: "/collectes", name: "收藏夹", icon: "favorite" },
      ],
      downloadFileInfo: {
        name: "",
        url: "",
      },
    };
  }
```

- 编写用户管理小组件`UserInfoCard.vue`，效果如下：

<img src="https://s1.ax1x.com/2022/07/15/jhIp0H.png" title="" alt="" data-align="center">

### 2022.7.16实现登录注册界面功能，实现文件上传下载

**更新内容：**

- 编写后端数据接口，实现用户登录、注册口令存储到本地数据库，数据库编写采用SQLite

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

- 实现文件、图片格式上传存储

![](https://s6.jpg.cm/2022/07/16/P5HNkC.png)

![](https://s6.jpg.cm/2022/07/16/P5HrQR.png)

- 对用户的注册信息口令进行存储

在登录注册主页面，将用户信息存储到对应的接口所对应的数据库中

![](https://s6.jpg.cm/2022/07/16/P5HS9t.png)

**问题反馈**

对于如何加密用户的存储信息和加解密，以及实现多种方式注册信息查阅了相关资料

- 此处查阅了Vue框架对于登陆界面的规则文档，此出提供了一个思路，通过引入**crypto JS**去实现对于信息的存储

  - 示例如下：

  - **HTML code**

  - ```html
    <template>
        <el-form :model="ruleForm">
            < H3 class = "title" > system login</h3>
            <el-form-item prop="mobile"><el-input type="text" v-model=" ruleForm.mobile "Auto complete =" off "placeholder =" account "> < / El input > < / El form item >
            <el-form-item prop="password"><el-input type="text" v-model=" ruleForm.password "Auto complete =" off "placeholder =" password "> < / El input > < / El form item >
            < El checkbox V-model = "checked" checked > remember password < / El checkbox >
            <el-form-item><el-button type="primary" @ click.native.prevent= "Handlesubmit" > login < / El button > < / El form item >
        </el-form>
    </template>
    ```

  - **js code**

  - ```vue
    <script>
    Import cryptojs from 'crypto JS' // encrypt JS
    export default {
        data() {
            return {
                ruleForm: {
                    Mobile: ', // account number
                    Password: '// password
                },
                Checked: true // check remember password. True is selected
            };
        },
        //Judge whether to remember the password
        //Note that true here is a string format, because Boolean will become string when stored in localstorage
        created(){
            //Judge whether to remember the password
            if(localStorage.getItem("rememberPsw") == 'true'){
                this.getCookie()
            }
        },
        methods:{
            //Login method
            handleSubmit() {
                var that = this;
                let loginParams = {
                   mobile:  this.ruleForm.mobile , // get the account number
                   password:  this.ruleForm.password  //Get password
                };
                if (that.checked == true) {
                    //Incoming account, password, save days
                    that.setCookie(that.ruleForm.mobile, that.ruleForm.password, 1);
                } else {
                    //Empty cookie
                    that.clearCookie();
                }
                    localStorage.setItem("rememberPsw", that.checked);
                //// login request
                // that.$axios.post(`${api}/auth/login`,loginParams).then((res)=>{
                //     if(res.data.errCode == 0){
                //          console.log ('login succeeded ')
                //         if (that.checked == true) {
                //             //Incoming account, password, save days
                //             that.setCookie(that.ruleForm.mobile, that.ruleForm.password, 7);
                //         } else {
                //             //Empty cookie
                //             that.clearCookie();
                //         }
                //// jump route
                //         that.$router.push({ path: '/index' });
                //     }else{
                //          console.log ('login failed ')
                //     }
                // })
            },
            //Set cookie method
            setCookie(mobile, password, days) {
                var text =  CryptoJS.AES.encrypt (password, 'secret key 123'); // encrypt with cryptojs method
                Var savedays = new date(); // get time
                saveDays.setTime ( saveDays.getTime () + 24 * 60 * 60 * 1000 * days); // number of days to save
                console.log(saveDays)
                console.log(saveDays.toGMTString())
                //String splicing into cookie
                // window.document.cookie = "mobile" + "=" + mobile + ";path=/;saveDays=" + saveDays.toGMTString();
                // window.document.cookie = "password" + "=" + text + ";path=/;saveDays=" + saveDays.toGMTString();
                
                window.document.cookie = "mobile" + "=" + mobile + ";path=/;expires=" + saveDays.toGMTString();
                window.document.cookie = "password" + "=" + text + ";path=/;expires=" + saveDays.toGMTString();
            },
            //Read cookie
            getCookie() {
                if (document.cookie.length > 0) {
                    var arr =  document.cookie.split (';'; // the format shown here needs to be cut. You can output it by yourself
                    console.log(arr)
                    for (var i = 0; i < arr.length; i++) {
                        Var arr2 = arr [i]. Split ('='); // cut again
                        //Here, the array with mobile as item 0 and the array with password as item 0 will be cut to determine and find the corresponding value
                        if (arr2[0] == 'mobile') {
                            this.ruleForm.mobile  =Arr2 [1]; // get the account number
                        } else if (arr2[0] == 'password') {
                            //Get the encrypted password arr2 [1] and decrypt it
                            var bytes = CryptoJS.AES.decrypt(arr2[1].toString(), 'secret key 123');
                            var plaintext =  bytes.toString ( CryptoJS.enc.Utf8 ）; // get the decrypted password (the password entered during login)
                            this.ruleForm.password = plaintext;
                        }
                    }
                }
            },
            //Clear cookies
            clearCookie() {
                this.setCookie (',', 0); // set the account password to blank and the number of days to 0
            }
        }
    }
    </script>
    ```


### 2022.7.17优化前端功能页面，修复文件下载、排序、显示问题

**更新内容：**

- 修复由于接口回调过程中出现下载中断，导致的文件不能下载问题。

- 新增根据文件大小进行排序功能。

- ```java
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

  ![](https://s6.jpg.cm/2022/07/17/P5SmKT.png)

### 2022.7.19修复文件管理页面在从数据库调取数据中出现的文件不能显示和打开，新增文件列表显示功能

**更新内容：**

- 在vue前端调取文件拉去接口中赋予每个文件一个用户id用以区分不同身份用户

- 成功实现不同身份用户上传数据不会出现重复

- ```vue
  // axios拦截器
      instance.interceptors.request.use(config => {
          return config
      })
  
      if (method && method == 'post') {
          if (type && type == "params") {
              if (params) {
                  // return instance.post(url, params)
                  if (header == 'json') {
                      return instance.request({
                          url,
                          data: params,
                          method: 'post',
                          headers: {
                              'Content-Type': 'application/json;charset=UTF-8'
                          },
                      })
                  } else if (type == 'paramsSerializer') {
                      return instance.request({
                          url,
                          data: qs.stringify(params, { arrayFormat: 'repeat' }),
                          method: 'post',
                      })
                  }
                  else {
                      return instance.request({
                          url,
                          data: params,
                          method: 'post',
                      })
                  }
              }
              else {
                  return instance.post(url)
              }
          }
          else {
              // resful的形式
              if (params) {
                  for (var key in params) {
                      // 拼接url
                      url = url + '/' + params[key];
                  }
              }
              return instance.post(url);
          }
      } else if (!method || method == 'get') {
          if (type == 'resful' || !type) {
              // resful的形式
              if (params) {
                  for (var key in params) {
                      // 拼接url
                      url = url + '/' + params[key];
                  }
              }
              return instance.get(url);
          } else if (type == 'params') {
              console.log(params);
              params = {
                  params: params
              }
              return instance.get(url, params)
          }
      } else if (method && method == 'put') {
          if (params) {
              return instance.put(url, params)
          } else {
              return instance.put(url)
          }
      } else if (method && method == 'delete') {
          // resful的形式
          if (params) {
              for (var key in params) {
                  // 拼接url
                  url = url + '/' + params[key];
              }
          }
          return instance.delete(url);
      }
  }
  ```


### 2022.7.21修复前端传递数据到后端中由于文件大小导致的TTL过长而请求中断的问题

**方法解析：**

**原因：**

1. 服务器配置:例如在PHP中默认的文件上传大小为8M【post_max_size = 8m】，若你在一个请求体中放入8M以上的内容时，便会出现异常
2. 请求超时：当你设置了接口的超时时间为10s，那么上传大文件时，一个接口响应时间超过10s，那么便会被Faild掉。
3. 网络波动：这个就属于不可控因素，也是较常见的问题。

**分片上传**

- 创建切片，循环分解文件即可

```vue
  createFileChunk(file, size = chunkSize) {
    const fileChunkList = [];
    var count = 0;
    while (count < file.size) {
      fileChunkList.push({
        file: file.slice(count, count + size)
      });
      count += size;
    }
    return fileChunkList;
  }
```

- 循环创建切片,既然咱们做的是多文件，所以这里就有循环去处理，依次创建文件切片，及切片的上传。

```vue
async handleUpload(resume) {
  if (!this.container.files) return;
  this.status = Status.uploading;
  const filesArr = this.container.files;
  var tempFilesArr = this.tempFilesArr;

  for (let i = 0; i < tempFilesArr.length; i++) {
    fileIndex = i;
    //创建切片
    const fileChunkList = this.createFileChunk(
      filesArr[tempFilesArr[i].index]
    );
      
    tempFilesArr[i].fileHash ='xxxx'; // 先不用看这个，后面会讲，占个位置
    tempFilesArr[i].chunkList = fileChunkList.map(({ file }, index) => ({
      fileHash: tempFilesArr[i].hash,
      fileName: tempFilesArr[i].name,
      index,
      hash: tempFilesArr[i].hash + '-' + index,
      chunk: file,
      size: file.size,
      uploaded: false,
      progress: 0, // 每个块的上传进度
      status: 'wait' // 上传状态，用作进度状态显示
    }));
    
    //上传切片
    await this.uploadChunks(this.tempFilesArr[i]);
  }
}
```

- 上传切片，这个里需要考虑的问题较多，也算是核心吧，uploadChunks方法只负责构造传递给后端的数据，核心上传功能放到sendRequest方法中

```vue
 async uploadChunks(data) {
  var chunkData = data.chunkList;
  const requestDataList = chunkData
    .map(({ fileHash, chunk, fileName, index }) => {
      const formData = new FormData();
      formData.append('md5', fileHash);
      formData.append('file', chunk);
      formData.append('fileName', index); // 文件名使用切片的下标
      return { formData, index, fileName };
    });

  try {
    await this.sendRequest(requestDataList, chunkData);
  } catch (error) {
    // 上传有被reject的
    this.$message.error('亲 上传失败了,考虑重试下呦' + error);
    return;
  }

  // 合并切片
  const isUpload = chunkData.some(item => item.uploaded === false);
  console.log('created -> isUpload', isUpload);
  if (isUpload) {
    alert('存在失败的切片');
  } else {
    // 执行合并
    await this.mergeRequest(data);
  }
}
```

- sendReques。上传这是最重要的地方，也是容易失败的地方,假设有10个分片，那我们若是直接发10个请求的话，很容易达到浏览器的瓶颈，所以需要对请求进行并发处理。
  - 并发处理：这里我使用for循环控制并发的初始并发数，然后在 handler 函数里调用自己，这样就控制了并发。在handler中，通过数组API.shift模拟队列的效果，来上传切片。
  - 重试: retryArr 数组存储每个切片文件请求的重试次数，做累加。比如[1,0,2],就是第0个文件切片报错1次，第2个报错2次。为保证能与文件做对应，const index = formInfo.index; 我们直接从数据中拿之前定义好的index。 若失败后，将失败的请求重新加入队列即可。

```vue
    // 并发处理
sendRequest(forms, chunkData) {
  var finished = 0;
  const total = forms.length;
  const that = this;
  const retryArr = []; // 数组存储每个文件hash请求的重试次数，做累加 比如[1,0,2],就是第0个文件切片报错1次，第2个报错2次

  return new Promise((resolve, reject) => {
    const handler = () => {
      if (forms.length) {
        // 出栈
        const formInfo = forms.shift();

        const formData = formInfo.formData;
        const index = formInfo.index;
        
        instance.post('fileChunk', formData, {
          onUploadProgress: that.createProgresshandler(chunkData[index]),
          cancelToken: new CancelToken(c => this.cancels.push(c)),
          timeout: 0
        }).then(res => {
          console.log('handler -> res', res);
          // 更改状态
          chunkData[index].uploaded = true;
          chunkData[index].status = 'success';
          
          finished++;
          handler();
        })
          .catch(e => {
            // 若暂停，则禁止重试
            if (this.status === Status.pause) return;
            if (typeof retryArr[index] !== 'number') {
              retryArr[index] = 0;
            }

            // 更新状态
            chunkData[index].status = 'warning';

            // 累加错误次数
            retryArr[index]++;

            // 重试3次
            if (retryArr[index] >= this.chunkRetry) {
              return reject('重试失败', retryArr);
            }

            this.tempThreads++; // 释放当前占用的通道

            // 将失败的重新加入队列
            forms.push(formInfo);
            handler();
          });
      }

      if (finished >= total) {
        resolve('done');
      }
    };

    // 控制并发
    for (let i = 0; i < this.tempThreads; i++) {
      handler();
    }
  });
}
```

- 切片的上传进度，通过axios的onUploadProgress事件，结合createProgresshandler方法进行维护

```vue
// 切片上传进度
createProgresshandler(item) {
  return p => {
    item.progress = parseInt(String((p.loaded / p.total) * 100));
    this.fileProgress();
  };
}
```

- 因此此处使用切片进行项目对于文件的上传和拉取：

```typescript
// 请求文件列表
    async getListData() {
      if (this.$route.params.path.search("search") !== -1) return;
      let res = await this.$request(
        `/educenter/file/getCurDirFiles/${this.$store.state.userInfo.id}`,
        this.$route.params.path,
        "post",
        "params",
        "json"
      );
      if (res.data.success) {
        if (this.$store.state.sortType == "size") {
          res.data.data.files.sort((a, b) => {
            return a.size - b.size;
          });
        }
        this.listData = res.data.data.files;
        await this.getVideoList(res.data.data.files);
      } else {
        this.$message.error("获取文件列表失败,请刷新页面重试!");
      }
    },

    // 获取文件目录树
    async getFolderList(dir) {
      if (!dir) {
        let res = await this.$request("/educenter/dir/getUserDir", {
          id: this.$store.state.userInfo.id,
        });
        if (res.data.data.dir == null) {
          this.$router.replace("/login");
          return;
        }
        this.folderList = JSON.parse(res.data.data.dir.memDir);
        this.$store.commit(
          "updateFolderList",
          JSON.parse(res.data.data.dir.memDir)
        );
        this.$store.commit("updateIsGetingFolder", false);
      } else {
        this.folderList = JSON.parse(dir);
        this.$store.commit("updateFolderList", JSON.parse(dir));
        this.$store.commit("updateIsGetingFolder", false);
      }
    },

    // 获取当前文件中的所有video文件
    getVideoList(listData) {
      listData.forEach(async (item, index, arr) => {
        if (item.filetype === "video") {
          arr[index].url = await this.getVideoUrl(item.videoId);
        }
      });
    },

    // 根据传入videoId获取url
    async getVideoUrl(videoId) {
      let res = await this.$request(
        "/eduoss/fileoss/getPlayAuth?isList=" + videoId,
        "",
        "post"
      );
      return res.data.data.urlList[0].url;
    },

    // 将上传成功的对象push到当前listData中
    async pushUploadData(item) {
      if (item.filetype == "video") {
        item.url = await this.getVideoUrl(item.videoId);
      }
      this.listData.push(item);
    },
```

### 2022.7.22修改文件存储方式，改用直接存储到阿里的OSS中

此前文件的上传和拉去均在本地数据库实现，为了能够更好的实现文件的存储管理，因此我开通了阿里的OSS对象存储服务，直接将文件统一上传到线上。

- 将文件上传接口和拉去接口联通到开通的OSS上（涉及到保密机制，故此处不给出全部代码）

![](https://s6.jpg.cm/2022/08/01/PQuTvD.png)

- 写一个公用的**ali-oss.js**

```typescript
// 引入ali-oss
let OSS = require('ali-oss')

/**
 *  [accessKeyId] {String}：通过阿里云控制台创建的AccessKey。
 *  [accessKeySecret] {String}：通过阿里云控制台创建的AccessSecret。
 *  [bucket] {String}：通过控制台或PutBucket创建的bucket。
 *  [region] {String}：bucket所在的区域， 默认oss-cn-hangzhou。
 */
let client = new OSS({
  region: '<oss region>',
  secure: true,  // secure: 配合region使用，如果指定了secure为true，则使用HTTPS访问  
  accessKeyId: '<Your accessKeyId>',
  accessKeySecret: '<Your accessKeySecret>',
  bucket: '<Your bucket name>'
})
```

- 调用

```typescript
/** 
*  上传文件，大小不能超过5GB 
* @param {string} ObjName OSS的储存路径和文件名字 
* @param {string} fileUrl 本地文件 
* @retruns Promise 
*/
export const put = async (ObjName, fileUrl) => {  
  try {    
    let result = await client.put(`${ObjName}`, fileUrl)    
    // ObjName为文件名字,可以只写名字，就直接储存在 bucket 的根路径，如需放在文件夹下面直接在文件名前面加上文件夹名称    
    return result  
  } catch (e) {    
   console.log(e)  
  }
}

// 上传成功之后，转换真实的地址
export const signatureUrl= async (ObjName) => {    
  try {        
    let result = await client.signatureUrl(`${ObjName}`)    
    return result  
  } catch (e) {    
    console.log(e)  
  }
}
```

- 使用Element-UI的 Upload 组件的自定义方法http-request上传，覆盖默认的。

```typescript
<template>
  <div class="hello">
    <el-upload
      class="upload-demo"
      action
      :http-request="handleUpload"
      :before-upload="beforeUpload"
    >
      <el-button size="small" type="primary">点击上传</el-button>
    </el-upload>
  </div>
</template>

<script>
import { put, signatureUrl, getFileNameUUID } from '@/utils/ali-oss'

export default {
  name: 'Upload',
  },
  methods: {
    beforeUpload(file) {      
      // 限制上传类型      
      const fileExtensions = getFileName(file.name) === '.doc' || getFileName(file.name) === '.docx' || getFileName(file.name) ==='.pdf'      
      //限制的上限为20M      
      const max20M = file.size / 1024 / 1024 < 20;      
      if (!fileExtensions) {        
        this.$message.error('上传文件类型可以是 .doc, .docx, .pdf 等多种格式');      
      }      
      if (!max20M) {        
        this.$message.error('上传文件大小不能超过 10MB!');      
      }      
      return fileExtensions && max20M;    
    },
    /**
     * 自定义上传方法
     */
    handleUpload(option) {
      // 获取文件的后缀名
      let objName = getFileNameUUID()
      var obj = option.file.name
      var index = obj.lastIndexOf(".");      
      obj = obj.substring(index,obj.length);
      // 生成的文件名，保留文件后缀名，进行拼接      
      let objName = getFileNameUUID() + getFileName(option.file.name)

      // 调用 ali-oss 中的方法,flieName是存放的文件夹名称，可自己定义
      put(`flieName/${objName}`, option.file).then(res => {        
        console.log(res)
        // 上传成功之后，转换真实的地址
        signatureUrl(`flieName/${objName}`).then(res => {          
            console.log(res)        
        })
      })
    }
  }
}
</script>
```

![](https://s6.jpg.cm/2022/08/01/PQuaip.png)

### 2022.7.24优化文件管理功能增加文件移动、重命名、属性查看功能

**更新内容：**

- 新增文件拖拽移动功能，修复文件移动时造成的获取数据失败Bug

![](https://s6.jpg.cm/2022/08/01/PQ1ExD.png)

- 调用ali-oss的数据接口，获取已上传的文件属性内容

  用于当前 Vue 实例的初始化选项。需要在选项中包含自定义属性时会有用处。

  `$option` 是用来获取data外面的数据和方法。

  `this.$options` 即可以获取自定义属性，也可以增加自定义属性，而且，获取自定义属性的方法有两种。

     `this.mydata = this.$options['myoption']`
     `this.mydata1 = this.$options.myoption`

```typescript
<template>
  <div>
    <h2>Vue的实例属性 $options</h2>
    <button type="button" @click="handleClick">点击获取值</button>
    <p>data外部变量 {{ $options.obj.name }}---{{ $options.obj.age }}</p>
    <p v-if="mydata">data数据 {{ mydata.name }}---{{ mydata.age }}</p>
  </div>
</template>
<script>
export default {
  // 在data外面定义的属性和方法通过$options可以获取和调用
  obj: {
    name: "muzidigbig",
    age: 22,
  },
  say() {
    // this 指向当前实例对象
    // console.log(this);
    console.log("我是 data 外部方法");
  },
  data() {
    return {
      mydata: null,
    };
  },
  created() {
    // 调用 data 外部方法
    this.$options.say();
    console.log(this, this.$options);
    console.log(this.$options.obj);
  },
  methods: {
    handleClick() {
      // 修改 data 外部 obj 对象的数据，视图也会改变
      this.$options.obj["age"] = 23;
      // 将 obj 赋值给 mydata
      this.mydata = this.$options["obj"];
    },
  },
};
</script>
```

![](https://s6.jpg.cm/2022/08/01/PQ1k6p.png)

### 2022.7.26新增文件查找功能

这里涉及到在vue中定义和调用函数，需要用到关键字`methods`,然后便可以在里面定义函数了。

这里关键点：

- `document.getElementById('open').files[0].path`，这里获取文件路径的方法是获取文件类元素的数组，然后通过`path`关键字获取文件的绝对路径。

```typescript
// js递归遍历树形json数据，根据关键字查找节点
    //@leafId  查找的id，
    //@nodes   原始Json数据
    //@path    供递归使用
    findPathByLeafId(leafId, nodes, path) {
      if (path === undefined) {
        path = [];
      }
      for (var i = 0; i < nodes.length; i++) {
        var tmpPath = path.concat();
        tmpPath.push(nodes[i].name);
        if (leafId == nodes[i].name) {
          return tmpPath;
        }
        if (nodes[i].childrenList) {
          var findResult = this.findPathByLeafId(
            leafId,
            nodes[i].childrenList,
            tmpPath
          );
          if (findResult) {
            return findResult;
          }
        }
      }
    },

    // 打开当前双击的文件夹
    // 点击的是folderList中第 index个子目录
    openCurrentFolder(item) {
      console.log("打开文件", item);
      let currentFolder = (
        "/" + this.findPathByLeafId(item.name, [this.folderList]).join("")
      ).slice(
        0,
        this.findPathByLeafId(item.name, [this.folderList]).join("").length
      );
      // let currentFolder =
      //   this.$route.params.path +
      //   "/" +
      //   item.name.substr(0, item.name.length - 1);
      // // 在vuex中更新当前目录
      // this.$store.commit("updateCurrentFolder", currentFolder);
      this.$router.push({ name: "files", params: { path: currentFolder } });
    },

    // 判断当前所在的文件夹位置
    getCurrentLocation() {
      if (this.$route.params.path.search("search") != -1) return;
      let currentFolder = this.$route.params.path;
      currentFolder = currentFolder.slice(1, currentFolder.length);
      let arr = currentFolder.split("/");
      // 如果是/search就不计算当前位置了
      // if (arr[arr.length - 1] == "search") {
      //   return;
      // }
      this.currentChildrenFolder = this.folderList.childrenList;
      if (arr.length > 1) {
        // 说明不在根目录
        for (var i = 1; i < arr.length; i++) {
          let index = this.currentChildrenFolder.findIndex(
            (item) => item.name.substr(0, item.name.length - 1) == arr[i]
          );
          this.currentFolderId = this.currentChildrenFolder[index].id;
          this.currentChildrenFolder = this.currentChildrenFolder[
            index
          ].childrenList;
        }
      } else {
        this.currentFolderId = 1;
      }
    },
```

![](https://s6.jpg.cm/2022/08/01/PQ1zo2.png)

![](https://s6.jpg.cm/2022/08/01/PQ1gbW.png)

### 2022.7.28用户登录界面对用户口令进行限制和密码强度检测

**用户名和口令**

**限用户输入一些非常容易被破解的口令**。如什么qwert，123456,password之类，就像twitter限制用户的口令一样做一个口令的黑名单。另外，可以限制用户口令的长度，是否有大小写，是否有数字，可以用程序做一下校验。当然，这可能会让用户感到很不爽，所以，现在很多网站都提供了UX让用户知道他的口令强度是什么样的这样可以让用户有一个选择，目的就是告诉用户——要想安全，先把口令设得好一点。

基于这种想法，我对于用户在注册时的口令进行了密码强度检验，对密码长度和复杂程度都进行了一个展示，通过下方出现的颜色字条来标识密码强度

**例：**

- 当密码为：123456789时，强度颜色条为红色

<img src="https://s6.jpg.cm/2022/08/01/PQ13VL.png" style="zoom:33%;" />



- 当密码为：xu123456789时，强度颜色条为蓝色

<img src="https://s6.jpg.cm/2022/08/01/PQ1CFf.png" style="zoom:33%;" />

- 当密码为xu123456789-.（此处设定-、_、.等合法字符集范围之外的字符不允许使用）

<img src="https://s6.jpg.cm/2022/08/01/PQ1KyU.png" style="zoom:33%;" />

**此外，在存储用户口令数据到数据库时，采用了md5方式进行了加密**

这里有两种方式可供参考

直接在需要使用md5加密的页面引入

```javascript
import md5 from 'js-md5';
```

然后将想要加密的数据放入

```ini
let a = md5("111111s");
console.log('a', a);  //结果为c85dfcf2cf8e79ba8239eff965483c5b
```

全局挂载，将js-md5添加到vue原型链上

```javascript
//在vue项目的mian.js文件中，引入js-md5并挂载原型
import md5 from 'js-md5';  
Vue.prototype.$md5 = md5;
```

然后将想要加密的数据放入

```ini
let a = this.$md5("111111s");
console.log('a', a);  //结果为c85dfcf2cf8e79ba8239eff965483c5b
```

在项目中，想要将用户注册的密码由明文转为密文，在传递参数时可以先将密码md5加密后，再传给后端接口，放到数据库中。这样数据库中密码存放的就是密文而不是明文了。

```csharp
// 收集参数  以便发送给后台
let params = {
user: this.addForm.name,
password: md5(this.addForm.pass) //此处将用户注册密码加密，再发给后端
};
```

在用户注册成功后进行登录时，因为`js-md5`加密是不可逆的，除非进行暴力破解，例如枚举，所以不需要将数据库中存储的密码密文再转为明文，而是在传递登录密码时将用户输入的密码进行md5加密处理，再与之对比验证。

<img src="https://s6.jpg.cm/2022/08/01/PQ1j5O.png" style="zoom: 50%;" />

### 2022.7.30新增文件加密和散列值获取功能界面

**更新内容：**

- 新增文件加密功能按钮选项，并与后端接口联通，成功实现数据加密

![](https://s6.jpg.cm/2022/08/01/PQ1aLi.png)

- 计算文件的散列值，并进行逐比特对比，判断文件是否遭到篡改

> 1.先下载

```bash
npm i browser-md5-file -S
```

> 2.在使用的vue页面引入和声明方法

```typescript
import BMF from 'browser-md5-file'
const bmf = new BMF()
```

> 3.使用方法：

```bash
function handle(e) {
  const file = e.target.files[0];
  bmf.md5(
    file,
    (err, md5) => {
      console.log('err:', err);
      console.log('md5 string:', md5); // 97027eb624f85892c69c4bcec8ab0f11
    },
    progress => {
      console.log('progress number:', progress);
    },
  );
}
```

> 4.终止md5计算方法（大文件计算很费时 ）：

```bash
bmf.abort()
```

> 5.代码可以直接复制使用：

```typescript
<template>
  <div>
    <!-- 上传文件 -->
    <!-- multiple  选择多个文件属性 -->
    <input type="file" placeholder-class="input-placeholder" @change="handle">
  </div>
</template>

<script>
// 先下载 npm i browser-md5-file -S
import BMF from 'browser-md5-file'
const bmf = new BMF()
export default {
  data () {
    return {}
  },
  methods: {
    handle (e) {
      // console.log(e)
      const file = e.target.files[0]
      // console.log(file)
      bmf.md5(
        file,
        (err, md5) => {
          console.log('err:', err)
          console.log('md5 string:', md5) // 97027eb624f85892c69c4bcec8ab0f11

          // const formData = new FormData() // 每次失败要重置formdata对象
          // formData.append('file', e.target.files[0])//文件掉接口方式

          // You can abort it before success to md5:中止计算md5--大文件计算很费时
          // bmf.abort()
        },
        progress => {
          console.log('progress number:', progress)
        }
      )
    }
  }
}
</script>

<style></style>
```

![](https://s6.jpg.cm/2022/08/01/PQ1928.png)

### 2022.7.31修复界面显示出错、文件数据获取等并发性错误



