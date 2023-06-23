package org.zamzam.organizationservice.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailSender {
    private final JavaMailSender javaMailSender;

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl (JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }


    public void sendToOwner (String receiver, String sender, String verificationToken) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare (MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setTo(receiver);
                messageHelper.setFrom(sender);
                messageHelper.setSubject("Please confirm your email address");
                messageHelper.setText("Please click the link below to confirm your email address:\n\n"
                        + "http://yourapp.com/confirm-email?token=" + verificationToken);

            }
        };
        this.javaMailSender.send(preparator);
    }
}
