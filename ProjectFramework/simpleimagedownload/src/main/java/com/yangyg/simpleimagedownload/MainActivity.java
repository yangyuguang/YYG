package com.yangyg.simpleimagedownload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yangyg.imageload.DisplayImageOption;
import com.yangyg.imageload.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageLoader loader = ImageLoader.getInstance();
    DisplayImageOption displayImageOption = new DisplayImageOption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loader.init(this);

        ListView mListView = (ListView) findViewById(R.id.mListView);
        mListView.setAdapter(new MyAdapter(getData()));
    }

    private List<ImageBean> getData() {
        List<ImageBean> list = new ArrayList<ImageBean>();
        list.add(getImageBean("http://img3.redocn.com/tupian/20150806/weimeisheyingtupian_4779232.jpg", "小船上有绳子【0】"));
        list.add(getImageBean("http://image72.360doc.com/DownloadImg/2014/05/0402/41292510_3.jpg", "池塘水边【1】"));
        list.add(getImageBean("http://h5.86.cc/walls/20150922/1024x768_0cce092c00c8dec.jpg", "长江大桥【2】"));
        list.add(getImageBean("http://img.daimg.com/uploads/allimg/141115/1-141115150604.jpg", "旭日小树【3】"));
        list.add(getImageBean("http://img2.zjolcdn.com/pic/0/13/66/56/13665652_914292.jpg", "晚霞珠峰【4】"));
        list.add(getImageBean("http://image.tianjimedia.com/uploadImages/2014/128/00/3O9F64F92PM9_1000x500.jpg", "李小璐【5】"));
        list.add(getImageBean("http://tupian.enterdesk.com/2015/saraxuss/04/21/wangbizhi/1/3.jpg", "苍白王珞丹【6】"));
        list.add(getImageBean("http://img2.niutuku.com/desk/374/371-49139.jpg", "周迅带帽子【7】"));
        list.add(getImageBean("http://a.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd11daf25f19bc12c8fcc3ce2d46.jpg", "性感照片【8】"));
        list.add(getImageBean("http://img1.imgtn.bdimg.com/it/u=3757412355,3884139848&fm=21&gp=0.jpg", "大海栈桥【9】"));
        list.add(getImageBean("http://img.taopic.com/uploads/allimg/110111/292-110111035J3100.jpg", "瀑布【10】"));
        list.add(getImageBean("http://img3.iqilu.com/data/attachment/forum/201308/22/091804t7ich4kdlcclzufe.jpg", "海浪【11】"));
        list.add(getImageBean("http://image.tianjimedia.com/uploadImages/2011/286/P72714OAI1N2.jpg", "雪峰【12】"));
        list.add(getImageBean("http://pic4.nipic.com/20090810/3114308_003924034_2.jpg", "金黄的枫叶林【13】"));
        list.add(getImageBean("http://www.sucaitianxia.com/photo/pic/200906/fengus27.jpg", "大山碧水【14】"));
        list.add(getImageBean("http://sc.jb51.net/uploads/allimg/140708/11-140FQ139514M.jpg", "小草蓝天【15】"));
        list.add(getImageBean("http://sc.jb51.net/uploads/allimg/140520/10-140520212515A9.jpg", "海边贝壳【16】"));
        list.add(getImageBean("http://imgsrc.baidu.com/forum/pic/item/3901213fb80e7bec0bb117142f2eb9389a506bdf.jpg", "油菜花【17】"));
        list.add(getImageBean("http://img05.tooopen.com/images/20140910/sy_70359596495.jpg", "牡丹【18】"));
        list.add(getImageBean("http://pic13.nipic.com/20110301/2457331_174932448000_2.jpg", "郁金香【19】"));
        list.add(getImageBean("http://pic.58pic.com/58pic/16/75/00/17358PICmI5_1024.jpg", "玫瑰【20】"));
        list.add(getImageBean("http://f.hiphotos.baidu.com/image/pic/item/b151f8198618367ac7d2a1e92b738bd4b31ce5af.jpg", "香车美女【21】"));
        list.add(getImageBean("http://photocdn.sohu.com/pvrs/background/d0/85/c2bc68febd57b79ecb3c039b5b52f5113cb5.jpg", "美女野兽（动画）【22】"));
        return list;
    }

    private ImageBean getImageBean(String url, String title) {
        return new ImageBean(url, title);
    }

    class MyAdapter extends BaseAdapter {

        private List<ImageBean> mData = null;

        MyAdapter(List<ImageBean> mData) {
            this.mData = mData;
        }

        @Override
        public int getCount() {
            if (mData == null || mData.isEmpty()) {
                return 0;
            }
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout, null);
                vh = new ViewHolder();
                vh.mIV = (ImageView) convertView.findViewById(R.id.mIV);
                vh.mTitle = (TextView) convertView.findViewById(R.id.mTitle);
                vh.mPosition = (TextView) convertView.findViewById(R.id.mPosition);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTitle.setText(mData.get(position).getTitle());
            vh.mPosition.setText("位置："+position);
            loader.displayImage(mData.get(position).getUrl(), vh.mIV, displayImageOption);
            return convertView;
        }

        class ViewHolder {
            ImageView mIV;
            TextView mTitle;
            TextView mPosition;
        }
    }
}
