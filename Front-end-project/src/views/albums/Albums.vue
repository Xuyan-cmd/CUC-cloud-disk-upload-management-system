<template>
  <div class="Album">
    <album-nav-bar
      :albumType="albumType"
      :albumListLength="showList.length"
      :isMultBtnsShow="isMultBtnsShow"
      @pushUploadData="pushUploadData"
      @multDownload="$refs.albumList.downloadCurrentFile('mult')"
      @multDelete="$refs.albumList.deleteCurrentFile('mult')"
      @multCollect="(flag) => $refs.albumList.collectCurrentFile(flag, 'mult')"
      @changeAlbumType="(type) => (albumType = type)"
      @selectAll="$refs.albumList.selectAll()"
    ></album-nav-bar>
    <album-list
      :albumList="showList"
      @getUserAllFiles="getUserAllFiles"
      @showMultButtons="
        (flag) => {
          if (isMultBtnsShow != flag) {
            isMultBtnsShow = flag;
          }
        }
      "
      ref="albumList"
    ></album-list>
  </div>
</template>

<script>
import AlbumList from "components/albumList/AlbumList.vue";
import AlbumNavBar from "components/albumNavBar/AlbumNavBar.vue";
export default {
  components: { AlbumNavBar, AlbumList },
  name: "Album",
  data() {
    return {
      allAlbumFiles: [],
      videoFiles: [],
      imageFiles: [],
      // 相册展示类型
      albumType: "all",
      // 是否显示多选按钮
      isMultBtnsShow: false,
    };
  },
  methods: {
    // 请求用户所有的文件
    async getUserAllFiles() {
      let res = await this.$request(
        "/educenter/file/getAllFileInfo/" + this.$store.state.userInfo.id
      );
      // console.log(res);
      // 对文件进行分类
      let fileList = res.data.data.fileList;
      let aafiles = [];
      let vfiles = [];
      let ifiles = [];
      fileList.forEach((item, index) => {
        if (item.filetype == "video") {
          aafiles.push(item);
          vfiles.push(item);
        } else if (item.filetype == "image") {
          aafiles.push(item);
          ifiles.push(item);
        }
      });
      // 最后再统一赋值
      this.allAlbumFiles = aafiles.reverse();
      this.videoFiles = vfiles.reverse();
      this.imageFiles = ifiles.reverse();
      // console.log(this.allAlbumFiles, this.videoFiles, this.imageFiles);
      this.getVideoUrl(this.allAlbumFiles);
    },

    // 请求视频的url
    getVideoUrl(list) {
      // 先找到所有的video文件
      list.forEach(async (item, index, arr) => {
        if (item.filetype == "video") {
          // 请求地址
          let res = await this.$request(
            "/eduoss/fileoss/getPlayAuth?isList=" + item.videoId,
            "",
            "post"
          );
          // console.log("执行了这里");
          // 因为这里是浅拷贝，所以对参数数组赋值，也会改变原本数组的值
          arr[index].url = res.data.data.urlList[0].url;
        }
      });
    },

    // 将上传成功的对象push到当前listData中
    async pushUploadData(item) {
      if (item.filetype == "video" || item.filetype == "image") {
        if (item.filetype == "video") {
          item.url = await this.getVideoUrl([item]);
          this.videoFiles.unshift(item);
        } else {
          this.imageFiles.unshift(item);
        }
        // this.listData.push(item);
        this.allAlbumFiles.unshift(item);
      }
    },
  },
  async created() {
    await this.getUserAllFiles();
  },
  computed: {
    showList() {
      if (this.albumType == "all") {
        return this.allAlbumFiles;
      } else if (this.albumType == "image") {
        return this.imageFiles;
      } else if (this.albumType == "video") {
        return this.videoFiles;
      }
    },
  },
};
</script>

<style scoped>
.videoItem {
  width: 100px;
}

.videoItem video {
  height: 100px;
  width: 100px;
}
</style>
