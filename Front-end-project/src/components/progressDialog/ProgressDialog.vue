<template>
  <div
    class="progressContainer"
    :class="isUploadProgressShow ? 'showProgressContainer' : ''"
  >
    <div class="title">上传进度:</div>
    <div
      v-for="(item, index) in uploadProgressList"
      :key="index"
      class="progressItem"
    >
      <span>{{ item.name }}</span>
      <!-- 上传进度条 -->
      <el-progress
        :percentage="item.progress"
        color="#696bcc"
        class="uploadProgress"
        v-if="isUploadProgressShow"
      ></el-progress>
    </div>
    <div class="tips">tips: 上传过程中,请不要离开该页面哦!</div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    // // 是否显示进度框
    // isUploadProgressShow: {
    //   type: Boolean,
    //   default() {
    //     return false;
    //   },
    // },
    // // 当前上传进度数据数组
    // uploadProgressList: {
    //   type: Array,
    //   default() {
    //     return [];
    //   },
    // },
  },
  data() {
    return {
      // 当前上传进度数据数组
      uploadProgressList: [],
      // 是否显示进度框
      isUploadProgressShow: false,
    };
  },
  methods: {},
  watch: {
    // "$store.state.isUploadProgressShow"(current, last) {
    //   if (current != last) this.isUploadProgressShow = current;
    // },
    "$store.state.uploadProgressList"(current) {
      this.uploadProgressList = current;
      if (current.length == 0) {
        this.isUploadProgressShow = false;
      } else {
        this.isUploadProgressShow = true;
      }
    },
  },
};
</script>

<style scoped>
.progressContainer {
  position: fixed;
  width: 330px;
  padding: 10px 5px 10px 20px;
  right: -330px;
  bottom: 10vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  transition: all 0.5s ease;
  border-radius: 5px;
  border: 1px solid #eee;
  box-shadow: 0px 0px 10px 1px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  background-color: #fff;
}

.progressContainer .progressItem {
  display: flex;
  align-items: center;
  margin: 0 0 10px 5px;
  color: rgb(82, 82, 82);
}

.progressContainer div span {
  font-size: 13px;
  width: 110px;
  overflow: hidden;
  margin-right: 5px;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.title {
  font-size: 14px;
  margin: 0 0 10px -5px;
}

.progressContainer .el-progress {
  flex: 1;
}

.progressContainer .tips {
  color: rgb(177, 177, 177);
  font-size: 12px;
}

.showProgressContainer {
  right: 30px;
}
</style>
