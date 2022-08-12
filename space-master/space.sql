/*
 Navicat Premium Data Transfer

 Source Server         : 11
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : 47.106.217.172:3306
 Source Schema         : space

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 2020.7.21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `mem_id` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户ID',
  `name` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名字',
  `type` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  `collection` tinyint(9) NULL DEFAULT 0 COMMENT '是否收藏',
  `f_dir` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/root',
  `filetype` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'file',
  `video_id` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` bigint(100) NULL DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ucenter_member
-- ----------------------------
DROP TABLE IF EXISTS `ucenter_member`;
CREATE TABLE `ucenter_member`  (
  `id` char(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员id',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `neicun` bigint(100) NULL DEFAULT NULL COMMENT '内存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_dir
-- ----------------------------
DROP TABLE IF EXISTS `user_dir`;
CREATE TABLE `user_dir`  (
  `mem_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mem_dir` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户目录结构',
  PRIMARY KEY (`mem_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Triggers structure for table ucenter_member
-- ----------------------------
DROP TRIGGER IF EXISTS `addUserDir`;
delimiter ;;
CREATE DEFINER = `space`@`%` TRIGGER `addUserDir` AFTER INSERT ON `ucenter_member` FOR EACH ROW BEGIN

       insert into user_dir values(NEW.id,'{"childrenList":[],"id":1,"name":"root/","parentId":0}');
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table ucenter_member
-- ----------------------------
DROP TRIGGER IF EXISTS `delUserDir`;
delimiter ;;
CREATE DEFINER = `space`@`%` TRIGGER `delUserDir` AFTER DELETE ON `ucenter_member` FOR EACH ROW BEGIN
    DELETE from user_dir WHERE mem_id =OLD.id;
      
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
