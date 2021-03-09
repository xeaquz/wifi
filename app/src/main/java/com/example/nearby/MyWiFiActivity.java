package com.example.nearby;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import java.time.Instant;

import static android.os.Looper.getMainLooper;

public class MyWiFiActivity extends AppCompatActivity {
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    Button btnConnect, btnTag, btnStart, btnStop;

    public static boolean setIsWifiP2pEnable;

    String TAG = "wifilog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnConnect = findViewById(R.id.btn_conenct);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnTag = findViewById(R.id.btn_tag);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);

        btnConnect.setOnClickListener(view -> {
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "success to discover peers : " + channel.toString());
                }

                @Override
                public void onFailure(int reasonCode) {
                    Log.d(TAG, "fail to discover peers: " + reasonCode);
                }
            });
        });

        checkPermission();
        checkWiFi();


//        //obtain a peer from the WifiP2pDeviceList
//        WifiP2pDevice device = null;
//        WifiP2pConfig config = new WifiP2pConfig();
//        config.deviceAddress = device.deviceAddress;
//        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
//
//            @Override
//            public void onSuccess() {
//                //success logic
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                //failure logic
//            }
//        });
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    private void checkPermission() {
        // Assume thisActivity is the current activity
        //앱 설정에서 권한 허용.. 근데 이게 왜 필요함.. 필요하네...
        int permissionCheck1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, String.valueOf(permissionCheck1) + String.valueOf(permissionCheck2));
    }

    public void checkWiFi(){
        if(setIsWifiP2pEnable){
            Toast.makeText(this, "WiFi가 연결되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "설정에서 WiFi를 연결시키세요", Toast.LENGTH_SHORT).show();
        }
    }

}
