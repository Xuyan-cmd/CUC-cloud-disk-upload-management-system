<template>
  <div class="AlbumListContainer">
    <div v-for="(item, index) in albumDateList" :key="index">
      <div class="titleDate">{{ item.date | handleAlbumDateShow }}</div>
      <div class="AlbumList">
        <div
          class="albumItem"
          :class="
            selectItemList.find((item) => item.id == item1.id)
              ? 'selectItem'
              : ''
          "
          v-for="(item1, idx) in item.dateList"
          :key="idx"
          @click="clickAlbumItem(item1)"
          @dblclick="openCurrentItem(item1)"
          @contextmenu.prevent="showMenu($event, item1)"
        >
          <el-image
            v-if="item1.filetype == 'image'"
            :src="item1.url"
            lazy
            :fit="imgScale ? 'cover' : 'contain'"
            :draggable="false"
          ></el-image>
          <div class="videoContainer" v-else>
            <video :src="item1.url" preload="meta"></video>
            <div class="iconContainer">
              <i class="iconfont icon-play_nor"></i>
            </div>
          </div>
          <img
            src="~assets/img/collect.png"
            alt=""
            class="collectIcon"
            v-if="item1.collection"
          />
        </div>
      </div>
    </div>

    <!-- 图片预览组件 -->
    <image-player
      :currentImg="currentImg"
      :isImgDialogShow="isImgDialogShow"
      @closeDialog="isImgDialogShow = false"
      @collectCurrentImg="
        (info) => this.collectCurrentFile(info.flag, 'player', info.item)
      "
      @shareCurrentFile="shareCurrentFile"
    ></image-player>

    <!-- 文件右击菜单框组件 -->
    <right-click-menu
      :menuType="'album'"
      :isMenuShow="isMenuShow"
      :menuTop="menuTop"
      :menuLeft="menuLeft"
      :isCurrentFileCollected="isCurrentFileCollected"
      @openCurrentFile="openCurrentItem(rightClickItem)"
      @collectCurrentFile="collectCurrentFile"
      @downloadCurrentFile="downloadCurrentFile('current', rightClickItem)"
      @deleteCurrentFile="deleteCurrentFile('current', rightClickItem)"
      @shareCurrentFile="shareCurrentFile(rightClickItem)"
      @encryption="encryption(rightClickItem)"
      @showAttribute="
        () => {
          isAttributeShow = true;
          this.$nextTick(() => {
            this.$refs.attributeDialog.getFileAttribute();
          });
        }
      "
    ></right-click-menu>

    <!-- 属性框组件 -->
    <attribute
      v-if="isAttributeShow"
      :fileId="rightClickItem.id"
      :style="{
        top: attributeTop + 'px',
        left: attributeLeft + 'px',
      }"
      @closeAttribute="isAttributeShow = false"
      ref="attributeDialog"
    ></attribute>
    <!-- 返回顶部 -->
    <go-top scrollObj=".AlbumListContainer"></go-top>

    <!-- 分享框 -->
    <share-dialog
      :isShareDialogShow="isShareDialogShow"
      :shareItem="shareItem"
      @closeDialog="isShareDialogShow = false"
    ></share-dialog>
  </div>
</template>

<script>
import ImagePlayer from "components/imagePlayer/ImagePlayer.vue";
import RightClickMenu from "components/rightClickMenu/RightClickMenu";
import Attribute from "components/attribute/Attribute";

import { handleAlbumDateShow } from "plugins/utils.js";
import GoTop from "components/goTop/GoTop.vue";
import ShareDialog from "components/shareDialog/ShareDialog.vue";

export default {
  components: { ImagePlayer, RightClickMenu, Attribute, GoTop, ShareDialog },
  name: "AlbumList",
  props: {
    // 相册列表数据
    albumList: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  data() {
    return {
      // 相册所有的日期
      albumDateList: [],
      // 图片显示比例
      imgScale: true,
      // 当前已被选中的item
      selectItemList: [],
      // 当前预览的图片的信息
      currentImg: {
        url: "",
        name: "",
      },
      //   是否显示预览图片组件
      isImgDialogShow: false,
      // 当前右击的item
      rightClickItem: {},

      // 是否显示右键菜单
      isMenuShow: false,
      // 下面是用于计算右键菜单框位置的参数
      menuTop: 0,
      menuLeft: 0,
      cardoffsetTop: 0,
      cardoffsetLeft: 0,
      // 是否显示属性框
      isAttributeShow: false,
      // 页面可见区域宽
      pageWidth: document.body.clientWidth,
      // 页面可见区域高
      pageHeight: document.body.clientHeight,
      // 属性dialog的位置
      attributeTop: 0,
      attributeLeft: 0,

      // 当前文件是否被收藏
      isCurrentFileCollected: false,

      // 是否显示分享框
      isShareDialogShow: false,
      // 分享的文件
      shareItem: {},
    };
  },
  methods: {
    // 对日期进行处理
    handleAlbumDate() {
      // 先清空数据
      this.albumDateList = [];

      this.albumList.forEach((item) => {
        let date = item.gmtCreate.substr(0, 10);
        let i = this.albumDateList.findIndex((item1) => item1.date == date);
        if (i == -1) {
          this.albumDateList.push({
            date: item.gmtCreate.substr(0, 10),
            dateList: [item],
          });
        } else {
          this.albumDateList[i].dateList.push(item);
        }
      });
    },

    // 点击item的回调
    clickAlbumItem(item) {
      // 先判断id是否已经被选中
      let index = this.selectItemList.findIndex((i) => i.id == item.id);
      //   这里操作dom会比较复杂，需要用到二维数组，所以不操作dom了，直接保存item，再循环判断是否选中
      if (index == -1) {
        this.selectItemList.push(item);
      } else {
        this.selectItemList.splice(index, 1);
      }
      this.getIsAllFileCollect();
    },

    // 判断选中文件中是否都收藏了
    getIsAllFileCollect() {
      // 说明存在文件为收藏
      let flag = this.selectItemList.find((item) => item.collection == 0);
      if (flag) {
        this.$store.commit("updateIsAllFileCollect", false);
      } else {
        this.$store.commit("updateIsAllFileCollect", true);
      }
    },

    // 双击打开当前文件
    openCurrentItem(item) {
      // 先判断是什么类型
      if (item.filetype == "image") {
        this.currentImg = item;
        this.isImgDialogShow = true;
      } else if (item.filetype == "video") {
        this.$store.commit("updateIsVideoPlayerShow", true);
        this.$store.commit("updateCurrentVideoInfo", item);
      }
    },
    
     encryption(item) {
    },
    // 右键单击item的事件
    showMenu(e, item) {
      if (this.isAttributeShow == true) {
        this.isAttributeShow = false;
      }

      // 判断当前文件是否被收藏
      if (item.collection == 1) {
        this.isCurrentFileCollected = true;
      } else {
        this.isCurrentFileCollected = false;
      }

      // console.log(e, index);
      // console.log(this.cardoffsetLeft);
      // 计算菜单dialog 的位置
      this.menuTop =
        e.pageY + 180 > this.pageHeight
          ? this.pageHeight - this.cardoffsetTop - 180
          : e.pageY - this.cardoffsetTop;
      this.menuLeft =
        e.pageX + 140 > this.pageWidth
          ? this.pageWidth - this.cardoffsetLeft - 140
          : e.pageX - this.cardoffsetLeft;

      // 计算属性dialog的位置
      this.attributeTop =
        e.pageY + 230 > this.pageHeight ? this.pageHeight - 230 : e.pageY;
      this.attributeLeft =
        e.pageX + 340 > this.pageWidth ? this.pageWidth - 340 : e.pageX;

      this.rightClickItem = item;
      this.isMenuShow = true;
      this.isFolderMenuShow = false;
    },

    // 点击下载文件的回调
    downloadCurrentFile(type, item) {
      let url;
      // 循环的数组，里面的item是索引
      let arr = [];
      if (type == "current") {
        arr.push(item);
      } else if (type == "mult") {
        arr = this.selectItemList;
      }
      arr.forEach((item) => {
        // 循环执行的速度太快，watch来不及监听 这里通过定时器放到异步执行
        setTimeout(() => {
          if (item.filetype == "video") {
            url = "/downloadvideo/" + item.url.split("com/")[1];
          } else {
            url = "/downloadfile/" + item.url.split("com/")[1];
          }
          this.$store.commit("updateCurrentDownloadFileInfo", {
            name: item.name + "." + item.type,
            url,
          });
        });
      });
    },

    // 点击删除文件的回调
    async deleteCurrentFile(type, item) {
      let res;
      let arr = [];
      if (type == "current") {
        arr.push(item.id);
      } else if (type == "mult") {
        this.selectItemList.forEach((item) => {
          arr.push(item.id);
        });
      }
      res = await this.$request(
        `/eduoss/fileoss/removeAlyVideo/${this.$store.state.userInfo.id}?idList=${arr}`,
        "",
        "delete"
      );
      // console.log(res);
      // 删除成功后重新获取列表
      // this.$emit("getUserAllFiles");
      if (res.data.success) {
        if (!type || type == "current") {
          let index = this.albumList.findIndex(
            (item) => item.id == this.rightClickItem.id
          );
          this.albumList.splice(index, 1);
          // 再判断一下被删除的item是否被多选了
          let idx = this.selectItemList.findIndex(
            (item) => item.id == this.rightClickItem.id
          );
          if (idx != -1) {
            this.selectItemList.splice(idx, 1);
          }

          // 更新用户存储空间
          let userInfo = this.$store.state.userInfo;
          userInfo.neicun -= item.size;
          this.$store.commit("updateUserInfo", userInfo);
        } else {
          let deleteSize = 0;
          this.selectItemList.forEach((item) => {
            let index = this.albumList.findIndex((i) => i.id == item.id);
            this.albumList.splice(index, 1);
            deleteSize += item.size;
          });
          this.selectItemList.splice(0, this.selectItemList.length);

          // 更新用户存储空间
          let userInfo = this.$store.state.userInfo;
          userInfo.neicun -= deleteSize;
          this.$store.commit("updateUserInfo", userInfo);
        }
      } else {
        this.$message.error("删除失败,请稍后重试!");
      }
    },

    // 全选
    selectAll() {
      if (this.selectItemList.length != this.albumList.length) {
        // 这里要深拷贝，浅拷贝在用户直接点击item后去除selectItemList中的选项时也会把albumList的选项去除掉
        this.selectItemList = this.albumList.concat();
      } else {
        this.selectItemList = [];
      }
      this.getIsAllFileCollect();
    },

    // 收藏当前文件
    async collectCurrentFile(collect, type, playerItem) {
      let ids = [];
      if (!type || type == "current") {
        ids.push(this.rightClickItem.id);
      } else if (type == "mult") {
        if (collect) {
          // 如果是未收藏的则加入ids
          this.selectItemList.forEach((item) => {
            if (item.collection == 0) {
              ids.push(item.id);
            }
          });
        } else {
          this.selectItemList.forEach((item) => {
            if (item.collection == 1) {
              ids.push(item.id);
            }
          });
        }
      } else if (type == "player") {
        ids.push(playerItem.id);
      }

      // 请求
      if (collect) {
        let res = await this.$request(
          `/educenter/file/collectFile?id=${ids}`,
          "",
          "post"
        );

        if (res.data.success) {
          this.$message.success("收藏成功!");
          // 避免直接获取数据 影响用户体验
          // this.$emit("getUserAllFiles");
          if (type == "mult") {
            this.selectItemList.forEach((item, index, arr) => {
              arr[index].collection = 1;
            });
            this.$store.commit("updateIsAllFileCollect", true);
          } else if (!type || type == "current") {
            this.rightClickItem.collection = 1;
          } else if (type == "player") {
            playerItem.collection = 1;
          }
        } else {
          this.$message.error("收藏失败,请稍后重试!");
        }
      } else {
        let res = await this.$request(
          `/educenter/file/cancelCollection?id=${ids}`,
          "",
          "post"
        );
        // console.log(res);

        if (res.data.success) {
          this.$message.success("取消收藏成功!");
          // this.$emit("getUserAllFiles");
          if (type == "mult") {
            this.selectItemList.forEach((item, index, arr) => {
              arr[index].collection = 0;
            });
            this.$store.commit("updateIsAllFileCollect", false);
          } else if (!type || type == "current") {
            this.rightClickItem.collection = 0;
          } else if (type == "player") {
            playerItem.collection = 0;
          }
        } else {
          this.$message.error("取消收藏失败,请稍后重试!");
        }
      }
    },
    // 分享当前文件
    shareCurrentFile(item) {
      this.shareItem = item;
      this.isShareDialogShow = true;
    },
    async encryption(item) {
    
    },
  },

  mounted() {
    // 监听页面窗口大小变化
    window.addEventListener("resize", (e) => {
      // console.log(e);
      this.pageWidth = document.body.clientWidth;
      this.pageHeight = document.body.clientHeight;
    });

    // 监听所有点击事件
    document.onclick = (e) => {
      //   console.log(e);
      if (this.isMenuShow == true) {
        this.isMenuShow = false;
      }
      if (this.$store.state.isUserInfoCardMenuShow) {
        this.$store.commit("updateIsUserInfoCardMenuShow", false);
      }
    };
  },
  watch: {
    // 当相册数据发生变化时执行
    albumList(current) {
      this.handleAlbumDate();
    },
    // 监听图片显示比例
    "$store.state.imgScale"(current) {
      this.imgScale = current;
    },
    // 监听选中的数量
    selectItemList(current) {
      if (current.length > 0) {
        this.$emit("showMultButtons", true);
      } else if (current.length == 0) {
        this.$emit("showMultButtons", false);
      }
    },
  },
  filters: {
    handleAlbumDateShow,
  },
};
</script>

<style scoped>
.AlbumListContainer {
  height: calc(100vh - 100px);
  overflow-y: scroll;
  padding: 0 15px;
}

.AlbumList {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 30px;
}

.albumItem {
  height: 115px;
  width: 115px;
  margin: 3px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  cursor: pointer;
  position: relative;
  user-select: none;
}

.albumItem .collectIcon {
  position: absolute;
  width: 15px;
  height: 15px;
  top: 8px;
  right: 9px;
}

.selectItem {
  background-color: #c3c4eb !important;
}

.albumItem:hover {
  background-color: #e1e1f5;
}

.albumItem:hover i {
  background-color: rgba(255, 255, 255, 0.5);
}

.albumItem /deep/ img {
  height: 100px;
  width: 100px;
}

.albumItem video {
  height: 100px;
  width: 100px;
  background-color: #000;
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
  transition: all 0.1s;
}

.titleDate {
  margin: 10px 10px;
}
</style>
