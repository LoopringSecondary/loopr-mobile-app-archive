package com.tomcat360.lyqb.utils;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tomcat360.lyqb.R;


public class DialogUtil {


    private Context mContext;

    public DialogUtil(Context context) {

        mContext = context;

    }

    public static AlertDialog dialog;

    /**
     * 创建钱包结果dialog
     *
     * @param context
     * @param listener
     */
    public static void showWalletCreateResultDialog(Context context, View.OnClickListener listener) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wallet_create_success, null);
        builder.setView(view);
        TextView create_success = (TextView) view.findViewById(R.id.wallet_create_success);
        TextView ok = (TextView) view.findViewById(R.id.got_it);

        ok.setOnClickListener(listener);
        builder.setCancelable(false);
        dialog = null;
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     * 城市合伙人激活码dialog
     *
     * @param context
     * @param listener
     */
    public static void showUserActivationCodeDialog(Context context, final OnConfirmClickListener listener) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_user_activation_code, null);
        builder.setView(view);
        TextView title = (TextView) view.findViewById(R.id.title);
        final EditText code_input = (EditText) view.findViewById(R.id.code_input);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (TextUtils.isEmpty(code_input.getText().toString())) {
                        ToastUtils.toast("请输入激活码！");
                        return;
                    }else {
                        listener.onconfirmClick(v,code_input.getText().toString());
                    }

            }
        });
        builder.setCancelable(false);
        dialog = null;
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public static interface OnConfirmClickListener{
        void onconfirmClick(View view,String info);
    }





}
