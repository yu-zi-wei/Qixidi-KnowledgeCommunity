package com.light.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "lipay")
public class AlipayConfig {
    /**
     * APPID
     */
    private String appId;
    /**
     * 应用私钥
     */
    private String appPrivateKey;
    /**
     * 阿里公钥
     */
    private String alipayPublicKey;
    /**
     * 阿里调用我们的地址【内网穿透】
     */
    private String notifyUrl;
    /**
     * 支付成功的跳转地址
     */
    private String returnUrl;
    /**
     * 网关地址
     */
    private String serverUrl;
    /**
     * 数据格式
     */
    private String format;
    /**
     * 编码格式
     */
    private String charset;
    /**
     * 签名方式,签名算法
     */
    private String signType;
}
