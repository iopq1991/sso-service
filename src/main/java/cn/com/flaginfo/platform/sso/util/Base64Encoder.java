package cn.com.flaginfo.platform.sso.util;

/**
 * Created by edward.tian on 2017/9/27.
 */



/**
 * @file Base64Encoder.java
 * @date 2016年8月5日
 * @version 3.4.1
 *
 * Copyright (c) 2013 Sihua Tech, Inc. All Rights Reserved.
 */

/**
 *
 *
 * @author chengjian.he
 * @version  3.4, 2016年8月5日 上午10:44:22
 * @since   Yeexun 3.4
 */
public class Base64Encoder {
    public static String getBASE64(String s) {
        if (s == null)
            return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }
    // 将 BASE64 编码的字符串 s 进行解码   解密
    public static String getFromBASE64(String s) {
        if (s == null)
            return null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
    public static String encrpty(Object ming){
        return Base64Encoder.getBASE64(Base64Encoder.getBASE64(Base64Encoder.getBASE64((String)ming)));
    }
    public static String decrpty(String an){
        return Base64Encoder.getFromBASE64(Base64Encoder.getFromBASE64(Base64Encoder.getFromBASE64(an)));
    }
}


