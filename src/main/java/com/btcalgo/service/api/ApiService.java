package com.btcalgo.service.api;

import com.btcalgo.service.api.templates.TickerTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ApiService {
    protected Logger log = LoggerFactory.getLogger(getClass());

    private String key;
    private String authBaseUrl;
    private String publicBaseUrl;

    private static Mac mac;
    /*
    private static final long START_MILLIS = 1356998400000L; // Jan 1st, 2013 in milliseconds
    private static final AtomicInteger nonce = new AtomicInteger((int) ((System.currentTimeMillis() - START_MILLIS) / 250));
    */
    private static AtomicInteger nonce = new AtomicInteger((int) (System.currentTimeMillis() / 1000));

    private Gson gson = new GsonBuilder().create();

    public ApiService(String key, String secret, String authBaseUrl, String publicBaseUrl) {
        this.key = key;
        this.authBaseUrl = authBaseUrl;
        this.publicBaseUrl = publicBaseUrl;

        log.debug("key: {}", key);
        log.debug("secret: {}", secret);

        // Create a new secret key
        SecretKeySpec secretKeySpec;
        try {
            secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA512");
        } catch(UnsupportedEncodingException e) {
            log.error("Unsupported encoding exception: ", e);
            throw new IllegalStateException(e);
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch(NoSuchAlgorithmException e) {
            log.error("No such algorithm exception: ", e);
            throw new IllegalStateException(e);
        }

        // Init mac with key.
        try {
            mac.init(secretKeySpec);
        } catch(InvalidKeyException e) {
            log.error("Invalid key exception: ", e);
            throw new IllegalStateException(e);
        }
    }

    public TickerTemplate getTicker(String symbol) {
        return request(symbol, "ticker", TickerTemplate.class);
    }

    private <T> T request(String symbol, String method, Class<T> clazz) {
        log.debug("request() invoked. symbol: {}, method: {}", symbol, method);

        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(publicBaseUrl + symbol + "/" + method);
            URLConnection c = url.openConnection();
            c.setUseCaches(false);

            // read
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
        } catch (IOException e) {
            log.error("Request error: ", e);
            return null;
        }

        T result = gson.fromJson(response.toString(), clazz);
        log.debug("request() completed. Result: {}", result);

        return result;
    }

    public <T> T auth(String method, Class<T> clazz) {
        return auth(method, null, clazz);
    }

    public <T> T auth(String method, Map<String, String> args, Class<T> clazz) {
        log.debug("auth() invoked. method: {}, args: {}", method, args);

        // add method and nonce to args
        if (args == null) {
            args = new HashMap<>();
        }

        args.put("method", method);
        args.put("nonce", String.valueOf(nonce.getAndIncrement()));

        // create url form encoded post data
        String postData = "";
        for (Map.Entry<String, String> entry : args.entrySet()) {
            if (postData.length() > 0) {
                postData += "&";
            }
            postData += entry.getKey() + "=" + entry.getValue();
        }

        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(authBaseUrl);
            URLConnection c = url.openConnection();
            c.setUseCaches(false);
            c.setDoOutput(true);

            c.setRequestProperty("Key", key);
            c.setRequestProperty("Sign", toHex(mac.doFinal(postData.getBytes("UTF-8"))));
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // write
            OutputStreamWriter out = new OutputStreamWriter(c.getOutputStream());
            out.write(postData);
            out.close();

            // read
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

        } catch (IOException e) {
            log.error("Authentication request error: ", e);
            return null;
        }

        T result = gson.fromJson(response.toString(), clazz);
        log.debug("auth() completed. Result: {}", result);

        return result;
    }

    private String toHex(byte[] b) throws UnsupportedEncodingException {
        return String.format("%040x", new BigInteger(1,b));
    }

}
