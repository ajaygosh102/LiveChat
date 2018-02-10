package com.example.ajay.livechat;


import android.widget.ImageView;

/**
 * Created by ajay on 23/1/18.
 */

public class customadapter  {


     public int ID;
    public String username;
    public String messages;
    public ImageView url;
    public boolean leMien;

    customadapter(int ID, String username, String messages,boolean leMien, ImageView url)
    {
        this.ID=ID;
        this.username=username;
        this.messages=messages;
        this.leMien =leMien;
        this.url=url;
    }
    public boolean getleMien() {
        return leMien;
    }
    public void setleMien(boolean leMien) {
        this.leMien = leMien;
    }


}
