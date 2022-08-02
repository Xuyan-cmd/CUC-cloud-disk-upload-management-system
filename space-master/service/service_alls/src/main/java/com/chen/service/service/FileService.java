package com.chen.service.service;

import com.chen.service.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-07-19
 */
public interface FileService extends IService<File> {

    List<File> getAllFileInfo(String memId);

    File getAllFileVedioId(String vedioId);

    List<File> getFileInfo(String id);

    List<File> getCurFiles(String userDir,String id);

    File getFiles(String id);

    List<File> getFindFile(String memid,String name);

    List<File> getList(String memid, String url,int result,String name);
}
