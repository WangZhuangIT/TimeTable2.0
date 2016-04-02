package com.lingzhuo.Utils;

import android.os.Handler;
import android.os.Message;


import com.lingzhuo.bean.CourseBean;
import com.lingzhuo.bean.LoginData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wang on 2016/3/29.
 */
public class GetTimeTable implements Runnable {
    private LoginData loginData;
    private List<CourseBean> courseList;
    private Handler myHanler;
    public GetTimeTable(LoginData loginData,List<CourseBean> courseList,Handler myHanler) {
        this.loginData = loginData;
        this.courseList=courseList;
        this.myHanler=myHanler;
    }

    @Override
    public void run() {
        System.out.println("读取课表");
        String url = null;
        try {
            url = "http://10.5.3.236/xskbcx.aspx?xh="+loginData.getId()
                    +"&xm="+URLEncoder.encode(loginData.getName(), "gb2312")+"&gnmkdm="
                    +loginData.getNeedString();
            System.out.println("获取课表的参数: " + url);
            URL Url;
            Url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(2000);
            conn.setRequestProperty("Cookie", loginData.getCookie());
            conn.setRequestProperty("Referer",
                    "http://10.5.3.236/xs_main.aspx?xh="+loginData.getId());
            conn.setInstanceFollowRedirects(false);
            conn.setDoOutput(true);
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"gb2312"));

            StringBuffer sb = new StringBuffer();
            String temp;
            while ((temp = read.readLine()) != null) {
                sb.append(temp);
            }

            dealTimeTableHtml(sb.toString());



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dealTimeTableHtml(String courseHtml) {
        Pattern pattern1 = Pattern.compile("width=\"7%\">.*?/td>");
        Matcher matcher1 = pattern1.matcher(courseHtml);
        StringBuffer sb = new StringBuffer();
        int begin = 0;
        int end = 0;
        while (matcher1.find()) {
            begin = matcher1.start();
            end = matcher1.end();
            String str = courseHtml.substring(begin + 11, end);
            sb.append(str);
        }

        pattern1 = Pattern.compile("rowspan=\"2\">.*?/td>");
        matcher1 = pattern1.matcher(courseHtml);

        while (matcher1.find()) {
            begin = matcher1.start();
            end = matcher1.end();
            String str = courseHtml.substring(begin + 12, end);
            sb.append(str);
        }
        String str=sb.toString().replaceAll("&nbsp;</td>","");
        String[] courseString = str.split("</td>");
        for (int i=0;i<courseString.length;i++){
            String[] courseDital=courseString[i].split("<br>");
            CourseBean courseBean=new CourseBean(courseDital[0],courseDital[1],courseDital[2],courseDital[3],courseDital[4]);
            courseList.add(courseBean);
        }
        Message msg=new Message();
        msg.obj=courseList;
        myHanler.sendMessage(msg);

    }

}
