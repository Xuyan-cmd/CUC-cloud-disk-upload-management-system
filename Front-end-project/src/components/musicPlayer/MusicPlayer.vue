<template>
  <div
    class="MusicPlayer"
    :class="$store.state.isMusicPlayerShow ? 'showPlayer' : ''"
  >
    <audio
      :src="audioUrl"
      class="currentMusicPlayer"
      autoplay
      @timeupdate="timeupdate"
      @play="onPlay"
      @pause="onPause"
      @ended="endPlay"
      ref="currentMusicPlayer"
    ></audio>
    <div class="top">
      <div class="cover">
        <img src="~assets/img/cover.jpg" alt="" />
        <div class="iconContainer" @click="changePlayState">
          <i class="iconfont icon-play_nor" v-if="!playState"></i>
          <i class="iconfont icon-pause_nor" v-else></i>
        </div>
      </div>
      <div class="musicInfo">
        <div class="musicName">{{ currentMusicInfo.name }}</div>
        <div class="singer">
          {{
            currentMusicInfo.singer == ""
              ? "未知艺术家"
              : currentMusicInfo.singer
          }}
        </div>
      </div>
      <i class="iconfont icon-close" @click="closeMusicPlayer"></i>
    </div>
    <div class="progressBar">
      <div
        class="time"
        v-if="$refs.currentMusicPlayer && $refs.currentMusicPlayer.duration"
      >
        {{ currentTime | handleTime }} /
        {{
          $refs.currentMusicPlayer &&
          $refs.currentMusicPlayer.duration | handleTime
        }}
      </div>
      <el-slider
        v-model="progress"
        :show-tooltip="false"
        @change="changeProgress"
      ></el-slider>
    </div>
  </div>
</template>

<script>
import { handleTime } from "plugins/utils.js";

export default {
  name: "MusicPlayer",
  data() {
    return {
      // 进度条长度
      progress: 0,
      // 播放状态
      playState: false,
      // 音乐信息
      currentMusicInfo: {
        url: "",
        name: "",
        singer: "",
        // 当前音乐播放时长
        duration: 0,
      },
      //   当前播放时间
      currentTime: 0,
      // 音频url
      audioUrl: "",
    };
  },
  methods: {
    changePlayState() {
      this.playState = !this.playState;
      let currentMusicPlayer = this.$refs.currentMusicPlayer;
      if (this.playState) {
        currentMusicPlayer.play();
      } else {
        currentMusicPlayer.pause();
      }
    },

    // 监听播放进度
    timeupdate() {
      let time = this.$refs.currentMusicPlayer.currentTime;
      time = Math.floor(time);
      //   该事件一秒调用4次，这里稍微做下节流
      if (this.currentTime != time) {
        this.currentTime = time;
        // console.log(time);
        // 计算进度条的长度
        this.progress = Math.floor(
          (time / this.$refs.currentMusicPlayer.duration) * 100
        );
        // console.log(this.progress);
      }
    },

    // 拖动进度条的回调
    changeProgress(e) {
      //   console.log(e);
      let time = Math.floor((e / 100) * this.$refs.currentMusicPlayer.duration);
      this.$refs.currentMusicPlayer.currentTime = time;
      this.currentTime = time;
    },

    // 播放结束的回调
    endPlay() {
      this.playState = false;
      this.$store.commit("updateMusicPlayState", false);
    },

    // 开始播放的回调
    onPlay() {
      this.playState = true;
      this.$store.commit("updateMusicPlayState", true);
    },

    // 暂停播放的回调
    onPause() {
      this.playState = false;
      this.$store.commit("updateMusicPlayState", false);
    },

    // 点击关闭音频播放器的回调
    closeMusicPlayer() {
      // 重置所有数据
      this.currentMusicInfo = {
        url: "",
        name: "",
        singer: "",
        duration: 0,
      };
      this.progress = 0;
      this.playState = false;
      this.$store.commit("updateIsMusicPlayerShow", false);
      this.$store.commit("updateCurrentMusicInfo", this.currentMusicInfo);
      this.audioUrl = "";
    },

    // 获取音频的url
    async getAudioUrl() {
      let res = await this.$request(
        `/eduoss/fileoss/getPlayAuth?isList=${this.currentMusicInfo.videoId}`,
        "",
        "post"
      );
      console.log(res);
      this.audioUrl = res.data.data.urlList[0].url;
    },
  },
  watch: {
    // 监听vuex中的当前音频播放信息
    async "$store.state.currentMusicInfo"(current) {
      this.currentMusicInfo = current;
      if (this.$store.state.isMusicPlayerShow) {
        this.getAudioUrl();
        await this.$store.commit("updateIsMusicPlayerShow", true);
      }
    },

    // 监听当前音频播放状态
    "$store.state.musicPlayState"(current) {
      // 如果不相同 说明在组件之外有地方改变了播放状态，需要将播放状态更新至此组件
      if (current != this.playState) {
        this.playState = current;
        if (current) {
          this.$refs.currentMusicPlayer.play();
        } else {
          this.$refs.currentMusicPlayer.pause();
        }
      }
    },
  },
  filters: {
    handleTime,
  },
  mounted() {},
};
</script>

<style scoped>
* {
  user-select: none;
}

.MusicPlayer {
  width: 270px;
  height: 100px;
  background-color: #fff;
  position: absolute;
  z-index: 10;
  left: 290px;
  bottom: 50px;
  padding: 7px;
  box-sizing: border-box;
  border-radius: 10px;
  box-shadow: 0px 0px 10px 1px rgba(0, 0, 0, 0.15);
  border: 1px solid #ccc;
  display: none;
}

.showPlayer {
  display: block;
}

.cover {
  width: 70px;
  position: relative;
}

.cover img {
  width: 100%;
  border-radius: 10px;
}

.top {
  display: flex;
  margin-bottom: 4px;
}

.musicInfo {
  margin: 0 8px;
  color: #25262b;
}

.musicName {
  margin: 10px 0 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 130px;
}

.singer {
  font-size: 13px;
  color: #43454d;
}

.icon-close {
  position: absolute;
  top: 8px;
  right: 10px;
  font-size: 20px;
  color: #43454d;
  cursor: pointer;
}

.el-slider {
  padding: 0 3px;
  width: 250px;
}

.time {
  position: absolute;
  bottom: 20px;
  right: 15px;
  font-size: 12px;
  color: #43454d;
}

.iconContainer {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.iconContainer i {
  color: #fcfcfc;
  font-size: 32px;
  padding: 5px;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  cursor: pointer;
}
</style>
