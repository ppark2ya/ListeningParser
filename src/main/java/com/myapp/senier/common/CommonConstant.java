package com.myapp.senier.common;

public class CommonConstant {
    // SERVER IP
    public static final String ZABBIX_SERVER_IP = "http://146.148.76.237:3000";
    public static final String POSTMAN_SERVER_IP = "http://146.148.76.237:3000";
    public static final String SEFILCARE_SERVER_IP = "http://146.148.76.237:3000";
    public static final String CHECKSERVER_SERVER_IP = "http://146.148.76.237:3000";

    // SERVER API ADDRESS
    public static final String API_ZABBIX = "/api/zabbix";
    public static final String API_POSTMAN = "/api/postman";
    public static final String API_SEFILCARE = "/api/sefilcare";
    public static final String API_CHECKSERVER = "/api/checkServer";

    // HTTP METHOD
    public static final String POST = "POST";
    public static final String GET = "GET";

    // SMS API KEY
    public static final String SMS_API_KEY = "NCSOHUBPGSDVMUCD";
    public static final String SMS_API_SECRET = "3GAFDNTCWWMUPAYJHUZEEYUBWR1BFEJG";

    // QUARTZ SCHEDULE
    public static final String CRON_EXPRESSION_ZABBIX = "0 0/1 * * * ?";
    public static final String CRON_EXPRESSION_POSTMAN = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_SEFILCARE = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_CHECKSERVER = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_STOP = "0 0 23 * * ? 2099";

}
