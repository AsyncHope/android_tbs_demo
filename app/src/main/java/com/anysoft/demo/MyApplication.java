package com.anysoft.demo;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Created by LiTingYao on 2018/8/11.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initTBS();
    }
    
    private void initTBS() {
        
        QbSdk.PreInitCallback preInitCallback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e("tbs", "onCoreInitFinished: ");
            }
            
            @Override
            public void onViewInitFinished(boolean b) {
                Log.e("tbs", "onViewInitFinished: " + b);
            }
        };
        
        //tbs内核下载跟踪
        TbsListener tbsListener = new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //tbs内核下载完成回调
                Log.e("tbs", "内核下载完成回调 onDownloadFinish: " + i);
            }
            
            @Override
            public void onInstallFinish(int i) {
                //内核安装完成回调，
                Log.e("tbs", "内核安装完成回调 onInstallFinish: " + i);
            }
            
            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
                Log.e("tbs", "下载进度监听 onDownloadProgress: " + i);
            }
        };
        QbSdk.initX5Environment(this, preInitCallback);
        //tbs内核下载跟踪
        QbSdk.setTbsListener(tbsListener);
        //判断是否要自行下载内核
        QbSdk.setDownloadWithoutWifi(true);
        boolean needDownload = TbsDownloader.needDownload(this, TbsDownloader.DOWNLOAD_OVERSEA_TBS);
        if (needDownload) {
            TbsDownloader.startDownload(this);
        }
    }
}
