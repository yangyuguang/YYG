package com.pami.test;

import android.view.View;

import com.pami.R;
import com.pami.activity.BaseActivity;
import com.pami.utils.MLog;
import com.pami.widget.BadgeView;

public class MyTest extends BaseActivity {

//	private RecyclerView
	@Override
	public void initViewFromXML() throws Exception {
		setContent(R.layout.activity_main);
		showLoadingDialogAndHint("提示信息", "sdfsd");
//		BadgeView bv = (BadgeView) findViewById(R.id.my_badge_view);
//		bv.setBadgeCount(42);
		
		
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

	
}
