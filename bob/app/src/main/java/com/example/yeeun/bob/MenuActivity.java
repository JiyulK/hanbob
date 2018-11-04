package com.example.yeeun.bob;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    FirebaseDatabase menudb;
    Intent intent;
    private static final int MY_PERMISSON = 1111;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        checkPermission();
        initdb();
    }

    public void initdb(){

        menudb = FirebaseDatabase.getInstance();
        DatabaseReference user = menudb.getReference("DEFAULT COUNT/");
        user.child("DefaultCount").setValue(0);


    }

    public void whatClick(View view) {
        intent = new Intent(MenuActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void chatClick(View view) {
        intent = new Intent(MenuActivity.this, ChatbotActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }

    public void registerClick(View view) {
        intent = new Intent(MenuActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }

    public void annaeClick(View view) {
        intent = new Intent(MenuActivity.this, InfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }

    public void daeClick(View view) {
        intent = new Intent(MenuActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void singoClick(View view) {
        intent = new Intent(MenuActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                    || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
                    || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
                    || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                    || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
                    ) { new AlertDialog.Builder(this).setTitle("알림").setMessage("해당 권한이 거부되었습니다. 직접 설정에 가서 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:"+getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SEND_SMS
                }, MY_PERMISSON);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case  MY_PERMISSON:
                for(int i = 0; i < grantResults.length; i++) {
                    if(grantResults[i] < 0) {
                        Toast.makeText(MenuActivity.this, "해당 권한을 활성화하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    //수정
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
