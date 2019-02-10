package com.myapp.senier.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    
    /**
     * sep를 기준으로 문자열 스플릿
     * @param message
     * @param role
     * @return
     */
    public static Object splitSeparator(String message, String sep, String role) {
        List<String> result = new ArrayList<>();

        List<Integer> sepList = KMPSearch.find(message, sep);

        if(!sepList.isEmpty()) {
            result = new ArrayList<String>(Arrays.asList(message.split(sep)));
        }

        if(role.equals("title")) {
            return result.get(0).trim();
        } else {
            return result;
        }
    }

    /**
     * 패턴 매칭 함수
     * @param message
     * @param regex
     * @return
     * @throws Exception
     */
    public static String findSpecificWord(String message, String regex) throws Exception {
        String findStr = "";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(message);

        while(m.find()) {
            String matchStr = m.group().trim();
            if(matchStr != null && !matchStr.equals("")) {
                findStr = matchStr;
                break;
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