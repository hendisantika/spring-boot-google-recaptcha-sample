package com.hendisantika.recaptchav3.config;

import com.hendisantika.recaptchav3.dto.ReCaptchaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

@Slf4j
public class ReCaptchaV3Handler {
    private final String secretKey = "6Lc4JNUbAAAAANKqiE4UXytmsQw35UcHkzAScS_o";
    private final String serverAddress = "https://www.google.com/recaptcha/api/siteverify";

    public float verify(String recaptchaFormResponse) throws InvalidReCaptchaTokenException {
        log.info("ReCaptcha v3 called.......");
        log.info("g-recaptcha-response: " + recaptchaFormResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", recaptchaFormResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ReCaptchaResponse response = restTemplate.postForObject(
                serverAddress, request, ReCaptchaResponse.class);


        log.info("ReCaptcha response: \n");

        log.info("Success: " + response.isSuccess());
        log.info("Action: " + response.getAction());
        log.info("Hostname: " + response.getHostname());
        log.info("Score: " + response.getScore());
        log.info("Challenge Timestamp: " + response.getChallengeTs());

        if (response.getErrorCodes() != null) {
            log.info("Error codes: ");
            for (String errorCode : response.getErrorCodes()) {
                log.info("\t" + errorCode);
            }
        }

        if (!response.isSuccess()) {
            throw new InvalidReCaptchaTokenException("Invalid ReCaptha. Please check site");
        }
        // return 0.4f;
        return response.getScore();
    }

}
