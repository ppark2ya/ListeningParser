package com.myapp.senier.common.utils;

import java.util.HashMap;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.model.DataModel;

import org.json.simple.JSONObject;

import net.nurigo.java_sdk.api.Message;

public class SMSSender {
    private final String api_key = CommonConstant.SMS_API_KEY;
    private final String api_secret = CommonConstant.SMS_API_SECRET;
    private Message coolsms;

    public SMSSender() {
        coolsms = new Message(this.api_key, this.api_secret);    
    }

    public DataModel execute(String serverType) {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("to", "01036023120");
            map.put("from", "01027253120");
            map.put("text", "문자보내기 테스트");
            map.put("type", "sms");
    
            JSONObject obj = (JSONObject) coolsms.send(map);

            resMap.putJson(obj);

        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return resMap;
    }
}