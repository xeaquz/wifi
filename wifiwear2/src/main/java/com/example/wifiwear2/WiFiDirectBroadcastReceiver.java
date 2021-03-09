package com.example.wifiwear2;

import android.companion.WifiDeviceFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MyWiFiActivity mActivity;
    public ArrayList<WifiP2pDevice> peers = new ArrayList<>();

    private String TAG = "wifilog";

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       MyWiFiActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d(TAG, "registered");

        WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());
                Log.d(TAG, "peer: " + peerList.getDeviceList());

            }
        };

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
            int state = intent.getIntExtra(mManager.EXTRA_WIFI_STATE, -1);
            if(state == mManager.WIFI_P2P_STATE_ENABLED) {
                MyWiFiActivity.setIsWifiP2pEnable = true;
            }
            else if(state == mManager.WIFI_P2P_STATE_DISABLED){
                MyWiFiActivity.setIsWifiP2pEnable = false;
            }
            else {

                Log.d(TAG, String.valueOf(state));
            }
            mActivity.checkWiFi();
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");

            if (mManager != null) {
                mManager.requestPeers(mChannel, peerListListener);

            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            Log.d(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }

    }
}