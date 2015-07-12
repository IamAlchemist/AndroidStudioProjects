package com.tencent.alchemistli.remotelibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alchemistli on 3/31/15.
 */
public class Student implements Parcelable {
    public String name;
    public String fatherName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(fatherName);
    }

    public Student(Parcel source){
        name = source.readString();
        fatherName = source.readString();
    }

    public Student(){}

    public void setName(String name){
        this.name = name;
    }

    public void setFatherName(String fatherName){
        this.fatherName = fatherName;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
