package com.hendisantika.recaptchav3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha-sample
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/11/23
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class OTPController {

    @PostMapping("/otp_login")
    public String otpForm() {
        return "otp_login";
    }
}
