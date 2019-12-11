package com.zuoyu.business.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;


public class ExitLoginDialog {

    private static Dialog dialog;


    private static void initDialog(final Context context) {

        if (dialog == null) {

            dialog = new Dialog(context, R.style.exit_dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_prompt);


            // 确认按钮
            Button sureBtn = (Button) dialog.findViewById(R.id.btn_sure);
            sureBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    MyApplication.exitApplication((Activity) context);

                }
            });

        }

    }

    public static void show_(final Context context) {

//        if (dialog == null) {

            dialog = new Dialog(context, R.style.exit_dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_prompt);


            // 确认按钮
            Button sureBtn = (Button) dialog.findViewById(R.id.btn_sure);
            sureBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.exitApplication((Activity) context);
                    dialog.dismiss();

                }
            });

//        }

        dialog.show();
    }

    public static void show(Context context) {
        initDialog(context);

        if (!dialog.isShowing() && context != null) {
            dialog.show();
        }
    }
}
