// IMyAidlInterface.aidl
package com.tiny.wizard.samplebasic;

// Declare any non-default types here with import statements
import com.tiny.wizard.samplebasic.IRemoteServiceCallback;

interface IRemoteService {
     void registerCallback(IRemoteServiceCallback callback);
     void unregisterCallback(IRemoteServiceCallback callback);
}
