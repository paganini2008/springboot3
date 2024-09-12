package com.fred.api.utils;

import java.util.Date;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fred.api.UserErrorCodes;
import com.fred.common.utils.BizException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: Send email by spring mail component
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Slf4j
@Component
public class SpringEmailService {

    @Qualifier("textTemplate")
    @Autowired
    private MessageTemplate textTemplate;

    @Qualifier("htmlTemplate")
    @Autowired
    private MessageTemplate htmlTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendPlainTextEmail(EmailMessage emailMessage) {
        checkRequiredParameters(emailMessage);
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(emailMessage.getTo());
            if (StringUtils.isNotBlank(emailMessage.getFrom())) {
                simpleMailMessage.setFrom(emailMessage.getFrom());
            }
            if (StringUtils.isNotBlank(emailMessage.getReplyTo())) {
                simpleMailMessage.setReplyTo(emailMessage.getReplyTo());
            }
            if (ArrayUtils.isNotEmpty(emailMessage.getCc())) {
                simpleMailMessage.setCc(emailMessage.getCc());
            }
            if (ArrayUtils.isNotEmpty(emailMessage.getBcc())) {
                simpleMailMessage.setBcc(emailMessage.getBcc());
            }
            simpleMailMessage.setSubject(emailMessage.getSubject());
            String text = textTemplate.loadContent(emailMessage.getSubject(),
                    emailMessage.getTemplate(), emailMessage.getVariables());
            simpleMailMessage.setText(text);
            simpleMailMessage.setSentDate(new Date());
            javaMailSender.send(simpleMailMessage);
            if (log.isInfoEnabled()) {
                log.info("Send mail to '{}' successfully.", String.join(",", emailMessage.getTo()));
            }
        } catch (MessagingException e) {
            throw new BizException(UserErrorCodes.EMAIL_SETTING_FAULT, e);
        } catch (MailException e) {
            throw new BizException(UserErrorCodes.EMAIL_SENDING_FAULT, e);
        } catch (Exception e) {
            throw new BizException(UserErrorCodes.EMAIL_SENDING_FAULT, e);
        }
    }

    @Async
    public void sendHtmlEmail(EmailMessage emailMessage) {
        checkRequiredParameters(emailMessage);
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setTo(emailMessage.getTo());
            if (StringUtils.isNotBlank(emailMessage.getFrom())) {
                mimeMessageHelper.setFrom(emailMessage.getFrom());
            }
            if (StringUtils.isNotBlank(emailMessage.getReplyTo())) {
                mimeMessageHelper.setReplyTo(emailMessage.getReplyTo());
            }
            if (ArrayUtils.isNotEmpty(emailMessage.getCc())) {
                mimeMessageHelper.setCc(emailMessage.getCc());
            }
            if (ArrayUtils.isNotEmpty(emailMessage.getBcc())) {
                mimeMessageHelper.setBcc(emailMessage.getBcc());
            }
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            String text = htmlTemplate.loadContent(emailMessage.getSubject(),
                    emailMessage.getTemplate(), emailMessage.getVariables());
            if (MapUtils.isNotEmpty(emailMessage.getAttachments())) {
                emailMessage.getAttachments().entrySet().forEach(att -> {
                    try {
                        mimeMessageHelper.addAttachment(att.getKey(), att.getValue());
                    } catch (MessagingException e) {
                        if (log.isErrorEnabled()) {
                            log.info(e.getMessage(), e);
                        }
                    }
                });
            }
            mimeMessageHelper.setSentDate(new Date());
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMailMessage);
            if (log.isInfoEnabled()) {
                log.info("Send mail to '{}' successfully.", String.join(",", emailMessage.getTo()));
            }
        } catch (MessagingException e) {
            throw new BizException(UserErrorCodes.EMAIL_SETTING_FAULT, e);
        } catch (MailException e) {
            throw new BizException(UserErrorCodes.EMAIL_SENDING_FAULT, e);
        } catch (Exception e) {
            throw new BizException(UserErrorCodes.EMAIL_SENDING_FAULT, e);
        }
    }

    private void checkRequiredParameters(EmailMessage emailMessage) {
        Assert.hasText(emailMessage.getSubject(), "Email subject must be required");
        Assert.hasText(emailMessage.getFrom(), "Email sender must be required");
        Assert.notEmpty(emailMessage.getTo(), "Email receiver must be required");
        Assert.hasText(emailMessage.getTemplate(), "Email template content must be required");
    }
}
