package com.pami.test;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.pami.R;
import com.pami.activity.BaseActivity;
import com.pami.service.DownloadAppService;
import com.pami.utils.MLog;
import com.pami.widget.BadgeView;
import com.pami.widget.DragListView;
import com.pami.widget.SquaredPassWord;
import com.pami.widget.SquaredPassWord.PasswordSettingOverListener;

public class MyTest extends BaseActivity {

//	private MenuDrawer menuDrawer = null;
//	private DragListView lv = null;
//	private TestAdapter adapter = null;
	
//	private SquaredPassWord passWord = null;
	@Override
	public void initViewFromXML() throws Exception {
		setContent(R.layout.activity_main);
		
		BadgeView bv = (BadgeView) findViewById(R.id.my_badge_view);
		bv.setBadgeCount(42);
		
//		passWord = (SquaredPassWord) findViewById(R.id.id_squared);
//		passWord.setListener(new PasswordSettingOverListener() {
//			
//			@Override
//			public void onOverSetting(String passwordStr) {
//				// TODO Auto-generated method stub
//				Toast.makeText(MyTest.this, passwordStr, 1).show();
//			}
//		});
//		showLoadingDialog();
		
//		findViewById(R.id.send_request).setOnClickListener(this);
//		lv = (DragListView) findViewById(R.id.lv);
//		adapter = new TestAdapter(mContext, getData());
//		lv.setAdapter(adapter);
		
//		Intent ii = new Intent(this, DownloadAppService.class);
//		ii.putExtra("app_url", "http://dldir1.qq.com/weixin/android/weixin610android540.apk");
//		ii.putExtra("app_url", "http://gdown.baidu.com/data/wisegame/f98d235e39e29031/baiduxinwen.apk");
//		startService(ii);
		
	}
	
	private List<String> getData(){
		List<String> data = new ArrayList<String>();
		for(int a = 0;a<= 20;a++){
			data.add("测试"+a);
		}
		return data;
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
	public void onButtonClick(View v) {
		MLog.e("yyg", "----------onButtonClick------->");
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
