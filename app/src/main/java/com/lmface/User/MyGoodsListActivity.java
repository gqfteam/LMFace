package com.lmface.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.goods_msg;
import com.lmface.pojo.user_msg;
import com.lmface.store.GoodsDetailsActivity;
import com.lmface.util.in.srain.cube.views.ptr.PtrClassicFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/1/27.
 */

public class MyGoodsListActivity extends AppCompatActivity {

    public final String title = "我的商品";

    Realm realm;
    CompositeSubscription mcompositeSubscription;

    @BindView(R.id.my_goods_list)
    RecyclerView myGoodsList;
    @BindView(R.id.my_goods_list_ptr)
    PtrClassicFrameLayout myGoodsListPtr;

    MyGoodsListAdapter myGoodsListAdapter;

    boolean isEdi = false;
    @BindView(R.id.my_goods_list_toolbar)
    Toolbar myGoodsListToolbar;

    public void setToolbar(String str) {
        myGoodsListToolbar.setTitle(getResources().getString(R.string.my_goods_list_title));
        setSupportActionBar(myGoodsListToolbar);
        myGoodsListToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        myGoodsListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressed();
            }
        });
    }

    public void backPressed(){
        if (isEdi) {
            //退出编辑状态
            if (myGoodsListAdapter != null) {
                isEdi = false;
                invalidateOptionsMenu();
                myGoodsListAdapter.isEdi(isEdi);
            }
        } else {
            onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods_list);
        ButterKnife.bind(this);

        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar(title);
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdi) {
            getMenuInflater().inflate(R.menu.my_goods_list_edi_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.my_goods_list_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myGoodsListAdapter != null) {
            if (item.getItemId() == R.id.action_editor) {
                //切换为编辑状态
                isEdi = true;
                invalidateOptionsMenu();

                myGoodsListAdapter.isEdi(isEdi);

            } else if (item.getItemId() == R.id.action_add) {
                //跳转上架界面，不传intent
                startActivity(new Intent(MyGoodsListActivity.this,AddOrUpdateGoodsActivity.class));
            } else if (item.getItemId() == R.id.action_delect) {
                //删除所选商品，弹出提示询问是否删除
                delectGoods();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void delectGoods() {
        if (myGoodsListAdapter.getSelectDelectId().size() > 0) {
            Subscription subscription = NetWork.getGoodsService().delectGoodsByListId(myGoodsListAdapter.getSelectDelectId())
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
                            if (s.getCode()==10000) {
                                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), s.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                            myGoodsListAdapter.setSelectDelectId(new ArrayList<Integer>());
                            initData();
                        }
                    });
            mcompositeSubscription.add(subscription);
        } else {
            Toast.makeText(getApplicationContext(), "您当前没有选择需要删除的商品", Toast.LENGTH_SHORT).show();
        }
    }
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    public void initData() {
        Subscription subscription = NetWork.getGoodsService().selectByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                //列表类型转化
                .flatMap(new Func1<List<goods_msg>, Observable<goods_msg>>() {
                    @Override
                    public Observable<goods_msg> call(List<goods_msg> seats) {

                        return Observable.from(seats);
                    }
                })
                //过滤
                .filter(new Func1<goods_msg, Boolean>() {
                    @Override
                    public Boolean call(goods_msg ogms) {
                        if(ogms.getGoodsnum()==-1){
                            return false;
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<goods_msg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<goods_msg> goods_msgs) {
                        initList(goods_msgs);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initList(List<goods_msg> goods_msgs) {

            myGoodsListAdapter = new MyGoodsListAdapter(this, goods_msgs);
            myGoodsList.setLayoutManager(new LinearLayoutManager(this));
            myGoodsList.setAdapter(myGoodsListAdapter);
            myGoodsListAdapter.setOnItemClickListener(new MyGoodsListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                                        if (isEdi) {
                                            //跳转修改商品信息界面，并传递intent保存id
                                            Intent intent=new Intent(MyGoodsListActivity.this,AddOrUpdateGoodsActivity.class);
                                            intent.putExtra("goodsId",myGoodsListAdapter.getDataItem(postion).getGoodsid());
                                            startActivity(intent);
                                        } else {
                                            //跳转商品详情界面
                                            Intent intent=new Intent(MyGoodsListActivity.this,GoodsDetailsActivity.class);
                                            intent.putExtra("goodsId",myGoodsListAdapter.getDataItem(postion).getGoodsid());
                                            startActivity(intent);

                                        }
                }
            });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}
