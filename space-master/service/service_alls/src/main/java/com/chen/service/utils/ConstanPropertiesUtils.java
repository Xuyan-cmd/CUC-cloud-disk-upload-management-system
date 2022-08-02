package com.chen.service.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口一个方法
@Component
public class ConstanPropertiesUtils implements InitializingBean {

    //读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    //定义公开静态常量
    public static String END_POIND;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POIND = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
    // 将OSS 客户端交给Spring容器托管
    @Bean
    public OSS OSSClient() {
        return new OSSClient(endpoint, keyId, keySecret);
    }
}
