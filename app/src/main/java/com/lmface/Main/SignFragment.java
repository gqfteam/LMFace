package com.lmface.Main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmface.R;
import com.lmface.pojo.user_msg;
import com.lmface.signin.SponporSignIn;
import com.lmface.signin.myInitiateSign.MyInItateSignActivity;
import com.lmface.signin.mySignHistory.MySignHistoryActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/28.
 */

public class SignFragment extends Fragment {

    Realm realm;
    CompositeSubscription compositeSubscription;
    @BindView(R.id.fragment_sign_image)
    CircleImageView fragmentSignImage;

    public static SignFragment newInstance(String param1) {
        SignFragment fragment = new SignFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.sign_lin, R.id.my_initiate_sign_lin, R.id.my_sign_history_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_lin:
                mListener.changeActivity(SponporSignIn.class);
                break;
            case R.id.my_initiate_sign_lin:
                mListener.changeActivity(MyInItateSignActivity.class);
                break;
            case R.id.my_sign_history_lin:
                mListener.changeActivity(MySignHistoryActivity.class);
                break;
        }
    }


    public interface mListener {
        public void changeActivity(
                Class activityClass);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        compositeSubscription = new CompositeSubscription();




        return view;
    }
    private user_msg userMsg;
    @Override
    public void onStart() {
        super.onStart();
        userMsg = realm.where(user_msg.class).findFirst();

        if (userMsg.getHeadimg() != null) {
            if (!userMsg.getHeadimg().equals("")) {
                Picasso.with(getActivity()).load(userMsg.getHeadimg())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(fragmentSignImage);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
