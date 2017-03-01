package com.lmface.signin.myInitiateSign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.courseinfo;
import com.lmface.pojo.sign_user_msg;
import com.lmface.pojo.user_msg;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 */

public class MtInitiateSignFragment extends Fragment {
    private static final String ARG_TYPE = "type";
    @BindView(R.id.order_list_RecyclerView)
    RecyclerView orderListRecyclerView;
    private Realm realm;
    private Unbinder unbinder;
    private CompositeSubscription mcompositeSubscription;

    private int type;//0：临时，1：日常


    CourseInfoListAdapter courseInfoListAdapter;
    DailySignListAdapter dailySignListAdapter;
    public static MtInitiateSignFragment newInstance(int type) {
        MtInitiateSignFragment fragment = new MtInitiateSignFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the food_listitem for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();

        if (type == 0) {
            //查询我发起的临时签到
            initTemporaryData();
        } else {
            //查询我发起的日常签到
            initDailyCourseData();
        }

        return view;
    }

    public void initTemporaryData() {
        Subscription subscription = NetWork.getSignService().selectInitialsigninInfoByUserIdAndTemporary(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<sign_user_msg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<sign_user_msg> datas) {

                        Log.i("gqf", "onNext" + datas.toString());
                        initTemporaryList(datas);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void initTemporaryList(List<sign_user_msg> datas){
        if(dailySignListAdapter==null){
            dailySignListAdapter=new DailySignListAdapter(getActivity(),datas);
            orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            orderListRecyclerView.setAdapter(dailySignListAdapter);

        }else{
            dailySignListAdapter.update(datas);
        }
    }

    public void initDailyCourseData() {
        Subscription subscription = NetWork.getSignService().selectCouresByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                //列表类型转化
                .flatMap(new Func1<List<courseinfo>, Observable<courseinfo>>() {
                    @Override
                    public Observable<courseinfo> call(List<courseinfo> seats) {

                        return Observable.from(seats);
                    }
                })
                //过滤
                .filter(new Func1<courseinfo, Boolean>() {
                    @Override
                    public Boolean call(courseinfo ogms) {
                        if(ogms.getCoursename().equals("临时")){
                            return false;
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<courseinfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<courseinfo> datas) {

                        Log.i("gqf", "onNext" + datas.toString());
                        initDailyList(datas);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

   public void  initDailyList(List<courseinfo> datas){
        if(courseInfoListAdapter==null){
            courseInfoListAdapter=new CourseInfoListAdapter(getActivity(),datas);
            orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            orderListRecyclerView.setAdapter(courseInfoListAdapter);
            courseInfoListAdapter.setOnItemClickListener(new CourseInfoListAdapter.MyItemClickListener() {
                @Override
                public void statistical(int position) {
                    //跳转统计详情页面
                    Intent intent=new Intent(getActivity(),DaliySignEndMsgActivity.class);
                    intent.putExtra("courseName",courseInfoListAdapter.getDataItem(position).getCoursename());
                    intent.putExtra("courseId",courseInfoListAdapter.getDataItem(position).getCourseid());
                    EventBus.getDefault().post(intent);

                }

                @Override
                public void showDrawerLayout(int position) {
                    //广播当前事件，打开抽屉
                    EventBus.getDefault().post(courseInfoListAdapter.getDatas().get(position));

                }
            });
        }else{
            courseInfoListAdapter.update(datas);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }


}
