<template>
  <div class="AsideBar">
    <div
      class="barItem"
      :class="selectIndex == index ? 'select' : ''"
      v-for="(item, index) in asideBarData"
      :key="index"
      @click="clickAsideBarItem(index)"
    >
      <i class="iconfont" :class="'icon-' + item.icon"></i>
      <span>{{ item.name }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "AsideBar",
  data() {
    return {
      selectIndex: 0,
    };
  },
  props: {
    // name   path   icon
    asideBarData: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  methods: {
    clickAsideBarItem(index) {
      if (this.selectIndex == index) return;
      this.selectIndex = index;
      // 路由跳转
      if (this.asideBarData[index].componentName) {
        this.$router.push({
          name: this.asideBarData[index].componentName,
          params: this.asideBarData[index].params,
        });
      } else {
        this.$router.push(this.asideBarData[index].path);
      }
    },
  },
  watch: {
    //   监听vuex中的当前所在目录
  },
  created() {
    // console.log(this.$route.path.split("/")[1]);
    this.selectIndex = this.asideBarData.findIndex(
      (item) => item.path.slice(1) == this.$route.path.split("/")[1]
    );
  },
};
</script>

<style scoped>
i {
  font-size: 24px;
  margin-right: 10px;
}

.barItem {
  display: flex;
  align-items: center;
  margin: 0 10px;
  padding: 10px 15px;
  border-radius: 13px;
  cursor: pointer;
}

.barItem:hover {
  background-color: #ececee;
}

.select {
  background-color: rgb(231, 231, 231) !important;
}
</style>
