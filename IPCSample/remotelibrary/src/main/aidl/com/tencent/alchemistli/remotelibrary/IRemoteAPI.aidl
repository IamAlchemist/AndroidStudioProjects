// IRemoteAPI.aidl
package com.tencent.alchemistli.remotelibrary;

// Declare any non-default types here with import statements
import com.tencent.alchemistli.remotelibrary.Student;

interface IRemoteAPI {
    Student getName();
    void setName(in Student st);
}
