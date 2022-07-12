<template>
  <div class="Index" @drop.prevent="dropFile" @dragover.prevent>
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="240px">
        <div class="asideHeader">
          <div class="logo"><img src="~assets/img/logo.png" alt="" /></div>
          <div class="name">中传放心传</div>
        </div>
        <aside-bar :asideBarData="asideBarData"></aside-bar>
        <user-info-card class="userInfoCard"></user-info-card>
      </el-aside>
      <el-main>
        <router-view :key="$route.path" ref="routerView"></router-view>
      </el-main>
    </el-container>
    <!-- 视频播放组件 -->
    <video-player></video-player>
    <!-- 音频播放组件 -->
    <music-player></music-player>
    <!-- 上传进度条提示框 -->
    <progress-dialog></progress-dialog>
    <!-- 用于下载的a标签 -->
    <a
      :href="downloadFileInfo.url"
      :download="downloadFileInfo.name"
      target="_blank"
      id="downloadCurrentFile"
    ></a>
  </div>
</template>

<script>
import axios from "axios";

import AsideBar from "components/asideBar/AsideBar.vue";
import UserInfoCard from "components/userInfoCard/UserInfoCard.vue";
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
  },
  name: "Index",
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
  },
  methods: {
    // 请求
    // 上传至数据库的请求
    async updateToSQL(data, fileName) {
      // 调用此接口以通知后端将上传的文件存入数据库
      let res = await this.$request(
        "/educenter/file/addFile",
        data,
        "post",
        "params"
      );
      console.log(res);
      this.isUploadProgressShow = false;
      if (res.data.success) {
        this.$message.success("文件上传成功!");
        // this.$emit("getListData");
        // this.$emit("pushUploadData", res.data.data.file);
        // console.log(this.$refs.routerView);
        if (this.$refs.routerView.$el._prevClass != "Collectes")
          this.$refs.routerView.pushUploadData(res.data.data.file);

        // 更新用户存储空间
        let userInfo = this.$store.state.userInfo;
        userInfo.neicun += res.data.data.file.size;
        this.$store.commit("updateUserInfo", userInfo);
        // this.isUploadProgressShow = false;
        // 更新进度数据
        let arr = this.$store.state.uploadProgressList;
        let idx = arr.findIndex((i) => {
          return i.name == fileName;
        });
        arr.splice(idx, 1);
        this.$store.commit("updateUploadProgressList", arr);
      } else {
        this.$message.error("文件上传失败，请稍后重试!");
      }
    },

    // 点击下载文件的回调
    downloadCurrentFile(name, url) {
      axios.get(url, { responseType: "blob" }).then((res) => {
        let blob = res.data;
        let url = URL.createObjectURL(blob);
        // download(url);
        console.log(url);
        let a = document.querySelector("#downloadCurrentFile");
        this.downloadFileInfo.name = name;
        this.downloadFileInfo.url = url;
        // console.log(a);
        //   console.log(this.downloadFileInfo.url.split("com")[1]);
        this.$nextTick(() => {
          a.click();
          // 用完释放URL对象
          URL.revokeObjectURL(url);
        });
      });
    },

    // 放下拖拽文件的回调
    dropFile(e) {
      // console.log(e);
      // console.log(e.dataTransfer.files[0]);
      let originLength = this.$store.state.uploadProgressList.length;

      e.dataTransfer.files.forEach((item, index) => {
        let param = new FormData();
        param.append("file", item);

        console.log(item);

        let arr = this.$store.state.uploadProgressList;

        // uploadProgressList push一个对象
        arr.push({ name: item.name, progress: 0 });

        let config = {
          header: { "Content-Type": "multipart/form-data" },
          onUploadProgress: (progressEvent) => {
            // this.isUploadProgressShow = true;
            // console.log(arr.length);
            arr[index + originLength].progress =
              ((progressEvent.loaded / progressEvent.total) * 100) | 0;
            this.$store.commit("updateUploadProgressList", arr);
            // console.log(complete);
            // console.log(progressEvent);
          },
        };
        axios
          .post(
            `/api/eduoss/fileoss/upload/${this.$store.state.userInfo.id}?catalogue=${this.$store.state.currentFolder}`,
            param,
            config
          )
          .then((response) => {
            console.log(response);
            if (!response.data.success) {
              this.$message.error(
                `${response.data.data.file.name}上传失败,请稍后重试!`
              );
              // this.isUploadProgressShow = false;
              // 不能直接用索引删除 因为如果之前已经有前面的索引被删除了，会导致后面的索引跟着发生变化 但又没有唯一标识 所以根据文件名进行删除（重名情况下默认删除第一个）
              let idx = arr.findIndex((i) => {
                return i.name == item.name;
              });
              arr.splice(idx, 1);
              this.$store.commit("updateUploadProgressList", arr);
              return;
            }
            let data = {
              memId: this.$store.state.userInfo.id,
              url: response.data.data.file.url,
              name: response.data.data.file.name,
              type: response.data.data.file.type,
              videoId: response.data.data.file.videoId,
              filetype: response.data.data.file.filetype,
              fdir: response.data.data.file.fdir,
              size: response.data.data.file.size,
            };
            console.log(data);
            this.updateToSQL(data, item.name);
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("上传失败!");
          });
      });
    },
  },
  watch: {
    "$store.state.currentDownloadFileInfo"(current) {
      this.downloadCurrentFile(current.name, current.url);
    },
    "$store.state.currentFolder"(current) {
      this.asideBarData[0].params.path = current;
    },
    "$route.params.path"(current) {
      // console.log(this.$route.name==files);
      if (this.$route.name == "files") {
        this.$store.commit("updateCurrentFolder", current);
        console.log(207);
      }
      // if (this.isCreated) {
      //   this.getListData();
      //   // console.log("199");
      // }
    },
  },
};
</script>

<style scoped>
.el-aside {
  background-color: #f5f5f6;
  height: 100vh;
}

.el-main {
  width: calc(100vw - 240px);
}

.asideHeader {
  display: flex;
  align-items: center;
  margin: 20px;
  position: relative;
}

.asideHeader .logo {
  width: 50px;
}

.asideHeader .logo img {
  width: 100%;
}

.asideHeader .name {
  color: #25262b;
  font-size: 20px;
  letter-spacing: 4px;
  font-weight: bold;
  margin-left: 7px;
}

.userInfoCard {
  position: absolute;
  bottom: 0;
}
</style>
