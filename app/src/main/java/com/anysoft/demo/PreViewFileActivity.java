package com.anysoft.demo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anysoft.demo.util.CommonUtil;
import com.anysoft.demo.util.DiskUtils;
import com.anysoft.demo.util.PermissionManager;
import com.anysoft.demo.util.PreferencesUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * Created by LiTingYao on 2018/8/11.
 */
public class PreViewFileActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback,PermissionManager.OnPermissionListener {
    private String pdfUrl="";
    private String fileName;
    private TbsReaderView mTbsReaderView;
    private RelativeLayout rl_file_container;
    private OkGo instance;
    private String fileExtension;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_layout);
        rl_file_container = (RelativeLayout) findViewById(R.id.rl_file_container);

//        pdfUrl = "http://xxxx/download/wkdLapWn-2018-05-07Log.txt?";
        
        if (TextUtils.isEmpty(pdfUrl)) {
            finish();
            return;
        }
        
        fileExtension = CommonUtil.getFileExtension(pdfUrl);
        if (TextUtils.isEmpty(fileExtension)) {
            finish();
            return;
        }
        instance = OkGo.getInstance();
        mTbsReaderView = new TbsReaderView(this, this);
        rl_file_container.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        fileName = CommonUtil.getFileNameHasExtension(pdfUrl);
        downloadOrOpenFile();
        
    }
    
    private void downloadOrOpenFile() {
        if (PermissionManager.getInstance().checkPermissions(this, PermissionManager.PERMISSION_READ_WRITE)) {
            File file = new File(DiskUtils.DOWNLOAD_PATH, fileName);
            long fileLength = PreferencesUtil.getLong(this, fileName);
            if (file != null && fileLength != -1 && file.length() == fileLength && file.exists() && file.isFile()) {
                //如果本地存在文件则直接打开否则去下载
                openPdfFile(file);
            } else {
                downloadFile();
            }
        } else {
            PermissionManager.getInstance().requestPermissions(this, PermissionManager.REQUEST_READ_WRITEE,
                    PermissionManager.PERMISSION_READ_WRITE, this);
        }
    }
    
    private void openPdfFile(File file) {
        if (null != file) {
            displayFile(file);
        }
    }
    
    private void downloadFile() {
            instance.<File>get(pdfUrl)//
                    .tag(pdfUrl)//
                    .execute(new FileCallback(DiskUtils.DOWNLOAD_PATH, fileName) {
                        
                        @Override
                        public void onStart(Request<File, ? extends Request> request) {
                        
                        }
                        
                        @Override
                        public void onError(Response<File> response) {
                        
                        }
                        
                        @Override
                        public void downloadProgress(Progress progress) {
                        
                        }
                        
                        @Override
                        public void onSuccess(Response<File> response) {
                          
                            File file = new File(DiskUtils.DOWNLOAD_PATH, fileName);
                            PreferencesUtil.putLong(PreViewFileActivity.this, fileName, file.length());
                            openPdfFile(response.body());
                        }
                    });
        
    }
    
    private void displayFile(File file) {
        if (file != null && !TextUtils.isEmpty(file.toString())) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
            String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
            File bsReaderTempFile = new File(bsReaderTemp);
            
            if (!bsReaderTempFile.exists()) {
                boolean mkdir = bsReaderTempFile.mkdir();
                if (!mkdir) {
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString("filePath", file.getPath());
            bundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
            boolean result = mTbsReaderView.preOpen(fileExtension, false);
            if (result) {
                mTbsReaderView.openFile(bundle);
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    private void destoryAll() {
        instance.cancelTag(pdfUrl);
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
    
    @Override
    public void onPermissionSuccess() {
        downloadOrOpenFile();
    }
    
    @Override
    public void onPermissionFail() {
        Toast.makeText(this,"获取权限失败",Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            destoryAll();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
    }
}
