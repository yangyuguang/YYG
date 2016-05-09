package com.yangyg.projectframework;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yangyg.activity.BaseActivity;

import java.util.Random;

public class NextActivity extends BaseActivity {
    @Override
    public void onAppDownLine() throws Exception {

    }

    @Override
    public void handleActionError(String actionName, Object object) {

    }

    @Override
    public void handleActionSuccess(String actionName, Object object) {

    }

    @Override
    public void initViewFromXML() throws Exception {
        setContent(R.layout.activity_next);
        hideTitleBar();

        RelativeLayout containerRl = (RelativeLayout) findViewById(R.id.container);

        //随机色

        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);

        containerRl.setBackgroundColor(Color.argb(255,red,green,blue));
    }

    @Override
    public void initData() throws Exception {

    }

    @Override
    public void fillView() throws Exception {

    }

    @Override
    public void initListener() throws Exception {

    }

    public void nextPage(View v) {
        startActivity(new Intent(this, NextActivity.class));
    }

}
