package com.chen.service.mapper;

import com.chen.service.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-07-18
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer registerCount(String day);
}
