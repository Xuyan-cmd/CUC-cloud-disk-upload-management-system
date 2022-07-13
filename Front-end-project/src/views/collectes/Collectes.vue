<template>
  <div class="Collectes">
    <function-bar
      :barType="'collect'"
      @getListData="getListData"
      @multDelete="$refs.iconTypeList.deleteCurrentFile('mult')"
      @multDownload="$refs.iconTypeList.downloadCurrentFile('mult')"
      @multCollect="$refs.iconTypeList.collectCurrentFile(false, 'mult')"
    ></function-bar>
    <icon-type-list
      :listData="listData"
      :rightClickMenuType="'collect'"
      @getListData="getListData"
      ref="iconTypeList"
    ></icon-type-list>
  </div>
</template>

<script>
import FunctionBar from "components/functionBar/FunctionBar.vue";
import IconTypeList from "components/iconTypeList/IconTypeList.vue";

export default {
  name: "Collectes",
  components: {
    FunctionBar,
    IconTypeList,
  },
  data() {
    return {
      listData: [],
    };
  },
  methods: {
    // 请求文件列表
    async getListData() {
      this.listData = [];
      let res = await this.$request(
        "/educenter/file/getAllFileInfo/" + this.$store.state.userInfo.id
      );
      // console.log(res);
      // foreach是异步的
      res.data.data.fileList.forEach((item) => {
        if (item.collection == 1) {
          this.listData.push(item);
        }
      });
      await this.getVideoList(this.listData);
    },

    // 获取当前文件中的所有video文件
    getVideoList(listData) {
      listData.forEach(async (item, index, arr) => {
        if (item.filetype === "video") {
          console.log("调用了这里");
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
      console.log(res);
      return res.data.data.urlList[0].url;
    },
  },
  created() {
    this.getListData();
  },
};
</script>

<style scoped>
</style>
