package com.topcom.intime.utils;

public class KeyGen {
    private static final String key="userIdx";

    public static String KeyGenerated(int userIdx){
        return key+":"+userIdx;
    }
}
