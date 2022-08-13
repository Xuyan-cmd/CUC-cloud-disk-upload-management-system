<template>
  <div class="FolderDialog">
    <el-dialog
      title="移动到"
      width="400px"
      :visible.sync="isFolderDialogShow"
      @close="$emit('closeFolderDialog')"
    >
      <el-tree
        highlight-current
        :data="[folderList]"
        :props="showProps"
        @node-click="handleNodeClick"
      ></el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="$emit('closeFolderDialog')"
          size="mini"
          class="cancel"
          >取 消</el-button
        >
        <el-button @click="confirmMove" size="mini" class="confirm"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import IconTypeList from "../iconTypeList/IconTypeList.vue";

export default {
  name: "FolderDialog",
  components: {
    IconTypeList,
  },
  props: {
    isFolderDialogShow: {
      type: Boolean,
      default() {
        return false;
      },
    },
    folderList: {
      type: Object,
      default() {
        return [];
      },
    },
    moveType: {
      type: String,
      default() {
        return "current";
      },
    },
  },
  data() {
    return {
      // 显示规则
      showProps: {
        children: "childrenList",
        label: (data) => {
          return data.name.slice(0, data.name.length - 1);
        },
      },

      //   选中的文件夹名称
      selectFolder: "",
    };
  },
  methods: {
    handleFolderList() {
    },

    getFullPath(node, path) {
      if (node != null) {
        path = node.parent.data.name + path;
        return this.getFullPath(node.parent, path);
      } else {
        return path;
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

    handleNodeClick(e, node) {
      //   this.selectFolder = e.parent.name.slice(0, e.parent.name.length - 1);
      let path = (
        "/" + this.findPathByLeafId(e.name, [this.folderList]).join("")
      ).slice(
        0,
        this.findPathByLeafId(e.name, [this.folderList]).join("").length
      );
      //   console.log(path);
      this.selectFolder = path;
    },

    // 点击确定的回调
    confirmMove() {
      // 关闭选择框
      this.$emit("closeFolderDialog");
      // 将选中的文件夹名称发给父组件
      this.$emit("confirmMove", {
        path: this.selectFolder,
        type: this.moveType,
      });
    },
  },
  watch: {
    isFolderDialogShow(current) {
      if (current) {
        this.handleFolderList();
      }
    },
  },
  mounted() {},
};
</script>

<style scoped>
.FolderDialog /deep/ .el-dialog {
  border-radius: 10px;
}

.FolderDialog /deep/ .el-dialog__header {
  border-bottom: 1px solid #eee;
  padding: 10px 20px;
}

.FolderDialog /deep/ .el-dialog__title {
  font-size: 15px;
  color: rgb(95, 95, 95);
}

.FolderDialog /deep/ .el-dialog__body {
  height: 100px;
  overflow-y: scroll;
  padding: 15px;
}

.FolderDialog /deep/ .el-tree-node__content {
  height: 30px;
}

.FolderDialog /deep/ .el-tree-node__content:hover {
  background-color: #f0f0fc;
}

.FolderDialog /deep/ .is-current > .el-tree-node__content {
  background-color: #e6e6f5;
}

.cancel:hover {
  background-color: white;
  color: #358da5;
  border-color: #91d5ea;
}

.confirm {
  background-color: #429fb9;
  border-color: #399cc6;
  color: white;
}

.confirm:hover {
  background-color: #61c1db;
}

.FolderDialog /deep/ .el-button:focus {
  background-color: #5dcae8 !important;
  color: white !important;
  border-color: #1e82a9 !important;
}
</style>
