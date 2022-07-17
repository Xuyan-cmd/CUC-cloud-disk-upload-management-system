package com.chen.service.service.impl;


import com.chen.service.service.MsmService;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MsmServiceImpl implements MsmService {

    public boolean send(String code, String phone) {
        System.out.println("333");
        // 需要发送短信的手机号码
        String[] phoneNumbers = {phone};
        int templateId = 952266;
        try {
            String[] params = {code};//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            SmsSingleSender ssender = new SmsSingleSender(1400517452, "9ca276a2e77b9aa4aa926714b1e841f7");
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0], templateId, params, "jen247s个人公众号", "", "");
            //System.out.println(result);
            return true;
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
            System.out.println("HTTPException");
            return false;
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
            System.out.println("JSONException");
            return false;
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
            System.out.println("IOException");
            return false;
        }
    }
}
