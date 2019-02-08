package com.myapp.senier.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    
    /**
     * 로그 타이틀
     * @param message
     * @return
     */
    public static String splitTitle(String message) {
        String sep = "\n";
        List<String> result = new ArrayList<>();

        List<Integer> sepList = KMPSearch.find(message, sep);

        if(!sepList.isEmpty()) {
            result = new ArrayList<String>(Arrays.asList(message.split(sep)));
        }

        return result.get(0);
    }

    /**
     * 패턴 매칭 함수
     * @param message
     * @param pattern
     * @return
     * @throws Exception
     */
    public static String findSpecificWord(String message, String pattern) throws Exception {
        String findStr = "";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(message);

        while(m.find()) {
            String matchStr = m.group().trim();
            if(matchStr != null && !matchStr.equals("")) {
                findStr = matchStr;
            }
        }

        return findStr;
    }

    /**
     * nvl 처리 함수
     * @param param
     * @return
     */
    public static String nvl(String param, String value) {
        if(param == null || param.equals("")) {
            return value;
        } else {
            return param;
        }
    }
}