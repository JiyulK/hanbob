package com.example.yeeun.bob;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yeeun.bob.GetDB.Defaultcountinfo;
import com.example.yeeun.bob.GetDB.Listcount;
import com.example.yeeun.bob.GetDB.Majorindex;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int MY_PERMISSON_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    ImageView img1, img2;
    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;

    StorageReference mStorageRef;
    int defaultcount=99;



    String bufferdb;
    String getbuffdata;
    String Buffermajor="";
    String Buffermajorindex;

    boolean pictest = false ;


    //--db
    EditText dbname,dbmajor,dbphone,dbemail,dboffice,dbpic;

    FirebaseDatabase db2,db0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        img1 = (ImageView)findViewById(R.id.img1);
        //img2 = (ImageView)findViewById(R.id.img2);

        init();
    }

    public void init(){

//        FirebaseDatabase initdb =FirebaseDatabase.getInstance();
//        DatabaseReference initdbR = initdb.getReference("DEFAULT COUNT/");
//
//        initdbR.addValueEventListener(new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                Defaultcountinfo info = dataSnapshot.getValue(Defaultcountinfo.class);
//                defaultcount = info.getDefaultCount();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        bufferdb="";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Userinfo/");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    bufferdb+=snap.getKey()+",";
                }
                FirebaseDatabase listcount = FirebaseDatabase.getInstance();
                DatabaseReference user = listcount.getReference("Listcount/key");
                user.setValue(bufferdb);
                //this for test
                //Toast.makeText(getApplicationContext(),"등록 완료되었습니다."+bufferdb, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        FirebaseDatabase getdata = FirebaseDatabase.getInstance();
        DatabaseReference getdataR = getdata.getReference("Listcount/");
        getdataR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Listcount l = dataSnapshot.getValue(Listcount.class);
                getbuffdata= l.getkey();

                //Toast.makeText(getApplicationContext(),"등록 완료되었습니다."+getbuffdata, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        FirebaseDatabase majorindex = FirebaseDatabase.getInstance();
        if(!Buffermajor.equals("")) {
            DatabaseReference majorindexR = majorindex.getReference("Majorindex/" + Buffermajor);
            majorindexR.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Majorindex m = dataSnapshot.getValue(Majorindex.class);
                    Buffermajorindex=m.getStr();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void photo1btn(View view) {
        pictest=true;
        captureCamera();
    }

    public void photo2btn(View view) {
        captureCamera();
    }

    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "저장공간 접근 불가능", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures", "yeeun");

        if(!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case  REQUEST_TAKE_PHOTO:
                if(resultCode == Activity.RESULT_OK) {
                    try {
                        galleryAddPic();
                        img1.setImageURI(imageUri);
                        // 여기에 cloud vision 추가
                    } catch (Exception e) {

                    }
                } else {

                }
                break;
        }
    }

    public void registerOKClick(View view) {
        dbname = (EditText)findViewById(R.id.registerName);
        dbmajor = (EditText)findViewById(R.id.registerMajor);
        dbphone = (EditText)findViewById(R.id.registerPhone);
        dbemail = (EditText)findViewById(R.id.registeremail);
        dboffice = (EditText)findViewById(R.id.registeraddr);


        if(pictest==false){   // can not signin without pic
            Toast.makeText(getApplicationContext(),"사진등록 해야합니다.", Toast.LENGTH_SHORT).show();
        }
        else if (dbname.getText().toString().length()>4||dbname.getText().toString().length()<1){
            Toast.makeText(getApplicationContext(),"이름을 확인하세요", Toast.LENGTH_SHORT).show();
        }
        else if (dbphone.getText().toString().length()>14||dbphone.getText().toString().length()<12) {
            Toast.makeText(getApplicationContext(), "전화 번호를 확인하세요", Toast.LENGTH_SHORT).show();
        }
        else{

            //--db


            String PrimiaryKey = dbphone.getText().toString();
            Buffermajor=dbmajor.getText().toString();

            db2 = FirebaseDatabase.getInstance();
            DatabaseReference user = db2.getReference("Userinfo/"+PrimiaryKey+"/");

            user.child("TEL").setValue(dbphone.getText().toString());
            user.child("NAME").setValue(dbname.getText().toString());
            user.child("MAJOR").setValue(dbmajor.getText().toString());
            user.child("EMAIL").setValue(dbemail.getText().toString());
            user.child("OFFICE").setValue(dboffice.getText().toString());

            //user.child("DEFAULTSIZE").setValue();

            //DatabaseReference majorindex = db2.getReference("Majorindex/" + Buffermajor+"/");
            //majorindex.child(dbmajor.getText().toString()).setValue(Buffermajorindex+","+dbphone.getText().toString());



            //reset
            dbname.setText("");
            dbmajor.setText("");
            dbphone.setText("");
            dbemail.setText("");
            dboffice.setText("");

            //--pic

            mStorageRef = FirebaseStorage.getInstance().getReference();


            Uri file = Uri.fromFile(new File(mCurrentPhotoPath));
            StorageReference riversRef = mStorageRef.child("Userinfo/"+PrimiaryKey+".jpg");

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });


            //--pic


            Toast.makeText(getApplicationContext(),"등록 완료되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
    }



}
