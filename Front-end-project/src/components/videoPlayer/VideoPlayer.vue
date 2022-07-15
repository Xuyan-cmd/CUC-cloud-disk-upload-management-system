<template>
  <div class="VideoPlayer" :class="isVideoPlayerShow ? 'showVideoPlayer' : ''">
    <div class="close">
      <i class="iconfont icon-close" @click="closeVideoPlayer"></i>
    </div>
    <div class="videoName">{{ videoInfo.name }}</div>
    <div class="videoContainer">
      <video
        :src="videoUrl"
        controls
        class="currentVideoPlayer"
        @play="onPlay"
      ></video>
    </div>
  </div>
</template>

<script>
// 定时器名称
let timer;

export default {
  name: "VideoPlayer",
  data() {
    return {
      isVideoPlayerShow: false,
      videoInfo: {
        videoId: "",
        name: "",
      },
      videoUrl: "",
    };
  },
  props: {},
  methods: {
    // 请求
    // 根据vid请求对应的url
    async getVideoUrl() {
      let res = await this.$request(
        "/eduoss/fileoss/getPlayAuth?isList=" + this.videoInfo.videoId,
        "",
        "post"
      );
      console.log(res);
      this.videoUrl = res.data.data.urlList[0].url;
    },

    // 事件
    // 点击关闭视频播放器的回调
    closeVideoPlayer() {
      // 先暂停视频
      document.querySelector(".currentVideoPlayer").pause();

      this.isVideoPlayerShow = false;
      this.$store.commit("updateIsVideoPlayerShow", false);
      //   半秒后清空数据
      clearTimeout(timer);
      timer = setTimeout(() => {
        this.videoInfo = {
          url: "",
          name: "",
        };
      }, 500);
    },

    // 视频开始播放的回调
    onPlay() {
      if (this.$store.state.musicPlayState) {
        this.$store.commit("updateMusicPlayState", false);
      }
    },
  },
  watch: {
    "$store.state.isVideoPlayerShow"(current) {
      this.isVideoPlayerShow = current;
      if (current == true) {
        this.videoInfo = this.$store.state.currentVideoInfo;
        // 请求视频的url
        this.getVideoUrl();
      }
    },
  },
};
</script>

<style scoped>
.VideoPlayer {
  position: relative;
  background-color: #fff;
  position: absolute;
  width: 100%;
  height: 100vh;
  bottom: -100vh;
  transition: bottom 0.5s;
  /* transform: translateY(100vh); */
  z-index: 100;
}

.videoContainer {
  display: flex;
  height: 100%;
  justify-content: center;
  margin-top: 10vh;
}

.showVideoPlayer {
  /* transform: translateY(0); */
  bottom: 0;
}

video {
  max-height: 85vh;
  max-width: 80vw;
  outline: none;
}

.videoName {
  position: absolute;
  top: 5vh;
  left: 50%;
  font-size: 18px;
  transform: translate(-50%, -50%);
  width: 200px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #25262b;
}

i {
  cursor: pointer;
}

.icon-close {
  position: absolute;
  font-size: 30px;
  top: 20px;
  left: 30px;
  color: #25262b;
}
</style>
