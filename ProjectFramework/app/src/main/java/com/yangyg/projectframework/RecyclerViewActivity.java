package com.yangyg.projectframework;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yangyg.adapter.*;
import com.yangyg.adapter.RecyclerViewHoler;
import com.yangyg.widget.YYGRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyuguang on 16/4/12.
 */
public class RecyclerViewActivity extends Activity{


    private static final int REFRESH = 0;
    private static final int LOAD = 1;

    private YYGRecyclerView mListView = null;
    private MyAdapter adapter = null;
    private SwipeRefreshLayout refreshLayout = null;
    private List<UserBean> mData = new ArrayList<UserBean>();

    private int currentIndex = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH:{
                    currentIndex = 0;
                    mData.clear();
                    addData();
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    break;
                }

                case LOAD:{
                    addData();
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_activity);


        initView();
        initData();
        initListener();

    }

    private void initListener() {
        mListView.setOnRefreshAndLoadListener(new YYGRecyclerView.OnRefreshAndLoadListener() {
            @Override
            public void onLoad() {
                mHandler.sendEmptyMessageDelayed(LOAD,3000);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("recyclerView","------【2】------>"+refreshLayout.isRefreshing());
                    refreshLayout.setRefreshing(true);
                    mHandler.sendEmptyMessageDelayed(REFRESH,3000);
            }
        });

    }

    private void initData() {
        addData();
        adapter = new MyAdapter(this,R.layout.recycler_view_item,mData);
        mListView.setAdapter(adapter);
    }

    private void initView() {
        mListView = (YYGRecyclerView) findViewById(R.id.mListView);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
    }

    private void addData() {
        for(int i=0;i<10;i++){

            UserBean bb = new UserBean();
            bb.setUserName("测试："+currentIndex);

            mData.add(bb);
            currentIndex++;

        }
    }

    class MyAdapter extends YYGRecyclerViewBaseAdapter<UserBean> {

        public MyAdapter(Context mContext, int layoutId, List<UserBean> mData) {
            super(mContext, layoutId, mData);
        }

        @Override
        public void getView(RecyclerViewHoler holer, UserBean userBean, int position) {
            try {
                holer.setText(R.id.mTextView,userBean.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
