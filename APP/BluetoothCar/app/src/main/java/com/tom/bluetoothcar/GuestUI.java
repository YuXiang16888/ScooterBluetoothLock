package com.tom.bluetoothcar;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.io.OutputStream;

public class GuestUI extends BluetoothCar {
    private static OutputStream mOutputStream = null;
    private static String RequestString="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_ui);
    }
    public void CarOpen(View v)  //開啟電門
    {
        try {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "C";
            mOutputStream.write(RequestString.getBytes());
        } catch (Exception e)
        {

        }
    }
    public void CarClose(View v) //關閉電門
    {
        try
        {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "B";
            mOutputStream.write(RequestString.getBytes());
        } catch (Exception e)
        {

        }
    }
    public void OpenAntiTheft(View v)  //開啟防盜模式
    {
        try
        {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "D";
            mOutputStream.write(RequestString.getBytes());
        } catch (Exception e)
        {

        }
    }
    public void CloseAntiTheft(View v) {   //關閉防盜模式
        try
        {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "E";
            mOutputStream.write(RequestString.getBytes());
        } catch (Exception e)
        {

        }
    }
    public void FindCar(View v) //尋車功能
    {
        try
        {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "H";
            mOutputStream.write(RequestString.getBytes());
        } catch (Exception e)
        {

        }
    }
    public void LoginOut(View v)
    {
        try
        {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "K";
            mOutputStream.write(RequestString.getBytes());
            finish();
        }catch (Exception e)
        {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try
        {
            mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
            RequestString = "K";
            mOutputStream.write(RequestString.getBytes());
        }
        catch (Exception e) {}

    }

}
