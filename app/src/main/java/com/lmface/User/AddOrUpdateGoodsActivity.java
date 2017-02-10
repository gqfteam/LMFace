package com.lmface.User;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.campusinfo;
import com.lmface.pojo.collegeinfo;
import com.lmface.pojo.goods_msg;
import com.lmface.pojo.goodsclassification;
import com.lmface.pojo.goodsspecies;
import com.lmface.pojo.user_msg;
import com.lmface.util.ReadJson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func4;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.lmface.R.id.commit_btn;
import static com.lmface.User.ChangeUserHeadImgActivity.RESULT_LOAD_IMAGE;

/**
 * Created by johe on 2017/1/27.
 */

public class AddOrUpdateGoodsActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.add_goods_toolbar)
    Toolbar addGoodsToolbar;
    @BindView(R.id.addgoods_goodsname_edi)
    EditText addgoodsGoodsnameEdi;
    @BindView(R.id.addgoods_goodsprice_edi)
    EditText addgoodsGoodspriceEdi;
    @BindView(R.id.addgoods_goodsimg_img1)
    ImageView addgoodsGoodsimgImg1;
    @BindView(R.id.addgoods_goodsimg_img2)
    ImageView addgoodsGoodsimgImg2;
    @BindView(R.id.addgoods_goodsimg_img3)
    ImageView addgoodsGoodsimgImg3;
    @BindView(R.id.addgoods_addImg_txt)
    TextView addgoodsAddImgTxt;
    @BindView(R.id.addgoods_deleteimg_txt)
    TextView addgoodsDeleteimgTxt;
    @BindView(R.id.addgoods_goodstxt_edi)
    EditText addgoodsGoodstxtEdi;
    @BindView(R.id.addgoods_phonenumber_edi)
    EditText addgoodsPhonenumberEdi;
    @BindView(R.id.addgoods_goodsaddress_txt)
    TextView addgoodsGoodsaddressTxt;
    @BindView(R.id.addgoods_goodsaddress_lin)
    LinearLayout addgoodsGoodsaddressLin;
    @BindView(R.id.addgoods_goodsclassification_txt)
    TextView addgoodsGoodsclassificationTxt;
    @BindView(R.id.addgoods_goodsclassification_lin)
    LinearLayout addgoodsGoodsclassificationLin;
    @BindView(R.id.User_addgoods_ScrollView)
    ScrollView UserAddgoodsScrollView;
    @BindView(R.id.User_allLin_lin)
    RelativeLayout UserAllLinLin;
    @BindView(commit_btn)
    Button commitBtn;

    int goodsId = -1;
    goods_msg goodsMsg;
    int chooseImgView = -1;

    int courierId=1;
    List<Bitmap> imgs;

    String goodscity="";

    String college="";
    String campus="";
    String classification="";
    String species="";
    @BindView(R.id.addgoods_goodsnum_edi)
    EditText addgoodsGoodsnumEdi;

    ArrayList<String> ListDatas;
    ArrayList<String> DialogDatas;


    List<collegeinfo> collegeinfos;
    List<campusinfo> campusinfos;
    List<goodsclassification> goodsclassifications;
    List<goodsspecies> goodsspecies;

    AddGoodsChooseListAdapter mAddGoodsChooseListAdapter;

    int goodsclassificationid = -1;
    int goodsspecieid = -1;

    int collegeid = -1;
    int campusid = -1;


    public int selectImgIndex=0;


    @BindView(R.id.loan_courier_rad_1)
    RadioButton loanCourierRad1;
    @BindView(R.id.loan_courier_rad_2)
    RadioButton loanCourierRad2;
    @BindView(R.id.loan_courier_rad_3)
    RadioButton loanCourierRad3;
    @BindView(R.id.courier_radioGroup)
    RadioGroup courierRadioGroup;

    public void setToolbar(int statu) {
        if (statu == 0) {
            addGoodsToolbar.setTitle("上架商品");
        } else {
            addGoodsToolbar.setTitle("修改商品");
        }
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
        setContentView(R.layout.activity_add_user_goods);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        bitmaps=new ArrayList<>();
        bitmaps.add(new File(""));
        bitmaps.add(new File(""));
        bitmaps.add(new File(""));
        imgs = new ArrayList<>();
        goodsId = getIntent().getIntExtra("goodsId", -1);
        Log.i("gqf","goodsId"+goodsId);
        if (goodsId > 0) {
            setToolbar(1);
            getData(goodsId);
        } else {
            setToolbar(0);
        }
        initButton();

    }

    public void initButton() {

        loanCourierRad1.setChecked(true);
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(addgoodsGoodsnameEdi).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(addgoodsGoodspriceEdi).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(addgoodsPhonenumberEdi).skip(1);
        Observable<CharSequence> CharSequence4 = RxTextView.textChanges(addgoodsGoodsnumEdi).skip(1);

        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, CharSequence3, CharSequence4, new Func4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                boolean B4 = !TextUtils.isEmpty(charSequence4);
                return Bl && B2 && B3 && B4;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @DebugLog
            @Override
            public void onNext(Boolean aBoolean) {
                Log.e("Daniel", "---aBoolean---" + aBoolean);
                commitBtn.setEnabled(aBoolean);
            }
        });
        mcompositeSubscription.add(etSc);
    }

    public void getData(int id) {
        Subscription subscription = NetWork.getGoodsService().selectByGoodsId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<goods_msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(goods_msg goods_msg) {
                        goodsMsg=goods_msg;
                        initViewMsg(goods_msg);
                    }
                });
        mcompositeSubscription.add(subscription);
    }


    public void initViewMsg(goods_msg goods_msg) {
        Log.i("gqf", "initViewMsg" + goods_msg.toString());
        addgoodsGoodsnameEdi.setText(goods_msg.getGoodsname());
        addgoodsPhonenumberEdi.setText(goods_msg.getUserphonenum());
        addgoodsGoodstxtEdi.setText(goods_msg.getGoodsdetails());
        addgoodsGoodspriceEdi.setText(goods_msg.getGoodsprice()+"");
        addgoodsGoodsnumEdi.setText(goods_msg.getGoodsnum()+"");
        for(int i=0;i<courierRadioGroup.getChildCount();i++){
            RadioButton r=(RadioButton)courierRadioGroup.getChildAt(i);
            Log.i("gqf","RadioButton"+r.getText().toString()+goods_msg.getCourierName());
            if(r.getText().toString().equals(goods_msg.getCourierName())){
                Log.i("gqf","RadioButton"+r.getText().toString()+"true"+goods_msg.getCourierName());
                r.setChecked(true);
                courierId=i+1;
            }
        }
        initImgView(goods_msg);
    }
    private static final String PICASSO_CACHE = "picasso-cache";
    public String changePath(String path){
        String http="http://192.168.56.1:8080/mface/goodsImgs/";
        path=path.substring(http.length(), path.length());
        String c="";
        path=c+path;
        return path;
    }
    public void initImgView(goods_msg goods_msg){
        final String AbsolutePath= Environment.getExternalStorageDirectory().getAbsolutePath();

        if(goods_msg.getGoodsimgaddress1()!=null){
            if(!goods_msg.getGoodsimgaddress1().equals("")){
                Picasso.with(this).load(goods_msg.getGoodsimgaddress1())
                        .placeholder(R.drawable.ic_comment_upload_add)
                        .error(R.drawable.ic_comment_upload_add)
                        .into(addgoodsGoodsimgImg1);
                final String Goodsimgaddress1=goods_msg.getGoodsimgaddress1();
                Picasso.with(this).load(goods_msg.getGoodsimgaddress1()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        File dcimFile =new File(getApplicationContext().getExternalCacheDir(),changePath(Goodsimgaddress1));
                        FileOutputStream ostream = null;
                        try {
                            ostream = new FileOutputStream(dcimFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(dcimFile.exists()){
                            bitmaps.remove(0);
                            bitmaps.add(0, dcimFile);
                        }else{
                            Log.i("gqf","Goodsimgaddress1");
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }
        }
        if(goods_msg.getGoodsimgaddress2()!=null){
            if(!goods_msg.getGoodsimgaddress2().equals("")){
                Picasso.with(this).load(goods_msg.getGoodsimgaddress2())
                        .placeholder(R.drawable.ic_comment_upload_add)
                        .error(R.drawable.ic_comment_upload_add)
                        .into(addgoodsGoodsimgImg2);
                final String Goodsimgaddress2=goods_msg.getGoodsimgaddress2();
                Picasso.with(this).load(goods_msg.getGoodsimgaddress2()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        File dcimFile =new File(getApplicationContext().getExternalCacheDir(),changePath(Goodsimgaddress2));
                        FileOutputStream ostream = null;
                        try {
                            ostream = new FileOutputStream(dcimFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(dcimFile.exists()){
                            bitmaps.remove(1);
                            bitmaps.add(1, dcimFile);
                        }else{
                            Log.i("gqf","Goodsimgaddress2");
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }
        }
        if(goods_msg.getGoodsimgaddress3()!=null){
            if(!goods_msg.getGoodsimgaddress3().equals("")){
                Picasso.with(this).load(goods_msg.getGoodsimgaddress3())
                        .placeholder(R.drawable.ic_comment_upload_add)
                        .error(R.drawable.ic_comment_upload_add)
                        .into(addgoodsGoodsimgImg3);
                final String Goodsimgaddress3=goods_msg.getGoodsimgaddress3();
                Picasso.with(this).load(goods_msg.getGoodsimgaddress3()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        File dcimFile =new File(getApplicationContext().getExternalCacheDir(),changePath(Goodsimgaddress3));
                        FileOutputStream ostream = null;
                        try {
                            ostream = new FileOutputStream(dcimFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(dcimFile.exists()){
                            bitmaps.remove(2);
                            bitmaps.add(2, dcimFile);
                        }else{
                            Log.i("gqf","Goodsimgaddress3");
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }

    @OnClick({R.id.loan_courier_rad_1, R.id.loan_courier_rad_2, R.id.loan_courier_rad_3,R.id.addgoods_goodsaddress_lin, R.id.addgoods_goodsclassification_lin, R.id.addgoods_goodsimg_img1, R.id.addgoods_goodsimg_img2, R.id.addgoods_goodsimg_img3, R.id.addgoods_addImg_txt, R.id.addgoods_deleteimg_txt, commit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addgoods_goodsimg_img1:
                chooseImgView(addgoodsGoodsimgImg1, 1);
                break;
            case R.id.addgoods_goodsimg_img2:
                chooseImgView(addgoodsGoodsimgImg2, 2);
                break;
            case R.id.addgoods_goodsimg_img3:
                chooseImgView(addgoodsGoodsimgImg3, 3);
                break;
            case R.id.addgoods_addImg_txt:
                //在所选imgview中天加图片
                //跳转页面选择图片
                if(selectImgIndex>0) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,RESULT_LOAD_IMAGE);

                }else{
                    Toast.makeText(getApplicationContext(),"请点击上方方框选择一个位置进行添加",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addgoods_deleteimg_txt:
                //删除所选imgview中的图片
                if(selectImgIndex>0) {
                    bitmaps.remove(selectImgIndex - 1);
                    bitmaps.add(selectImgIndex - 1, new File(""));
                    if (selectImgIndex == 1) {
                        addgoodsGoodsimgImg1.setImageResource(R.drawable.ic_comment_upload_add);
                    }
                    if (selectImgIndex == 2) {
                        addgoodsGoodsimgImg2.setImageResource(R.drawable.ic_comment_upload_add);
                    }
                    if (selectImgIndex == 3) {
                        addgoodsGoodsimgImg3.setImageResource(R.drawable.ic_comment_upload_add);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"请点击上方方框选择一个删除",Toast.LENGTH_SHORT).show();
                }
                break;
            case commit_btn:
                commitBtn.setEnabled(false);
                boolean isImgCommit=false;
                for(File f:bitmaps){
                    if(f!=null){
                        if(f.exists()){
                            isImgCommit=true;
                        }
                    }
                }
                if (goodscity.equals("")) {
                    Toast.makeText(getApplicationContext(), "商品地址信息不完整", Toast.LENGTH_SHORT).show();
                    commitBtn.setEnabled(true);
                } else if (goodsclassificationid == -1 || goodsspecieid == -1) {
                    Toast.makeText(getApplicationContext(), "商品分类信息不完整", Toast.LENGTH_SHORT).show();
                    commitBtn.setEnabled(true);
                } else if (collegeid == -1 || campusid == -1) {
                    Toast.makeText(getApplicationContext(), "商品地址信息不完整"+collegeid+campusid, Toast.LENGTH_SHORT).show();
                    commitBtn.setEnabled(true);
                }else if(!isImgCommit){
                    Toast.makeText(getApplicationContext(), "请上传至少一张图片", Toast.LENGTH_SHORT).show();
                    commitBtn.setEnabled(true);
                }
                else {
                    if(goodsId!=-1){
                        update();
                    }else{
                        commit();
                    }

                }
                break;
            case R.id.addgoods_goodsaddress_lin:
                //选择地址
                showPopwindow();
                updateAddressPopuList();
                break;
            case R.id.addgoods_goodsclassification_lin:
                //选择分类
                showPopwindow();
                updateClassificationPopuList();
                break;
            case R.id.loan_courier_rad_1:
                courierId=1;
                break;
            case R.id.loan_courier_rad_2:
                courierId=2;
                break;
            case R.id.loan_courier_rad_3:
                courierId=3;
                break;
        }
    }

    public void update(){
        Subscription subscription = NetWork.getGoodsService().updateGoodsByGoodsId(goodsId,realm.where(user_msg.class).findFirst().getUserId(),
                addgoodsGoodsnameEdi.getText().toString(), addgoodsGoodstxtEdi.getText().toString(),
                goodsMsg.getGoodsimgaddress1(), goodsMsg.getGoodsimgaddress2(), goodsMsg.getGoodsimgaddress3(), addgoodsPhonenumberEdi.getText().toString(),
                Double.parseDouble(addgoodsGoodspriceEdi.getText().toString()), goodscity, collegeid, campusid, goodsclassificationid,
                goodsspecieid, Integer.parseInt(addgoodsGoodsnumEdi.getText().toString()), goodsMsg.getShelvestime(),courierId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultCode s) {
                        result("修改",s);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void result(String name,ResultCode s){
        if(s.getCode()==10000){
            goodsimg(Integer.parseInt(s.getMsg()),name);

        }else{
            Toast.makeText(getApplicationContext(),name+"失败",Toast.LENGTH_SHORT).show();
            commitBtn.setEnabled(true);
        }

    }
    boolean isFile=false;
    //上传图片
    public void goodsimg(int goodsId,final String name){
        boolean isAddress1=false;
        boolean isAddress2=false;
        boolean isAddress3=false;

        MultipartBody.Builder builder = new MultipartBody.Builder();
        for(int i=0;i<bitmaps.size();i++){
            File f=bitmaps.get(i);
            if(f!=null) {
                Log.i("gqf","File"+i);
                if(f.exists()){
                        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), f);
                        builder.addFormDataPart("zichifile", f.getName(), photoRequestBody);
                    Log.i("gqf","exists"+i);
                    if(i==0){
                        isAddress1=true;
                    }
                    if(i==1){
                        isAddress2=true;
                    }
                    if(i==2){
                        isAddress3=true;
                    }
                }
            }
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody mb=builder.build();


        List<MultipartBody.Part> zichifile=new ArrayList<>();
        for(int i=0;i<mb.size();i++){
            zichifile.add(mb.part(i));
        }
        if(isFile) {
            Subscription subscription = NetWork.getGoodsService().insertHtdffile(zichifile,isAddress1,isAddress2,isAddress3, goodsId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResultCode resultCode) {
                            if (resultCode.getCode() == 10000) {
                                Toast.makeText(getApplicationContext(),"提交成功", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(), name + "失败", Toast.LENGTH_SHORT).show();
                            }
                            commitBtn.setEnabled(true);
                        }
                    });
            mcompositeSubscription.add(subscription);
        }else{
            commitBtn.setEnabled(true);
        }

    }
    //先上传图片，然后返回图片地址记录，然后上传商品信息
    public void commit() {
            Subscription subscription = NetWork.getGoodsService().insertGoods(realm.where(user_msg.class).findFirst().getUserId(),
                    addgoodsGoodsnameEdi.getText().toString(), addgoodsGoodstxtEdi.getText().toString(),
                    "", "", "", addgoodsPhonenumberEdi.getText().toString(),
                    Double.parseDouble(addgoodsGoodspriceEdi.getText().toString()), goodscity, collegeid, campusid, goodsclassificationid,
                    goodsspecieid, Integer.parseInt(addgoodsGoodsnumEdi.getText().toString()), courierId
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResultCode s) {
                            result("提交",s);
                        }
                    });
            mcompositeSubscription.add(subscription);

    }


    public void chooseImgView(ImageView imgv, int index) {
        //显示选择框
        addgoodsGoodsimgImg1.setBackground(null);
        addgoodsGoodsimgImg2.setBackground(null);
        addgoodsGoodsimgImg3.setBackground(null);
        if (imgv.getBackground() == null) {
            imgv.setBackgroundResource(R.drawable.initialsignin_top_search_edittext);
            chooseImgView = index;
            selectImgIndex=index;
        } else {
            imgv.setBackground(null);
            chooseImgView = -1;
            selectImgIndex=0;
        }



    }
    List<File> bitmaps;
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
            if(selectImgIndex==1) {
                addgoodsGoodsimgImg1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                bitmaps.remove(0);
                bitmaps.add(0,new File(picturePath));
            }
            if(selectImgIndex==2) {
                addgoodsGoodsimgImg2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                bitmaps.remove(1);
                bitmaps.add(1,new File(picturePath));
            }
            if(selectImgIndex==3) {
                bitmaps.remove(2);
                addgoodsGoodsimgImg3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                bitmaps.add(2,new File(picturePath));
            }
            isFile=true;
        }

    }



    ListView popupList;
    RadioGroup popuRadioGroup;
    RadioButton radioCompus;
    RadioButton radioDistrict;
    RadioButton radioCity;
    RadioButton radioProvince;
    PopupWindow window;
    int chooseIndes = 0;

    public void changeRadColor() {
        RadioButton r;
        for (int i = 0; i < 4; i++) {
            r = (RadioButton) popuRadioGroup.getChildAt(i);
            if (i == chooseIndes) {
                r.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                r.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void showPopwindow() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow, null);
        popupList = (ListView) view.findViewById(R.id.popup_list);
        popuRadioGroup = (RadioGroup) view.findViewById(R.id.popu_RadioGroup);
        radioCompus = (RadioButton) view.findViewById(R.id.radio_compus);
        radioDistrict = (RadioButton) view.findViewById(R.id.radio_district);
        radioCity = (RadioButton) view.findViewById(R.id.radio_city);
        radioProvince = (RadioButton) view.findViewById(R.id.radio_province);
        radioProvince.setChecked(true);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        window.setBackgroundDrawable(new BitmapDrawable());

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 在参照的View控件下方显示
        // window.showAsDropDown(MainActivity.this.findViewById(R.id.start));
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(AddOrUpdateGoodsActivity.this.findViewById(R.id.User_allLin_lin),
                Gravity.BOTTOM, 0, 0);

        // popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                mAddGoodsChooseListAdapter = null;
                chooseIndes = 0;
            }
        });
    }

    public void changeRid(String choose) {
        RadioButton r;
        //点击后读取点击项地址信息
        r = (RadioButton) popuRadioGroup.getChildAt(chooseIndes - 1);
        r.setTextColor(this.getResources().getColor(R.color.black));
        r.setText(choose);

        if (chooseIndes < 4) {
            //设置第二个button可点击
            r = (RadioButton) popuRadioGroup.getChildAt(chooseIndes);
            r.setClickable(true);
            r.setEnabled(true);
            r.setChecked(true);
            r.setVisibility(View.VISIBLE);
            r.setText("请选择");
            r.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            for (int m = chooseIndes + 1; m < popuRadioGroup.getChildCount(); m++) {
                popuRadioGroup.getChildAt(m).setVisibility(View.INVISIBLE);
            }
        }

    }

    public void updateAddressPopuList() {
        radioProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIndes = 0;
                mAddGoodsChooseListAdapter.update(readJson(chooseIndes));
                changeRadColor();

            }
        });
        radioCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIndes = 1;
                mAddGoodsChooseListAdapter.update(readJson(chooseIndes));
                changeRadColor();
            }
        });
        radioDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIndes = 2;
                //根据城市搜大学
                getJson(radioCity.getText().toString(), 3);
                changeRadColor();
            }
        });
        radioCompus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIndes = 3;
                //根据大学id搜校区
                getJson(radioDistrict.getText().toString(), 0);
                changeRadColor();
            }
        });
        mAddGoodsChooseListAdapter = new AddGoodsChooseListAdapter(this, readJson(chooseIndes));
        popupList.setAdapter(mAddGoodsChooseListAdapter);
        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (chooseIndes == 0) {
                    radioProvince.setText(mAddGoodsChooseListAdapter.getItem(position).toString());
                    chooseIndes = 1;
                    //初始化所选
                    addgoodsGoodsaddressTxt.setText("");
                    goodscity = "";
                    collegeid = -1;
                    campusid = -1;
                } else if (chooseIndes == 1) {
                    radioCity.setText(mAddGoodsChooseListAdapter.getItem(position).toString());
                    chooseIndes = 2;
                    popupList.setEnabled(false);
                    //初始化所选
                    addgoodsGoodsaddressTxt.setText("");
                    collegeid = -1;
                    campusid = -1;
                    //根据城市名获取大学
                    getJson(mAddGoodsChooseListAdapter.getItem(position).toString(), 3);
                } else if (chooseIndes == 2) {
                    radioDistrict.setText(mAddGoodsChooseListAdapter.getItem(position).toString());
                    chooseIndes = 3;
                    popupList.setEnabled(false);
                    //初始化所选
                    addgoodsGoodsaddressTxt.setText("");
                    campusid = -1;
                    //根据大学id搜校区
                    getJson(mAddGoodsChooseListAdapter.getItem(position).toString(), 0);
                }
                if (chooseIndes < 3) {
                    changeRid(mAddGoodsChooseListAdapter.getItem(position).toString());
                    mAddGoodsChooseListAdapter.update(readJson(chooseIndes));
                } else if (chooseIndes == 4) {
                    chooseIndes = 0;
                    addgoodsGoodsaddressTxt.setText(radioProvince.getText() + " " + radioCity.getText() +
                            " " + radioDistrict.getText() + " " + mAddGoodsChooseListAdapter.getItem(position).toString());
                    goodscity = radioCity.getText().toString();
                    college = radioDistrict.getText().toString();
                    campus = mAddGoodsChooseListAdapter.getItem(position).toString();

                    collegeid=campusinfos.get(position).getCollegeid();
                    campusid=campusinfos.get(position).getCampusid();
                    window.dismiss();
                } else if (chooseIndes == 3) {
                    chooseIndes = 4;
                }
            }
        });
    }

    public void updateClassificationPopuList() {
        radioProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIndes = 0;
                //获得全部种类
                getJson("", 1);
                changeRadColor();
            }
        });
        radioCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIndes = 1;
                //根据种类id求分类
                getJson(radioProvince.getText().toString(), 2);
                changeRadColor();
            }
        });
        getJson("", 1);
    }

    ReadJson rj;

    public ArrayList<String> readJson(int position) {
        String json = null;
        ArrayList<String> data = new ArrayList<String>();
        if (position == 0 || position == 1) {
            json = this.getString(R.string.city_json);
        } else if (position == 2) {
            json = this.getString(R.string.university_json);
        }
        rj = ReadJson.getInstance();
        if (position == 0 || position == 2) {
            for (int i = 0; i < rj.readSaleTopChooseJson(json).size(); i++) {
                data.add(rj.readSaleTopChooseJson(json).get(i).getName());
            }
        } else {
            data = rj.readSaleTopChooseJson(json).get(17).getHave();
        }

        return data;
    }

    public void getJson(String name, final int index) {
        if (index == 0) {
            //获得校区
            int id = 0;
            for (collegeinfo c : collegeinfos) {
                if (c.getCollegename().equals(name)) {
                    id = c.getCollegeid();
                }
            }
            if (id > 0) {
                Subscription subscription = NetWork.getGoodsService().getcampusMsg(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<campusinfo>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<campusinfo> c) {
                                campusinfos = c;
                                updatePopuList(index);
                            }
                        });
                mcompositeSubscription.add(subscription);

            }
        } else if (index == 1) {
            //获得种类
            Subscription subscription = NetWork.getGoodsService().getClassificationMsg()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<goodsclassification>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<goodsclassification> goodscs) {
                            goodsclassifications = goodscs;
                            //                            radioCity.setEnabled(true);
                            //                            radioCity.setChecked(true);
                            Log.i("gqf", "goodsclassification" + goodsclassifications.toString());
                            updatePopuList(index);
                        }
                    });
            mcompositeSubscription.add(subscription);

        } else if (index == 2) {
            //获得分类
            int id = 0;
            for (goodsclassification c : goodsclassifications) {
                if (c.getGoodsclassification().equals(name)) {
                    id = c.getClassificationid();
                }
            }
            if (id > 0) {
                Subscription subscription = NetWork.getGoodsService().getspeciesMsg(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<com.lmface.pojo.goodsspecies>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<goodsspecies> goodsss) {
                                goodsspecies = goodsss;
                                updatePopuList(index);
                            }
                        });
                mcompositeSubscription.add(subscription);

            }
        } else if (index == 3) {
            //获得大学
            Subscription subscription = NetWork.getGoodsService().getcollegeMsg(name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<collegeinfo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<collegeinfo> cs) {
                            collegeinfos = cs;
                            updatePopuList(index);
                        }
                    });
            mcompositeSubscription.add(subscription);

        }

    }

    public void updatePopuList(int index) {
        switch (index) {
            case 0:
                ArrayList<String> campusdate = new ArrayList<>();
                for (int i = 0; i < campusinfos.size(); i++) {
                    campusdate.add(campusinfos.get(i).getCampusname());
                }
                mAddGoodsChooseListAdapter.update(campusdate);
                popupList.setEnabled(true);
                break;
            case 1:
                //解析json

                ArrayList<String> classificationdate = new ArrayList<>();
                for (int i = 0; i < goodsclassifications.size(); i++) {
                    classificationdate.add(goodsclassifications.get(i).getGoodsclassification());
                }
                //初始化list
                mAddGoodsChooseListAdapter = new AddGoodsChooseListAdapter(this, classificationdate);
                popupList.setAdapter(mAddGoodsChooseListAdapter);
                popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (chooseIndes == 0) {
                            chooseIndes = 1;
                            popupList.setEnabled(false);
                            changeRid(mAddGoodsChooseListAdapter.getItem(position).toString());
                            goodsclassificationid = goodsclassifications.get(position).getClassificationid();
                            Log.i("gqf","goodsclassificationid"+goodsclassificationid);
                            getJson(mAddGoodsChooseListAdapter.getItem(position).toString(), 2);
                            //初始化所选
                            goodsspecieid = -1;
                            addgoodsGoodsclassificationTxt.setText("");
                        } else if (chooseIndes == 1) {
                            chooseIndes = 0;
                            addgoodsGoodsclassificationTxt.setText("商品分类:" + radioProvince.getText() + "/" + mAddGoodsChooseListAdapter.getItem(position).toString());
                            classification = radioProvince.getText().toString();
                            species = mAddGoodsChooseListAdapter.getItem(position).toString();

                                goodsspecieid = goodsspecies.get(position).getSpeciesid();
                                goodsclassificationid=goodsspecies.get(position).getClassificationid();

                            Log.i("gqf",goodsclassificationid+"goodsclassificationid"+goodsspecieid);
                            window.dismiss();
                        }
                    }
                });
                popupList.setEnabled(true);

                break;
            case 2:
                //解析json
                ArrayList<String> specisdate = new ArrayList<>();
                for (int i = 0; i < goodsspecies.size(); i++) {
                    specisdate.add(goodsspecies.get(i).getSpeciesname());
                }
                specisdate.add("全部");
                mAddGoodsChooseListAdapter.update(specisdate);
                popupList.setEnabled(true);

                break;

            case 3:
                ArrayList<String> colleges = new ArrayList<>();
                for (int i = 0; i < collegeinfos.size(); i++) {
                    colleges.add(collegeinfos.get(i).getCollegename());
                }
                mAddGoodsChooseListAdapter.update(colleges);
                popupList.setEnabled(true);

                break;
        }
    }


}
