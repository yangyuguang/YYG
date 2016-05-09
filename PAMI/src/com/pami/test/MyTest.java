package com.pami.test;

import android.content.Intent;
import android.view.View;

import com.pami.R;
import com.pami.activity.BaseActivity;

public class MyTest extends BaseActivity {

//	private RecyclerView
	@Override
	public void initViewFromXML() throws Exception {
		setContent(R.layout.activity_main);
		hideTitleBar();
//		showLoadingDialogAndHint("提示信息", "sdfsd");
//		BadgeView bv = (BadgeView) findViewById(R.id.my_badge_view);
//		bv.setBadgeCount(42);
		
		findViewById(R.id.mButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyTest.this,NextActivity.class);
				startActivity(intent);
			}
		});
		
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
		protected boolean isSupportSwipeBack() {
			return false;
		}
	
//	@Override
//	public void onButtonClick(View v) {
//		MLog.e("yyg", "----------onButtonClick------->");
//		super.onButtonClick(v);
//	}
	
	@Override
		protected void onButtonClick(View v) throws Exception {
			super.onButtonClick(v);
		}

	@Override
	public void onDowLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAppDownLine() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void uploadException(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
