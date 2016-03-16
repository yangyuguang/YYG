package com.yangyg.projectframework;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yangyg.http.HttpJSONRequest;
import com.yangyg.test.MD5;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sendHttp).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    HttpJSONRequest request = new HttpJSONRequest();
                    ArrayMap<String, Object> params = new ArrayMap<String, Object>();
                    params.put("username", "13522890823");
                    params.put("pwd", MD5.getMessageDigest("123456"));
                    request.sendRequestJsonParams("tag", "http://abbapi.17yun.com.cn/app/user/login", "post", params, null, new Callback() {

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            // TODO Auto-generated method stub
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "请求成功http", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.e("yyg", "-----请求结果------->" + arg0.body().string());
                        }

                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            // TODO Auto-generated method stub
                            Log.e("yyg", "-----请求错误------->");
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "请求错误http", Toast.LENGTH_SHORT).show();
                                }
                            });

                            arg1.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.sendHttps).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    HttpJSONRequest request = new HttpJSONRequest();
                    ArrayMap<String, Object> params = new ArrayMap<String, Object>();
                    params.put("tel", "18611019406");
                    params.put("password", "321123");
                    params.put("deviceType", "phone");
                    /**
                     *
                     */
//					String url = "https://kyfw.12306.cn";
                    String url = "https://tapp.kangs100.com/doctor/api/20160215/DoctorInfo/login";
                    request.sendRequestJsonParams("tag", url, "post", params, null,
                            new Callback() {

                                @Override
                                public void onResponse(Response arg0) throws IOException {
                                    // TODO Auto-generated method stub
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "请求成功https", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Log.e("yyg", "-----请求结果------->" + arg0.body().string());
                                }

                                @Override
                                public void onFailure(Request arg0, IOException arg1) {
                                    // TODO Auto-generated method stub
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "请求错误https", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Log.e("yyg", "-----请求错误------->");
                                    arg1.printStackTrace();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.uploadOneImage).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        findViewById(R.id.uploadMoreImage).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });


    }


}
