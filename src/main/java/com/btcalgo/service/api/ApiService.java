package com.btcalgo.service.api;

import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.templates.LoginTemplate;
import com.btcalgo.service.api.templates.NewOrderTemplate;
import com.btcalgo.service.api.templates.TickerTemplate;
import com.google.common.base.Strings;
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

    private static final int ATTEMPTS_NUMBER = 4;

    private static Mac mac;
    /*
    private static final long START_MILLIS = 1356998400000L; // Jan 1st, 2013 in milliseconds
    private static final AtomicInteger nonce = new AtomicInteger((int) ((System.currentTimeMillis() - START_MILLIS) / 250));
    */
    private static AtomicInteger nonce = new AtomicInteger((int) (System.currentTimeMillis() / 1000));

    private Gson gson = new GsonBuilder().create();

    public ApiService(String key, String secret, String authBaseUrl, String publicBaseUrl) {
        this.authBaseUrl = authBaseUrl;
        this.publicBaseUrl = publicBaseUrl;

        initKeys(key, secret);
    }

    public synchronized void updateKeys(String key, String secret) {
        initKeys(key, secret);
    }

    private void initKeys(String key, String secret) {
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(secret)) {
            return;
        }

        this.key = key;

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

    /////////////////////////////////////////
    // NON AUTH REQUESTS START             //
    /////////////////////////////////////////


    public TickerTemplate getTicker(String symbol) {
        return request(symbol, "ticker", TickerTemplate.class);
    }

    private <T> T request(String symbol, String method, Class<T> clazz) {
        T result;
        int i = 0;
        do {
            log.debug("request() invoked. Attempt: {}. symbol: {}, method: {}", i, symbol, method);
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
                result = gson.fromJson(response.toString(), clazz);
            } catch (IOException e) {
                log.error("Request error: ", e);
                result = null;
            }
        } while ((++i < ATTEMPTS_NUMBER) && result == null);

        log.debug("request() completed. Attempts taken: {}. Result: {}", i, result);
        return result;
    }

    /////////////////////////////////////////
    // NON AUTH REQUESTS END               //
    /////////////////////////////////////////


    /////////////////////////////////////////
    //     AUTH REQUESTS START             //
    /////////////////////////////////////////

    public NewOrderTemplate sendNewOrder(SymbolEnum symbol, Direction direction, double price, double amount) {
        Map<String,String> args = new HashMap<>() ;
        args.put("pair", symbol.getValue()) ;
        args.put("type", direction.getApiValue()) ;
        args.put("rate", String.valueOf(price)) ;
        args.put("amount", String.valueOf(amount)) ;

        NewOrderTemplate result = auth("Trade", args, NewOrderTemplate.class);

        if (result.isSuccess()) {
            log.info("order was sent to market. Result: {}");
        } else {
            log.error("order was NOT sent to market. Result: {}");
        }

        return result;
    }

    public <T extends LoginTemplate> T auth(String method, Class<T> clazz) {
        return auth(method, null, clazz);
    }

    public <T extends LoginTemplate> T auth(String method, Map<String, String> args, Class<T> clazz) {
        log.debug("auth() invoked. method: {}, args: {}", method, args);

        // add method and nonce to args
        if (args == null) {
            args = new HashMap<>();
        }

        T result;
        int i = 0;
        do {
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
                result = gson.fromJson(response.toString(), clazz);
            } catch (IOException e) {
                log.error("Authentication request error: ", e);
                result = null;
            }
        } while ((++i < ATTEMPTS_NUMBER) && (result == null || !result.isSuccess()));

        log.debug("auth() completed. Attempts taken: {}. Result: {}", i, result);
        return result;
    }

    /////////////////////////////////////////
    //     AUTH REQUESTS END               //
    /////////////////////////////////////////

    private String toHex(byte[] b) throws UnsupportedEncodingException {
        return String.format("%040x", new BigInteger(1,b));
    }

}
