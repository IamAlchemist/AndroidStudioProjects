package com.tencent.alchemistli.ipcsample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tencent.alchemistli.remotelibrary.IRemoteAPI;
import com.tencent.alchemistli.remotelibrary.RemoteAPIService;
import com.tencent.alchemistli.remotelibrary.Student;


public class MainActivity extends ActionBarActivity {

    IRemoteService remoteService;
    IRemoteAPI remoteAPI;

    private TextView textView1;
    private TextView textView2;

    private ServiceConnection remoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
        }
    };

    private ServiceConnection remoteAPIConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteAPI = IRemoteAPI.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.text_view1);
        textView2 = (TextView)findViewById(R.id.text_view2);

        Intent intent = new Intent(MainActivity.this, AIDLService.class);
        bindService(intent, remoteServiceConnection, BIND_AUTO_CREATE);
        startService(intent);

        Intent intent1 = new Intent(MainActivity.this, RemoteAPIService.class);
        bindService(intent1, remoteAPIConnection, BIND_AUTO_CREATE);
        startService(intent1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void button1Clicked(View view) {
        try {
            int temp = remoteService.getPid();
            textView1.setText(String.valueOf(temp));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button2Clicked(View view) {
        Student st1 = new Student();
        st1.setName("Rahul");
        st1.setFatherName("Nand Kishor");
        try {
            remoteAPI.setName(st1);
            textView2.setText(remoteAPI.getName().name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
