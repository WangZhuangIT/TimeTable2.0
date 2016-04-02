package com.lingzhuo.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.lingzhuo.bean.LoginData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Wang on 2016/3/29.
 */
public class GetCheckCode implements Runnable{
    private  LoginData loginData;
    private Handler myHandler;

    public GetCheckCode(LoginData loginData, Handler myHandler) {
        this.loginData = loginData;
        this.myHandler = myHandler;
    }

    @Override
    public void run() {
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
