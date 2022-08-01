<template>
  <div
    class="iconTypeListContainer"
    :class="[
      banScroll ? 'banScroll' : '',
      $store.state.showType == 'icon' ? '' : 'tableTypeListContainer',
    ]"
  >
    <div class="IconTypeList" v-if="$store.state.showType == 'icon'">
      <!-- 文件夹 -->
      <div
        class="listItem folderItem"
        v-for="(item, index) in currentChildrenFolder"
        @dblclick="openCurrentFolder(item)"
        @contextmenu.prevent="showMenu($event, item, 'folderMenu')"
        @dragstart="(e) => e.preventDefault()"
        @dragover="(e) => e.preventDefault()"
        @drop="dropItem(item)"
        :key="index"
      >
        <div class="imgContainer">
          <img src="~assets/img/folder.png" alt="" :draggable="false" />
        </div>
        <input
          type="text"
          v-model="renameInput"
          class="renameInput"
          @blur="
            rightClickFolderItem.id
              ? renameDone(rightClickFolderItem, index, 'folder')
              : ''
          "
          @keyup.enter="
            rightClickFolderItem.id
              ? renameDone(rightClickFolderItem, index, 'folder')
              : ''
          "
          v-if="isFolderRenameInputShow && rightClickFolderItem.id == item.id"
        />
        <div class="name" v-else>
          {{
            item.name == null
              ? "NoNameFolder"
              : item.name.substr(0, item.name.length - 1)
          }}
        </div>
      </div>

      <!-- 新建文件夹的模板 -->
      <div class="listItem createItem" v-if="isCreateFolderShow">
        <img src="~assets/img/folder.png" alt="" />
        <input
          type="text"
          v-model="createdName"
          class="renameInput createNameInput"
          @blur="createNameDone"
          @keyup.enter="createNameDone"
        />
      </div>

      <!-- 文件列表 -->
      <div
        class="listItem"
        :class="selectFiles.find((i) => i.id == item.id) ? 'selectFile' : ''"
        v-for="(item, index) in listData"
        :key="item.id"
        @click="selectCurrentItem(item)"
        @dblclick="openCurrentFile(item)"
        @encryption="encryption(item)"
        @sanlie="sanlie(item)"
        @contextmenu.prevent="showMenu($event, item)"
        :draggable="rightClickMenuType == 'files'"
        @dragstart="onDragItemStart($event, item)"
        @dragend="onDragEndItem"
        slot="reference"
      >
        <div class="imgContainer">
          <img
            src="~assets/img/music.png"
            alt=""
            v-if="item.filetype == 'audio'"
            :draggable="false"
          />
          <img
            :src="item.url"
            alt=""
            v-else-if="item.filetype == 'image'"
            :draggable="false"
          />

          <!-- 视频文件 -->
          <div class="videoContainer" v-else-if="item.filetype == 'video'">
            <video :src="item.url" class="videoItem" preload="meta"></video>
            <div class="iconContainer">
              <i class="iconfont icon-play_nor"></i>
            </div>
          </div>
          <img
            :src="require(`assets/img/${computeType(item.type)}.png`)"
            alt=""
            v-else
            :draggable="false"
          />
          <img
            src="~assets/img/collect.png"
            alt=""
            class="collectIcon"
            v-if="item.collection"
            :draggable="false"
          />
        </div>
        <input
          type="text"
          v-model="renameInput"
          class="renameInput"
          @blur="rightClickItem.id ? renameDone(rightClickItem, index) : ''"
          @keyup.enter="
            rightClickItem.id ? renameDone(rightClickItem, index) : ''
          "
          v-if="isRenameShow && rightClickItem.id == item.id"
        />
        <div class="name" v-else>
          {{ item.name == null ? "NoNameFile" : item.name }}
        </div>
      </div>
    </div>

    <div class="tableTypeList" v-else>
      <!-- 文件夹 -->
      <div
        class="tableListItem"
        v-for="(item, index) in currentChildrenFolder"
        @dblclick="openCurrentFolder(item)"
        @contextmenu.prevent="showMenu($event, item, 'folderMenu')"
        @dragover="(e) => e.preventDefault()"
        @drop="dropItem(item)"
        :key="index"
      >
        <div class="tableImgContainer">
          <img src="~assets/img/folder.png" alt="" :draggable="false" />
        </div>
        <div class="tableName">
          {{
            item.name == null
              ? "NoNameFolder"
              : item.name.substr(0, item.name.length - 1)
          }}
        </div>
        <div class="tableCollect">-</div>
        <div class="tableItemSize">-</div>
        <div class="tableItemCreateTime">-</div>
      </div>

      <!-- 新建文件夹的模板 -->
      <div class="tableListItem" v-if="isCreateFolderShow">
        <img src="~assets/img/folder.png" alt="" />
        <div class="tableCreateInputContainer">
          <input
            type="text"
            v-model="createdName"
            class="tableCreateInput"
            @blur="createNameDone"
            @keyup.enter="createNameDone"
          />
        </div>
      </div>

      <!-- 文件列表 -->
      <div
        class="tableListItem"
        :class="selectFiles.find((i) => i.id == item.id) ? 'selectFile' : ''"
        v-for="(item, index) in listData"
        :key="item.id"
        @click="selectCurrentItem(item)"
        @dblclick="openCurrentFile(item)"
        @encryption="encryption(item)"
        @sanlie="sanlie(item)"
        @contextmenu.prevent="showMenu($event, item)"
        :draggable="rightClickMenuType == 'files'"
        @dragstart="onDragItemStart($event, item)"
        @dragend="onDragEndItem"
        slot="reference"
      >
        <div class="tableImgContainer">
          <img
            src="~assets/img/music.png"
            alt=""
            v-if="item.filetype == 'audio'"
            :draggable="false"
          />
          <el-image
            :src="item.url"
            alt=""
            v-else-if="item.filetype == 'image'"
            :draggable="false"
            fit="cover"
          />

          <div class="tableVideoContainer" v-else-if="item.filetype == 'video'">
            <video
              :src="item.url"
              class="tableVideoItem"
              preload="meta"
            ></video>
          </div>
          <img
            :src="require(`assets/img/${computeType(item.type)}.png`)"
            alt=""
            v-else
            :draggable="false"
          />
        </div>
        <div
          class="tableRenameInputContainer"
          v-if="isRenameShow && rightClickItem.id == item.id"
        >
          <input
            type="text"
            v-model="renameInput"
            class="tableRenameInput"
            @blur="rightClickItem.id ? renameDone(rightClickItem, index) : ''"
            @keyup.enter="
              rightClickItem.id ? renameDone(rightClickItem, index) : ''
            "
          />
        </div>

        <div class="tableName" v-else>
          {{ item.name == null ? "NoNameFile" : item.name + "." + item.type }}
        </div>
        <div class="tableCollect">
          <img src="~assets/img/collect.png" alt="" v-if="item.collection" />
        </div>
        <div class="tableItemSize">
          {{
            item.size == null
              ? "未知大小"
              : (item.size / 1048576).toFixed(2) + " MB"
          }}
        </div>
        <div class="tableItemCreateTime">
          {{ item.gmtCreate.substr(0, 16) }}
        </div>
      </div>
    </div>

    <!-- 文件右击菜单框组件 -->
    <right-click-menu
      :menuType="rightClickMenuType == 'collect' ? 'collect' : 'file'"
      :isMenuShow="isMenuShow"
      :menuTop="menuTop"
      :menuLeft="menuLeft"
      :isCurrentFileCollected="isCurrentFileCollected"
      @rename="rename"
      @collectCurrentFile="collectCurrentFile"
      @openCurrentFile="openCurrentFile(rightClickItem)"
      @encryption="encryption(rightClickItem)"
      @sanlie="sanlie(rightClickItem)"
      @downloadCurrentFile="downloadCurrentFile('current', rightClickItem)"
      @deleteCurrentFile="deleteCurrentFile('current', rightClickItem)"
      @shareCurrentFile="shareCurrentFile(rightClickItem)"
      @showAttribute="
        () => {
          isAttributeShow = true;
          this.$nextTick(() => {
            this.$refs.attributeDialog.getFileAttribute();
          });
        }
      "
      @moveFile="moveFile"
    ></right-click-menu>

    <!-- 文件夹右击菜单框组件 -->
    <right-click-menu
      :menuType="'folder'"
      :isFolderMenuShow="isFolderMenuShow"
      :menuTop="menuTop"
      :menuLeft="menuLeft"
      @openCurrentFolder="openCurrentFolder(rightClickFolderItem)"
      @renameCurrentFolder="rename('folder')"
      @deleteCurrentFolder="deleteCurrentFolder(rightClickFolderItem)"
    ></right-click-menu>

    <!-- 图片预览组件 -->
    <image-player
      :currentImg="currentImg"
      :isImgDialogShow="isImgDialogShow"
      @collectCurrentImg="
        (info) => this.collectCurrentFile(info.flag, 'player', info.item)
      "
      @closeDialog="isImgDialogShow = false"
      @shareCurrentFile="shareCurrentFile"
    ></image-player>

    <!-- 属性框组件 -->
    <attribute
      v-if="isAttributeShow"
      :fileId="rightClickItem.id"
      :attributeTop="attributeTop"
      :attributeLeft="attributeLeft"
      @closeAttribute="isAttributeShow = false"
      ref="attributeDialog"
    ></attribute>

    <!-- 移动到中选择文件夹的dialog -->
    <folder-dialog
      :isFolderDialogShow="isFolderDialogShow"
      :folderList="folderList"
      :moveType="moveType"
      @confirmMove="(info) => confirmMove(info.path, info.type)"
      @closeFolderDialog="isFolderDialogShow = false"
    ></folder-dialog>

    <!-- 搜索为空的提醒 -->
    <div
      class="searchTips"
      v-if="
        $route.params.path &&
          $route.params.path.search('search') != -1 &&
          searchFolder.length == 0 &&
          this.listData.length == 0
      "
    >
      没有找到相应内容哦!
    </div>
    <!-- 返回顶部按钮 -->
    <go-top scrollObj=".iconTypeListContainer"></go-top>
    <!-- 拖动时的预览容器 -->
    <div
      class="dragImgContainer"
      :class="showDragImgContainer ? 'showDragImgContainer' : ''"
      :style="[
        { top: dragImgContainerPosition.y + 'px' },
        { left: dragImgContainerPosition.x + 'px' },
      ]"
    ></div>

    <!-- 分享框 -->
    <share-dialog
      :isShareDialogShow="isShareDialogShow"
      :shareItem="shareItem"
      @closeDialog="isShareDialogShow = false"
    ></share-dialog>
  </div>
</template>

<script>
let isClickSelectAll = true;

import { getTypeIcon } from "plugins/utils.js";

import Attribute from "components/attribute/Attribute.vue";
import ImagePlayer from "components/imagePlayer/ImagePlayer.vue";
import RightClickMenu from "components/rightClickMenu/RightClickMenu.vue";
import FolderDialog from "components/folderDialog/FolderDialog.vue";
import GoTop from "components/goTop/GoTop.vue";
import ShareDialog from "components/shareDialog/ShareDialog.vue";
import { MessageBox } from "element-ui";

export default {
  components: {
    Attribute,
    ImagePlayer,
    RightClickMenu,
    FolderDialog,
    GoTop,
    ShareDialog,
  },
  name: "IconTypeList",
  data() {
    return {
      selectFiles: [],
      isMenuShow: false,
      menuTop: 0,
      menuLeft: 0,
      cardoffsetTop: 0,
      cardoffsetLeft: 0,
      // 重命名输入框
      renameInput: "",
      // 是否显示重命名输入框
      isRenameShow: false,
      // 右击的item
      rightClickItem: {},
      // 新建文件夹名称
      createdName: "",
      isCreateFolderShow: false,
      // 是否显示图片diaload
      isImgDialogShow: false,
      //   当前要打开diaload的图片
      currentImg: {},
      // 当前目录的子目录
      currentChildrenFolder: [],
      //   是否显示文件右键菜单
      isFolderMenuShow: false,
      // 下载的文件信息
      downloadFileInfo: {
        url: "",
        name: "",
      },
      // 视频列表
      videoList: [],
      // 是否显示属性框
      isAttributeShow: false,
      // 页面可见区域宽
      pageWidth: document.body.clientWidth,
      // 页面可见区域高
      pageHeight: document.body.clientHeight,
      // 属性dialog的位置
      attributeTop: 0,
      attributeLeft: 0,
      // 是否禁止页面滚动
      banScroll: false,
      // 是否显示文件选择框
      isFolderDialogShow: false,
      // 当前文件是否已收藏
      isCurrentFileCollected: false,
      // 文件移动类型  'mult'和'current'
      moveType: "current",
      // 右键选中的文件夹item
      rightClickFolderItem: {},
      // 当前文件夹的id
      currentFolderId: 0,
      // 是否显示文件夹重命名输入框
      isFolderRenameInputShow: false,
      // 被拖动的itemList
      dragItemList: [],
      // 是否显示dragImgContainer
      showDragImgContainer: false,
      // dragImgContainer的位置
      dragImgContainerPosition: {
        x: 0,
        y: 0,
      },
      // 是否显示分享框
      isShareDialogShow: false,
      // 分享的文件
      shareItem: {},
    };
  },
  props: {
    // type name
    listData: {
      type: Array,
      default() {
        return [];
      },
    },
    folderList: {
      type: Object,
      default() {
        return {};
      },
    },
    // 右键菜单类型
    rightClickMenuType: {
      type: String,
      default() {
        return "files";
      },
    },
    // item展示方式
    showType: {
      type: String,
      default() {
        return "icon";
      },
    },
    searchFolder: {
      type: Array,
      default() {
        return [];
      },
    },
  },

  methods: {
    // 单击item的回调
    selectCurrentItem(item) {
      // 操作dom  直接操作dom可以减少循环，提高性能
      // let listItem = document.querySelectorAll(".listItem");
      // 先判断该选项是否已经被选中
      let i = this.selectFiles.findIndex((item1) => item1.id == item.id);
      if (i == -1) {
        this.selectFiles.push(item);
      } else {
        this.selectFiles.splice(i, 1);
      }
      this.getIsAllFileCollect();
    },

    // 判断选中文件中是否都收藏了
    getIsAllFileCollect() {
      // 说明存在文件为收藏
      let flag = this.selectFiles.find((item) => {
        return item.collection == 0;
      });
      if (flag) {
        this.$store.commit("updateIsAllFileCollect", false);
      } else {
        this.$store.commit("updateIsAllFileCollect", true);
      }
    },

    // 全选
    // flag为boolean 全选或者取消全选
    selectAll(flag) {
      // let listItem = document.querySelectorAll(".listItem");
      if (flag) {
        this.selectFiles = this.listData.concat();
      } else {
        this.selectFiles = [];
      }
      this.getIsAllFileCollect();
    },

    // 右键单击item的事件
    showMenu(e, item, type) {
      // 如果属性框打开，则先关闭属性框
      if (this.isAttributeShow == true) {
        this.isAttributeShow = false;
      }
      this.banScroll = true;
      // 获取菜单的高度
      let menu = document.querySelector(".RightClickMenu");
      let menuHeight = menu.offsetTop;
      // 计算菜单dialog 的位置
      // files的菜单高度和collect不一样
      if (this.rightClickMenuType == "files") {
        this.menuTop =
          e.pageY + 250 > this.pageHeight ? this.pageHeight - 250 : e.pageY;
      } else {
        this.menuTop =
          e.pageY + 220 > this.pageHeight ? this.pageHeight - 220 : e.pageY;
      }
      this.menuLeft =
        e.pageX + 140 > this.pageWidth ? this.pageWidth - 140 : e.pageX;

      // 计算属性dialog的位置
      this.attributeTop =
        e.pageY + 230 > this.pageHeight ? this.pageHeight - 230 : e.pageY;
      this.attributeLeft =
        e.pageX + 340 > this.pageWidth ? this.pageWidth - 340 : e.pageX;

      if (!type || type == "menu") {
        // 判断右击文件是否已收藏
        this.isCurrentFileCollected = item.collection == 1 ? true : false;
        this.rightClickItem = item;
        // this.$store.commit("updateRightClickItem", item);
        this.isMenuShow = true;
        this.isFolderMenuShow = false;
      } else if (type == "folderMenu") {
        this.isFolderMenuShow = true;
        this.isMenuShow = false;
        // this.$store.commit("updateRightClickFolderItem", item);
        this.rightClickFolderItem = item;
      }
    },

    // 点击重命名的回调
    rename(type) {
      // 获取点击重命名的索引
      // this.rightClickItem = this.$store.state.rightClickItem;
      if (!type) {
        // 文件
        this.isRenameShow = true;
        this.renameInput = this.rightClickItem.name;
      } else if (type == "folder") {
        // 文件夹
        this.isFolderRenameInputShow = true;
        this.renameInput = this.rightClickFolderItem.name.slice(
          0,
          this.rightClickFolderItem.name.length - 1
        );
      }
      //在input的属性中添加autofocus只能触发一次 这里改用操作DOM
      this.$nextTick(() => {
        if (this.$store.state.showType == "icon") {
          document.querySelector(".renameInput").focus();
        } else if (this.$store.state.showType == "table") {
          document.querySelector(".tableRenameInput").focus();
        }
      });
    },

    // 重命名完成后的回调  失去焦点或者回车
    async renameDone(item, index, type) {
      // 判断输入内容是否为空
      if (this.renameInput.trim().length == 0) {
        if (!type) {
          this.$message.warning("文件名不能为空哦!");
          this.isRenameShow = false;
        } else {
          this.$message.warning("文件夹名称不能为空哦!");
          this.isFolderRenameInputShow = false;
        }
      }

      // 先判断rightClickIndex是否被重置为-1 被重置为-1说明已经调用过此函数（enter和blur会冲突，导致调用两次此函数）
      // 已经在html中调用方法时判断了
      // if (this.rightClickItem.id) {
      // 判断名字是否改变
      // 如果发生改变
      if (!type) {
        // 文件
        if (this.rightClickItem.name != this.renameInput.trim()) {
          // 发送请求给服务器
          let res = await this.$request(
            "/educenter/file/updateFile",
            {
              id: item.id,
              name: this.renameInput.trim(),
            },
            "post"
          );
          if (res.data.success) {
            // 通知父组件重新请求服务器数据 重新渲染此组件
            // 这里直接修改数据，避免出现刷新 影响用户体验
            this.listData[index].name = this.renameInput.trim();
          } else {
            this.$message.error("重命名失败,请稍后重试!");
          }
        }
        this.rightClickItem = {};
        this.isRenameShow = false;
      } else {
        // 文件夹
        if (this.rightClickFolderItem.name != this.renameInput.trim()) {
          // 计算当前文件夹的路径
          let url = (
            "/" +
            this.findPathByLeafId(this.rightClickFolderItem.name, [
              this.folderList,
            ]).join("")
          ).slice(
            0,
            this.findPathByLeafId(this.rightClickFolderItem.name, [
              this.folderList,
            ]).join("").length
          );
          let res = await this.$request(
            `/educenter/dir/updateDirStruct/${
              this.$store.state.userInfo.id
            }/${this.renameInput.trim() + "/"}/${this.rightClickFolderItem.id}`,
            url,
            "post",
            "params",
            "json"
          );
          if (res.data.success) {
            this.rightClickFolderItem.name = this.renameInput.trim() + "/";
          } else {
            this.$message.error("重命名失败,请稍后重试!");
          }
        }
        this.rightClickFolderItem = {};
        this.isFolderRenameInputShow = false;
      }
      // this.$store.commit("updateRightClickItem", {});
      // 清空renameInput
      this.renameInput = "";
      // }
    },

    // 新建文件夹命名完成的回调
    createNameDone() {
      // 避免 enter和 blur冲突调用两次此函数
      this.$nextTick(async () => {
        let input;
        if (this.$store.state.showType == "icon") {
          input = document.querySelector(".createNameInput");
        } else if (this.$store.state.showType == "table") {
          input = document.querySelector(".tableCreateInput");
        }
        if (input) {
          if (this.createdName == "") {
            this.$message.warning("文件夹名称不能为空哦!");
            this.$store.commit("updateIsCreateFolder", false);
          } else {
            // 发送请求给服务器
            let res = await this.$request(
              `/educenter/dir/setUserDir/${this.$store.state.userInfo.id}/${this.createdName}/${this.currentFolderId}`,
              "",
              "post"
            );
            //   重新加载组件
            if (res.data.success) {
              this.$emit("getFolderList");
              this.$store.commit("updateIsGetingFolder", true);
            } else {
              this.$message.error(res.data.message);
              this.createdName = "";
              this.$store.commit("updateIsCreateFolder", false);
            }
          }
        }
      });
    },

    // 双击打开文件
    openCurrentFile(item) {
      // 先判断打开的文件类型
      let filetype = item.filetype;
      // 打开的是video文件
      if (filetype == "video") {
        this.$store.commit("updateIsVideoPlayerShow", true);
        this.$store.commit("updateCurrentVideoInfo", item);
      }
      // 打开的是音频文件
      else if (filetype == "audio") {
        this.$store.commit("updateIsMusicPlayerShow", true);
        this.$store.commit("updateCurrentMusicInfo", item);
      }
      // 打开的是图片文件
      else if (filetype == "image") {
        this.currentImg = item;
        this.isImgDialogShow = true;
      }
      // 其它文件暂时无法直接打开 提醒暂时无法直接打开，可以下载后打开
      else {
        this.$message.warning("该文件暂时不能直接打开哦,可以下载后在本地打开!");
      }
    },
    async sanlie(data) {
      let res = await this.$request(
        "/educenter/file/findFile/xiazai",
        data,
        "post",
        "params"
      );
      console.log(11111, res);
      let slz = res.data.data.data;
      MessageBox.alert(
        "该文件的散列值为：" + slz + " , 经检测文件没有被篡改",
        "文件篡改检测",
        {
          confirmButtonText: "关闭",
          callback: (action) => {
           
          },
        }
      );
    },
    // 加密函数
    async encryption(item) {
      // item.encryption = "1";
      let data = item;
      // let vacaacca;
      MessageBox.prompt("请输入密码", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
        .then(({ value }) => {
          data.encryption = "2";
          data.pass = value;
          this.jm(data);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入",
          });
        });
    },
    async jm(data) {
      if (data.pass) {
        await this.$request(
          "/educenter/file/encryption",
          data,
          "post",
          "params"
        );
      }
    },
    // 删除文件
    async deleteCurrentFile(type, item) {
      let passcheck = false;
      if (item.encryption === "2") {
        MessageBox.prompt("请输入密码", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        }).then(({ value }) => {
          item.pass = value;
          this.deletejiemi(item, type);
        });
      } else {
        this.shanchu(type, item);
      }
    },

    // 解密
    async deletejiemi(data, type) {
      let r = await this.$request(
        "/educenter/file/decrypt",
        data,
        "post",
        "params"
      );
      let state;
      console.log(123, r);
      state = r.data.success;
      if (state) {
        this.shanchu(type, data);
      } else {
        this.$message.error("文件密码输入错误,不能删除文件");
      }
    },
    async shanchu(type, item) {
      let res;
      let arr = [];
      if (type == "current") {
        arr.push(item.id);
      } else if (type == "mult") {
        this.selectFiles.forEach((item) => {
          arr.push(item.id);
        });
      }
      res = await this.$request(
        `/eduoss/fileoss/removeAlyVideo/${this.$store.state.userInfo.id}?idList=${arr}`,
        "",
        "delete"
      );
      // 删除成功后重新获取列表
      // 减少刷新次数，提升用户体验
      // this.$emit("getListData");
      if (res.data.success) {
        if (type == "current") {
          let index = this.listData.findIndex(
            (item) => item.id == this.rightClickItem.id
          );
          this.listData.splice(index, 1);
          // 再判断一下被删除的item是否被多选了 如果被多选了，还需要再删除多选数组中的item
          let idx = this.selectFiles.findIndex(
            (item) => item.id == this.rightClickItem.id
          );
          if (idx != -1) {
            this.selectFiles.splice(idx, 1);
          }

          // 更新用户存储空间
          let userInfo = this.$store.state.userInfo;
          userInfo.neicun -= item.size;
          this.$store.commit("updateUserInfo", userInfo);
        } else {
          let deleteSize = 0;
          this.selectFiles.forEach((item) => {
            let index = this.listData.findIndex((i) => i.id == item.id);
            this.listData.splice(index, 1);
            deleteSize += item.size;
          });
          this.selectFiles.splice(0, this.selectFiles.length);

          // 更新用户存储空间
          let userInfo = this.$store.state.userInfo;
          userInfo.neicun -= deleteSize;
          this.$store.commit("updateUserInfo", userInfo);
        }
      } else {
        this.$message.error("删除失败,请稍后重试!");
      }
    },
    // js递归遍历树形json数据，根据关键字查找节点
    //@leafId  查找的id，
    //@nodes   原始Json数据
    //@path    供递归使用
    findPathByLeafId(leafId, nodes, path) {
      if (path === undefined) {
        path = [];
      }
      for (var i = 0; i < nodes.length; i++) {
        var tmpPath = path.concat();
        tmpPath.push(nodes[i].name);
        if (leafId == nodes[i].name) {
          return tmpPath;
        }
        if (nodes[i].childrenList) {
          var findResult = this.findPathByLeafId(
            leafId,
            nodes[i].childrenList,
            tmpPath
          );
          if (findResult) {
            return findResult;
          }
        }
      }
    },

    // 打开当前双击的文件夹
    // 点击的是folderList中第 index个子目录
    openCurrentFolder(item) {
      console.log("打开文件", item);
      let currentFolder = (
        "/" + this.findPathByLeafId(item.name, [this.folderList]).join("")
      ).slice(
        0,
        this.findPathByLeafId(item.name, [this.folderList]).join("").length
      );
      // let currentFolder =
      //   this.$route.params.path +
      //   "/" +
      //   item.name.substr(0, item.name.length - 1);
      // // 在vuex中更新当前目录
      // this.$store.commit("updateCurrentFolder", currentFolder);
      this.$router.push({ name: "files", params: { path: currentFolder } });
    },

    // 判断当前所在的文件夹位置
    getCurrentLocation() {
      if (this.$route.params.path.search("search") != -1) return;
      let currentFolder = this.$route.params.path;
      currentFolder = currentFolder.slice(1, currentFolder.length);
      let arr = currentFolder.split("/");
      // 如果是/search就不计算当前位置了
      // if (arr[arr.length - 1] == "search") {
      //   return;
      // }
      this.currentChildrenFolder = this.folderList.childrenList;
      if (arr.length > 1) {
        // 说明不在根目录
        for (var i = 1; i < arr.length; i++) {
          let index = this.currentChildrenFolder.findIndex(
            (item) => item.name.substr(0, item.name.length - 1) == arr[i]
          );
          this.currentFolderId = this.currentChildrenFolder[index].id;
          this.currentChildrenFolder = this.currentChildrenFolder[
            index
          ].childrenList;
        }
      } else {
        this.currentFolderId = 1;
      }
    },
    // 解密
    async jiemi(data, type) {
      let r = await this.$request(
        "/educenter/file/decrypt",
        data,
        "post",
        "params"
      );
      let state;
      console.log(123, r);
      state = r.data.success;
      if (state) {
        this.xiazai(type, data);
      } else {
        this.$message.error("文件密码输入错误,不能下载文件");
      }
    },
    async xiazai(type, item) {
      let url;
      // 循环的数组，里面的item是索引
      let arr = [];
      if (type == "current") {
        arr.push(item);
      } else if (type == "mult") {
        arr = this.selectFiles;
      }
      arr.forEach((i) => {
        // 循环执行的速度太快，watch来不及监听 这里通过定时器放到异步执行
        setTimeout(async () => {
          if (i.filetype == "video" || i.filetype == "audio") {
            // 请求url
            let res = await this.$request(
              "/eduoss/fileoss/getPlayAuth?isList=" + i.videoId,
              "",
              "post"
            );
            url = "/downloadvideo/" + i.url.split("com/")[1];
          } else {
            url = "/downloadfile/" + i.url.split("com/")[1];
          }
          console.log("url", url);
          this.$store.commit("updateCurrentDownloadFileInfo", {
            name: i.name + "." + i.type,
            url,
          });
        });
      });
    },

    // 点击下载文件的回调
    downloadCurrentFile(type, item) {
      let passcheck = false;
      if (item.encryption === "2") {
        MessageBox.prompt("请输入密码", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        }).then(({ value }) => {
          item.pass = value;
          this.jiemi(item, type);
        });
      } else {
        this.xiazai(type, item);
      }
    },

    // 收藏文件
    async collectCurrentFile(collect, type, playerItem) {
      let ids = [];
      if (!type || type == "current") {
        // ids = this.$store.state.rightClickItem.id;
        ids.push(this.rightClickItem.id);
      } else if (type == "mult") {
        if (collect) {
          this.selectFiles.forEach((item) => {
            if (item.collection == 0) {
              ids.push(item.id);
            }
          });
        } else {
          this.selectFiles.forEach((item) => {
            if (item.collection == 1) {
              ids.push(item.id);
            }
          });
        }
        if (ids == "") return;
      } else if (type == "player") {
        ids.push(playerItem.id);
      }
      //  发送请求
      if (collect) {
        let res = await this.$request(
          `/educenter/file/collectFile?id=${ids}`,
          "",
          "post"
        );

        if (res.data.success) {
          this.$message.success("收藏成功!");
          // 减少刷新次数 直接修改数据
          // this.$emit("getListData");

          if (type == "mult") {
            this.selectFiles.forEach((item, index, arr) => {
              // 虽然数组是深拷贝，但是里面的对象仍是指向同一个地址的，所以直接修改 selectFiles的对象就行
              // let idx = this.listData.findIndex((i) => i.id == item.id);
              // this.listData[idx].collection = 1;
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

        if (res.data.success) {
          this.$message.success("取消收藏成功!");
          // this.$emit("getListData");
          if (type == "mult") {
            this.selectFiles.forEach((item, index, arr) => {
              // let idx = this.listData.findIndex((i) => i.id == item.id);
              // this.listData[idx].collection = 1;
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

    // 移动文件
    async confirmMove(path, type, list) {
      // 先判断是否是当前文件夹 是的话直接return
      if (path == this.$store.state.currentFolder) {
        return;
      }

      let arr = [];
      if (!type || type == "current") {
        // arr.push(this.$store.state.rightClickItem.id);
        arr.push(this.rightClickItem.id);
      } else if (type == "mult") {
        this.selectFiles.forEach((item) => arr.push(item.id));
      } else if (type == "drag") {
        list.forEach((item) => arr.push(item.id));
      }
      let res = await this.$request(
        `/educenter/file/fileMove?id=${arr}`,
        path,
        "post",
        "params",
        "json"
      );
      if (res.data.success) {
        this.$message.success("移动成功!");
        // 避免直接获取数据，以避免dom刷新 影响用户体验
        // this.$emit("getListData");
        if (!type || type == "current") {
          let index = this.listData.findIndex(
            (item) => item.id == this.rightClickItem.id
          );
          this.listData.splice(index, 1);
          // 再判断一下被删除的item是否被多选了
          let idx = this.selectFiles.findIndex(
            (item) => item.id == this.rightClickItem.id
          );
          if (idx != -1) {
            this.selectFiles.splice(idx, 1);
          }
        } else if (type == "mult") {
          this.selectFiles.forEach((item) => {
            let index = this.listData.findIndex((i) => i.id == item.id);
            this.listData.splice(index, 1);
          });
          this.selectFiles.splice(0, this.selectFiles.length);
        } else if (type == "drag") {
          list.forEach((item) => {
            let index = this.listData.findIndex((i) => i.id == item.id);
            this.listData.splice(index, 1);
          });
          this.selectFiles.splice(0, this.selectFiles.length);
        }
      } else {
        this.$message.error("移动失败,请稍后重试!");
      }
    },

    // 分享当前文件
    shareCurrentFile(item) {
      this.shareItem = item;
      this.isShareDialogShow = true;
    },

    // 点击移动文件的回调
    moveFile(type) {
      this.isFolderDialogShow = true;
      if (!type || type == "current") {
        this.moveType = "current";
      } else {
        this.moveType = "mult";
      }
    },

    // 删除当前文件夹
    async deleteCurrentFolder(item) {
      let res = await this.$request(
        `/educenter/dir/deleteDirStruct/${this.$store.state.userInfo.id}/${item.id}`,
        this.$store.state.currentFolder +
          "/" +
          item.name.substr(0, item.name.length - 1),
        "post",
        "params",
        "json"
      );
      if (res.data.success) {
        this.$emit("getFolderList");
      }
    },

    // 重命名当前文件夹
    renameCurrentFolder(item) {
      this.isFolderRenameShow = true;
    },

    // 以下是拖动事件
    // item 拖动事件
    // 文件对象
    onDragItemStart(e, item) {
      this.dragItemList = [];
      // 判断item是否被选中 选中则拖动所有选中的选项，否则只拖动当前item
      if (this.selectFiles.find((i) => i.id == item.id)) {
        // 深拷贝
        this.dragItemList = this.selectFiles.concat();
      } else {
        this.dragItemList[0] = item;
      }
      let img = document.createElement("img");
      e.dataTransfer.setDragImage(img, 0, 0);
      // 确认自定义拖动框的位置
      this.dragImgContainerPosition.x = e.clientX + 5;
      this.dragImgContainerPosition.y = e.clientY + 5;
      // 向拖动款中插入img
      let imgContainer = document.querySelector(".dragImgContainer");
      let img1;
      if (item.filetype != "video") {
        img1 = e.target.querySelector("img").cloneNode();
      } else {
        img1 = e.target.querySelector("video").cloneNode();
      }
      imgContainer.appendChild(img1);
      // 判断item是否被选中
      if (this.selectFiles.find((i) => i.id == item.id)) {
        this.selectFiles.some((i) => {
          if (imgContainer.children.length == 3) {
            return true;
          }
          if (i.id != item.id) {
            if (i.filetype != "video") {
              let img = document.createElement("img");
              // 图片
              if (i.filetype == "image") {
                img.setAttribute("src", i.url);
              } else {
                let typeIcon = getTypeIcon(i.filetype);
                img.setAttribute(
                  "src",
                  require(`/src/assets/img/${typeIcon}.png`)
                );
              }
              imgContainer.appendChild(img);
            } else if (i.filetype == "video") {
              let video = document.createElement("video");
              video.setAttribute("src", i.url);
              video.setAttribute("preload", "meta");
              imgContainer.appendChild(video);
            }
          }
        });
      } else {
      }

      this.$nextTick(() => {
        this.showDragImgContainer = true;
      });

      // if (this.myBrowser !== "FF") return;
      // 火狐浏览器存在bug  在drag事件中的 e不能获取到client值，所以用document作为目的地，通过dragover获得在document中的坐标值
      document.ondragover = (event) => {
        this.dragImgContainerPosition.x = event.clientX + 5;
        this.dragImgContainerPosition.y = event.clientY + 5;
      };
    },

    // 为了代码的简洁，统一使用document.ondragover事件
    // onDragItem(e) {
    // if (e.clientX == 0 && e.clientY == 0) {
    //   this.showDragImgContainer = false;
    // } else {
    //   if (this.showDragImgContainer == false) {
    //     this.dragImgContainerPosition.x = e.clientX + 5;
    //     this.dragImgContainerPosition.y = e.clientY + 5;
    //     this.$nextTick(() => {
    //       this.showDragImgContainer = true;
    //     });
    //   }
    // }
    // this.dragImgContainerPosition.x = e.clientX + 5;
    // this.dragImgContainerPosition.y = e.clientY + 5;
    // },

    onDragEndItem() {
      document.ondragover = null;
      this.showDragImgContainer = false;
      let imgContainer = document.querySelector(".dragImgContainer");
      imgContainer.innerHTML = "";
    },

    // 文件夹对象
    dropItem(item) {
      // 获得文件夹的完整路径
      let currentFolder = (
        "/" + this.findPathByLeafId(item.name, [this.folderList]).join("")
      ).slice(
        0,
        this.findPathByLeafId(item.name, [this.folderList]).join("").length
      );
      this.confirmMove(currentFolder, "drag", this.dragItemList);
      this.dragItemList = [];
    },
  },
  computed: {
    computeType() {
      return getTypeIcon;
    },
  },
  watch: {
    "$store.state.isSelectAll"(current) {
      if (isClickSelectAll == false) {
        isClickSelectAll = true;
        return;
      }
      this.selectAll(current);
    },

    selectFiles(current) {
      let listItem = document.querySelectorAll(".listItem");
      if (current.length == listItem.length) {
        this.$store.commit("updateIsSelectAll", true);
      } else {
        if (this.$store.state.isSelectAll == true) {
          isClickSelectAll = false;
          this.$store.commit("updateIsSelectAll", false);
        }
      }
    },

    // 监听是否正在新建文件夹的状态
    "$store.state.isCreateFolder"(current) {
      this.isCreateFolderShow = current;
      if (current == true) {
        if (this.$store.state.showType == "icon") {
          this.$nextTick(() => {
            document.querySelector(".createNameInput").focus();
          });
        } else if (this.$store.state.showType == "table") {
          this.$nextTick(() => {
            document.querySelector(".tableCreateInput").focus();
          });
        }
      }
    },
    folderList(current) {
      this.getCurrentLocation();
    },

    // 监听选中文件的变化
    selectFiles(current) {
      this.$store.commit("updateSelectFiles", current);
    },

    // 监听是否正在获取文件夹
    "$store.state.isGetingFolder"(current) {
      if (current == false) {
        this.createdName = "";
        this.$store.commit("updateIsCreateFolder", false);
      }
    },

    // 监听排序方式
    "$store.state.sortType"(current) {
      if (current == "time") {
        this.listData.sort((a, b) => {
          return Date.parse(a.gmtCreate) - Date.parse(b.gmtCreate);
        });
      } else if (current == "size") {
        this.listData.sort((a, b) => {
          return a.size - b.size;
        });
      }
    },
    searchFolder(current) {
      this.currentChildrenFolder = current;
    },
  },
  created() {},
  mounted() {
    // 获取组件的offset
    let Card = document.querySelector(".iconTypeListContainer");
    this.cardoffsetTop = Card.offsetTop;
    this.cardoffsetLeft = Card.offsetLeft;

    // 监听页面窗口大小变化
    window.addEventListener("resize", (e) => {
      this.pageWidth = document.body.clientWidth;
      this.pageHeight = document.body.clientHeight;
    });

    // 监听所有点击事件
    document.onclick = (e) => {
      // console.log(e);
      if (this.isMenuShow == true || this.isFolderMenuShow == true) {
        this.isMenuShow = false;
        this.isFolderMenuShow = false;
        this.banScroll = false;
      }
      if (this.$store.state.isUserInfoCardMenuShow) {
        this.$store.commit("updateIsUserInfoCardMenuShow", false);
      }
    };
  },
};
</script>

<style scoped>
.iconTypeListContainer {
  height: calc(100vh - 100px);
  width: calc(100vw - 240px);
  overflow-y: scroll;
  position: relative;
}

.tableTypeListContainer {
  height: calc(100vh - 150px);
}

.banScroll {
  overflow-y: hidden;
}

.IconTypeList {
  display: flex;
  flex-wrap: wrap;
  position: relative;
  padding: 10px 20px 0;
}

.listItem {
  margin: 0 5px 10px;
  padding: 15px 5px 10px;
  text-align: center;
  width: 100px;
  font-size: 14px;
  border-radius: 10px;
  cursor: pointer;
  position: relative;
  height: 118px;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* box-sizing: border-box; */
}

.collectIcon {
  position: absolute;
  width: 15px;
  height: 15px;
  top: 0px;
  right: 7px;
}

.folderItem:hover {
  background-color: unset !important;
}

.name {
  word-break: break-all;
  text-overflow: ellipsis;
  overflow: hidden; /** 隐藏超出的内容 **/
  white-space: nowrap;
  padding-top: 10px;
  width: 100px;
  height: 18px;
}

.imgContainer {
  height: 90px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.listItem img {
  max-height: 90px;
  max-width: 90%;
  user-select: none;
}

.listItem:hover {
  background-color: rgba(61, 158, 207, 0.2);
}

.listItem:hover i {
  background-color: rgba(255, 255, 255, 0.5);
}

.selectFile {
  background-color: rgba(33, 151, 198, 0.4) !important;
}

.renameInput {
  width: 92px;
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
}

.videoContainer {
  height: 90px;
  width: 90px;
  margin-bottom: 2px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #000;
}

.videoItem {
  height: 90px;
  width: 90px;
  border-radius: 5px;
  overflow: hidden;
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

.tableListItem {
  display: flex;
  /* background-color: pink; */
  width: calc(100vw - 260px);
  height: 55px;
  padding: 6px 25px;
  box-sizing: border-box;
  /* align-items: center; */
  border-bottom: 1px solid #eee;
  cursor: pointer;
  font-size: 14px;
  color: rgb(66, 66, 66);
}

.tableListItem > div {
  height: 43px;
  line-height: 43px;
  box-sizing: border-box;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tableListItem:hover {
  background-color: #e1e1f5;
}

.tableImgContainer {
  width: 48px;
}

.tableImgContainer img,
.tableImgContainer .el-image {
  height: 43px;
  width: 43px;
  border-radius: 5px;
}

.tableVideoContainer {
  height: 43px;
  width: 43px;
  background-color: black;
  border-radius: 5px;
}

.tableVideoItem {
  height: 43px;
  width: 43px;
}

.tableName {
  padding-left: 13px;
  line-height: 43px;
  width: 50%;
}

.tableItemSize {
  width: 20%;
  padding-left: 80px;
  box-sizing: border-box;
}

.tableItemCreateTime {
  padding-left: 60px;
  box-sizing: border-box;
  width: 25%;
}

.tableCollect {
  width: 10%;
  text-align: center;
}

.tableCollect img {
  height: 15px;
}

.tableCreateInput {
  margin-left: 15px;
  height: 23px;
}

.tableRenameInput {
  height: 23px;
  margin-left: 10px;
}

.searchTips {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 22px;
  color: rgb(63, 63, 63);
}
</style>
