package com.dajjas.tapali;

import java.io.UnsupportedEncodingException;

public class Utils {
    public static String getUtf8String(String string) {
        try {
            return new String(string.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return string;
    }
}
