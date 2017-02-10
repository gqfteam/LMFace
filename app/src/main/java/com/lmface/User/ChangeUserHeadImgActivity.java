package com.lmface.User;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_msg;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/9.
 */

public class ChangeUserHeadImgActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.add_goods_toolbar)
    Toolbar addGoodsToolbar;
    @BindView(R.id.user_head_img)
    ImageView userHeadImg;
    @BindView(R.id.select_user_head_img)
    Button selectUserHeadImg;
    @BindView(R.id.change_user_head_img)
    Button changeUserHeadImg;


    private static final int PICK_CODE = 0;
    private static final int CAMERA_CODE=1;

    private String currentphoto;
    private Bitmap mPhotoimg;
    private File mPhotoFile;
    private String mPhotoPath;


    public void setToolbar(String str) {
        addGoodsToolbar.setTitle(str);
        setSupportActionBar(addGoodsToolbar);
        addGoodsToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        addGoodsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_head_img);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("");
    }

    public static final int RESULT_LOAD_IMAGE=101;
    @OnClick({R.id.select_user_head_img, R.id.change_user_head_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_user_head_img:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.change_user_head_img:
                if(mPhotoFile!=null){
                    changeUserHeadImg();
                }else{
                    Toast.makeText(getApplicationContext(),"请从图库或相机中获取一张图片",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void changeUserHeadImg(){
        Log.i("gqf","changeUserHeadImg"+mPhotoFile.getName()+mPhotoFile.getName());

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), mPhotoFile);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("zichifile", mPhotoFile.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);
        MultipartBody mb=builder.build();

        int userId=realm.where(user_msg.class).findFirst().getUiId();


        Subscription subscription = NetWork.getUserService().changeUserHeadImg(mb.part(0),userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode s) {
                        if(s.getCode()==10000){
                            user_msg um=realm.where(user_msg.class).findFirst();
                            realm.beginTransaction();
                            um.setHeadimg(s.getMsg());
                            realm.copyToRealmOrUpdate(um);
                            realm.commitTransaction();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),s.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mcompositeSubscription.add(subscription);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("gqf","onActivityResult");
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i("gqf","onActivityResult"+picturePath);
            cursor.close();
            mPhotoFile=new File(picturePath);

            mPhotoimg=BitmapFactory.decodeFile(picturePath);
            userHeadImg.setImageBitmap(mPhotoimg);

        }

    }
    private void reSizephoto() {
        // TODO Auto-generated method stub
        BitmapFactory.Options op=new BitmapFactory.Options();
        op.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(currentphoto,op);
        op.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)op.outWidth / 1024f, (double)op.outHeight / 1024f)));
        op.inJustDecodeBounds = false;
        mPhotoimg = BitmapFactory.decodeFile(currentphoto, op);
    }
    class imageListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                mPhotoPath = "mnt/sdcard/DCIM/Camera/" + getPhotoFileName();
                mPhotoFile = new File(mPhotoPath);
                if (!mPhotoFile.exists()) {
                    mPhotoFile.createNewFile();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, CAMERA_CODE);
            } catch (Exception e) {
            }
        }


    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

}
