package com.lingzhuo.timetable;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lingzhuo.Utils.GetTimeTable;
import com.lingzhuo.bean.CourseBean;
import com.lingzhuo.bean.LoginData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2016/3/29.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginData loginData;
    private List<CourseBean> courseList;
    private int table_Width = 300;
    private Button button_title;
    private PopupWindow popupWindow;

    private String[] week = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private String[] day_time = {"2节", "4节", "6节", "8节", "10节"};
    private LinearLayout[] linearLayouts = new LinearLayout[7];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tableshow);
        init();
        Intent intent = getIntent();
        loginData = intent.getParcelableExtra("loginData");
        Toast.makeText(MainActivity.this, "欢迎登陆，" + loginData.getName() + "同学", Toast.LENGTH_SHORT).show();
        new Thread(new GetTimeTable(loginData, courseList, myHanler)).start();

    }

    private Handler myHanler = new Handler() {
        public void handleMessage(Message msg) {
            courseList = (List) msg.obj;
            writeTimeTable(courseList);
        }
    };

    private void writeTimeTable(List<CourseBean> courseList) {
        for (int i=0;i<linearLayouts.length;i++){
            linearLayouts[i].removeAllViews();
        }

        //当前的周数
        int nowWeekNum=Integer.parseInt(button_title.getText().toString().substring(1,button_title.getText().toString().indexOf("周")));
        int[] counts = new int[7];
        for (int i = 0; i < courseList.size(); i++) {
            final CourseBean courseBean = courseList.get(i);
            int startNum=Integer.parseInt(courseBean.getCourse_week().toString().substring(courseBean.getCourse_week().lastIndexOf("第")+1,courseBean.getCourse_week().lastIndexOf("第")+2));
            String utilNum=courseBean.getCourse_week().substring(courseBean.getCourse_week().indexOf("-"),courseBean.getCourse_week().indexOf("-")+4);
            int endNum=Integer.parseInt(utilNum.substring(1,utilNum.indexOf("周")));
            System.out.println("开始周数"+startNum+"结束周数"+endNum);

            for (int j = 0; j < 7; j++) {
                if (courseBean.getCourse_week().contains(week[j])) {
                    for (int k = 0; k < day_time.length; k++) {
                        if (courseBean.getCourse_week().contains(day_time[k])) {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setTextColor(Color.BLACK);
                            textView.setGravity(Gravity.CENTER);
                            if (nowWeekNum<=endNum&&nowWeekNum>=startNum){
                                if ((courseBean.getCourse_week().contains("双周")&&nowWeekNum%2==0)||(courseBean.getCourse_week().contains("单周")&&nowWeekNum%2!=0)||(!courseBean.getCourse_week().contains("单周")&&!courseBean.getCourse_week().contains("双周"))) {
                                    linearLayouts[j].addView(textView);
                                    textView.setText(courseBean.toString());
                                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT, table_Width);
                                    para.setMargins(0, (k - counts[j]) * table_Width, 0, 0); //left,top,right, bottom
                                    textView.setLayoutParams(para);
                                    textView.setBackgroundResource(R.drawable.shape_lesson);
                                    counts[j] = k + 1;
                                }
                            }

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Dialog dialog = new Dialog(MainActivity.this);
                                    dialog.setTitle("课程细节：");
                                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_lesson_detial, null);
                                    TextView lesson_name = (TextView) view.findViewById(R.id.lesson_name);
                                    TextView lesson_teacher = (TextView) view.findViewById(R.id.lesson_teacher);
                                    TextView lesson_major = (TextView) view.findViewById(R.id.lesson_major);
                                    TextView lesson_address = (TextView) view.findViewById(R.id.lesson_address);
                                    TextView lesson_time = (TextView) view.findViewById(R.id.lesson_time);
                                    lesson_name.setText(courseBean.getCourse_name());
                                    lesson_address.setText(courseBean.getCourse_address());
                                    lesson_major.setText("专业");
                                    lesson_teacher.setText(courseBean.getCourse_teacher());
                                    lesson_time.setText(courseBean.getCourse_week());
                                    dialog.setContentView(view);
                                    dialog.show();
                                }
                            });


                        }
                    }
                }
            }
        }
    }


    private void init() {
        courseList = new ArrayList<>();
        linearLayouts[0] = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayouts[1] = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayouts[2] = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayouts[3] = (LinearLayout) findViewById(R.id.linearLayout4);
        linearLayouts[4] = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayouts[5] = (LinearLayout) findViewById(R.id.linearLayout6);
        linearLayouts[6] = (LinearLayout) findViewById(R.id.linearLayout7);
        button_title = (Button) findViewById(R.id.title_button);
        button_title.setOnClickListener(this);
        popupWindow = new PopupWindow(300, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_out:
                this.finish();
                Toast.makeText(MainActivity.this, "用户已注销，请重新登录！！！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.title_button:
                if (!popupWindow.isShowing()) {
                    String[] weeks = new String[18];
                    for (int i = 1; i <= 18; i++) {
                        weeks[i - 1] = "第" + i + "周";
                    }
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_weeklist, null);
                    ListView weekList = (ListView) view.findViewById(R.id.listView_weeks);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_list_item, weeks);
                    weekList.setAdapter(adapter);
                    popupWindow.setContentView(view);
                    int xOffSet = -(popupWindow.getWidth() - v.getWidth()) / 2;
                    popupWindow.showAsDropDown(v, xOffSet, 0);
                    weekList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView textView= (TextView) view;
                            button_title.setText(textView.getText());
                            popupWindow.dismiss();
                            writeTimeTable(courseList);
                        }
                    });
                }else if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
        }
    }
}
