package com.tom.bluetoothcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginUI extends BluetoothCar {

    private static String Userstring = "";
    private static String Password1 = "";
    private static String Password2 = "";
    private static String Passwordregister = "";
    private static EditText PasswordEdit = null;
    private static TextView identity = null;
    private static Button LoginButton = null;
    private static OutputStream mOutputStream = null;
    private static InputStream mInputStream = null;
    private static CheckBox DisplayPassword=null;
    private static String CheckWord="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class Login extends Thread {
        private String requestpassword = "";
        private String response="";
        private String response1="";
        @Override
        public void run() {
            try
            {
                mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
                requestpassword = "A";
                byte[] buffer = requestpassword.toString().getBytes();
                mOutputStream.write(buffer);

            }
            catch (Exception e)
            {

            }
            while (true)
            {
                    try
                    {
                        mInputStream = BluetoothCar.mBluetoothSocket.getInputStream();
                        byte[] receive = new byte[30];
                        int bytes = mInputStream.read(receive);
                        response = new String(receive, 0, bytes);
                        if (response.equals("OK")) {
                            break;
                        }
                    }
                    catch (Exception e)
                    {

                    }
            }
            if ((Userstring.equals("Administrator")))
            {
                try {
                        mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
                        Password1=getMD5(Passwordregister);   //MD5加密
                        Password1=Password1+'@';
                        Log.d("Enter",Password1);
                        byte[] buffer = Password1.getBytes();
                        mOutputStream.write(buffer);

                    }
                    catch (Exception e)
                    {

                    }
            }
            else if ((Userstring.equals("Guest")))
            {
                try
                {
                        mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
                        Password2=getMD5(Passwordregister);  //MD5加密
                        Password2=Password2+'!';
                        Log.d("Enter",Password2);
                        byte[] buffer = Password2.getBytes();
                        mOutputStream.write(buffer);

                }catch (Exception e) {}
            }
            while (true)
            {
                    try
                    {
                        mInputStream = BluetoothCar.mBluetoothSocket.getInputStream();
                        byte[] receive1 = new byte[30];
                        int bytes = mInputStream.read(receive1);
                        response1 = new String(receive1, 0, bytes);
                        Log.d("Enter",response1);
                        if(!(response1.equals("")))
                        {
                            break;
                        }
                    }
                    catch (Exception e)
                    {

                    }
            }
            if(response1.equals("Right")&&Userstring.equals("Administrator"))
            {
                SharedPreferences setting=getSharedPreferences("bluetoothcarpassword",MODE_PRIVATE);
                setting.edit()
                        .putString("StoreAdministratorPassword",PasswordEdit.getText().toString())
                        .commit();
                Intent intent = new Intent(LoginUI.this, AdministratorUI.class);
                startActivity(intent);
            }
            else if(response1.equals("Right")&&Userstring.equals("Guest"))
            {
                Intent intent = new Intent(LoginUI.this, GuestUI.class);
                startActivity(intent);
            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(LoginUI.this)
                                .setMessage("密碼錯誤，請重新輸入")
                                .setTitle("密碼錯誤")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                });
            }
        }

    }

    public void login(View v)//登入按鈕
    {
        Passwordregister=PasswordEdit.getText().toString();
        new Thread(new Login()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PasswordEdit=(EditText)findViewById(R.id.PasswordEdit);
        identity=(TextView)findViewById(R.id.IDTextView);
        LoginButton=(Button)findViewById(R.id.LoginButton);
        Intent intent=getIntent();
        Userstring=intent.getStringExtra("User");
        identity.setText(Userstring);
        DisplayPassword=(CheckBox)findViewById(R.id.DisplayPassword);
        PasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        DisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DisplayPassword.isChecked()) {//設定是否顯示密碼
                    PasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    PasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        if(Userstring.equals("Administrator"))
        {
            SharedPreferences devicename=getSharedPreferences("bluetoothcarpassword",MODE_PRIVATE);
            PasswordEdit.setText(devicename.getString("StoreAdministratorPassword",""));
        }
        else
        {
            PasswordEdit.setText("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static String getMD5(String val){ //加密
        byte[] m=null;
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            m = md5.digest();//加密
        }
        catch (NoSuchAlgorithmException e)
        {}
        return getString(m);
    }

    private static String getString(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int a = b[i];
            if (a < 0)
                a += 256;
            if (a < 16)
                buf.append("0");
            buf.append(Integer.toHexString(a));

        }
        return buf.toString().substring(8, 24);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub, menu);
        return true;
    }
}
