package com.hendisantika.recaptchav3.config;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha-sample
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/11/23
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */
public class InvalidReCaptchaTokenException extends Exception {
    public InvalidReCaptchaTokenException(String message) {
        super(message);
    }
}
