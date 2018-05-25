package com.nzgreens.common.utils;

import java.util.HashMap;
import java.util.Map;

public class FGSmsUtil
{
    
    
    /**
     *
     * @param mobile 需要发送的手机号码，多个使用,分割
     * @param templateId  模版ID
     * @param params 参数内容
     * @param fgUesrName 账号
     * @param fgPassword 密码
     * @param fgSignId 签名ID
     * @param fgSendTemplateUrl 请求地址
     * @return
     * @throws Exception
     */
    public static String feigeSendSmsOneByTemplate(String mobile,
                                           String templateId,
                                           String[] params,
                                           String fgUesrName,
                                           String fgPassword,
                                           String fgSignId,
                                           String fgSendTemplateUrl)throws Exception{
        String method="post";
        Map<String,String> map=new HashMap<>();
        map.put("Account", fgUesrName);
        map.put("pwd", fgPassword);
        map.put("Mobile",mobile);
        map.put("Content",String.join("||", params));
        map.put("SignId", fgSignId);
        map.put("TemplateId", templateId);
        try {
             return HttpClientUtils.executeS(map, fgSendTemplateUrl, method);
        } catch (Exception e) {
            throw e;
        }
        
    }
}
