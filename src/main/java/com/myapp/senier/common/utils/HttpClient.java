package com.myapp.senier.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.myapp.senier.model.DataModel;

public class HttpClient {

    private String ip;
    private String api;
    private String method;

    private HttpClient(HttpClientBuilder builder) {
        this.ip = builder.ip;
        this.api = builder.api;
        this.method = builder.method;
    }

    public static HttpClientBuilder Builder() {
        return new HttpClientBuilder();
    }

    public static class HttpClientBuilder {
        private String ip;
        private String api;
        private String method;

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
            conn.connect();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            
            while((jsonData = br.readLine()) != null) {
                sb.append(jsonData);
            }

            dm.putJsonString(sb.toString());
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