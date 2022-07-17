package com.chen.service.controller;


import com.chen.service.service.MsmService;
import com.chen.service.utils.R;
import com.chen.service.utils.RandomUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
@CrossOrigin
public class MsmController {

    private final MsmService msmService;

    private final RedisTemplate<String, String> redisTemplate;

    public MsmController(MsmService msmService, RedisTemplate<String, String> redisTemplate) {
        this.msmService = msmService;
        this.redisTemplate = redisTemplate;
    }

    @ApiOperation(value = "发送验证码")
    @GetMapping("send/{phone}")
    public R sesdMsm(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        //System.out.println(code + "111");
        if (!StringUtils.isEmpty(code)) {
            //System.out.println("2");
            return R.ok();
        }
        //生成随机验证码
        code = RandomUtil.getFourBitRandom();
        boolean isSend = msmService.send(code, phone);
        if (isSend) {
            //发送成功放到redis里面
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }
}
