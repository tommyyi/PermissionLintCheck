package com.yxp.mylintcheck;

import java.io.File;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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

    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    private static final int PHONES_CONTACT_ID_INDEX = 3;

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

    public void contact()
    {
        ContentResolver resolver = getContentResolver();

        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        // 得到联系人ID
        Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

        Uri uri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactid);
        InputStream input = ContactsContract.Contacts
                                    .openContactPhotoInputStream(resolver, uri);

        input = ContactsContract.Contacts
                        .openContactPhotoInputStream(resolver, uri);
    }

    private void calendar()
    {
        Uri contentUri = CalendarContract.Calendars.CONTENT_URI;
    }

    private void camera()
    {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    private void location()
    {
        Object systemService = getSystemService(Context.LOCATION_SERVICE);
    }
}
