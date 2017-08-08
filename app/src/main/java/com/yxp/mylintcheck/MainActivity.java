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
    *调试lint检查过程
    ①、创建remote debug，lint_debug
    
    ②、在terminal中执行下面的命令
    gradlew assembleDebug -Dorg.gradle.daemon=false -Dorg.gradle.debug=true -Dorg.gradle.java.home="C:\\Program Files\\Java\\jdk1.8.0_131"
    然后会显示下面的信息，意味着在等待client
    To honour the JVM settings for this build a new ...
    
    ③、在插件代码打上断点，点击apt_debug的debug、再点击terminal激活界面即可
    * */
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
