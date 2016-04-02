package com.lingzhuo.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lingzhuo.Utils.GetCheckCode;
import com.lingzhuo.Utils.GetNameByUrl;
import com.lingzhuo.Utils.GetUtils;
import com.lingzhuo.bean.LoginData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginData loginData = new LoginData();
    private Button button_login;
    private ImageView imageView;
    private EditText editText_id, editText_password, editText_code;
    private TextView textView_care;

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        init();
        //广播的值
        intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        //注册广播，表示NetworkChangeReceiver只会接收值为android.net.conn.CONNECTIVITY_CHANGE的广播
        registerReceiver(networkChangeReceiver,intentFilter);


    }

    private void init() {
        button_login = (Button) findViewById(R.id.button_login);
        editText_id = (EditText) findViewById(R.id.editText_id);
        imageView = (ImageView) findViewById(R.id.imageView_checkcode);
        editText_code = (EditText) findViewById(R.id.editText_checkcode);
        editText_password = (EditText) findViewById(R.id.editText_password);
        textView_care= (TextView) findViewById(R.id.textView_care);
        button_login.setOnClickListener(this);
        imageView.setOnClickListener(this);
        textView_care.setOnClickListener(this);

    }

    private Handler myHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1==10086){
                editText_code.setText("");
            }else{
                loginData = (LoginData) msg.obj;
                imageView.setImageBitmap(loginData.getBitmap_checkcode());
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                loginData.setId(editText_id.getText().toString());
                loginData.setPassword(editText_password.getText().toString());
                loginData.setCheckCode(editText_code.getText().toString());
                if (TextUtils.isEmpty(loginData.getViewState())){
                    Toast.makeText(LoginActivity.this,R.string.login_error, Toast.LENGTH_SHORT).show();
                }else{
                    loginThread();
                }
                break;
            case R.id.imageView_checkcode:
                new Thread(new GetCheckCode(loginData, myHanler)).start();
                break;
            case R.id.textView_care:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle(R.string.builder_title);
                builder.setMessage(R.string.builder_message);
                builder.setPositiveButton(R.string.builder_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginActivity.this, R.string.builder_ok_button_show, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
        }
    }

    private void loginThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {

                    // 拼接请求字符串
                    String str = "__VIEWSTATE=" + URLEncoder.encode(loginData.getViewState(), "gb2312")
                            + "&txtUserName=" + loginData.getId() + "&TextBox2="
                            + loginData.getPassword()+ "&txtSecretCode=" + loginData.getCheckCode() + "&RadioButtonList1=" + URLEncoder.encode("学生", "gb2312")
                            + "&Button1=&lbLanguage=&hidPdrs=&hidsc=";
                    ;
                    System.out.println("参数列表:" + str);

                    // 登录提交的网址
                    URL url = new URL("http://10.5.3.236/default2.aspx");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // Post提交
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setUseCaches(false);
                    // 禁止程序自己跳转到目标网址，必须设置，不然程序会自己响应
                    // 302返回码，自己请求跳转后的网址，出现Object Had Moved!错误
                    conn.setInstanceFollowRedirects(false);
                    // 写入cookie
                    conn.setRequestProperty("Cookie", loginData.getCookie());
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();
                    // 写入参数
                    out.write(str.getBytes());
                    out.close();
                    // 打印返回码
                    System.out.println("返回码:" + conn.getResponseCode());
                    // 打印服务器返回的跳转网址
                    System.out.println("Location :" + conn.getHeaderField("Location"));

                    BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String temp;
                    // 读取页面源码
                    StringBuffer ab = new StringBuffer();
                    while ((temp = read.readLine()) != null) {
                        ab.append(temp);
                    }
                    System.out.println(ab);
                    if (conn.getResponseCode() != 302) {
                        new Thread(new GetCheckCode(loginData, myHanler)).start();
                        Toast.makeText(LoginActivity.this, "用户名或密码错误！！！", Toast.LENGTH_SHORT).show();
                        Message msg=new Message();
                        msg.arg1=10086;
                        myHanler.sendMessage(msg);
                    } else {
                        GetNameByUrl.getNameByUrl(conn.getHeaderField("Location"),loginData);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("loginData",loginData);
                        startActivity(intent);
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //动态广播务必要记得取消注册才行
        unregisterReceiver(networkChangeReceiver);
    }

    //定义一个内部类，使其继承BroadcastReceiver，复写其中的onReceive方法
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取系统服务类中专门用于管理网络连接的对象，
            ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //再得到NetworkInfo，用其判断当前的网络状态
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null&&networkInfo.isAvailable()){
                new Thread(new GetUtils(loginData, myHanler)).start();
            }else{
                Toast.makeText(LoginActivity.this, "网络不可用！！！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
