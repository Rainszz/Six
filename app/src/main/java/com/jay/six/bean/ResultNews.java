package com.jay.six.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public class ResultNews implements Parcelable {
    private int code;
    private String msg;
    private List<News> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<News> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<News> newslist) {
        this.newslist = newslist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
        dest.writeTypedList(this.newslist);
    }

    public ResultNews() {
    }

    protected ResultNews(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.newslist = in.createTypedArrayList(News.CREATOR);
    }

    public static final Parcelable.Creator<ResultNews> CREATOR = new Parcelable.Creator<ResultNews>() {
        @Override
        public ResultNews createFromParcel(Parcel source) {
            return new ResultNews(source);
        }

        @Override
        public ResultNews[] newArray(int size) {
            return new ResultNews[size];
        }
    };
}
