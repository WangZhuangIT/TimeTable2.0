package com.lingzhuo.Utils;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.lingzhuo.bean.LoginData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Wang on 2016/3/29.
 */
public class GetUtils implements Runnable {
    private static LoginData loginData;
    private static Handler myHandler;

    public GetUtils(LoginData loginData, Handler handler) {
        this.loginData = loginData;
        this.myHandler=handler;
    }

    @Override
    public void run() {
        getCookie();
        getCheckcode();
    }

    public static void getCookie(){
        String viewstate;
        String cookie;
        try {
            URL url = new URL("http://10.5.3.236/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求超时时间
            conn.setReadTimeout(1000);
            if (conn.getResponseCode() != 200) {
                System.out.println("error");
                return;
            }
            // 获取Set-Cookie
            String str = conn.getHeaderField("Set-Cookie");
            int begin = str.indexOf("ASP.NET_SessionId");
            int end = str.indexOf(';', begin);
            cookie = str.substring(begin, end);
            loginData.setCookie(cookie);
            BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // 读取整个网页的源码
            StringBuffer buffer = new StringBuffer();
            while ((str = read.readLine()) != null) {
                buffer.append(str);
            }
            read.close();
            begin = buffer.indexOf("__VIEWSTATE\"");
            begin = buffer.indexOf("value=\"", begin + 1);
            begin = buffer.indexOf("\"", begin + 1);
            end = buffer.indexOf("\"", begin + 1);
            viewstate = buffer.substring(begin + 1, end);
            System.out.println("隐藏字段 = " + viewstate);
            System.out.println("隐藏字段 = " + cookie);

            loginData.setViewState(viewstate);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getCheckcode(){
        try {
            /**
             * img变量的值为http:http://10.5.3.236/CheckCode.aspx
             * 也就是上面图片中，验证码的网址，在浏览器中，右键验证码 即可选择复制图片网址 请求 URL:
             * http://10.5.3.236/CheckCode.aspx
             */
            URL url = new URL("http://10.5.3.236/CheckCode.aspx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            // cookie一同提交(ASP.NET_SessionId=4kusfii0urpbrazkhxvuas45,只需要等号后面的一串数据)
            conn.setRequestProperty("Cookie", loginData.getCookie());

            InputStream in = conn.getInputStream();
            loginData.setBitmap_checkcode(BitmapFactory.decodeStream(conn.getInputStream()));
            System.out.println("读取验证码完毕!");
            Message msg=new Message();
            msg.obj=loginData;
            myHandler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
