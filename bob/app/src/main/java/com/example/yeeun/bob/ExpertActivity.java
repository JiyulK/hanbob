package com.example.yeeun.bob;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yeeun.bob.GetDB.Listcount;
import com.example.yeeun.bob.GetDB.Userinfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExpertActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    //String strObject; // 전문 분야
    ArrayList<Expert> experts; // 전문가들의 객체 담을 arraylist
    // 짧게 클릭시 이미지를 보여주고, 길게 클릭시 전화 가게하기

    ListView expertList;
    MyExpertAdapter adapter;

    String bufferdb;
    String getbuffdata;


    boolean tt = false;
    String[] splitbuff;
    StorageReference mStorageRef;

    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        init();
    }

    public void init() {

        Intent intent = getIntent();
        //strObject = intent.getStringExtra("strObject");
        check = intent.getIntExtra("check", 0);
        experts = new ArrayList<>();


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

        //--db
        FirebaseDatabase getdata = FirebaseDatabase.getInstance();
        DatabaseReference getdataR = getdata.getReference("Listcount/");
        getdataR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Listcount l=dataSnapshot.getValue(Listcount.class);
                getbuffdata= l.getkey();
                //tt = true;


                String [] splitbuff = getbuffdata.split(",");

                for(int i = 0;i<splitbuff.length;i++){
                    FirebaseDatabase split = FirebaseDatabase.getInstance();
                    DatabaseReference splitR = split.getReference("Userinfo/"+splitbuff[i]+"/");

                    final String target = splitbuff[i];

                    splitR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Userinfo U=dataSnapshot.getValue(Userinfo.class);
                            String splitname = U.getNAME();
                            String splitmajor = U.getMAJOR();
                            String splitPhone = U.getTEL();

                            //experts.add(new Expert(target, splitmajor, splitPhone));

                            if(check == 0) {
                                //normal
                                if(splitmajor.equals("일반")) {
                                    experts.add(new Expert(splitname, splitmajor, splitPhone));
                                }
                            } else if(check == 1) {
                                //jang
                                if(splitmajor.equals("장애")) {
                                    experts.add(new Expert(splitname, splitmajor, splitPhone));
                                }
                            } else if(check == 2) {
                                //kid
                                if(splitmajor.equals("아동")) {
                                    experts.add(new Expert(splitname, splitmajor, splitPhone));
                                }
                            }

                            //experts.add(new Expert(splitname, splitmajor, splitPhone));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                //experts.add(new Expert("홍길동", "아동", "010-2784-2932"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //addexperts();
        expertList = (ListView)findViewById(R.id.expertList);

        adapter = new MyExpertAdapter(this, R.layout.expert_layout, experts);
        expertList.setAdapter(adapter);

        // 길게 클릭 리스너 달기
        expertList.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Expert e = experts.get(position);

        // 팝업창 비슷한거
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle(e.getName()+"의 상세 정보");

        final int p = position;

        TextView tv = (TextView) dialog.findViewById(R.id.diatext);
        tv.setText("전문분야 : " + e.getSubject() + "\n전화번호 : \n" + e.getPhoneNumber());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+experts.get(p).getPhoneNumber()));
                startActivity(callIntent);
            }
        });


        final ImageView iv = (ImageView) dialog.findViewById(R.id.diaimage);

        //--db
        String imgkey = e.getPhoneNumber();

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child("Userinfo/"+imgkey+".jpg");

        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(getApplicationContext())
                        .load(uri).into(iv);
            }
        });
//0: 일반인 1: 장애인 2: 아동


        //--db
        //iv.setImageResource(R.drawable.lawicon);

        dialog.show();

        return false;
    }

//    public void addexperts() { // 여기서 strObject에 해당하는 expert를 찾아서 add 시켜준다.
//
//        experts.add(new Expert("홍길동", "아동", "010-2784-2932"));
//        experts.add(new Expert("홍길순", "아동", "010-2784-2932"));
//    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}