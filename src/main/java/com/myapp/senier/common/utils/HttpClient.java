package com.myapp.senier.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.model.DataModel;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private String ip;
    private String api;
    private String method;
    private DataModel params;

    private HttpClient(HttpClientBuilder builder) {
        this.ip = builder.ip;
        this.api = builder.api;
        this.method = builder.method;
        this.params = builder.params;
    }

    public static HttpClientBuilder Builder() {
        return new HttpClientBuilder();
    }

    public static class HttpClientBuilder implements Builder<HttpClient>{
        private String ip;
        private String api;
        private String method;
        private DataModel params;

        public HttpClientBuilder() {}

        public HttpClientBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public HttpClientBuilder setApi(String api) {
            this.api = api;
            return this;
        }

        public HttpClientBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public HttpClientBuilder setParams(DataModel params) {
            this.params = params;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }

    public DataModel send() {
        URL url = null;
        HttpURLConnection conn = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        String jsonData = "";
        DataModel dm = new DataModel();

        try {
            StringBuilder sendUrl = new StringBuilder();
            sendUrl.append(this.ip);
            sendUrl.append(this.api);

            url = new URL(sendUrl.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod(this.method);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Accept", "application/json");

            if(this.method.equals(CommonConstant.POST)) {
                logger.info("POST 메세지 파라미터 - {}", this.params);
                String body = new JSONObject(this.params).toString();
                // post 데이터 전송타입 : JSON
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes());
                os.flush();
            } else {
                conn.connect();
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            
            while((jsonData = br.readLine()) != null) {
                sb.append(jsonData);
            }

            dm.putJsonString(sb.toString());
        } catch(MalformedURLException mue) {
            mue.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null) br.close();
                if(conn != null) conn.disconnect();
            } catch(Exception e) {}
        }

        return dm;
    }
}