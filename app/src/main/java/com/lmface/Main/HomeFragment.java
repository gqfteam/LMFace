package com.lmface.Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lmface.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by johe on 2017/1/5.
 */

public class HomeFragment extends Fragment {
    Realm realm;
    @BindView(R.id.webview)
    WebView webview;

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        initDisplay();
        return view;
    }

    /*初始化显示信息*/
    public void initDisplay() {

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl("file:///android_asset/hkd/main.html");
        //设置Web视图
        // webview.setWebViewClient(new webViewClient ());


        //initDatas();
        // mSchoolinfoScrollView.setOnTouchListener(new TouchListenerImpl());
    }
}
