package com.simiomobile.transferdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.simiomobile.transferdemo.dialog.LoadingDialog;
import com.simiomobile.transferdemo.model.JsonTransfer;
import com.simiomobile.transferdemo.utils.Constanc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {
    private Gson gson ;
    private JsonTransfer mJsontranfer;
    private TextView tvStatus,tvFromAC,tvToAC,tvAmount,tvFree;
    private LinearLayout mLinearSlip;
    private String mPathSlip;
    private LoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        gson = new Gson();
        Intent intent = getIntent();
        String sJsonTranfer = intent.getStringExtra(Constanc.KEY_JSONTRANFER);
        mJsontranfer = gson.fromJson(sJsonTranfer,JsonTransfer.class);
        mLoadingDialog = new LoadingDialog(ResultActivity.this);
        mLoadingDialog.requestWindowFeature(mLoadingDialog.getWindow().FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.dialog_loadingt);
        mLoadingDialog.setCancelable(false);
        initail();
        setup();

        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
                createSaveImage();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ResultActivity.this, "Storage Permission Denied.", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                // request permission when call method again
                token.continuePermissionRequest();
                // ask permission once time
                token.cancelPermissionRequest();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        openMainActivity();
    }

    private void initail() {
        mLinearSlip = (LinearLayout)findViewById(R.id.r_ln_slip) ;
        tvStatus = (TextView)findViewById(R.id.r_tv_status);
        tvFromAC = (TextView)findViewById(R.id.r_tv_from_ac);
        tvToAC = (TextView)findViewById(R.id.r_tv_to_ac);
        tvAmount = (TextView)findViewById(R.id.r_tv_amount);
        tvFree = (TextView)findViewById(R.id.r_tv_fee);
    }

    private void setup() {
        if (mJsontranfer.isStatus()){
            tvStatus.setText("Success");
        }else{
            tvStatus.setText("Fail");
        }
        tvFromAC.setText(mJsontranfer.getFromAcountId());
        tvToAC.setText(mJsontranfer.getToAccountId());
        tvAmount.setText(bath(mJsontranfer.getAmount()));
        tvFree.setText(bath(mJsontranfer.getFee()));

    }

    private String bath(String number){
        return number +" บาท";
    }
    public void onClickShare(View view) {
        shareToFacebookMessenger();
    }

    private void shareToFacebookMessenger() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        File imageFileToShare = new File(mPathSlip);
        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image!"));
    }

    public void onClickSave(View view) {
        callSeviceSave();
    }

    private void createSaveImage() {
        File pictureFile = null;
        Bitmap bmpSuc;
        bmpSuc = Bitmap.createBitmap( mLinearSlip.getWidth(), mLinearSlip.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpSuc);
        mLinearSlip.draw(canvas);
        try {
            pictureFile = getOutputMediaFileJPEG(String.valueOf(getCurrentTimestamp()));
            if (pictureFile == null) {
                Log.d("createSaveImage", "Error creating media file, check storage permissions: ");// e.getMessage());
                return;
            }
            FileOutputStream fos = new FileOutputStream(pictureFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bmpSuc.compress(Bitmap.CompressFormat.JPEG,100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pictureFile != null) {
            mPathSlip = pictureFile.getAbsolutePath();
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
            Toast.makeText(ResultActivity.this,"path: "+pictureFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        }
    }
    private void callSeviceSave() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mLoadingDialog != null) mLoadingDialog.dismiss();
                Toast.makeText(ResultActivity.this,"Save Success",Toast.LENGTH_SHORT).show();
                openMainActivity();
            }
        }, 3000);
    }
    private void openMainActivity(){
        Intent intent = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
    private File getOutputMediaFileJPEG(String _Filename) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                 + "/DemoTranfer");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        String mImageName = _Filename + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator+ mImageName);
        return mediaFile;
    }
    public static Long getCurrentTimestamp() {
        Calendar c = Calendar.getInstance();
        Long ts = new Timestamp(c.getTimeInMillis()).getTime();
        return ts/1000;
    }
}
