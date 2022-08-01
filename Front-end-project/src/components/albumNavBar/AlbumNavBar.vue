<template>
  <div class="albumNavBarContianer">
    <div class="navbarPlaceholder"></div>
    <div class="AlbumNavBar">
      <div class="top">
        <div class="left">我的相册</div>
        <div class="right">
          <div class="selectAll" @click="$emit('selectAll', isSelectAll)">
            <i class="iconfont icon-paixu1"></i>
          </div>
          <el-upload
            multiple
            :action="`/api/eduoss/fileoss/upload/${$store.state.userInfo.id}?catalogue=/root`"
            class="uploadButton"
            :show-file-list="false"
            :on-success="upload"
            :on-error="onError"
            :on-progress="onProgress"
            :before-upload="beforeUpload"
          >
            <div class="add"><span>+</span></div>
          </el-upload>
        </div>
      </div>
      <div class="bottom">
        <div class="left">共{{ albumListLength }}项</div>
        <div class="right">
          <!-- 多选操作按钮 -->
          <div class="multButtons" v-if="isMultBtnsShow">
            <div class="tips">多选操作按钮</div>
            <div class="multButtonsContainer">
              <div @click="$emit('multDownload')">
                <i class="iconfont icon-bottom"></i> 下载
              </div>
              <div @click="$emit('multDelete')">
                <i class="iconfont icon-ashbin"></i> 删除
              </div>
              <div
                @click="$emit('multCollect', true)"
                v-if="!$store.state.isAllFileCollect"
              >
                <i class="iconfont icon-favorite"></i> 收藏
              </div>
              <div @click="$emit('multCollect', false)" v-else>
                <i class="iconfont icon-favorite"></i> 取消收藏
              </div>
            </div>
          </div>
          <div class="buttons">
            <el-popover placement="bottom" width="90" trigger="hover">
              <div class="buttonItem" slot="reference">
                <i class="iconfont icon-paixu1"></i>&nbsp;{{ albumTypeName }}
              </div>
              <div
                class="sortTypeItem"
                @click="
                  albumType != 'all' ? $emit('changeAlbumType', 'all') : ''
                "
              >
                <i class="iconfont icon-select" v-show="albumType == 'all'"></i>
                全部
              </div>
              <div
                class="sortTypeItem"
                @click="
                  albumType != 'image' ? $emit('changeAlbumType', 'image') : ''
                "
              >
                <i
                  class="iconfont icon-select"
                  v-show="albumType == 'image'"
                ></i>
                图片
              </div>
              <div
                class="sortTypeItem"
                @click="
                  albumType != 'video' ? $emit('changeAlbumType', 'video') : ''
                "
              >
                <i
                  class="iconfont icon-select"
                  v-show="albumType == 'video'"
                ></i>
                视频
              </div>
            </el-popover>
            <el-tooltip effect="dark" content="显示比例" placement="bottom">
              <div class="buttonItem" @click="changeImgScale">
                <i class="iconfont icon-tupian"></i>
              </div>
            </el-tooltip>
          </div>
        </div>
      </div>

      <!-- 进度条提示框 -->
      <!-- <progress-dialog
        :isUploadProgressShow="isUploadProgressShow"
        :uploadProgress="uploadProgress"
      ></progress-dialog> -->
    </div>
  </div>
</template>

<script>
import ProgressDialog from "../progressDialog/ProgressDialog.vue";

export default {
  name: "AlbumNavBar",
  components: {
    ProgressDialog,
  },
  props: {
    albumType: {
      type: String,
      default() {
        return "all";
      },
    },
    // 相册总长度
    albumListLength: {
      type: Number,
      default() {
        return 0;
      },
    },
    // 是否显示多选按钮
    isMultBtnsShow: {
      type: Boolean,
      default() {
        return false;
      },
    },
  },
  data() {
    return {
      // 图片显示比例
      imgScale: true,
      // 是否显示上传进度条
      // isUploadProgressShow: false,
      // 当前上传进度
      // uploadProgress: 0,
      // 当前是否全选
      isSelectAll: false,
    };
  },
  methods: {
    // 改变图片显示比例
    changeImgScale() {
      this.imgScale = !this.imgScale;
      this.$store.commit("updateImgScale", this.imgScale);
    },

    // 上传成功的钩子
    async upload(response, file) {
      if (!response.data.file) {
        let arr = this.$store.state.uploadProgressList;
        let idx = arr.findIndex((item) => item.name == file.name);
        arr.splice(idx, 1);
        this.$store.commit("updateUploadProgressList", arr);
        return;
      }
      let data = {
        memId: this.$store.state.userInfo.id,
        url: response.data.file.url,
        name: response.data.file.name,
        type: response.data.file.type,
        videoId: response.data.file.videoId,
        filetype: response.data.file.filetype,
        fdir: response.data.file.fdir,
        size: response.data.file.size,
      };
      // 调用此接口以通知后端将上传的文件存入数据库
      let res = await this.$request(
        "/educenter/file/addFile",
        data,
        "post",
        "params"
      );
      // this.isUploadProgressShow = false;
      let arr = this.$store.state.uploadProgressList;
      let idx = arr.findIndex((item) => item.name == file.name);
      arr.splice(idx, 1);
      this.$store.commit("updateUploadProgressList", arr);

      if (res.data.success) {
        this.$message.success("文件上传成功!");
        this.$emit("pushUploadData", res.data.data.file);

        // 更新用户存储空间
        let userInfo = this.$store.state.userInfo;
        userInfo.neicun += response.data.file.size;
        this.$store.commit("updateUserInfo", userInfo);
      } else {
        this.$message.error("文件上传失败，请稍后重试!");
      }
    },

    // 上传失败的钩子
    onError(err, file) {
      //   console.log(err);
      // this.isUploadProgressShow = false;
      let arr = this.$store.state.uploadProgressList;
      let idx = arr.findIndex((item) => item.name == file.name);
      arr.splice(idx, 1);
      this.$store.commit("updateUploadProgressList", arr);
      this.$message.error("文件上传失败，请稍后重试!");
    },

    // 文件上传时的钩子
    onProgress(e, file) {
      //   console.log(e);
      // this.isUploadProgressShow = true;
      // this.uploadProgress = Math.round(file.percentage);
      // console.log(this.uploadProgress);
      let arr = this.$store.state.uploadProgressList;
      arr.findIndex((item, index, arr) => {
        if (item.name == file.name) {
          arr[index].progress = Math.round(file.percentage);
          return 1;
        }
      });
      this.$store.commit("updateUploadProgressList", arr);
    },

    // 文件上传之前的钩子
    beforeUpload(file) {
      // console.log(file);
      let arr = this.$store.state.uploadProgressList;
      arr.push({ name: file.name, progress: 0 });
      this.$store.commit("updateUploadProgressList", arr);
    },
  },
  computed: {
    albumTypeName() {
      if (this.albumType == "all") {
        return "全部";
      } else if (this.albumType == "video") {
        return "视频";
      } else if (this.albumType == "image") {
        return "图片";
      }
    },
  },
};
</script>

<style scoped>
* {
  user-select: none;
}

.navbarPlaceholder {
  height: 100px;
}

.AlbumNavBar {
  position: absolute;
  top: 0;
  left: 240px;
  min-width: 980px;
  width: calc(100vw - 240px);
  color: rgb(61, 61, 61);
  background-color: white;
  padding: 20px 20px 10px;
  box-sizing: border-box;
  height: 100px;
  z-index: 100;
}

.top,
.bottom {
  display: flex;
  justify-content: space-between;
  padding: 5px;
}

.top .left {
  font-size: 18px;
}

.top .right {
  display: flex;
}

.top .right div {
  background-color: #0ea1d6;
  border-radius: 50%;
  color: white;
  width: 30px;
  height: 30px;
  line-height: 27px;
  text-align: center;
  font-size: 30px;
  font-weight: 200;
  cursor: pointer;
}

.selectAll {
  line-height: 21px !important;
  margin-right: 13px;
}

.bottom .left {
  font-size: 14px;
  color: rgb(94, 94, 94);
}

.buttons {
  display: flex;
}

.buttonItem {
  margin-left: 20px;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.bottom .right {
  font-size: 14px;
  display: flex;
  margin-right: 5px;
  height: 30px;
}

.icon-tupian {
  font-weight: bold;
}

.multButtons {
  display: flex;
  align-items: center;
  font-size: 14px;
  transform: scale(0.9);
}

.tips {
  color: rgb(177, 177, 177);
}

.multButtonsContainer {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 8px;
  color: rgb(97, 97, 97);
  margin-left: 10px;
  overflow: hidden;
}

.multButtonsContainer div {
  padding: 8px 15px;
  border-right: 1px solid #ccc;
  cursor: pointer;
}

.multButtonsContainer div:nth-last-child(1) {
  border: none;
}

.multButtonsContainer div:hover {
  background-color: #17c4db;
  color: white;
}

.uploadProgress {
  width: 230px;
}

.sortTypeItem {
  font-size: 13px;
  padding: 10px 0 10px 40px;
  position: relative;
  cursor: pointer;
  color: #15c4df;
}

.sortTypeItem i {
  position: absolute;
  left: 13px;
  color: #0fb9d3;
}

.sortTypeItem:hover {
  background-color: #efeff5;
}

.buttons span:nth-child(1) {
  display: flex;
  align-items: center;
}
</style>
