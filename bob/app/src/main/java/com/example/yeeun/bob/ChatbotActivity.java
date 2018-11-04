package com.example.yeeun.bob;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChatbotActivity extends SpeechActivity implements SensorEventListener{


   //---------1st




    static Activity mActivity;
    String[] RobotChat = {
            "안녕하세요? 여기는 한법짝 채팅방입니다.\n저의 질문에 답해주시면 법률 정보를 전해드려요. :\n질문에 간결하게 대답해 주시면 더 정확한 )",
            "언제 이 사건이 발생했나요?",
            "공소 시효 체크를 위해서 당사자의 생년월을 입력해주세요",
            "정신적 또는 신체적 장애를 앓고 있나요?",
            "어디서 이 사건이 발생했나요?",
            "어떤 사건이 발생했나요?",
            "입력 범위를 벗어났습니다. 다시 한번 입력해주실래요?",
            "감사합니다. 채팅이 끝났습니다.\n다시 채팅을 시작하려면 again을 입력하세요."};
    String[] specific = {
            "가해자는 몇 명이였나요?",
            "가해자가 협박 또는 폭행을 하였나요?",
            "가해자가 흉기를 들고 있었나요?",
            "가해자 중에 사실상에 기반한 4촌 이내에 해당하는 친족이 있었나요?",
            "가해자가 위계 또는 위력을 갖고 있나요?",
            "음주나 약물을 취한 상태였나요?",
    };
    String[] childhandicapped = {
            "해당하는 행위를 하였나요?\n①\t구강, 항문 등 신체에 성기를 넣는 행위\n②\t성기 항문에 손가락 등 신체의 일부나 도구를 넣는 행위",
    };
    String[] record = {
            "촬영 당시 본인이 동의를 하셨나요?",
            "가해자가 촬영물을 유포, 상영하였나요?",
            "촬영물을 통하여 금전적인 이득을 취하였나요?"
    };
    String userChat; // 사용자가 입력한 채팅메세지
    static int chatIndex = 1; // RobotChat에 대한 인덱스
    Handler handler;

    ImageButton voiceBtn;
    EditText chatEditText;
    Button addchatBtn;
    Button exampleBtn;
    Button expertBtn;

    ListView m_ListView;
    CustomAdapter m_Adapter;
    int mHour, mMinute;
    int requestCode = 100;



    //--5
    int first = 0;
    int second = 0;
    int third = 0;
    int fourth = 0;
    int fifth = 0;
    //--5

    // 흔들기 sensor 변수들
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 5000;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    //
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    int chk = 0;
    Reply pre_reply;
    ArrayList<ListContents> data;
    boolean chkch;

    //1번 질문 년월 픽
    static int syear;
    static int smonth;

    //년월 픽
    static int myear;
    static int mmonth;

    static boolean[] what = new boolean[0];

    ArrayList<String> law_data;
    //-------1st

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        mActivity = this;
        what = new boolean[5];
        chkch = false;
        for (int i = 0; i < 5; i++) {
            what[i] = false;
        }

        pre_reply = new Reply();

        //chatText = (TextView) findViewById(R.id.chatText);
        voiceBtn = (ImageButton) findViewById(R.id.voiceBtn);
        addchatBtn = (Button) findViewById(R.id.addchatBtn);
        expertBtn = (Button) findViewById(R.id.expertBtn);
        exampleBtn = (Button) findViewById(R.id.exampleBtn);
        chatEditText = (EditText) findViewById(R.id.chatEditText);
        handler = new Handler();


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        initSpeech(this);
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "말하세요.", Toast.LENGTH_SHORT).show();
                startSpeech();
            }
        });
        m_Adapter = new CustomAdapter();
        data = new ArrayList<>();
        m_ListView = (ListView) findViewById(R.id.listView1);
        m_ListView.setAdapter(m_Adapter);
        m_ListView.setAdapter(m_Adapter);
        m_Adapter.add(RobotChat[0], 0, false);
        m_Adapter.add(RobotChat[1], 0, false);
        insertBirth();


    }



    //--2

    public void addchatBtnClick(View view) {
        Log.v("nnum", chatIndex + "");
        userChat = "";
        userChat = chatEditText.getText().toString();
        if(userChat != " ") {
            m_Adapter.add(userChat, 1, false);
            m_Adapter.notifyDataSetChanged();
        }

        chatEditText.setText("");
        if(chkch == true){//추가적인 마지막 질문을 해주었을 때인다.
            if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                pre_reply.setAct(true);
                m_Adapter.add(RobotChat[7], 0, false);
                searchLaw();
            }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                pre_reply.setAct(false);
                m_Adapter.add(RobotChat[7], 0, false);
                searchLaw();
            }else{
                m_Adapter.add(RobotChat[6], 0, false);
            }
        }

        if (chatIndex == 6 && userChat.equals("again")) { // again을 입력했을 때
            chatIndex = 1;
            chk = 0;//---1
            chkch = false;
            what = new boolean[5];
            for (int i = 0; i < 5; i++) {
                what[i] = false;
            }
            pre_reply = new Reply();


            m_Adapter.add(RobotChat[1], 0, false); // 기본 메세지 출력
            insertBirth();
            expertBtn.setEnabled(false);
            exampleBtn.setEnabled(false); // 버튼 비활성화
        } else {
            if (chatIndex > 5) { // 메인 채팅이 끝났다.
                if (fifth != 5) {
                    makeSpecificQuestion();
                } else {
                    makeRecordQuestion();
                }

            } else {
                makeMainQuestion(chatIndex);
            }
        }
    }
    //--2

    //--3


    //--4

    public void makeRecordQuestion() {
        boolean chkR = false;//답변을 인식할 수 있을 경우 true
        boolean chkL = false;//끝났을 떄 true가 된다.
        Log.i("chk", chk + "");
        switch (chk) {
            case 0:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkR = true;
                    pre_reply.setAgree(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkR = true;
                    pre_reply.setAgree(false);
                }else{
                    chkR = false;
                }
                break;
            case 1:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkR = true;
                    pre_reply.setShow(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkR = true;
                    pre_reply.setShow(false);
                }else{
                    chkR = false;
                }
                break;
            case 2:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkR = true;
                    pre_reply.setMoney(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkR = true;
                    pre_reply.setMoney(false);
                }else{
                    chkR = false;
                }
                chkL = true;
                m_Adapter.add(RobotChat[7], 0, false);
                searchLaw();
        }
        if (!chkR) {
            m_Adapter.add(RobotChat[6], 0, false);
        } else if (chkL != true) {
            chk++;
            m_Adapter.add(record[chk], 0, false);
        }


    }

    public void makeSpecificQuestion() {
        Log.i("fifth", fifth + "");
        Log.i("ASdf", "makeSpecificQuestion");
        boolean chkS = false;
        switch (chk) {
            case 0:
                if (userChat.contains("한 명")||userChat.contains("한")||userChat.contains("1")||userChat.contains("one")) {
                    chkS = true;
                    pre_reply.setNum(false);
                }else{
                    chkS = true;
                    pre_reply.setNum(true);
                }
                break;
            case 1:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkS = true;
                    pre_reply.setHp(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkS = true;
                    pre_reply.setHp(false);
                }else{
                    chkS = false;
                }
                break;
            case 2:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkS = true;
                    pre_reply.setHg(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkS = true;
                    pre_reply.setHg(false);
                }else{
                    chkS = false;
                }
                break;
            case 3:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkS = true;
                    pre_reply.setCousin(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkS = true;
                    pre_reply.setCousin(false);
                }else{
                    chkS = false;
                }
                break;
            case 4:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkS = true;
                    pre_reply.setPower(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkS = true;
                    pre_reply.setPower(false);
                }else{
                    chkS = false;
                }
                break;
            case 5:
                if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
                    chkS = true;
                    pre_reply.setDrunken(true);
                }else if(userChat.contains("n")||userChat.contains("아니")||userChat.contains("ㄴ")){
                    chkS = true;
                    pre_reply.setDrunken(false);
                }else{
                    chkS = false;
                }
                if (fifth == 3) {
                    m_Adapter.add(record[0], 0, false);
                    chk = 0;
                    fifth = 5;
                    return;
                } else {
                    if(pre_reply.getGroup() == 1 || pre_reply.getGroup() ==2) {
                        if(!chkch) {
                            m_Adapter.add(childhandicapped[0], 0, false);
                            chkch = true;//이 질문이 불렸을 때 활성화시킨다.
                        }
                    }else{
                        m_Adapter.add(RobotChat[7], 0, false);
                        searchLaw();

                    }
                    return;
                }
        }
        if (!chkS) {
            m_Adapter.add(RobotChat[6], 0, false);
        } else {
            chk++;
            m_Adapter.add(specific[chk], 0, false);
        }


    }
    //--4

    //--6
    public int makeMainQuestion(int num) {
        boolean chkF = true;//입력이 제대로 들어왔는지.. 제대로 들어오면 false가 된다.
        switch (num) {
            case 1:
                chkF = false;

                addchatBtn.setEnabled(true);

                chatEditText.setClickable(true);
                chatEditText.setEnabled(true);
                chatEditText.setFocusable(true);
                chatEditText.setFocusableInTouchMode(true);
                break;
            case 2:
                chkF = false;

                addchatBtn.setEnabled(true);

                chatEditText.setClickable(true);
                chatEditText.setEnabled(true);
                chatEditText.setFocusable(true);
                chatEditText.setFocusableInTouchMode(true);
                calAge();
                break;
            case 3:
                if (!checkThirdQuestion(userChat, third)) {
                    chkF = true;
                    pre_reply.setGroup(1);
                } else
                    chkF = false;


                break;
            case 4:
                if (!checkFourthQuestion(userChat, fourth)) {
                    chkF = true;
                    pre_reply.setWhere(1);
                } else
                    chkF = false;
                break;
            case 5:
                if (!checkFifthQuestion(userChat)) {
                    chkF = true;


                } else {
                    chkF = false;
                    chatIndex++;
                    addchatBtn.setEnabled(true);

                    chatEditText.setClickable(true);
                    chatEditText.setEnabled(true);
                    chatEditText.setFocusable(true);
                    chatEditText.setFocusableInTouchMode(true);

                    if (fifth == 1) {
                        m_Adapter.add(specific[0], 0, false);

                    } else if (fifth == 5) {
                        m_Adapter.add(record[0], 0, false);
                    } else if (fifth == 3) {//둘다 해줘야 하는 경우
                        m_Adapter.add(specific[0], 0, false);

                    }
                    m_Adapter.notifyDataSetChanged();
                    return 1;

                }
                break;
        }
        Log.i("chatIndex11", chatIndex+"");

        if (chkF) {
            m_Adapter.add(RobotChat[6], 0, false);

        } else {
            chatIndex++;
            m_Adapter.add(RobotChat[chatIndex], 0, false);


            if (chatIndex == 5) {//무엇을 당했는지 선택하도록.....
                insertWhat();

            } else if (chatIndex == 2) {
                insertBirth();//생일
                int year = 2018 - myear;
                int month = 6 - mmonth;
            }
        }
        return 1;

    }

    private void insertBirth() {
        addchatBtn.setEnabled(false);

        chatEditText.setClickable(false);
        chatEditText.setEnabled(false);
        chatEditText.setFocusable(false);
        chatEditText.setFocusableInTouchMode(false);
        m_Adapter.add("birth", 1, false);
    }

    private void insertWhat() {
        addchatBtn.setEnabled(false);

        chatEditText.setClickable(false);
        chatEditText.setEnabled(false);
        chatEditText.setFocusable(false);
        chatEditText.setFocusableInTouchMode(false);
        m_Adapter.add("checkbox", 1, false);


    }
    //--6

    //--7
    public boolean checkThirdQuestion(String userChat, int third) {
        if (userChat.contains("y")||userChat.contains("네")||userChat.contains("예")||userChat.contains("ㅇ")) {
            pre_reply.setGroup(1);
            return true;
        } else if (userChat.contains("no")||userChat.contains("n")||userChat.contains("아니")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkFourthQuestion(String userChat, int fourth) {//어떤 사건이 발생했나요?
        //선택으로 받게 하는게 나을만한 정보
        if (userChat.contains("집") || userChat.contains("숙소")|| userChat.contains("호텔")|| userChat.contains("펜션")|| userChat.contains("게스트하우스")|| userChat.contains("기숙사")) {
            fourth = 1;
            return true;//주거 침입
        } else if (userChat.contains("지하철") || userChat.contains("학교") || userChat.contains("버스")|| userChat.contains("화장실")|| userChat.contains("탈의실")|| userChat.contains("수영장")) {
            fourth = 2;
            return true;//공공장소
        } else {
            fourth = 3;
            return true;//그외
        }

    }

    public boolean checkFifthQuestion(String userChat) {//어떤 사건이 발생했나요?
        //선택으로 받게 하는게 나을만한 정보

        if (what[4]) {
            fifth = 5;//record
            if (what[0] || what[1] || what[2] || what[3])
                fifth = 3;

        } else if (what[0] || what[1] || what[2] || what[3]) {
            fifth = 1;
        }
        return true;
    }
    //--7

    private void searchLaw() {
        Log.i("searchLaw", "called");

        //db.child("tmp").setValue(pre_reply);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("law");
        law_data = new ArrayList<>();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    boolean chk1 = false;
                    String description = "";
                    Reply reply = postSnapshot.getValue(Reply.class);
                    if (reply.isDate() != false) {
                        if (pre_reply.isDate() != reply.isDate()) {
                            continue;
                        }
                        description += "현재 공소시효 기간을 넘지 않았습니다." + "\n";
                        chk1 = true;
                    }

                    Log.i("bobbob", "!!!");

                    if (reply.getGroup() != 0) {
                        if (pre_reply.getGroup() != reply.getGroup()) {
                            continue;
                        }
                        if (pre_reply.getGroup() == 1)
                            description += "당사자는 장애인입니다." + "\n";
                        else if (pre_reply.getGroup() == 2)
                            description += "당사자는 13세 미만 아동입니다." + "\n";
                        chk1 = true;

                    }
                    if (reply.getWhere() != 0) {
                        if (pre_reply.getWhere() != reply.getWhere()) {
                            continue;
                        }
                        if (pre_reply.getWhere() == 1)
                            description += "가해자는 주거 침입에 해당하는 죄를 갖고 있습니다." + "\n";
                        else if (pre_reply.getWhere() == 2)
                            description += "사건은 공공장소에서 일어났습니다." + "\n";
                        chk1 = true;

                    }

                    boolean tmpFlag = false;
                    for (int i = 1; i < 6; i++) {
                        if (i == reply.getWhat()) {
                            Log.i("asdfasdfasdf", reply.getWhat() + "");
                            if (what[i - 1] != false) {
                                switch (i) {
                                    case 1:
                                        description += "피해자는 강간을 당했습니다." + "\n";
                                        tmpFlag = true;
                                        break;
                                    case 2:
                                        description += "피해자는 추행을 당했습니다." + "\n";
                                        tmpFlag = true;
                                        break;
                                    case 3:
                                        description += "피해자는 준강간/추행을 당했습니다." + "\n";
                                        tmpFlag = true;
                                        break;
                                    case 4:
                                        description += "피해자는 간음을 당했습니다." + "\n";
                                        tmpFlag = true;
                                        break;
                                    case 5:
                                        description += "피해자는 촬영을 당했습니다." + "\n";
                                        tmpFlag = true;
                                        break;
                                }
                            }
                            chk1 = true;
                        }
                    }
                    if (tmpFlag == false) {
                        continue;
                    }
                    if (reply.isAct() != false) {
                        if (pre_reply.isAct() != reply.isAct()) {
                            continue;
                        }
                        description += "가해자는 특정 행동을 하였습니다.." + "\n";
                        chk1 = true;
                    }
                    if (reply.isNum() != false) {
                        if (pre_reply.isNum() != reply.isNum()) {
                            continue;
                        }
                        description += "가해자는 두명 이상입니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isHp() != false) {
                        if (pre_reply.isHp() != reply.isHp()) {
                            continue;
                        }
                        description += "가해자는 협박을 하였습니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isHg() != false) {
                        if (pre_reply.isHg() != reply.isHg()) {
                            continue;
                        }
                        description += "가해자는 흉기를 갖고 있었습니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isCousin() != false) {
                        if (pre_reply.isCousin() != reply.isCousin()) {
                            continue;
                        }
                        description += "가해자에는 4촌 이내의 가족이 있습니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isPower() != false) {
                        if (pre_reply.isPower() != reply.isPower()) {
                            continue;
                        }
                        description += "가해자는 권력을 갖고 있습니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isDrunken() != false) {
                        if (pre_reply.isDrunken() != reply.isDrunken()) {
                            continue;
                        }
                        description += "약물 또는 음주를 한 상태입니다" + "\n";
                        chk1 = true;
                    }


                    if (reply.isAgree() != false) {
                        if (pre_reply.isAgree() != reply.isAgree()) {
                            continue;
                        }
                        description += "초반에 피해자는 촬영에 대한 동의를 하지 않았습니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isShow() != false) {
                        if (pre_reply.isShow() != reply.isShow()) {
                            continue;
                        }
                        description += "가해자가 허락없이 촬영물을 배포/상영하였습니다." + "\n";
                        chk1 = true;
                    }
                    if (reply.isMoney() != false) {
                        if (pre_reply.isMoney() != reply.isMoney()) {
                            continue;
                        }
                        description += "가해자가 해당 영상물을 통하여 금전적 이득을 취했습니다." + "\n";
                        chk1 = true;
                    }
                    if (chk1 == true) {
                        Log.v("confirm", what[0] + "" + what[1] + "");
                        description += "따라서 \'";
                        description += postSnapshot.getKey();
                        description += "\' 에 해당하는 법이 적용됩니다.";
                        m_Adapter.add(description, 0, true);
                        m_Adapter.getData(postSnapshot.getKey());
                        m_Adapter.notifyDataSetChanged();
                    }

                    Log.v("pre_Reply", pre_reply + "");
                    Log.v("Reply", reply + "");
                    Log.v("법", postSnapshot.getKey() + "");
                    int num1 = postSnapshot.getKey().indexOf("장");
                    int num2 = postSnapshot.getKey().indexOf("조");
                    law_data.add(postSnapshot.getKey().substring(num1+1, num2+1));
                    expertBtn.setEnabled(true);
                    exampleBtn.setEnabled(true); // 버튼 활성화
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }
    //--3

    public boolean isitRightInput(String userChat) { // 사용자에게 맞는 입력을 받았는지 체크
        userChat = userChat.trim(); // 앞뒤 공백없애기 그냥 해준겁니다..ㅎㅎ
        if(chatIndex == 1) { // '누가' 를 입력받을 때의 경우
            if (userChat.equals("kid")||userChat.equals("아이")) {
                return true;
            } else {
                return false;
            }
        } else if(chatIndex == 2 ) { // '언제' 를 입력받을 때의 경우
            if (userChat.equals("yesterday")||userChat.equals("어제")) {
                return true;
            } else {
                return false;
            }
        } else { // '어디서' 를 입력받을 때의 경우
            if (userChat.equals("school")||userChat.equals("학교")) {
                return true;
            } else {
                return false;
            }
        }
    }
    private void calAge() {
        if(2018 - myear < 13){
            pre_reply.setGroup(2);
        }
    }

    // 판례 버튼 클릭
    public void exampleBtnClick(View view) {
        int exNum = 0; // 판례 구분 번호? 같은걸 설정해서 인텐트 넘기는 식으로.
        Intent intent = new Intent(ChatbotActivity.this, ExampleActivity.class);
        intent.putStringArrayListExtra("law_data", law_data);
        startActivity(intent);
        Log.i("intent", law_data.get(0)+"");
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    // 전문가 버튼 클릭
    public void expertBtnClick(View view) {
        String strObject = ""; // 전문 분야 string으로 넘겨준다.
        Intent intent = new Intent(ChatbotActivity.this, ExpertActivity.class);
        intent.putExtra("check", pre_reply.getGroup());
        Toast.makeText(this, String.valueOf(pre_reply.getGroup()), Toast.LENGTH_SHORT).show();

        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    // 음성인식관련

    @Override
    void onSpeech(String result) {
        chatEditText.setText(result);
        Log.d("chatyeeun", "손예은 왈 : " + result);
    }

    @Override
    void onSmallVoice() {
        Toast.makeText(getApplicationContext(), "큰 목소리로 말하세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // 이벤트발생
                    Toast.makeText(getApplicationContext(), "흔들기 감지 : 말하세요.", Toast.LENGTH_SHORT).show();
                    startSpeech();
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }


    // 채팅 알람 -> 아직 구현못함 ㅠ
    public void alarmClick(View view) {
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        new TimePickerDialog(ChatbotActivity.this, mTimeSetListener, mHour, mMinute, false).show();
    }

    public void alarmSet(int h, int m) {
        //Toast.makeText(this, "생성", Toast.LENGTH_SHORT).show();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, h);
        mCalendar.set(Calendar.MINUTE, m);
        mCalendar.set(Calendar.SECOND, 0);

        Intent mAlarmIntent = new Intent("com.example.yeeun.bob.ALARM_STAR");
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(
                this, requestCode, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), mPendingIntent);
    }

    TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;

                    makeAlarm();
                }
            };

    void makeAlarm() {
        alarmSet(mHour, mMinute);
        Toast.makeText(this, mHour+"시 "+mMinute+"분 으로 알람 설정", Toast.LENGTH_SHORT).show();
    }

    void giveDelay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //수정
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
    }
}

