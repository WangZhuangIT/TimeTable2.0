package com.lingzhuo.Utils;

import com.lingzhuo.bean.LoginData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wang on 2016/3/29.
 */
public class GetNameByUrl {
    public static String getNameByUrl(String url, LoginData loginData) {

        url = "http://10.5.3.236/" + url;
        try {
            URL Url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(2000);
            conn.setRequestProperty("Cookie", loginData.getCookie());

            BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gb2312"));

            StringBuffer sb = new StringBuffer();
            String temp;
            while ((temp = read.readLine()) != null) {
                sb.append(temp);
            }
            int start=sb.toString().indexOf("<span id=\"xhxm\">")+16;
            int end=sb.toString().indexOf("同学</span></em>");
            Pattern pattern = Pattern.compile("xm=..");
            Matcher matcher = pattern.matcher(sb.toString());

            matcher.find();

            String name = sb.substring(start, end);
            loginData.setName(name);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
