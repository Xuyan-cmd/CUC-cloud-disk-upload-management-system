<template>
  <div class="ImagePlayer" v-if="isImgDialogShow">
    <!-- 查看图片的dialog -->
    <el-dialog
      :title="currentImg.name"
      :visible.sync="isPlayerShow"
      @close="$emit('closeDialog')"
    >
      <div class="dialogImgContainer">
        <img
          :src="currentImg.url"
          alt=""
          class="dialogImg"
          :style="[
            {
              transform: `scale(${currentScale}) rotate(${rotateDeg}deg) translate(${movePosition.x}px,${movePosition.y}px)`,
            },
          ]"
          @mousedown="moveImg"
          @dragstart="(e) => e.preventDefault()"
          draggable="false"
        />
      </div>
    </el-dialog>
    <div class="control">
      <i class="iconfont icon-fangda" @click="changeSize('large')"></i>
      <i class="iconfont icon-suoxiao" @click="changeSize('small')"></i>
      <i class="iconfont icon-youxuan" @click="rotateImg"></i>
      <i
        class="iconfont icon-shoucang"
        v-if="currentImg.collection == 0 || currentImg.collection == null"
        @click="$emit('collectCurrentImg', { flag: true, item: currentImg })"
      ></i>
      <img
        src="~assets/img/collect.png"
        alt=""
        @click="$emit('collectCurrentImg', { flag: false, item: currentImg })"
        v-else-if="currentImg.collection == 1"
      />
      <i
        class="iconfont icon-fenxiang"
        @click="$emit('shareCurrentFile', currentImg)"
      ></i>
    </div>
  </div>
</template>

<script>
export default {
  name: "ImagePlayer",
  props: {
    currentImg: {
      type: Object,
      default() {
        return {
          name: "",
          url: "",
        };
      },
    },
    isImgDialogShow: {
      type: Boolean,
      default() {
        return false;
      },
    },
  },
  data() {
    return {
      isPlayerShow: false,
      // 显示比例
      currentScale: 1,
      // 旋转角度
      rotateDeg: 0,
      // 移动的位置
      movePosition: {
        x: 0,
        y: 0,
      },
    };
  },
  methods: {
    // 放大缩小
    changeSize(size) {
      if (size == "large") {
        this.currentScale += 0.1;
      } else {
        if (this.currentScale > 0.2) {
          this.currentScale -= 0.1;
        }
      }
    },
    // 点击旋转的回调
    rotateImg() {
      this.rotateDeg += 90;
      this.rotateDeg == 360 ? 0 : this.rotateDeg;
    },

    // 拖动img
    moveImg(e) {
      // console.log(e);
      // 鼠标移动事件
      window.onmousemove = (e) => {
        // console.log(e);
        this.movePosition.x +=
          e.movementX / e.view.devicePixelRatio / this.currentScale;
        this.movePosition.y +=
          e.movementY / e.view.devicePixelRatio / this.currentScale;
        // console.log(this.movePosition.x, this.movePosition.y);
      };
      // 鼠标松开时清空事件
      window.onmouseup = () => {
        window.onmousemove = null;
        window.onmouseup = null;
      };
    },

    mousewheelFn(e) {
      console.log(e);
      if (e.wheelDelta > 0 || e.detail < 0) {
        this.changeSize("large");
      } else {
        this.changeSize("small");
      }
    },
  },
  watch: {
    isImgDialogShow(current) {
      let mousewheel =
        navigator.userAgent.indexOf("Firefox") > -1
          ? "DOMMouseScroll"
          : "mousewheel";
      this.isPlayerShow = current;
      if (current == false) {
        // window.onmousewheel = null;
        window.removeEventListener(mousewheel, this.mousewheelFn);
        // 关闭时重置
        this.currentScale = 1;
        this.movePosition = {
          x: 0,
          y: 0,
        };
        this.rotateDeg = 0;
      } else {
        // 开启时监听滚轮事件
        window.addEventListener(mousewheel, this.mousewheelFn);
      }
    },
  },
};
</script>

<style scoped>
* {
  user-select: none;
}

.dialogImg {
  max-width: 95%;
  max-height: 95%;
}

.ImagePlayer /deep/ .el-dialog {
  height: 80vh;
  width: 70vw;
  margin-top: 7vh !important;
  border-radius: 10px;
}

.ImagePlayer /deep/ .el-dialog__header {
  padding: 0;
  width: 100%;
  text-align: center;
  min-height: 45px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ImagePlayer /deep/ .el-dialog__body {
  padding: 0;
  margin: 0 auto 20px;
  width: 100%;
  height: calc(100% - 45px);
}

.ImagePlayer /deep/ .el-dialog__headerbtn {
  top: 15px;
}

.dialogImgContainer {
  width: 100%;
  height: 95%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.dialogImgContainer img {
  height: 95%;
}

.control {
  position: fixed;
  bottom: calc(13% - 50px);
  left: 50%;
  transform: translateX(-50%);
  z-index: 3000;
  background-color: #fff;
  padding: 10px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  height: 40px;
  box-sizing: border-box;
}

.control i,
.control img {
  margin: 0 10px;
  cursor: pointer;
}

.control img {
  width: 16px;
  height: 16px;
  cursor: pointer;
}
</style>
