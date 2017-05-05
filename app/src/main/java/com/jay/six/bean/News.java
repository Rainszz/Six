package com.jay.six.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public class News implements Parcelable {

    /**
     * ctime : 2017-05-05 14:52
     * title : 武术名将联合发声指责徐晓冬言论 劝其回头是岸
     * description : 搜狐社会
     * picUrl : http://photocdn.sohu.com/20170505/Img491832797_ss.jpeg
     * url : http://news.sohu.com/20170505/n491844638.shtml
     */

    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ctime);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.picUrl);
        dest.writeString(this.url);
    }

    public News() {
    }

    protected News(Parcel in) {
        this.ctime = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.picUrl = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
