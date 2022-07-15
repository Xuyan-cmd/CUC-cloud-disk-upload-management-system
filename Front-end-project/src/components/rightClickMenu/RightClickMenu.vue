<template>
  <div class="RightClickMenu">
    <!-- 文件点击右键的菜单框 -->
    <div
      class="menu"
      v-if="menuType == 'file'"
      :style="{
        display: isMenuShow ? 'block' : 'none',
        top: menuTop + 'px',
        left: menuLeft + 'px',
      }"
    >
      <div class="group">
        <div class="open" @click="$emit('openCurrentFile')">打开</div>
        <div class="download" @click="$emit('downloadCurrentFile')">下载</div>
      </div>
      <div class="group">
        <div
          @click="$emit('collectCurrentFile', true)"
          v-if="!isCurrentFileCollected"
        >
          收藏
        </div>
        <div @click="$emit('collectCurrentFile', false)" v-else>取消收藏</div>
        <div class="share" @click="$emit('shareCurrentFile')">分享</div>
      </div>
      <div class="group">
        <div @click="$emit('moveFile')">移动到</div>
      </div>
      <div class="group">
        <div class="rename" @click="$emit('rename')">重命名</div>
        <div @click="$emit('deleteCurrentFile')">删除</div>
      </div>
      <div class="group">
        <div class="rename" @click="$emit('showAttribute')">属性</div>
      </div>
    </div>

    <!-- 文件夹点击右键的菜单框 -->
    <div
      class="menu"
      v-else-if="menuType == 'folder'"
      :style="{
        display: isFolderMenuShow ? 'block' : 'none',
        top: menuTop + 'px',
        left: menuLeft + 'px',
      }"
    >
      <div class="group">
        <div class="open" @click="$emit('openCurrentFolder')">打开</div>
      </div>
      <div class="group">
        <div class="rename" @click="$emit('renameCurrentFolder')">重命名</div>
        <div @click="$emit('deleteCurrentFolder')">删除</div>
      </div>
    </div>

    <!-- 相册点击右键的菜单框 -->
    <div
      class="menu"
      v-if="menuType == 'album'"
      :style="{
        display: isMenuShow ? 'block' : 'none',
        top: menuTop + 'px',
        left: menuLeft + 'px',
      }"
    >
      <div class="group">
        <div class="open" @click="$emit('openCurrentFile')">打开</div>
        <div class="download" @click="$emit('downloadCurrentFile')">下载</div>
      </div>
      <div class="group">
        <div
          @click="$emit('collectCurrentFile', true)"
          v-if="!isCurrentFileCollected"
        >
          收藏
        </div>
        <div @click="$emit('collectCurrentFile', false)" v-else>取消收藏</div>
        <div class="share" @click="$emit('shareCurrentFile')">分享</div>
      </div>
      <div class="group">
        <div @click="$emit('deleteCurrentFile')">删除</div>
      </div>
      <div class="group">
        <div class="rename" @click="$emit('showAttribute')">属性</div>
      </div>
    </div>

    <!-- 收藏点击右键的菜单框 -->
    <div
      class="menu"
      v-else-if="menuType == 'collect'"
      :style="{
        display: isMenuShow ? 'block' : 'none',
        top: menuTop + 'px',
        left: menuLeft + 'px',
      }"
    >
      <div class="group">
        <div class="open" @click="$emit('openCurrentFile')">打开</div>
        <div class="download" @click="$emit('downloadCurrentFile')">下载</div>
      </div>
      <div class="group">
        <div @click="$emit('collectCurrentFile')">取消收藏</div>
        <div class="share" @click="$emit('shareCurrentFile')">分享</div>
      </div>
      <div class="group">
        <div class="rename" @click="$emit('rename')">重命名</div>
        <div @click="$emit('deleteCurrentFile')">删除</div>
      </div>
      <div class="group">
        <div class="rename" @click="$emit('showAttribute')">属性</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RightClickMenu",
  props: {
    menuType: {
      type: String,
    },
    isMenuShow: {
      type: Boolean,
    },
    menuTop: {
      type: Number,
    },
    menuLeft: {
      type: Number,
    },
    isFolderMenuShow: {
      type: Boolean,
    },
    isCurrentFileCollected: {
      type: Boolean,
      default() {
        return false;
      },
    },
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped>
.menu {
  width: 130px;
  padding: 3px 0px;
  box-sizing: border-box;
  border-radius: 7px;
  box-shadow: 0px 0px 10px 1px rgba(0, 0, 0, 0.1);
  border: 1px solid #eee;
  background-color: #fff;
  position: fixed;
  z-index: 1000;
}

.group {
  padding: 4px 0;
  border-bottom: 1px solid #eee;
}

.group:last-child {
  border: none;
}

.group > div {
  padding: 4px 20px;
  font-size: 14px;
  color: rgb(70, 70, 70);
  user-select: none;
}

.group > div:hover {
  background-color: #696bcc;
  color: white;
}
</style>
