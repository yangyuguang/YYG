package com.pami.test;

import android.os.Bundle;
import android.widget.GridView;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import com.pami.R;
import com.pami.adapter.DragGridViewAdapter;
import com.pami.widget.DragGridView;


public class MainActivity extends Activity {
    private List<String> strList;
    private DragGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    public void initData(){
        strList = new ArrayList<String>();
        for(int i = 0; i < 10; i++){
            strList.add("item:"+i);
        }
    }

    private void initView() {
//        gridView = (DragGridView)findViewById(R.id.drag_grid_view);
        gridView.setItemNoDrag(strList.size() - 1);
        MyAdapter adapter = new MyAdapter(this, strList, 1);
        gridView.setAdapter(adapter);
    }
}
