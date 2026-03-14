package com.light.core.utils.email;

import com.light.core.utils.spring.SpringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;


/**
 * 邮件工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class MailUtils {

    private static final JavaMailSender javaMailSender = SpringUtils.getBean(JavaMailSender.class);

    @Value("${spring.mail.username}")
    private String username;

    public String getUsername() {
        return username;
    }

    public static String getSendMail() {
        MailUtils bean = SpringUtils.getBean(MailUtils.class);
        return bean.getUsername();
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    public static void sendHtml(String to, String subject, String content, File... files) {
        // 创建 MimeMessage 对象
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(getSendMail()); // 发件人（需与配置中的 username 一致）
            helper.setTo(to);                   // 收件人
            helper.setSubject(subject);         // 邮件主题
            helper.setText(content, true);// 第二个参数为 true，表示内容为 HTML
            javaMailSender.send(message);  // 发送邮件
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    public static void sendHtml(Collection<String> tos, String subject, String content, File... files) {
        tos.forEach(to -> {
            sendHtml(to, subject, content, files);
        });
    }


    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    public static void sendText(Collection<String> tos, String subject, String content, File... files) {
        tos.forEach(to -> {
            sendText(to, subject, content, files);
        });
    }

    public static void sendText(String to, String subject, String content, File... files) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getSendMail()); // 发件人（需与配置中的 username 一致）
        message.setTo(to);                   // 收件人
        message.setSubject(subject);         // 邮件主题
        message.setText(content);

        // 发送邮件
        javaMailSender.send(message);// 邮件内容
    }
}
