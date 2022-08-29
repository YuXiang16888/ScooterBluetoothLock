package com.tom.bluetoothcar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePassword extends BluetoothCar {
    private static EditText MainPasswordEdit = null;
    private static EditText SecondaryPasswordEdit = null;
    private static OutputStream mOutputStream = null;
    private static InputStream mInputStream = null;
    private static String CheckWord = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.changepasswd_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class SendPasswordThread extends Thread {
        @Override
        public void run() {
            try {
                if (!(MainPasswordEdit.getText().toString().equals("")) || !(SecondaryPasswordEdit.getText().toString().equals(""))) {
                    if (!(MainPasswordEdit.getText().toString().equals(""))) {
                        new Thread(new SendData("I")).start();
                        String MainPasswd = "";
                        new Thread(new ReceiveData()).start();
                        while (true) {
                            if (CheckWord.equals("OK")) {
                                MainPasswd = getMD5(MainPasswordEdit.getText().toString()) + '@';
                                new Thread(new SendData(MainPasswd)).start();
                                CheckWord = "";
                                break;
                            }
                        }
                        //mOutputStream.write(MainPasswd.getBytes());
                        new Thread(new ReceiveData()).start();
                        while (true) {
                            if ((CheckWord.equals("success"))) {
                                break;
                            }
                        }
                        if (CheckWord.equals("success")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ChangePassword.this)
                                            .setMessage("車主密碼更改完成")
                                            .setTitle("更改完成")
                                            .setPositiveButton("確定", null)
                                            .show();
                                }
                            });
                            CheckWord = "";
                        } else if (!(CheckWord.equals("success"))) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ChangePassword.this)
                                            .setMessage("因資料傳輸發生遺失，導致失敗，請再試一次!")
                                            .setTitle("更改失敗")
                                            .setPositiveButton("確定", null)
                                            .show();
                                }
                            });
                        }
                    } else if (!(SecondaryPasswordEdit.getText().toString().equals(""))) {
                        new Thread(new SendData("I")).start();
                        String SecondPasswd = "";
                        new Thread(new ReceiveData()).start();
                        while (true) {
                            if (CheckWord.equals("OK")) {
                                SecondPasswd = getMD5(SecondaryPasswordEdit.getText().toString()) + '!';
                                new Thread(new SendData(SecondPasswd)).start();
                                CheckWord = "";
                                break;
                            }
                        }
                        new Thread(new ReceiveData()).start();
                        while (true) {
                            if ((CheckWord.equals("success"))) {
                                break;
                            }
                        }
                        if (CheckWord.equals("success")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ChangePassword.this)
                                            .setMessage("借車人密碼更改完成")
                                            .setTitle("更改完成")
                                            .setPositiveButton("確定", null)
                                            .show();
                                }
                            });
                            CheckWord = "";
                        }
                        else if (!(CheckWord.equals("success")))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ChangePassword.this)
                                            .setMessage("因資料傳輸發生遺失，導致失敗，請再試一次!")
                                            .setTitle("更改失敗")
                                            .setPositiveButton("確定", null)
                                            .show();
                                }
                            });
                        }

                    }
                }
                else
                {
                    new AlertDialog.Builder(ChangePassword.this)
                            .setMessage("更換密碼欄位不可皆為空")
                            .setTitle("錯誤")
                            .setPositiveButton("OK", null)
                            .show();
                }
            } catch (Exception e) {
            }
        }

    }
    public void SendPassword(View v)
    {
        new Thread(new SendPasswordThread()).start();
    }
    private class ReceiveData extends Thread {
        @Override
        public void run() {
            try {
                mInputStream = BluetoothCar.mBluetoothSocket.getInputStream();
                byte[] receive = new byte[30];
                while (true)
                {

                    int bytes = mInputStream.read(receive);
                    CheckWord += new String(receive, 0, bytes);
                    if(CheckWord.equals("OK")||CheckWord.equals("success"))
                    {
                        break;
                    }
                }
            } catch (Exception e)
            {

            }
        }
    }

    public class SendData extends Thread {
        String str1;

        public SendData(String str) {
            str1 = str;
        }

        @Override
        public void run() {
            try {
                mOutputStream = BluetoothCar.mBluetoothSocket.getOutputStream();
                mOutputStream.write(str1.getBytes());
            } catch (Exception e) {

            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MainPasswordEdit = (EditText) findViewById(R.id.MainPassword);
        SecondaryPasswordEdit = (EditText) findViewById(R.id.SecondaryPassword);
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
    public static String getMD5(String val) { //加密
        byte[] m = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            m = md5.digest();//加密
        } catch (NoSuchAlgorithmException e) {
        }
        return getString(m);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub, menu);
        return true;
    }
}
