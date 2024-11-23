package com.aurora.common.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsSendingConfig {

    /**
     * 访问的域名
     */
    private String endpoint;
    /**
     * AccessKey ID
     */
    private String accessKeyId;
    /**
     * accessKeySecret
     */
    private String accessKeySecret;
    /**
     * 发送方名称
     */
    private String signName;
    /**
     * 短信模板id
     */
    private String templateCode = "SMS_460730282";

    /**
     * 初始化短信发送配置
     *
     * @return
     * @throws Exception
     */
    public Client createClient() throws Exception {
        Config config = new com.aliyun.teaopenapi.models.Config()
            .setAccessKeyId(accessKeyId)
            .setAccessKeySecret(accessKeySecret);
        config.endpoint = endpoint;
        return new Client(config);
    }

    public static void main(String[] args) throws Exception {
        if (!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
            "1123")) {
            //手机号码格式错误
            throw new Exception("手机号格式错误");
        }
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @param code        验证嘛
     * @return
     */
    public Boolean SendSmsCode(String phoneNumber, String code) throws Exception {
        if (!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", phoneNumber)) {
            //手机号码格式错误
            throw new Exception("手机号格式错误");
        }
        Client client = createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
            .setSignName(signName)
            .setPhoneNumbers(phoneNumber)
            .setTemplateParam("{code:" + code + "}")
            .setTemplateCode(templateCode);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
        return true;
    }
}
