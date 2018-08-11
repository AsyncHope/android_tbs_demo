package com.anysoft.demo.util;

import android.os.Environment;

import java.io.File;


/**
 *  Created by LiTingYao on 2018/8/11.
 */

public class DiskUtils {
    
    public static String SDPATH = Environment.getExternalStorageDirectory().getPath();
    
    public static final String ROOT_PATH = SDPATH + "/" + "01_tbs_demo";
  
    public static final String DOWNLOAD_PATH = ROOT_PATH + "/download";
    
    static {
        createFileHolder(DOWNLOAD_PATH);
    }
    
    public static void createFileHolder(String fileHolder) {
        File file = new File(fileHolder);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
