package com.anysoft.demo.util;

import android.text.TextUtils;

/**
 *  Created by LiTingYao on 2018/8/11.
 */

public class CommonUtil {
    
    public static String getFileExtension(String url) {
        String str = "";
        
        if (TextUtils.isEmpty(url)) {
            return str;
        }
    
        String urlstr1 = url.split("\\?")[0];
        int i = urlstr1.lastIndexOf("/");
        if (i <= -1) {
            return str;
        }
        String urlstr2 = urlstr1.substring(i + 1);
        int indexOf = urlstr2.lastIndexOf(".");
        if (indexOf <= -1) {
            return str;
        }
        str = urlstr2.substring(indexOf + 1);
        return str;
    }
    
    public static String getFileNameHasExtension(String url) {
        String str = "";
        
        if (TextUtils.isEmpty(url)) {
            return str;
        }
    
        String urlStr = url.split("\\?")[0];
        int i = urlStr.lastIndexOf("/");
        if (i <= -1) {
            return str;
        }
        str = urlStr.substring(i + 1);
        return str;
    }
    
    public static String getFileNameNoExtension(String url) {
        String str = "";
        
        if (TextUtils.isEmpty(url)) {
            return str;
        }
    
        String urlStr1 = url.split("\\?")[0];
        int i = urlStr1.lastIndexOf("/");
        if (i <= -1) {
            return str;
        }
        String urlStr2 = urlStr1.substring(i + 1);
        int lastIndexOf = urlStr2.lastIndexOf(".");
        if (lastIndexOf <= -1) {
            return str;
        }
    
        str = urlStr2.substring(0, lastIndexOf);
        return str;
    }
    
}
