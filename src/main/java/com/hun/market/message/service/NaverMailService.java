package com.hun.market.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverMailService implements MailService {

    private final Environment env;

//    @PostConstruct
//    public void init() throws Exception {
//        sendMail("lee_seunghun06@eland.co.kr", "제목", "내용");
//    }

    @Override
    public void sendMail(String to, String subject, String content) throws Exception {
        Email email = new SimpleEmail();
        email.setHostName("smtp.naver.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("aaddss639", env.getProperty("mail.pwd")));
        email.setSSL(true);
        email.setFrom("aaddss639@naver.com", "이승훈");
        email.setSubject(subject);
        email.setMsg(content);
        email.addTo(to, "INNOMARKET");
        email.send();
    }
}
