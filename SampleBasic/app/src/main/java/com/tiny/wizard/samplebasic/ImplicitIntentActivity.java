package com.tiny.wizard.samplebasic;
// Created by wizard on 1/30/15.

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImplicitIntentActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit_intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == Util.REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            ImageView imageView = (ImageView)findViewById(R.id.activity_implicit_intent_image);
            imageView.setImageURI(uri);
        }
    }

    public void getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, Util.REQUEST_IMAGE_GET);
        }
    }


}
