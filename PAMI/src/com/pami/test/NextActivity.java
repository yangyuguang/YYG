package com.pami.test;

import java.util.Random;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.pami.R;
import com.pami.activity.BaseActivity;

public class NextActivity extends BaseActivity{

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
	
	public void nextPage(View v) {
        startActivity(new Intent(this, NextActivity.class));
    }

	@Override
	public void initData() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillView() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAppDownLine() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
