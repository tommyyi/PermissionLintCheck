package com.yxp.mylintcheck;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

public class MainActivity extends AppCompatActivity
{
    /*
    gradlew build -info -Dorg.gradle.daemon=false -Dorg.gradle.debug=true -Dorg.gradle.java.home="C:\\Program Files\\Java\\jdk1.8.0_131"

    lint JVM启动项
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
    server =y 表示监听debuuger的attach请求
    suspend =y 表示在debugger连接前，服务端被挂起
    address 监听端口，在5005接收debugger发起的TCP连接
    
    lint-debugger
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
    server =y 表示监听debuuger的attach请求，这里debugger不会收到另一个debugger的attach请求，所以没有意义
    suspend =y 表示debugger JVM直接运行
    address=5005 表示debugger向5005发出attach请求
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void accessIMEI()
    {
        TelephonyManager tm = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        CellLocation cellLocation = tm.getCellLocation();
    }
    
    /*WRITE_EXTERNAL_STORAGE*/
    public void accessSDCard()
    {
        File directory = Environment.getExternalStorageDirectory();
    }
    
    /*WRITE_EXTERNAL_STORAGE*/
    public void accessCall()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        startActivity(intent);
    }
}
