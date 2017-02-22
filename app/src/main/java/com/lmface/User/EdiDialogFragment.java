package com.lmface.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lmface.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/20.
 */

public class EdiDialogFragment extends DialogFragment {

    String ediTxt = "";

    String title = "";

    @BindView(R.id.fuifu_msg)
    EditText fuifuMsg;
    @BindView(R.id.positiveButton)
    Button positiveButton;
    @BindView(R.id.negativeButton)
    Button negativeButton;

    Realm realm;
    @BindView(R.id.edi_title_txt)
    TextView ediTitleTxt;

    int inputType= InputType.TYPE_NUMBER_FLAG_DECIMAL;

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public interface DimssLinsener {
        public void fragmentDimss();

        public void onOk(String ediTxt);
    }

    public String getEdiTxt() {
        return ediTxt;
    }

    public void setEdiTxt(String ediTxt) {
        this.ediTxt = ediTxt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    DimssLinsener dimssLinsener;

    public void setDimssLinsener(DimssLinsener dimssLinsene) {
        dimssLinsener = dimssLinsene;
    }

    CompositeSubscription compositeSubscription;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.edi_dialog, container);
        realm = Realm.getDefaultInstance();
        compositeSubscription = new CompositeSubscription();
        ButterKnife.bind(this, view);
        ediTitleTxt.setText(title);
        fuifuMsg.setHint(title);
        if(inputType==InputType.TYPE_NUMBER_FLAG_DECIMAL){
            fuifuMsg.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }else{
            fuifuMsg.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        return view;
    }

    @OnClick({R.id.positiveButton, R.id.negativeButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.positiveButton:
                ediTxt = fuifuMsg.getText().toString();
                if(!ediTxt.equals("")){
                    if (dimssLinsener != null) {
                        positiveButton.setEnabled(false);
                        dimssLinsener.onOk(ediTxt);
                    }
                }else{
                    fuifuMsg.setHint("请输入");
                }

                break;
            case R.id.negativeButton:
                this.dismiss();
                break;
        }
    }
    public void setEnd(boolean isSuccess){
        if (isSuccess) {
            if (dimssLinsener != null) {
                dimssLinsener.fragmentDimss();
            }
            EdiDialogFragment.this.dismiss();
        } else {
            Toast.makeText(getActivity(), title+"失败，请稍后尝试", Toast.LENGTH_SHORT).show();
        }
        positiveButton.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
