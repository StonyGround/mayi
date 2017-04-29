package com.mayizaixian.myzx.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.EditText;

import static com.mayizaixian.myzx.utils.CommonUtils.getDynamicPassword;

/*
   * 监听短信数据库
   */
public class SMSContent extends ContentObserver {
    private Cursor cursor = null;
    private Activity activity;
    private EditText editText;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SMSContent(Handler handler, Activity activity, EditText editText) {
        super(handler);
        this.activity = activity;
        this.editText = editText;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onChange(boolean selfChange) {
        // TODO Auto-generated method stub
        super.onChange(selfChange);
        // 读取收件箱中指定号码的短信
        cursor = activity.managedQuery(Uri.parse("content://sms/inbox"),
                new String[]{"_id", "address", "read", "body"},
                " address=? and read=?",
                new String[]{"1069064013003877", "0"}, "_id desc");
        // 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put("read", "1"); // 修改短信为已读模式
            cursor.moveToNext();
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            editText.setText(getDynamicPassword(smsBody));
        }
        // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }

}
