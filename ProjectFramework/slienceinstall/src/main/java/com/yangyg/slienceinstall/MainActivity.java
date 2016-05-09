package com.yangyg.slienceinstall;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {


//    private IPackageManager mPm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
