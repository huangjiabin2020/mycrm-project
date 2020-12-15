package com.binge.common.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
@Component
public class EmailSender {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${mail.from}")
    private String from;
    @Value("${mail.subject}")
    private String subject;
    @Value("${mail.subject1}")
    private String activeSubject;

    public void sendEmail(String userEmailAddr, String htmlStr) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(from);
            helper.setSubject("新用户信息");
            helper.setTo(userEmailAddr);
            helper.setText(htmlStr, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
