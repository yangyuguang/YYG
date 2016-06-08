package com.yangyg.projectframework;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yangyg.http.HttpJSONRequest;
import com.yangyg.test.MD5;
import com.yangyg.utils.MLog;

import java.io.IOException;

public class MainActivity extends Activity {

    private IntentFilter mIntentFilter = null;
    private WiFiDirectBroadcastReceiver mReceiver = null;

    private WifiP2pManager mManager = null;
    private WifiP2pManager.Channel mChannel = null;

    private AppCompatActivity dpd = null;
    private BluetoothSocket bs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testConnection();
        textWifiP2P();
    }

    /**
     * 测试 wifi p2p
     */
    private void textWifiP2P() {
//        ActivityManagerNative.getDefault()
//        overridePendingTransition();
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                MLog.e("wifip2pyyg", "onChannelDisconnected");
            }
        });

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);


        //注册广播
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                MLog.e("wifip2pyyg", "discoverPeers  onSuccess");
            }

            @Override
            public void onFailure(int reasonCode) {
                MLog.e("wifip2pyyg", "discoverPeers  onFailure:" + reasonCode);
            }
        });
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    /**
     * 测试用okhttp发送请求
     */
    private void testConnection() {
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
