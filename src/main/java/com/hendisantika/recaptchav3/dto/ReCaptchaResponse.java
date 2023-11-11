package com.hendisantika.recaptchav3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha-sample
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/11/23
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ReCaptchaResponse {
    private boolean success;
    private String hostname;
    private String action;
    private float score;
    private String challengeTs;

    @JsonProperty("error-codes")
    private String[] errorCodes;
}
