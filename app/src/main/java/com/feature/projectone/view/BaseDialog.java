package com.feature.projectone.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;

/**
 * Created by Administrator on 2018/4/17.
 * 自定义的dialog，在项目里通用
 */

public class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(int title) {
            this.title = ((String) context.getText(title));
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = ((String) context.getText(positiveButtonText));
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = ((String) context.getText(negativeButtonText));
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public BaseDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final BaseDialog baseDialog = new BaseDialog(context, R.style.mDialog);//定义dialog样式
            View layout = inflater.inflate(R.layout.base_dialog_layout, null);
            ((TextView) layout.findViewById(R.id.tv_title)).setText(title);//设置标题

            //设置标题
            if (title != null) {
                ((TextView) layout.findViewById(R.id.tv_title)).setText(title);
            }

            //设置积极的按钮
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.tv_positive)).setText(positiveButtonText);
                ((TextView) layout.findViewById(R.id.tv_positive)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        positiveButtonClickListener.onClick(baseDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            //设置消极的按钮
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.tv_negative)).setText(negativeButtonText);
                ((TextView) layout.findViewById(R.id.tv_negative)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        negativeButtonClickListener.onClick(baseDialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }

            baseDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            baseDialog.setCancelable(false);//设置不可点击外部消失
            return baseDialog;
        }

    }
}
