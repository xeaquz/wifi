package com.example.nearby;

import android.companion.WifiDeviceFilter;
import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceListRecycler extends RecyclerView.Adapter<DeviceListRecycler.ViewHolder> {

    private ArrayList<WifiP2pDevice> data = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDeviceName;
        ViewHolder(View itemView) {
            super(itemView);
            txtDeviceName = itemView.findViewById(R.id.txt_device_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        final WifiP2pDevice device = data.get(pos);
                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        config.wps.setup = WpsInfo.PBC; //이게모지

                        WiFiDirectBroadcastReceiver.mManager.connect(WiFiDirectBroadcastReceiver.mChannel, config, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("wifilogrecycler", "Click_Success!!");
                                //modelDev.setDevice(device, device.deviceName, device.deviceAddress);
                                Log.d("wifilogrecycler", device.deviceAddress + "연결성공");
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.d("wifilogrecycler", "Click_Failed!!");
                            }
                        });
                    }
                }
            });
        }
    }

    DeviceListRecycler(ArrayList<WifiP2pDevice> deviceList) {
        data = deviceList;
    }

    @NonNull
    @Override
    public DeviceListRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.device_recycler, parent, false);
        DeviceListRecycler.ViewHolder vh = new DeviceListRecycler.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListRecycler.ViewHolder holder, int position) {
        String deviceName = data.get(position).deviceName;
        holder.txtDeviceName.setText(deviceName);
    }

    @Override
    public int getItemCount() {
        Log.d("wifilogrecycler", String.valueOf(data.size()));
        return data.size();
    }
}
