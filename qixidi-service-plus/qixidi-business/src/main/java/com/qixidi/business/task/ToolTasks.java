package com.qixidi.business.task;


import com.alibaba.fastjson.JSONObject;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.email.MailUtils;
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

    @Scheduled(cron = "0 0 1,5 * * ?")
//    @Scheduled(cron = "0 */2 * * * ?")
    public void jueJin() throws IOException, MessagingException {
        logger.info("掘金签到开始");
        ofJueJin("__tea_cookie_tokens_2608=%257B%2522web_id%2522%253A%25227432535300213622299%2522%252C%2522user_unique_id%2522%253A%25227432535300213622299%2522%252C%2522timestamp%2522%253A1730521997513%257D; sid_guard=7c32df1c76e72fd258616bda809c2591%7C1730522008%7C31536000%7CSun%2C+02-Nov-2025+04%3A33%3A28+GMT; uid_tt=4116588e8e5ebc2e3ea277da28f3ab07; uid_tt_ss=4116588e8e5ebc2e3ea277da28f3ab07; sid_tt=7c32df1c76e72fd258616bda809c2591; sessionid=7c32df1c76e72fd258616bda809c2591; sessionid_ss=7c32df1c76e72fd258616bda809c2591; is_staff_user=false; sid_ucp_v1=1.0.0-KGY5NTVlMjA4NWRjMWFjYzQxY2UwZGI3ZTk2ZGQzZDRmM2RkZmUyZDEKFgi96vCrxYzKAhCY15a5BhiwFDgIQAsaAmxxIiA3YzMyZGYxYzc2ZTcyZmQyNTg2MTZiZGE4MDljMjU5MQ; ssid_ucp_v1=1.0.0-KGY5NTVlMjA4NWRjMWFjYzQxY2UwZGI3ZTk2ZGQzZDRmM2RkZmUyZDEKFgi96vCrxYzKAhCY15a5BhiwFDgIQAsaAmxxIiA3YzMyZGYxYzc2ZTcyZmQyNTg2MTZiZGE4MDljMjU5MQ; store-region=cn-sc; store-region-src=uid; _ga=GA1.2.2129623950.1730522008; _ga_S695FMNGPJ=GS1.2.1731204847.2.0.1731204847.60.0.0; _tea_utm_cache_2018={%22utm_source%22:%22push%22%2C%22utm_medium%22:%22juejin%22%2C%22utm_campaign%22:%22techcall%22}; _tea_utm_cache_2608={%22utm_source%22:%22push%22%2C%22utm_medium%22:%22juejin%22%2C%22utm_campaign%22:%22techcall%22}; csrf_session_id=47d58593b79d6e601e41daaec84c0a5a");
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
            if (LocalDateTime.now().getHour() == 5)
                MailUtils.sendText(SystemConstant.AdministratorMailboxList, "掘金签到失败", "掘金签到失败");
            if (error.contains("must login")) {
                MailUtils.sendText(SystemConstant.AdministratorMailboxList, "掘金签到失败-登录过期", "掘金签到失败-登录过期");
            }
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
