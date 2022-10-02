package com.example.customer_api.helper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration configuration;

    public void sendOTPMail(String mailTo, Map<String, String> model) {
       
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = configuration.getTemplate("otpEmail.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setTo(mailTo);
            helper.setFrom("benyademofordev.demo@gmail.com");
            helper.setSubject("Demo:รหัสเพื่อยืนยันการเข้าสู่ระบบ");
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Email has been sent to :" + mailTo);
        } catch (MessagingException | IOException | TemplateException e) {
            log.info("Email send failure to :" + mailTo);
        }
    }
}
