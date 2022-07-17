package com.chen.service.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.chen.service.entity.File;
import com.chen.service.entity.TreeNode;
import com.chen.service.entity.UcenterMember;
import com.chen.service.entity.UserDir;
import com.chen.service.excepyionhandler.SpaceException;
import com.chen.service.service.FileService;
import com.chen.service.service.OssService;
import com.chen.service.service.UcenterMemberService;
import com.chen.service.service.UserDirService;
import com.chen.service.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-06-06
 */
@RestController
@RequestMapping("/educenter/file")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService service;

    @Autowired
    private UcenterMemberService memberService;

    @Autowired
    private UserDirService userDirService;

    //根据名字模糊查询文件
    @ApiOperation(value = "根据名字模糊查询文件")
    @PostMapping("findFile/{memid}/{name}")
    public R findFile(@PathVariable String name, @PathVariable String memid) {
        List<File> fileList = service.getFindFile(memid, name);
        System.out.println(fileList);
        UserDir userDir = userDirService.getUserDir(memid);
        TreeNode treeNode = JSON.parseObject(userDir.getMemDir(), new TypeReference<TreeNode>() {
        });
        List list=new ArrayList();
        findTreeNode(treeNode, name,list);
        //System.out.println(list);
        return R.ok().data("fileList", fileList).data("list",list);
    }

    private static void findTreeNode(TreeNode treeNode, String name,List list1) {
        //System.out.println(treeNode);
        //System.out.println(list1);
        List<TreeNode> list=treeNode.getChildrenList();
        //System.out.println(list);
        if (list==null || list.isEmpty()){
            return;
        }
        else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().indexOf(name)>-1){
                    //System.out.println(list.get(i).getChildrenList());
                    list1.add(list.get(i));
                    List<TreeNode> list2=list.get(i).getChildrenList();
                    System.out.println(list2);
                  if (list2.size()>=1){
                      findTreeNode(list.get(i),name,list1);
                  }
                }else{
                    findTreeNode(list.get(i),name,list1);
                }
            }
        }

    }

    //添加文件的接口方法
    @ApiOperation(value = "添加文件的信息到数据库")
    @PostMapping("addFile")
    public R addFile(@RequestBody File file) {
            boolean save = service.save(file);
            if (save) {
                return R.ok().data("file", file);
            } else {
                return R.error();
            }

    }


    //查询当前用户下的所有文件
    @ApiOperation(value = "查询当前用户下的所有文件")
    @GetMapping("getAllFileInfo/{memId}")
    public R getAllFileInfo(@PathVariable String memId) {
        //System.out.println(memId);
        List<File> fileList = service.getAllFileInfo(memId);
        //System.out.println(files);
        return R.ok().data("fileList", fileList);
    }

    //根据文件id查询文件具体信息
    @ApiOperation(value = "根据文件id查询文件具体信息")
    @GetMapping("getFileInfo/{id}")
    public R getfileInfo(@PathVariable String id) {
        //System.out.println(memId);
        List<File> files = service.getFileInfo(id);
        //System.out.println(files);
        return R.ok().data("file", files);
    }

    //文件重命名
    @ApiOperation(value = "文件重命名")
    @PostMapping("updateFile/{id}/{name}")
    public R updateFile(@PathVariable String id, @PathVariable String name) {
        QueryWrapper<File> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        File one = service.getOne(wrapper);
        File file = new File();
        file.setId(id);
        file.setName(name);
        file.setSize(one.getSize());
        boolean update = service.updateById(file);
        if (update) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //文件收藏
    @ApiOperation(value = "文件收藏")
    @PostMapping("collectFile")
    public R collectionFile(@RequestParam("id") String[] id) {
        boolean flag = false;
        for (int i = 0; i < id.length; i++) {
            System.out.println(id[i]);
            File file = new File();
            file.setId(id[i]);
            file.setCollection(1);
            boolean update = service.updateById(file);
            if (update) {
                flag = true;
            }
        }
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //文件收藏
    @ApiOperation(value = "取消文件收藏")
    @PostMapping("cancelCollection")
    public R cancelCollection(@RequestParam("id") String[] id) {
        boolean flag = false;
        for (int i = 0; i < id.length; i++) {
            System.out.println(id[i]);
            File file = new File();
            file.setId(id[i]);
            file.setCollection(0);
            boolean update = service.updateById(file);
            if (update) {
                flag = true;
            }
        }
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据当前路径查询所有文件
    @ApiOperation(value = "根据当前路径查询所有文件")
    @PostMapping("getCurDirFiles/{id}")
    public R setDirStruct(@RequestBody String userDir, @PathVariable String id) {
        //System.out.println(userDir);
        List<File> files = service.getCurFiles(userDir, id);
        System.out.println(files);
        return R.ok().data("files", files);
    }

    //根据当前路径查询所有文件
    @ApiOperation(value = "多选文件移动")
    @PostMapping("fileMove")
    public R fileMove(@RequestBody String movingPath, @RequestParam("id") String[] id) {
        boolean flag = false;
        for (int i = 0; i < id.length; i++) {
            System.out.println(id[i]);
            File file = new File();
            file.setId(id[i]);
            file.setFDir(movingPath);
            boolean update = service.updateById(file);
            if (update) {
                flag = true;
            }
        }
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

