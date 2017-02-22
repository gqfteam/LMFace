package com.lmface.util;

import android.content.Context;

import com.lmface.dialog.SweetAlertDialog;


/**
 * Created by ice on 2016/1/12.
 */
public class DialogUtils {

    private static DialogUtils dialogUtils;

    private DialogUtils() {
    }

    public static DialogUtils getInstance(){
        if (dialogUtils==null){
            dialogUtils=new DialogUtils();
        }
        return dialogUtils;
    }

    public void ConfirmAndCancel(Context context,String title,SweetAlertDialog.OnSweetClickListener listener){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(listener)
                .show();
    }
    public void ConfirmAndCancel(Context context,String title,String confirm,SweetAlertDialog.OnSweetClickListener listener){
        new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setCancelText("取消")
                .setConfirmText(confirm)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(listener)
                .show();
    }
    public SweetAlertDialog DialogLoading(Context context,String title){
        SweetAlertDialog dialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                dialog.setTitleText(title)
                .show();
        return dialog;
    }

    public SweetAlertDialog DialogLoading(Context context){
        SweetAlertDialog dialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading...")
                .show();
        return dialog;
    }
    public void DialogLoadingVoid(Context context){
        SweetAlertDialog dialog=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading...")
                .show();
    }

}
