package com.anysoft.demo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 *  Created by LiTingYao on 2018/8/11.
 * 权限管理类
 */

public class PermissionManager {

    public static final int REQUEST_ALL_STATE = 0x1000;
    public static final int REQUEST_PHONE_STATE = 0x1001;
    public static final int REQUEST_READ_WRITEE = 0x1002;
    public static final int REQUEST_PHONE_READ_WRITEE = 0x1003;
    public static final int REQUEST_READ_WEITE_CAMERA = 0x1004;

    public PermissionManager() {
    }
    
    private static volatile PermissionManager instance = null;
    
    public static PermissionManager getInstance() {
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager();
                }
            }
        }
        return instance;
    }
    
    private static OnPermissionListener mOnPermissionListener;
    private static int mRequestCode;

    public static final String[] PERMISSION_PHONE_STATE = new String[]{
            Manifest.permission.READ_PHONE_STATE, //电话权限
    };
    public static final String[] PERMISSION_READ_WRITE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE, // 读取权限
    };

    public static final String[] PERMISSION_READ_WEITE_CAMERA = new String[]{ //相机权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE, // 读取权限
            Manifest.permission.CAMERA,
    };

    public static final String[] PERMISSION_ALL_STATE = new String[]{ //相机权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE, // 读取权限
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.VIBRATE,
    };

    public interface OnPermissionListener {
        
        /**
         * 获取权限成功
         */
        void onPermissionSuccess();
        
        
        /**
         * 权限获取失败
         */
        void onPermissionFail();
    }
    
    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     */
    public void requestPermissions(Context context, int requstCode, String[] permissions,
                                   OnPermissionListener listener) {
        if (context instanceof Activity) {
            mRequestCode = requstCode;
            mOnPermissionListener = listener;
            if (checkPermissions(context, permissions)) {
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.onPermissionSuccess();
                }
            } else {
                List<String> needPermissions = getDeniedPermissions(context, permissions);
                ActivityCompat.requestPermissions((Activity) context, needPermissions.toArray(new String[needPermissions.size()]),
                        requstCode);
            }
        }
    }
    
    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(Context context, String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }
    
    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (requestCode == mRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.onPermissionSuccess();
                }
            } else {
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.onPermissionFail();
                }
            }
        }
    }
}
