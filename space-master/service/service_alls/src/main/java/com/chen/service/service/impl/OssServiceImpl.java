package com.chen.service.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.service.entity.File;
import com.chen.service.service.FileService;
import com.chen.service.service.OssService;
import com.chen.service.service.UcenterMemberService;
import com.chen.service.utils.ConstanPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
//
    @Autowired
    private OSS ossClient;// 注入阿里云oss文件服务器客户端
//    @Autowired
//    private AliyunOssConfig aliyunOssConfig;// 注入阿里云OSS基本配置类

    @Autowired
    private FileService fileService;

    @Autowired
    private UcenterMemberService memberService;

    //上传头像到oss
    @Override
    public File upload(MultipartFile file,String catalogue) {
        // 工具类获取值
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            File file1=new File();
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件类型
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String name=originalFilename.substring(0, originalFilename.indexOf("."));
            String type = fileType.substring(1);
            if(type.equals("bmp")||type.equals("mp3")||type.equals("jpg")||type.equals("jpeg")||type.equals("png")||type.equals("mp3")||type.equals("tif")||type.equals("gif")
                    ||type.equals("pcx")||type.equals("tga")||type.equals("exif")||type.equals("fpx")||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                    ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                    ||type.equals("WMF")||type.equals("webp")||type.equals("mp3")||type.equals("avif")){
                file1.setFiletype("image");
            }

            //2 把文件按照日期进行分类
            //获取当前日期
            //   2019/11/12
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            originalFilename = datePath + "/" + originalFilename;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, originalFilename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;
            file1.setName(name);
            file1.setType(type);
            file1.setUrl(url);
            file1.setFDir(catalogue);
            file1.setSize(file.getSize());
            return file1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 文件删除
     * @param: objectName
     * @return: java.lang.String
     * @create: 2020/10/31 16:50
     * @author: csp1999
     */
    public String delete(String id) {
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;
        // 日期目录
        // 注意，这里虽然写成这种固定获取日期目录的形式，逻辑上确实存在问题，但是实际上，filePath的日期目录应该是从数据库查询的
        QueryWrapper<File> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        File fileServiceOne = fileService.getOne(wrapper);
        String name = fileServiceOne.getName();
        //System.out.println(fileServiceOne);
        boolean remove = fileService.remove(wrapper);
        if (remove==true){
            System.out.println("删除成功");
        }
        else{
            System.out.println("删除失败");
        }
        SimpleDateFormat data = new SimpleDateFormat("yyyy/MM/dd");
        Date gmtCreate = fileServiceOne.getGmtCreate();
        data.format(gmtCreate);
        //String filePath = new DateTime().toString("yyyy/MM/dd");
        String filePath = data.toString();
        //System.out.println(filePath);
        try {
            /**
             * 注意：在实际项目中，不需要删除OSS文件服务器中的文件，
             * 只需要删除数据库存储的文件路径即可！
             */
            // 建议在方法中创建OSSClient 而不是使用@Bean注入，不然容易出现Connection pool shut down
            OSSClient ossClient = new OSSClient(endpoint,
                    accessKeyId, accessKeySecret);
            // 根据BucketName,filetName删除文件
            // 删除目录中的文件，如果是最后一个文件fileoath目录会被删除。
            String fileKey =filePath + "/" + name;
            ossClient.deleteObject(bucketName, fileKey);
            try {
            } finally {
                ossClient.shutdown();
            }
            System.out.println("文件删除！");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public String uploadfile(MultipartFile file) {
        try {
            //accessKeyId, accessKeySecret
            //fileName：上传文件原始名称
            // 01.03.09.mp4
            String fileName = file.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：上传文件输入流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstanPropertiesUtils.ACCESS_KEY_ID, ConstanPropertiesUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
                System.out.println(response.getRequestId());
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String deleteVideo(String id) {
        QueryWrapper<File> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        File file = fileService.getOne(wrapper);
        boolean b = fileService.remove(wrapper);
        System.out.println(file);
        return file.getVideoId();
    }

    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = ConstanPropertiesUtils.END_POIND;
        String accessKeyId = ConstanPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstanPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstanPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();


            //2 把文件按照日期进行分类
            //获取当前日期
            //   2019/11/12
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            fileName = datePath + "/" + fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
