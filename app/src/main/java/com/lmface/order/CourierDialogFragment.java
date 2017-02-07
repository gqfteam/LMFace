package com.lmface.order;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lmface.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by johe on 2017/2/7.
 */

public class CourierDialogFragment extends DialogFragment {

    @BindView(R.id.courier_edi)
    EditText courierEdi;
    @BindView(R.id.courier_no)
    Button courierNo;
    @BindView(R.id.courier_ok)
    Button courierOk;

    @OnClick({R.id.courier_no, R.id.courier_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.courier_no:
                dismiss();
                break;
            case R.id.courier_ok:
                if(!courierEdi.getText().equals("")) {
                    if (mLinsener != null) {
                        mLinsener.courierNum(courierEdi.getText().toString());
                        dismiss();
                    }
                }else{
                    Toast.makeText(getActivity(),"请输入快递号再提交",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public interface linsener {
        void courierNum(String num);

    }

    linsener mLinsener;


    public void setmLinsener(linsener mLinsener) {
        this.mLinsener = mLinsener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //去标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.courier_num_edi_dialog, container);
        ButterKnife.bind(this, view);
        return view;
    }

}