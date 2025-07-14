package com.qixidi.business.task;


import com.alibaba.fastjson.JSONObject;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.email.MailUtils;
import com.light.redission.utils.RedisUtils;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:掘金签到
 * @Author: xph
 * @Date: 2025-02-07 17:23
 */
@Component
@RestController
public class ToolTasks {
    public static final Logger logger = LoggerFactory.getLogger(ToolTasks.class);

    @Scheduled(cron = "0 0 6,8 * * ?")//每天凌晨6点、8点执行
//    @Scheduled(cron = "0 */2 * * * ?")
    public void jueJin() throws IOException, MessagingException {
        logger.info("掘金签到开始");
        String cacheObject = RedisUtils.getCacheObject(RedisBusinessKeyEnums.JUEJING_TOKEN.getKey());
        if (cacheObject != null) {
            ofJueJin(cacheObject);
        }
        logger.info("掘金签到结束");
    }

    /**
     * 掘金签到
     *
     * @param cookie cookie
     * @throws IOException io
     */
    public void ofJueJin(String cookie) throws IOException, MessagingException {
        // 签到
        Map<String, String> map = apiQuest("https://api.juejin.cn/growth_api/v1/check_in", cookie, "POST");
        String error = map.get("err_msg");
        if (map.containsKey("err_msg") && !Objects.equals(error, "success")) {
            logger.error("签到失败：{}", error);
            if (error.contains("must login")) {
                MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "掘金签到失败-登录过期", "掘金签到失败-登录过期");
                return;
            }
            if (LocalDateTime.now().getHour() == 8) {
                logger.error("重复签到：{}", error);
                return;
            }
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "掘金签到失败", error);
            return;
        }
        // 抽奖
        apiQuest("https://api.juejin.cn/growth_api/v1/lottery/draw", cookie, "POST");
    }

    /**
     * 接口请求
     *
     * @param url    请求地址
     * @param cookie cookie
     * @param method 请求方式
     * @return 返回值
     * @throws IOException io
     */
    public static Map<String, String> apiQuest(String url, String cookie, String method) throws IOException {
        URL obj = new URL(url + "?aid=&uuid=");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        OutputStreamWriter wr = null;
        BufferedReader in = null;
        try {
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Cookie", "is_staff_user=false; store-region=cn-sc; store-region-src=uid; " + cookie);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setDoOutput(true);
            con.setDoInput(true);
            wr = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8);
            wr.write("{}");
            wr.flush();
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    logger.error("关闭输出流时出错", e);
                }
            }
        }
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return JSONObject.parseObject(response.toString(), Map.class);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("关闭输入流时出错", e);
                }
            }
            con.disconnect();
        }
    }
}
