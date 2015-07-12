// IRemoteService.aidl
package com.tencent.alchemistli.ipcsample;

// Declare any non-default types here with import statements

interface IRemoteService {
    int getPid();
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
