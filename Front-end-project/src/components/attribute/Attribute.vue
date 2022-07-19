<template>
  <div
    class="Attribute"
    v-if="fileInfo.id"
    @mousedown="moveDialog"
    :style="{
      top: attributeTop + 'px',
      left: attributeLeft + 'px',
    }"
  >
    <div class="header">
      {{
        (fileInfo.name + "." + fileInfo.type + " 属性")
          | handleLongString(10, 15)
      }}
      <i class="iconfont icon-close" @click="closeAttribute"></i>
    </div>
    <div class="group">
      <div class="groupItem">
        <span class="left">文件类型:</span>
        <span class="right">{{
          fileInfo.filetype + "(." + fileInfo.type + ")"
        }}</span>
      </div>
    </div>
    <div class="group">
      <div class="groupItem">
        <span class="left">位置:</span>
        <span class="right">{{ fileInfo.fdir }}</span>
      </div>
      <div class="groupItem">
        <span class="left">大小:</span>
        <span class="right">{{
          fileInfo.size == null
            ? "未知大小"
            : (fileInfo.size / 1048576).toFixed(2) + " MB"
        }}</span>
      </div>
    </div>
    <div class="group">
      <div class="groupItem">
        <span class="left">创建时间:</span>
        <span class="right">{{ fileInfo.gmtCreate }}</span>
      </div>
      <div class="groupItem">
        <span class="left">修改时间:</span>
        <span class="right">{{ fileInfo.gmtModified }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { handleLongString } from "plugins/utils.js";

export default {
  name: "Attribute",
  props: {
    // 当前查看属性的文件的id
    fileId: {
      type: String,
      default() {
        return "";
      },
    },
    attributeTop: {
      type: Number,
      default() {
        return 0;
      },
    },
    attributeLeft: {
      type: Number,
      default() {
        return 0;
      },
    },
  },
  data() {
    return {
      fileInfo: {},
    };
  },
  methods: {
    //   请求
    // 根据id查看当前文件的属性
    async getFileAttribute() {
      let res = await this.$request(
        `/educenter/file/getFileInfo/${this.fileId}`
      );
      console.log(res);
      this.fileInfo = res.data.data.file[0];
    },

    // 点击关闭按钮的回调
    closeAttribute() {
      this.fileInfo = {};
      this.$emit("closeAtrribute");
    },

    // 拖动属性框的回调
    moveDialog(e) {
      // console.log(e);
      // console.log([e.target]);
      let dialog;
      if (e.target.className != "Attribute") {
        dialog = e.target.parentNode;
      } else {
        return;
      }
      //算出鼠标相对元素的位置
      let disX = e.clientX - dialog.offsetLeft;
      let disY = e.clientY - dialog.offsetTop;
      let pageWidth = document.body.clientWidth;
      let pageHeight = document.body.clientHeight;
      // console.log(pageWidth, pageHeight);
      document.onmousemove = (e) => {
        // console.log(e);
        //用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
        let left = e.clientX - disX;
        let top = e.clientY - disY;

        top = top < 100 ? 100 : top;
        top = top > pageHeight - 225 ? pageHeight - 225 : top;
        left = left < 245 ? 245 : left;
        left = left > pageWidth - 325 ? pageWidth - 325 : left;
        // console.log(top, left);

        //移动当前元素
        dialog.style.left = left + "px";
        dialog.style.top = top + "px";
      };
      // 鼠标松开时清空事件
      document.onmouseup = () => {
        document.onmousemove = null;
        document.onmouseup = null;
      };
    },
  },
  created() {},
  watch: {
    // fileId() {
    //   this.getFileAttribute();
    // },
  },
  filters: {
    handleLongString,
  },
};
</script>

<style scoped>
.Attribute {
  box-shadow: 0px 0px 10px 1px rgba(0, 0, 0, 0.1);
  border: 1px solid #eee;
  padding-bottom: 10px;
  border-radius: 10px;
  width: 300px;
  font-size: 14px;
  position: fixed;
  background-color: #fff;
  z-index: 100;
}

.header {
  padding: 15px;
  position: relative;
  cursor: move;
  user-select: none;
}

.header i {
  position: absolute;
  right: 10px;
  color: rgb(124, 124, 124);
  cursor: pointer;
}

.group {
  padding: 5px 0;
  margin: 0 15px;
  border-top: 1px solid #ddd;
}

.groupItem {
  padding: 5px 0;
  display: flex;
}

.groupItem .left {
  width: 80px;
}
</style>
