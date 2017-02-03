package com.lmface.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.lmface.R;
import com.lmface.application.LMFaceApplication;
import com.lmface.pojo.ChangeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

import static com.lmface.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements StoreFragment.mListener,UserFragment.mListener{

    private static final String HOME_TAG = "home_flag";
    private static final String STORE_TAG = "store_flag";
    private static final String MY_TAG = "my_flag";
    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(toolbar)
    Toolbar mToolbar;
    private static final int CONTENT_ORDERS = 1;
    private static final int CONTENT_MY = 2;
    private static final int CONTENT_HOME = 0;
    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;
    @BindView(R.id.toolbar_text)
    TextView toolbarText;

    private void setToolbar(String toolstr, int position) {


        switch (position) {
            case 0:
                mToolbar.setTitle(toolstr);
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mToolbar.setTitleTextColor(Color.WHITE);
                toolbarText.setVisibility(View.GONE);
                this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
                setSupportActionBar(mToolbar);
                break;
            case 1:
                mToolbar.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(toolstr);
                toolbarText.setTextColor(getResources().getColor(R.color.black));
                mToolbar.setTitle("");
                setSupportActionBar(mToolbar);
                toolbarText.setGravity(Gravity.CENTER_VERTICAL);
                mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId=item.getItemId();
                        if(itemId==R.id.action_search){
                            if(storeFragment!=null){
                                storeFragment.showOrHideSearch();
                            }
                        }else{
                            //跳转到消息中心

                        }
                        return false;
                    }
                });
                break;
            case 2:
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(toolstr);
                toolbarText.setTextColor(getResources().getColor(R.color.white));
                mToolbar.setTitle("");
                toolbarText.setGravity(Gravity.CENTER);
                setSupportActionBar(mToolbar);
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible((bottomBar.getCurrentSelectedPosition()==1));
        menu.findItem(R.id.action_notification).setVisible((bottomBar.getCurrentSelectedPosition()==1));
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //加入activity列表
        ((LMFaceApplication) getApplication()).addActivity(this);
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("校园首页", 0);
        initBotomBar();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startActivity(ChangeActivity activity) {
        Intent intent=new Intent(MainActivity.this,activity.getActivity());
        if(activity.getGoodsId()!=-1){
            intent.putExtra("goodsId",activity.getGoodsId());
        }
        startActivity(intent);
    }

    public void initBotomBar() {
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar
                .setActiveColor(R.color.colorPrimaryDark)
                .setInActiveColor(R.color.frame_gary)
                .setBarBackgroundColor(R.color.whitesmoke);

        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_deal_off, R.string.firstPage))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_poi_off, R.string.store))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_user_off, R.string.my))
                .initialise();

        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        setContent(CONTENT_HOME);

                        break;
                    case 1:
                        setContent(CONTENT_ORDERS);
                        break;
                    case 2:
                        setContent(CONTENT_MY);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 页面切换
     *
     * @param contentHome
     */
    HomeFragment homeFragment;
    StoreFragment storeFragment;
    UserFragment userFragment;
    private void setContent(int contentHome) {
        switch (contentHome) {
            case CONTENT_HOME:
                String home_str = getResources().getString(R.string.firstPage);
                setToolbar("校园首页", contentHome);
                homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance(home_str);
                }
                setFragment(homeFragment, HOME_TAG);
                break;
            case CONTENT_ORDERS:
                String orders_str = getResources().getString(R.string.store);
                setToolbar("淘个不停", contentHome);
                storeFragment = (StoreFragment) getSupportFragmentManager().findFragmentByTag(STORE_TAG);
                if (storeFragment == null) {
                    storeFragment = StoreFragment.newInstance(orders_str);
                }
                setFragment(storeFragment, STORE_TAG);
                break;
            case CONTENT_MY:
                String my_str = getResources().getString(R.string.my);
                setToolbar("个人中心", contentHome);
                userFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag(MY_TAG);
                if (userFragment == null) {
                    userFragment = UserFragment.newInstance(my_str);
                }
                setFragment(userFragment, MY_TAG);
                break;
        }

    }

    /**
     * 设置fragment
     *
     * @param fragment
     */
    @DebugLog
    private void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
        realm.close();
        KeyboardUtils.hideSoftInput(this);
        if (mcompositeSubscription != null && !mcompositeSubscription.isUnsubscribed()) {
            mcompositeSubscription.unsubscribe();
        }
    }
    public void changeActivity(
            Class activityClass){
        startActivity(new Intent(MainActivity.this,activityClass));
    }
}
