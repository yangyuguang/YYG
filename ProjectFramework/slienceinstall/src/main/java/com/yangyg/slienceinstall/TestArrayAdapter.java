package com.yangyg.slienceinstall;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by yangyuguang on 16/4/4.
 */
public class TestArrayAdapter extends ArrayAdapter<User>{

    public TestArrayAdapter(Context context, int resource, int textViewResourceId, List<User> objects) {
        super(context, resource, textViewResourceId, objects);
    }


}
