package com.tom.bluetoothcar;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import static android.Manifest.permission.*;

public class BluetoothCar extends AppCompatActivity {
    public static BluetoothAdapter mBluetoothAdapter = null;//宣告藍芽適配器(搜尋、管理藍芽)
    public static BluetoothSocket mBluetoothSocket = null; //連結裝置，傳送指令
    public static BluetoothDevice device=null;
    private final int REQUEST_ENABLE_BT = 1;
    private final int REQUEST_EXTERNEL_PERMISSION=5;
    public static String DeviceName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_car);
        initBluetooth();
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION}, REQUEST_EXTERNEL_PERMISSION);
            }
        }
        else
        {
            Toast.makeText(this,"No Request Permission",Toast.LENGTH_SHORT).show();
        }
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);
    }

    private void initBluetooth() {
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter(); //判斷裝置是否可用藍芽
        if(mBluetoothAdapter==null){
            Toast.makeText(this, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled())//判斷有無開啟藍芽，若無，則跑出請求開啟藍芽的視窗
        {
            Intent mIntentOpenBT=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntentOpenBT,REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_EXTERNEL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"BT Permissions Open!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"BT Permissions Not Open!",Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //判斷藍芽是否打開成功
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"BT Open!",Toast.LENGTH_SHORT).show();
            }
            if (requestCode==RESULT_CANCELED){
                Toast.makeText(this,"BT Not Open",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"BT abnormal",Toast.LENGTH_SHORT).show();
        }
    }
    public void CarAdministrator(View v){
        if (DeviceName.equals("WMWTeam                                               ")||DeviceName.equals("WMWTeam1                                               ")) {
            Intent intent = new Intent(BluetoothCar.this, LoginUI.class);
            intent.putExtra("User", "Administrator");
            startActivity(intent);
        }
        else
        {
            new AlertDialog.Builder(BluetoothCar.this)
                    .setMessage("您的藍芽未連接，或是連接非指定藍芽!")
                    .setTitle("錯誤")
                    .setPositiveButton("確定",null)
                    .show();
        }
    }
    public void CarGuest(View v){
        if(DeviceName.equals("WMWTeam                                               ")) {
            Intent intent = new Intent(BluetoothCar.this, LoginUI.class);
            intent.putExtra("User", "Guest");
            startActivity(intent);
        }
        else
        {
            new AlertDialog.Builder(BluetoothCar.this)
                    .setMessage("您的藍芽未連接，或是連接非指定藍芽!")
                    .setTitle("錯誤")
                    .setPositiveButton("確定",null)
                    .show();
                }
        }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.devicelist){
            Intent intent=new Intent(this,DeviceList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
