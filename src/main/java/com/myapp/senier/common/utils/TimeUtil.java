package com.myapp.senier.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String currentDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String currentTime() {
        return new SimpleDateFormat("HHmmss").format(new Date());
    }
}