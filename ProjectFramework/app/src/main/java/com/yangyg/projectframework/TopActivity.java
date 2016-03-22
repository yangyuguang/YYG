package com.yangyg.projectframework;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by yangyuguang on 16/3/21.
 */
public class TopActivity extends Activity implements TopView{

    protected TopPresenter mTopPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        mTopPresenter = new TopPresenter();
        mTopPresenter.onCreate(null);
    }


    @Override
    public void initViews() {

    }

    @Override
    public void openDatePickerDialog() {

    }

    @Override
    public void startListActivity() {

    }
}
