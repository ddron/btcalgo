package com.algo.btce.service;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.converter.json.MappingJackson2HttpMessageConverter.DEFAULT_CHARSET;

public class AuthService {
    protected Logger log = LoggerFactory.getLogger(getClass());

    private String key;
    private String baseUrl;

    private static Mac mac;
    private static AtomicInteger nonce = new AtomicInteger((int) (System.currentTimeMillis() / 1000));

    private static SecretKeySpec secretKeySpec;

    public AuthService(String key, String secret, String baseUrl) {
        this.key = key;
        this.baseUrl = baseUrl;

        log.debug("key: {}", key);
        log.debug("secret: {}", secret);

        // Create a new secret key
        //SecretKeySpec secretKeySpec;
        try {
            secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA512");
        } catch(UnsupportedEncodingException e) {
            log.error("Unsupported encoding exception: {}", e);
            throw new IllegalStateException(e);
        }

/*        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch(NoSuchAlgorithmException e) {
            log.error("No such algorithm exception: {}", e);
            throw new IllegalStateException(e);
        }

        // Init mac with key.
        try {
            mac.init(secretKeySpec);
        } catch(InvalidKeyException e) {
            log.error("Invalid key exception: {}", e);
            throw new IllegalStateException(e);
        }*/
    }

    protected <T> T auth(String method, Class<T> clazz) {
        return auth(method, null, clazz);
    }

    protected <T> T auth(String method, Map<String, String> args, Class<T> clazz) {
        log.debug("auth() invoked. method: {}, args: {}, clazz: {}", method, args, clazz);
        RestTemplate restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "json", DEFAULT_CHARSET),
                new MediaType("application", "*+json", DEFAULT_CHARSET),
                new MediaType("text", "html", DEFAULT_CHARSET)
        ));
        restTemplate.setMessageConverters(Arrays.asList(new AllEncompassingFormHttpMessageConverter(), jacksonConverter));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        if (args == null) {
            args = new HashMap<>();
        }

        body.add("method", method);
        body.add("nonce", String.valueOf(getNonce()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Add the key to the header lines.
        headers.add("Key", key);

        String postData = "";
        for (Map.Entry<String, String> entry : args.entrySet()) {
            if (postData.length() > 0) {
                postData += "&";
            }
            postData += entry.getKey() + "=" + entry.getValue();
            body.add(entry.getKey(), entry.getValue());
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch(NoSuchAlgorithmException e) {
            log.error("No such algorithm exception: {}", e);
            throw new IllegalStateException(e);
        }

        // Init mac with key.
        try {
            mac.init(secretKeySpec);
        } catch(InvalidKeyException e) {
            log.error("Invalid key exception: {}", e);
            throw new IllegalStateException(e);
        }

        // Encode the post data by the secret and encode the result as base64.
        try {
            mac.update(postData.getBytes("UTF-8"));
            headers.add("Sign", Hex.encodeHexString(mac.doFinal()));
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding exception: {}", e);
            throw new IllegalStateException(e);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        log.debug("Request: {}", request);

        T result = restTemplate.postForObject(baseUrl, request, clazz);
        log.debug("auth() completed. Result: {}", result);

        return result;
    }

    private long getNonce() {
        return nonce.getAndIncrement();
    }

}
