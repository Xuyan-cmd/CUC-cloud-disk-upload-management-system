package com.chen.service.service;

import com.chen.service.entity.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface OssService {
    File upload(MultipartFile file,String catalogue);

    String delete(String id);

    String uploadfile(MultipartFile file);

    String deleteVideo(String id);

    String uploadFileAvatar(MultipartFile file);
}
