<template>
  <!-- 使用注意：滚动的盒子必须有个固定的高度，且设有 overflow-y:scroll; 否则会出现scrollTop=0的情况 -->
  <div
    class="GoTop"
    :class="isGoTopShow ? 'showBtn' : isFirstLoad ? '' : 'hideBtn'"
    @click="goTop"
  >
    <i class="iconfont icon-fanhuidingbu"></i>
  </div>
</template>

<script>
let obj;

export default {
  name: "GoTop",
  props: {
    // 滚动对象的选择器
    scrollObj: {
      type: String,
      default() {
        return "";
      },
    },
  },
  data() {
    return {
      // 是否显示改组件
      isGoTopShow: false,
      // 是否是第一次加载  第一次加载组件不需要添加hideBtn类
      isFirstLoad: true,
    };
  },
  methods: {
    goTop() {
      this.el.scrollTo({
        behavior: "smooth",
        top: 0,
      });
    },
  },
  mounted() {
    if (this.scrollObj == "") {
      obj = document;
      this.el = document.documentElement;
    } else {
      obj = document.querySelector(this.scrollObj);
      this.el = obj;
    }

    obj.onscroll = (e) => {
      // console.log(e);
      // console.log(obj.scrollTop);
      if (this.el.scrollTop >= 500 && this.isGoTopShow == false) {
        this.isGoTopShow = true;
        this.isFirstLoad ? (this.isFirstLoad = false) : "";
      } else if (this.el.scrollTop < 500 && this.isGoTopShow) {
        this.isGoTopShow = false;
      }
    };
  },
};
</script>

<style scoped>
.GoTop {
  position: fixed;
  background-color: #fafafa;
  border-radius: 50%;
  height: 40px;
  width: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0px 0px 10px 1px rgba(0, 0, 0, 0.1);
  z-index: 10000;
  cursor: pointer;
  bottom: -50px;
  right: 50px;
}

.showBtn {
  animation: showBtn 0.7s ease;
  animation-fill-mode: forwards;
}

.hideBtn {
  animation: hideBtn 0.3s ease-in;
  animation-fill-mode: forwards;
}

.GoTop i {
  font-size: 18px;
  color: rgb(126, 126, 126);
}

.GoTop:hover {
  background-color: #f5f5f5;
}

@keyframes showBtn {
  0% {
    bottom: -40px;
  }
  50% {
    bottom: 80px;
  }
  100% {
    bottom: 55px;
  }
}

@keyframes hideBtn {
  from {
    bottom: 55px;
  }
  to {
    bottom: -50px;
  }
}
</style>
