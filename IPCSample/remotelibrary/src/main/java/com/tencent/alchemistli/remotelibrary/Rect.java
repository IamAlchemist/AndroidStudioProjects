package com.tencent.alchemistli.remotelibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alchemistli on 3/31/15.
 */
public class Rect implements Parcelable {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public static final Creator<Rect> CREATOR = new Creator<Rect>(){

        @Override
        public Rect createFromParcel(Parcel source) {
            return new Rect(source);
        }

        @Override
        public Rect[] newArray(int size) {
            return new Rect[size];
        }
    };

    public Rect(){
    }

    public Rect(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source){
        left = source.readInt();
        top = source.readInt();
        right = source.readInt();
        bottom = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(left);
        dest.writeInt(top);
        dest.writeInt(right);
        dest.writeInt(bottom);
    }
}
