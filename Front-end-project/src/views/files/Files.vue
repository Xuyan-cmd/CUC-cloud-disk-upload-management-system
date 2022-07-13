<template>
  <div class="Files">
    <function-bar
      @pushUploadData="pushUploadData"
      @multDelete="$refs.iconTypeList.deleteCurrentFile('mult')"
      @multDownload="$refs.iconTypeList.downloadCurrentFile('mult')"
      @multMove="$refs.iconTypeList.moveFile('mult')"
      @changeSortType="(type) => (sortType = type)"
      @changShowType="(type) => (showType = type)"
      @goSearch="goSearch"
      @multCollect="
        (flag) => {
          $refs.iconTypeList.collectCurrentFile(flag, 'mult');
        }
      "
    ></function-bar>
    <icon-type-list
      :listData="listData"
      :folderList="folderList"
      :sortType="sortType"
      :showType="showType"
      :searchFolder="searchFolder"
      @getListData="getListData"
      @getFolderList="getFolderList"
      ref="iconTypeList"
    ></icon-type-list>
  </div>
</template>

<script>
import FunctionBar from "components/functionBar/FunctionBar.vue";
import IconTypeList from "components/iconTypeList/IconTypeList.vue";
export default {
  name: "Files",
  data() {
    return {
      listData: [],
      folderList: {},
      // 是否已经被创建
      // isCreated: false,
      // 排序方式
      sortType: "time",
      // 展示方式 icon table
      showType: "icon",
      // 搜索的文件夹
      searchFolder: [],
    };
  },
  components: {
    FunctionBar,
    IconTypeList,
  },
  methods: {
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
      console.log(res, 63);
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
        console.log(81);
        let res = await this.$request("/educenter/dir/getUserDir", {
          id: this.$store.state.userInfo.id,
        });
        if (res.data.data.dir == null) {
          this.$router.replace("/login");
          return;
        }
        // console.log(res);
        // console.log(JSON.parse(res.data.data.dir.memDir));

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
          // console.log("调用了这里");
          arr[index].url = await this.getVideoUrl(item.videoId);
        }
      });
      // console.log(this.videoList);
    },

    // 根据传入videoId获取url
    async getVideoUrl(videoId) {
      let res = await this.$request(
        "/eduoss/fileoss/getPlayAuth?isList=" + videoId,
        "",
        "post"
      );
      // console.log(res);
      return res.data.data.urlList[0].url;
    },

    // 将上传成功的对象push到当前listData中
    async pushUploadData(item) {
      if (item.filetype == "video") {
        item.url = await this.getVideoUrl(item.videoId);
      }
      this.listData.push(item);
    },

    // 搜索
    goSearch(content) {
      // 如果路径中已经有搜索了 则替换之前的路径
      let path;
      if (this.$store.state.currentFolder.search("search") != -1) {
        path = this.$store.state.currentFolder.split("/search")[0];
      } else {
        path = this.$store.state.currentFolder;
        console.log(131);
      }
      console.log(path);
      this.$router.push({
        name: "files",
        params: {
          path: path + `/search?${content}`,
        },
      });
    },
  },

  // 注意：在子文件夹中刷新会导致子文件夹的key变为vuex的初始currentFolder 也就是空
  async created() {
    // 如果vuex中的当前文件夹为空 就在这里更新一下  watch监听不到第一次加入files时的路由变化
    if (this.$store.state.currentFolder == "") {
      this.$store.commit("updateCurrentFolder", this.$route.params.path);
    }
    console.log("createFiles");
    await this.getFolderList();
    this.$route.params.path.search("search"); //找不到返回-1
    if (this.$route.params.path.search("search") == -1) {
      await this.getListData();
      // this.isCreated = true;
    } else {
      let data =
        this.$route.params.path.split("/")[
          this.$route.params.path.split("/").length - 1
        ];
      let content = data.split("?")[1];
      let res = await this.$request(
        `/educenter/file/findFile/${this.$store.state.userInfo.id}/${content}`,
        "",
        "post"
      );
      console.log(res);
      if (!res.data.success) return;
      if (this.$store.state.sortType == "size") {
        res.data.data.fileList.sort((a, b) => {
          return a.size - b.size;
        });
      }
      this.listData = res.data.data.fileList;
      this.searchFolder = res.data.data.list;
      await this.getVideoList(this.listData);
    }
  },
  watch: {},
};
</script>

<style scoped>
.Files {
  width: 100%;
}
</style>
