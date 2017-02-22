package com.lmface.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.lmface.R;
import com.lmface.dialog.BaseDialog;
import com.lmface.huanxin.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PhotoGet {
    private View avatorView;
    private String headIconPath;
    private static final int TAKEPHOTO = 1; // 拍照
    private static final int GALLERY = 2; // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3; // 结果
    private File headFile;
    private String headBase64;
    private Context context;
    private BaseDialog dialog;

    private static PhotoGet mInstance;
    private PhotoGet(){}

    public File getHeadFile() {
        return headFile;
    }

    public void setHeadFile(File headFile) {
        this.headFile = headFile;
    }

    public static PhotoGet getInstance(){
        if (mInstance==null){
            mInstance=new PhotoGet();
        }
        return mInstance;
    }

//    @Overridea
//    public void OnViewClick(View v) {
//        switch (v.getId()){
//            case R.id.tv_save:
//               // String intro=et_intro.getText().toString().trim();
////                if (TextUtils.isEmpty(intro)) {
////                    et_intro.setError("店铺描述不能为空");
////                    et_intro.requestFocus();
////                    return;
////                }
//                if(headBase64!=null){
//                    TreeMap<String,String> treeMap1=new TreeMap<String, String>();
//                    treeMap1.put("operation", "setlogo");
//                    treeMap1.put("shop_logo", headBase64);
//                    treeMap1.put("token", token);
//                    setStoreInfo(treeMap1);
//                }else {
//                    showToast("请选择图片！");
//                }
////                TreeMap<String,String> treeMap3=new TreeMap<String, String>();
////                treeMap3.put("operation", "setbase");
////                treeMap3.put("shop_desc", intro);
////                treeMap3.put("token", token);
////                setStoreInfo(treeMap3);
//                break;
//            case R.id.iv_storeicon:
//                showAvatarDialog();
//                break;
//            case R.id.photo_pop_tv_capture:
//                dialog.dismiss();
//                startCameraPicCut();
//                break;
//            case R.id.photo_pop_tv_album:
//                dialog.dismiss();
//                startImageCaptrue();
//                break;
//            case R.id.photo_pop_tv_cancel:
//                dialog.dismiss();
//                break;
//        }
//    }

    public void showAvatarDialog(Context context,BaseDialog dialog) {
        this.context=context;
        this.dialog=dialog;
        /**
         //         * 头像选择
         //         */

            avatorView = ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_modify_avator, null);
            avatorView.findViewById(R.id.photo_pop_tv_capture).setOnClickListener(new PhotoOnClickListener());
            avatorView.findViewById(R.id.photo_pop_tv_album).setOnClickListener(new PhotoOnClickListener());
            avatorView.findViewById(R.id.photo_pop_tv_cancel).setOnClickListener(new PhotoOnClickListener());

        dialog.show(avatorView);
    }
    class PhotoOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.photo_pop_tv_capture:
                dialog.dismiss();
                startCameraPicCut();
                break;
            case R.id.photo_pop_tv_album:
                dialog.dismiss();
                startImageCaptrue();
                break;
            case R.id.photo_pop_tv_cancel:
                dialog.dismiss();
                break;
            }
        }
    }
    private void startCameraPicCut() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String imageSavePath = Constant.SAVED_IMAGE_DIR_PATH;
            File dir = new File(imageSavePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 调用系统的拍照功能
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra("camerasensortype", 2); // 调用前置摄像头
            intent.putExtra("autofocus", true); // 自动对焦
            intent.putExtra("fullScreen", false); // 全屏
            intent.putExtra("showActionIcons", false);
            // 指定调用相机拍照后照片的储存路径
            headIconPath = imageSavePath + File.separator + "userHeader" + ".jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(headIconPath)));
            ((Activity)context).startActivityForResult(intent, TAKEPHOTO);
        } else {
            ToastUtil.showToast("请确认已经插入SD卡");
        }
    }

    public String getHeadIconPath(){
        if (headIconPath!=null)
            return headIconPath;
        else return null;
    }

    private void startImageCaptrue() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity)context).startActivityForResult(intent, GALLERY);
    }

    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        ((Activity)context).startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片上传
    public void saveImage(Intent picdata,ImageView imageView) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            final Bitmap header = bundle.getParcelable("data");
            //保存图片到本地
            headFile = new File(BaseViewUtils.getFileSavePath(context) + "head.png");
            // 将头像显示出来
            imageView.setImageBitmap(header);

            //headBase64 = bitmaptoString(header, 100);
            headBase64 = "data:image/jpeg;base64,";
            headBase64 = headBase64 + bitmaptoString(header, 100);

            BaseViewUtils.saveBitmap(header,headFile);
            dialog.dismiss();
            dialog=null;
        }
    }

    /**
     * 　　* 将bitmap转换成base64字符串
     * 　　* @param bitmap
     * 　　* @return base64 字符串
     */
    public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return string;
    }
}
