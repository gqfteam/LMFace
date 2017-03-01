package com.lmface.signin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.lmface.R;
import com.lmface.huanxin.DemoHelper;
import com.lmface.sortListView.CharacterParser;
import com.lmface.sortListView.ClearEditText;
import com.lmface.sortListView.PinyinComparator;
import com.lmface.sortListView.SideBar;
import com.lmface.sortListView.SmoothCheckBox;
import com.lmface.sortListView.SortAdapter;
import com.lmface.sortListView.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

import static com.lmface.R.id.allCheck_btn;
import static com.lmface.signin.SponporSignInActivity.list_userName;

public class SignInPersonActivity extends AppCompatActivity implements SortAdapter.MyCheckedChangeListener {

    @BindView(allCheck_btn)
    Button allCheckBtn;
    @BindView(R.id.confim_btn)
    Button confimBtn;
    private List<String> usernames;
    private DemoHelper demoHelper;
    private CompositeSubscription mcompositeSubscription;

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_person);
        ButterKnife.bind(this);
        mcompositeSubscription = new CompositeSubscription();
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        //        usernames = new ArrayList<>();
        //        for (int i = 0; i <20 ; i++) {
        //            usernames.add("第"+i+"学生");
        //        }
        //        initList();
        //        initFriendsData();
                initFriendsData();
//        initViews();

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SortModel bean = (SortModel) parent.getAdapter().getItem(position);
                bean.setChecked(!bean.isChecked());
                SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.scb);
                checkBox.setChecked(bean.isChecked(), true);
//                if (bean.isChecked()){
//                    list_userName.add(bean.getName());
//                }else {
//                    list_userName.remove(bean.getName());
//                }
                Log.i("gqf", "setOnItemClickListener---list_userName.size()---" + list_userName.size());
                for (int i = 0; i < list_userName.size(); i++) {

                    Log.i("gqf", "setOnItemClickListener---list_userName.size()---" + list_userName.get(i));
                }

            }
        });

    }

    private void initViews() {

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });



                int lenth = usernames.size();
                Log.e("Daniel","---lenth---"+lenth);
                String[] strArray = new String[lenth];
                for (int i = 0; i < lenth; i++) {
                    strArray[i]=usernames.get(i);
                }
                SourceDateList = filledData(strArray);
//        SourceDateList = filledData(getResources().getStringArray(R.array.date));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(SignInPersonActivity.this, SourceDateList);
        sortListView.setAdapter(adapter);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    public void initFriendsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                } catch (Exception e) {

                }

            }
        }).start();
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                                        initList(usernames);
                    initViews();
                    break;
                case 2://删除好友后
                    initFriendsData();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @OnClick({allCheck_btn, R.id.confim_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case allCheck_btn:
                if (SortAdapter.mAllChecked){
                    allCheckBtn.setText("全选");
                }else {
                    allCheckBtn.setText("取消全选");
                }
                adapter.setAllCheckBoxChecked();

                break;
            case R.id.confim_btn:
                finish();
                break;
        }
    }

    @Override
    public void setBtnText() {

        if (list_userName.size()!=SourceDateList.size()){
            allCheckBtn.setText("全选");
        }
        Log.e("Daniel","---setBtnText---"+allCheckBtn.getText().toString());
    }


    //        SingInPersonAdapter mSingInPersonAdapter;
//
//        public void initList() {
//    //        Log.e("Daniel",""+users.size());
//    //        if (mSingInPersonAdapter == null) {
//            recyclerviewSingInPerson.setLayoutManager(new LinearLayoutManager(this));
//                SingInPersonAdapter mSingInPersonAdapter = new SingInPersonAdapter(usernames, SignInPersonActivity.this);
//                recyclerviewSingInPerson.setAdapter(mSingInPersonAdapter);
//                recyclerviewSingInPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        Intent _intent = new Intent(getActivity(), ChatActivity.class);
//                        _intent.putExtra("friendName", mFriendsListAdapter.getDatas().get(i).getUserName());
//                        _intent.putExtra("FriendList_to_ChatFragment", true);
//                        startActivity(_intent);
//                    }
//                });
//                friendListAllfriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                        //长按删除好友
//                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
//                        alert.setTitle("是否删除联系人" + users.get(position).getUserName() + "")
//                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        delectFriend(users.get(position).getUserName());
//                                    }
//                                })
//                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        alert.create().show();
//
//
//                        return false;
//                    }
//                });
//            } else {
//    //            mSingInPersonAdapter.update(users);
//            }
//        }
}
