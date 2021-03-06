package com.myapp.senier.common.utils;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMPSearch {
    private static final Logger logger = LoggerFactory.getLogger(KMPSearch.class);
    /**
     * 
     * @param context : 로그 문장
     * @param pattern : 키워드
     * @return : 문장에서 키워드가 존재하고 있는 인덱스 리스트
     */
    public static ArrayList<Integer> find(String context, String pattern) {
        ArrayList<Integer> list = new ArrayList<>();
        int[] pi = getPi(pattern);
        int n = context.length(), m = pattern.length(), j = 0;
        char[] ctx = context.toCharArray();
        char[] p = pattern.toCharArray();

        try {
            for(int i = 0; i < n; i++) {
                while(j > 0 && ctx[i] != p[j]) {
                    j = pi[j - 1];
                }
    
                if(ctx[i] == p[j]) {
                    if(j == m - 1) {
                        // j는 비교 문자열로써, 인덱스가 찾을 문자열 크기에 도달하면 문자열 찾음.
                        list.add(i - m + 1);
                        j = pi[j];
                    } else {
                        j++;
                    }
                }
            }
            return list;
        } catch(ArrayIndexOutOfBoundsException e) {
            return new ArrayList<Integer>();
        }

    }

    /**
     * 
     * @param pattern : 키워드
     * @return
     */
    private static int[] getPi(String pattern) {
        int m = pattern.length();
        int j = 0;
        char[] p = new char[m];
        int[] pi = new int[m];

        p = pattern.toCharArray();

        try {
            for(int i = 1; i < m; i++) {
                while(i > 0 && p[i] != p[j]) {
                    j = pi[j - 1];
                }
                if(p[i] == p[j]) {
                    pi[i] = ++j;
                }
            }
            return pi;
        } catch(ArrayIndexOutOfBoundsException e) {
            logger.error("KMPSearch - 입력받은 로그에서 찾을 수 없는 문자열 입니다.");
            return new int[0];
        }

    }
}