package com.lingzhuo.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wang on 2016/3/29.
 */
public class LoginData implements Parcelable {
    private String id;
    private String password;
    private String checkCode;
    private String viewState;
    private String cookie;
    private Bitmap bitmap_checkcode;
    private String name;
    private String needString="N121603";

    public LoginData() {
    }

    protected LoginData(Parcel in) {
        id = in.readString();
        password = in.readString();
        checkCode = in.readString();
        viewState = in.readString();
        cookie = in.readString();
        bitmap_checkcode = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        needString = in.readString();
    }

    public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {
        @Override
        public LoginData createFromParcel(Parcel in) {
            return new LoginData(in);
        }

        @Override
        public LoginData[] newArray(int size) {
            return new LoginData[size];
        }
    };

    public String getNeedString() {
        return needString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap_checkcode() {
        return bitmap_checkcode;
    }

    public void setBitmap_checkcode(Bitmap bitmap_checkcode) {
        this.bitmap_checkcode = bitmap_checkcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getViewState() {
        return viewState;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(password);
        dest.writeString(checkCode);
        dest.writeString(viewState);
        dest.writeString(cookie);
        dest.writeParcelable(bitmap_checkcode, flags);
        dest.writeString(name);
        dest.writeString(needString);
    }
}
