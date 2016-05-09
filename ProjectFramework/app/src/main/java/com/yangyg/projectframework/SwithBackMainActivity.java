package com.yangyg.projectframework;

import android.content.Intent;
import android.view.View;

import com.yangyg.activity.BaseActivity;

/**
 * Created by yangyuguang on 16/4/22.
 */
public class SwithBackMainActivity extends BaseActivity{
    @Override
    public void onAppDownLine() throws Exception {

    }

    @Override
    public void handleActionError(String s, Object o) {

    }

    @Override
    public void handleActionSuccess(String s, Object o) {

    }

    @Override
    public void initViewFromXML() throws Exception {
        setContent(R.layout.swith_back_main_activity);
        hideTitleBar();

        findViewById(R.id.mButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwithBackMainActivity.this,NextActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
