// IRemoteServiceSecond.aidl
package com.tiny.wizard.samplebasic;

// Declare any non-default types here with import statements

interface IRemoteServiceSecond {
    int getPid();
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
