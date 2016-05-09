package com.yangyg.projectframework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import com.yangyg.utils.MLog;

import java.util.Collection;
import java.util.Iterator;


/**
 * Created by yangyuguang on 16/3/23.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private MainActivity mActivity = null;
    private WifiP2pManager mManager = null;
    private WifiP2pManager.Channel mChannel = null;

    private WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener(){
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            MLog.e("wifip2pyyg receiver", "PeerListListener  onPeersAvailable : " + peers.describeContents());

            Collection<WifiP2pDevice> list = peers.getDeviceList();
            if(list != null && !list.isEmpty()){
                Iterator<WifiP2pDevice> ii = list.iterator();
                while(ii.hasNext()){
                    WifiP2pDevice device = ii.next();
                    MLog.e("wifip2pyyg receiver", "PeerListListener  device  " + device.deviceAddress+" , "+device.deviceName);
                }
            }

        }
    };

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel mChannel, MainActivity activity) {
        this.mActivity = activity;
        this.mManager = manager;
        this.mChannel = mChannel;
    }

    /**
     * 03-23 18:17:14.370: E/LGS: wifip2pyyg(25392): discoverPeers  onSuccess
     03-23 18:17:14.371: E/LGS: wifip2pyyg receiver(25392): WIFI_P2P_STATE_CHANGED_ACTION  启用
     03-23 18:17:14.372: E/LGS: wifip2pyyg receiver(25392): WIFI_P2P_CONNECTION_CHANGED_ACTION
     03-23 18:17:14.373: E/LGS: wifip2pyyg receiver(25392): WIFI_P2P_THIS_DEVICE_CHANGED_ACTION

     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            /**
             * Check to see if Wi-Fi is enabled and notify appropriate activity
             * 检查是否启用了无线网络连接，并通知相应的活动
             */

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled 启用
                MLog.e("wifip2pyyg receiver", "WIFI_P2P_STATE_CHANGED_ACTION  启用");
            } else {
                // Wi-Fi P2P is not enabled 没有启用
                MLog.e("wifip2pyyg receiver", "WIFI_P2P_STATE_CHANGED_ACTION  没有启用");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            /**
             * 呼叫WifiP2pManager.requestPeers（）来获得当前的同龄人名单
             * Call WifiP2pManager.requestPeers() to get a list of current peers
             */
            MLog.e("wifip2pyyg receiver", "WIFI_P2P_PEERS_CHANGED_ACTION");
            if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            /**
             * 应对新的连接或断开
             * Respond to new connection or disconnections
             */
            MLog.e("wifip2pyyg receiver", "WIFI_P2P_CONNECTION_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            /**
             * Respond to this device's wifi state changing
             * 回应此设备的WiFi状态改变
             */
            MLog.e("wifip2pyyg receiver", "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }
}
