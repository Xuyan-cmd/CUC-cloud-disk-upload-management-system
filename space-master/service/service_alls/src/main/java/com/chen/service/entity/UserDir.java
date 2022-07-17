package com.chen.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="用户目录对象", description="")
public class UserDir {

    private static final long serialVersionUID = 1L;

    @TableId(value = "mem_id",type = IdType.ID_WORKER_STR)
    private String memId;

    @TableField("mem_dir")
    private String memDir;
}
