package com.chen.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.service.entity.File;
import com.chen.service.entity.TreeNode;
import com.chen.service.entity.UserDir;
import com.chen.service.excepyionhandler.SpaceException;
import com.chen.service.service.FileService;
import com.chen.service.service.UserDirService;
import com.chen.service.utils.R;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.Tree;

import java.util.*;

@RestController
@RequestMapping("/educenter/dir")
@CrossOrigin
public class UserDirController {
    @Autowired
    private UserDirService userDirService;
    @Autowired
    private FileService fileService;

    int result=1;

    /**
     * 获取当前目录下的文件列表
     */
    @ApiOperation(value = "获取当前目录下的文件列表")
    @GetMapping("getUserDir/{id}")
    public R getDirStruct(@PathVariable String id) {
        System.out.println(id);
        UserDir dir = userDirService.getUserDir(id);
        //System.out.println(files);
        return R.ok().data("dir", dir);
    }

    @ApiOperation(value = "根据传入的路径、名字、和父文件夹的id新建文件夹")
    @PostMapping("setUserDir/{memid}/{name}/{id}")
    public R setDirStruct(@PathVariable String memid, @PathVariable String name, @PathVariable long id) {
        UserDir userDir = userDirService.getUserDir(memid);
        TreeNode treeNode = JSON.parseObject(userDir.getMemDir(), new TypeReference<TreeNode>() {
        });
        //System.out.println(treeNode);
        TreeNode treeNode1 = new TreeNode();
        treeNode1.setName(name + "/");
        treeNode1.setParentId(id);
        treeNode1.setChildrenList(new ArrayList<>());
        insert(treeNode, id, treeNode1);
            System.out.println(treeNode);
            String s = JSONObject.toJSONString(treeNode);
            System.out.println(s);
            userDir.setMemDir(s);
            //String s=JSONObject.toJSONString(l);
            //treeNode.setChildrenList(list);
            int ret = userDirService.setUserDir(userDir);
            if (ret > 0) {
                return R.ok().data("dir", userDir);
            }
            //System.out.println(files);
            return R.error().data("dir", null);
    }

    @ApiOperation(value = "根据传入用户memid、目录路径url、和父文件夹id的删除文件夹")
    @PostMapping("deleteDirStruct/{memid}/{id}")
    public R deleteDirStruct(@PathVariable String memid, @PathVariable long id,@RequestBody String url) {
        UserDir userDir = userDirService.getUserDir(memid);
        TreeNode treeNode = JSON.parseObject(userDir.getMemDir(), new TypeReference<TreeNode>() {
        });
        boolean i=userDirService.deleteStruct(memid,url);
        System.out.println(i);
        if (i){
            StringBuffer sb=new StringBuffer();
            delete(treeNode, id,sb);
            //System.out.println(sb);
            String s = JSONObject.toJSONString(treeNode);
            userDir.setMemDir(s);
            userDirService.setUserDir(userDir);
            return R.ok();
        }else {
            return R.error();
        }
    }

    public static void insert(TreeNode treeNode, long id, TreeNode newNode) {
        List<TreeNode> list = treeNode.getChildrenList();
        List arrayList=new ArrayList();
        for (int i=0;i<list.size();i++){
            arrayList.add(list.get(i).getName());
        }
        arrayList.add(newNode.getName());
        System.out.println(arrayList);
        HashSet set=new HashSet<>(arrayList);
        Boolean result=set.size()==arrayList.size()?true:false;
        if (result==false){
            System.out.println("名字不可重复");
            throw new SpaceException(20001, "名字不可重复");
        }
        if (id==treeNode.getId()) {
                treeNode.getChildrenList().add(newNode);
                return;
        }
        //System.out.println(list.size());
        if (list == null || list.isEmpty()) {
            return;                            //若该结点 的子结点集合为空 返回
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (result==true){
                    insert(list.get(i), id, newNode);
                }
            }
        }
    }

    public void delete(TreeNode treeNode, long id,StringBuffer sb) {
        sb.append("/").append(treeNode.getName());
        List<TreeNode> list = treeNode.getChildrenList();
        if (list == null || list.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (id==list.get(i).getId()) {
                    list.remove(i);
                    delete(new TreeNode(), id,sb);
                    break;
                } else {
                    delete(list.get(i), id,sb);
                }
            }
        }
    }


    @PostMapping("updateDirStruct/{memid}/{name}/{id}")
    public R updateDirStruct(@PathVariable String memid,@PathVariable String name,@PathVariable long id,@RequestBody String url){
        UserDir userDir = userDirService.getUserDir(memid);
        TreeNode treeNode = JSON.parseObject(userDir.getMemDir(), new TypeReference<TreeNode>() {
        });
        update(treeNode, id, name,1);
        System.out.println(result);
        String s=JSONObject.toJSONString(treeNode);
        userDir.setMemDir(s);
        List<File> list=fileService.getList(memid,url,result,name);
        for (int i = 0; i < list.size(); i++) {
            String id1 = list.get(i).getId();
            File file=new File();
            file.setId(id1);
            file.setSize(list.get(i).getSize());
            file.setFDir(list.get(i).getFDir());
            fileService.updateById(file);
        }
        int i = userDirService.setUserDir(userDir);
        if (i>0){
            return R.ok().data("s",treeNode);
        }else {
            return R.ok();
        }
    }
    public void update(TreeNode treeNode, long id, String name,int d) {
        //result=d;
        //System.out.println(result);
        if (treeNode.getId()==id){
            treeNode.setName(name+"/");
           // System.out.println(d);
            result=d;
            return;
        }
        List<TreeNode> sonList = treeNode.getChildrenList();
        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                //result++;
                update(sonList.get(i),id,name,d+1);
            }
        }
    }

}
