package com.topcom.intime.utils;

public class KeyGen {
    private static final String key="userIdx";
    private static final String strkey="scheduleIdx";

    public static String KeyGenerated(int userIdx){
        return key+":"+userIdx;
    }
    public static String StrKeyGenerated(int scheduleIdx) {return strkey+":"+scheduleIdx;}
}
