package com.tom.bluetoothcar;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Set;
import java.util.UUID;
public class DeviceList extends BluetoothCar{
    private static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static ArrayAdapter<String> mArrayAdapter=null;
    private static ListView searchList = null;
    private static String bluetoothname;
    private static int FirstScan=0;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) { //當搜尋到裝置
                Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices(); //搜尋綁定
                BluetoothCar.device = devices.iterator().next();
                //BluetoothCar.device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(FirstScan==0)
                {
                    bluetoothname=BluetoothCar.device.getName();
                    bluetoothname=bluetoothname+"                                               ";
                    mArrayAdapter.add(bluetoothname);
                    mArrayAdapter.notifyDataSetChanged();
                }
                if(BluetoothCar.device.getName().concat("                                               ").equals(BluetoothCar.DeviceName)) {
                    BluetoothCar.mBluetoothAdapter.cancelDiscovery();
                    new Thread(new ConnectThread(BluetoothCar.device)).start();
                }

        }
    }
};

private class ConnectThread extends Thread{
    private BluetoothDevice device;

    public ConnectThread(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public void run() {
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DeviceList.this,"開始連接",Toast.LENGTH_SHORT).show();
                }
            });
            BluetoothCar.mBluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            BluetoothCar.mBluetoothSocket.connect();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DeviceList.this,"連接上"+device.getName(),Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }
        catch (Exception e){

        }
    }
}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        searchList=(ListView) findViewById(R.id.list);
        mArrayAdapter = new ArrayAdapter<String>(DeviceList.this, android.R.layout.simple_list_item_1, android.R.id.text1);
        SharedPreferences devicename=getSharedPreferences("bluetoothcar",MODE_PRIVATE);
        BluetoothCar.DeviceName=devicename.getString("StoreDeviceName","");
        BluetoothCar.mBluetoothAdapter.startDiscovery();
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirstScan=1;
                String BtName=searchList.getItemAtPosition(position).toString();
                SharedPreferences storeBluetoothname=getSharedPreferences("bluetoothcar",MODE_PRIVATE);
                storeBluetoothname.edit()
                        .putString("StoreDeviceName",BtName)
                        .commit();
                BluetoothCar.DeviceName=storeBluetoothname.getString("StoreDeviceName","");
                BluetoothCar.mBluetoothAdapter.cancelDiscovery();
                BluetoothCar.mBluetoothAdapter.startDiscovery();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//註冊藍芽接收器
        registerReceiver(mReceiver, filter);//註冊藍芽接收器
        searchList.setAdapter(mArrayAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub, menu);
        return true;
    }


}

