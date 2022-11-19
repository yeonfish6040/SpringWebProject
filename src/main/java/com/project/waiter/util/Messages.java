package com.project.waiter.util;

import com.project.waiter.DevController.logger;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Messages {
    private final logger log = new logger();

    public void send(String to, String from, String text) throws FileNotFoundException {

        Scanner scIn = new Scanner(new FileInputStream("src/message.apiKey"));

        String api_key = scIn.next();
        String api_secret = scIn.next();

        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

            params.put("to", to);
        params.put("from", from);
        params.put("type", "SMS");
        params.put("text", text);
        params.put("app_version", "test app 1.0");

        try {
            JSONObject obj = (JSONObject)coolsms.send(params);
            log.info(obj.toString());
        } catch (CoolsmsException e) {
            log.info(e.getMessage());
            log.info(e.getCode());
        }
    }
}
