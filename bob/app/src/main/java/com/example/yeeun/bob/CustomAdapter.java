package com.example.yeeun.bob;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.yeeun.bob.ChatbotActivity.chatIndex;
import static com.example.yeeun.bob.ChatbotActivity.mActivity;
import static com.example.yeeun.bob.ChatbotActivity.mmonth;
import static com.example.yeeun.bob.ChatbotActivity.myear;
import static com.example.yeeun.bob.ChatbotActivity.smonth;
import static com.example.yeeun.bob.ChatbotActivity.syear;
import static com.example.yeeun.bob.ChatbotActivity.what;

/**
 * Created by YeEun on 2018-05-31.
 */
public class CustomAdapter extends BaseAdapter {

    private ArrayList<ListContents> m_List;
    private int lastPosition = -1;
    TextToSpeech tts;
    boolean ttsReady = false;
    boolean chk = false;


    public CustomAdapter() {
        m_List = new ArrayList();
    }


    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg, int _type, boolean chk) {

        m_List.add(new ListContents(_msg, _type));
        this.chk = chk;

    }

    //  외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    int num = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView text = null;
        CustomHolder holder = null;
        LinearLayout layout = null;
        View viewRight = null;
        View viewLeft = null;

        CheckboxHolder cholder = null;
        LinearLayout cb_layout = null;
        Button btn = null;
        Button bt = null;
        CheckBox gg = null;
        CheckBox ph = null;
        CheckBox jgg = null;
        CheckBox ge = null;
        CheckBox record = null;
        LayoutInflater inflater = null;

        CalendarView cal;
        //if (convertView == null) {
        Log.i("position", position + "");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //}

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴

        // view가 null일 경우 커스텀 레이아웃을 얻어 옴
        Log.i("checked", m_List.get(position).getMsg() + "");
        if (m_List.get(position).getMsg() == "checkbox") {


            Log.i("position2size", m_List.size() + "");
            Log.i("position2", position + "");
            Log.i("aslkdjfsaj", "asdf");
            convertView = inflater.inflate(R.layout.checkbox_layout, parent, false);
            cb_layout = (LinearLayout) convertView.findViewById(R.id.cb_layout);
            gg = (CheckBox) convertView.findViewById(R.id.gg);
            ph = (CheckBox) convertView.findViewById(R.id.ph);
            jgg = (CheckBox) convertView.findViewById(R.id.jgg);
            ge = (CheckBox) convertView.findViewById(R.id.ge);
            record = (CheckBox) convertView.findViewById(R.id.record);
            bt = (Button) convertView.findViewById(R.id.bt_cb);


            cholder = new CheckboxHolder();
            cholder.cb_layout = cb_layout;
            cholder.gg = gg;
            cholder.ph = ph;
            cholder.jgg = jgg;
            cholder.ge = ge;
            cholder.record = record;

        } else if (m_List.get(position).getMsg() == "birth") {
            convertView = inflater.inflate(R.layout.cal_layout, parent, false);
            cal = (CalendarView) convertView.findViewById(R.id.cal);
            cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    if (chatIndex == 1) {
                        syear = year;
                        smonth = month + 1;
                    } else if (chatIndex == 2) {
                        myear = year;
                        mmonth = month + 1; // 월은 가져올 때 1을 더해준다.

                    }
                }
            });
            btn = (Button) convertView.findViewById(R.id.btn_cal);


        } else {
            convertView = inflater.inflate(R.layout.activity_chatitem, parent, false);

            layout = (LinearLayout) convertView.findViewById(R.id.layout);
            text = (TextView) convertView.findViewById(R.id.text);
            viewRight = (View) convertView.findViewById(R.id.imageViewright);
            viewLeft = (View) convertView.findViewById(R.id.imageViewleft);


            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView = text;
            holder.layout = layout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //다음 질문을 뿌려주기
                    ((ChatbotActivity) mActivity).addchatBtnClick(new View(context));
                }
            });
        }
        if (bt != null) {
            final CheckBox finalGg = gg;
            final CheckBox finalPh = ph;
            final CheckBox finalJgg = jgg;
            final CheckBox finalGe = ge;
            final CheckBox finalRecord = record;
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalGg.isChecked()) {
                        Log.i("what", "!!!!!");
                        what[0] = true;
                    }
                    if (finalPh.isChecked()) {
                        what[1] = true;
                    }
                    if (finalJgg.isChecked()) {
                        what[2] = true;
                    }
                    if (finalGe.isChecked()) {
                        what[3] = true;
                    }
                    if (finalRecord.isChecked()) {
                        what[4] = true;
                    }
                    ((ChatbotActivity) mActivity).addchatBtnClick(new View(context));
                }

            });
        }


//        else{
//            if (m_List.get(position).getMsg() != "checkbox") {
//                holder = (CustomHolder) convertView.getTag();
//                text = holder.m_TextView;
//                layout = holder.layout;
//                viewRight = holder.viewRight;
//                viewLeft = holder.viewLeft;
//            }
//        }


        // Text 등록
        if (m_List.get(position).getMsg() != "checkbox" && m_List.get(position).getMsg() != "birth") {
            text.setText(m_List.get(position).getMsg());
            if (m_List.get(position).getType() == 0) {
                text.setBackgroundResource(R.color.chat2);
                layout.setGravity(Gravity.LEFT);
                viewRight.setVisibility(View.GONE);
                viewLeft.setVisibility(View.GONE);
            } else if (m_List.get(position).getType() == 1) {
                text.setBackgroundResource(R.color.chat1);
                layout.setGravity(Gravity.RIGHT);
                viewRight.setVisibility(View.GONE);
                viewLeft.setVisibility(View.GONE);
            }


            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // 터치 시 해당 아이템 이름 출력
                    if (chk) {
                        TextView tv = (TextView) v.findViewById(R.id.text);
                        String str = tv.getText().toString();
                        str = str.substring(str.indexOf("\'") + 1, str.length());
                        str = str.substring(0, str.indexOf("\'"));
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("law");
                        final String des_str;
                        final String finalStr = str;
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    //Toast.makeText(v.getContext(), postSnapshot.getKey()+finalStr, Toast.LENGTH_SHORT).show();
                                    if (postSnapshot.getKey().equals(finalStr)) {
                                        Log.i("getkey", postSnapshot+"");
                                        final CustomDialog addCateDialog = new CustomDialog(v.getContext());
                                        addCateDialog.show();
                                        addCateDialog.setLawDetail(postSnapshot.getValue(Reply.class).getDescription().toString(), postSnapshot.getKey().toString());

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //Toast.makeText(v.getContext(), str, Toast.LENGTH_SHORT).show();

                        //해당하는 법 보여주기
                    } else {
                        //Toast.makeText(context, "리스트 클릭 : " + m_List.get(pos), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    //Toast.makeText(context, "리스트 롱 클릭 : " + m_List.get(pos), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Animation animation = AnimationUtils.loadAnimation(
                    context, (position > this.lastPosition) ?
                            R.anim.up_from_bottom : R.anim.down_from_top);
            convertView.startAnimation(animation);
            this.lastPosition = position;

        }
        return convertView;

    }

    public void getData(String key) {
        str = key;
    }

    String str = "";
    boolean chkbox = false;

    public void check() {
        chkbox = true;
    }

    public void uncheck() {
        chkbox = false;
    }

    private class CustomHolder {
        TextView m_TextView;
        LinearLayout layout;
        View viewRight;
        View viewLeft;
    }

    private class CheckboxHolder {
        LinearLayout cb_layout;

        CheckBox gg;
        CheckBox ph;
        CheckBox jgg;
        CheckBox ge;
        CheckBox record;

    }


}