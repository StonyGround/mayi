package com.mayizaixian.myzx.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.utils.ConstantUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/10/19.
 */
public class DialogPicSelector extends Dialog implements View.OnClickListener {

    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static String imageDir = "temp.jpg";

    private Context context;

    public DialogPicSelector(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pic_selector);

        findViewById(R.id.photo).setOnClickListener(this);
        findViewById(R.id.local).setOnClickListener(this);
        findViewById(R.id.cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageDir)));
                ((Activity) context).startActivityForResult(intent, ConstantUtils.REQUEST_TAKE_PHOTO);
                DialogPicSelector.this.dismiss();
                break;
            case R.id.local:
                Intent intentLocal = new Intent(Intent.ACTION_GET_CONTENT);
                intentLocal.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intentLocal, null);
                ((Activity) context).startActivityForResult(wrapperIntent, ConstantUtils.REQUEST_PHOTO_ZOOM);
                DialogPicSelector.this.dismiss();
                break;
            case R.id.cancle:
                dismiss();
                break;
        }
    }

    public static void photoZoom(Activity activity, Uri uri) {

        if (activity == null || uri == null) return;

        Intent intent = new Intent();

        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 220);// 输出图片大小
        intent.putExtra("outputY", 220);
        intent.putExtra("return-data", false);

        activity.startActivityForResult(intent, ConstantUtils.REQUEST_PHOTO_RESULT);
    }
}
