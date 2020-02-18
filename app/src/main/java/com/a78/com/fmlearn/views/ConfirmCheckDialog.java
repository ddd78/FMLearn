package com.a78.com.fmlearn.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.a78.com.fmlearn.R;

/**
 * Created by home on 2020/2/18.
 */

public class ConfirmCheckDialog extends Dialog {

    private TextView mCanacel;
    private TextView mConfirm;
    private CheckBox mCheckBox;
    private ConfirmCheckDialogAction confirmCheckDialogAction = null;

    public ConfirmCheckDialog(@NonNull Context context) {
        this(context,0);
    }

    public ConfirmCheckDialog(@NonNull Context context, int themeResId) {
        this(context, true, null);
    }

    protected ConfirmCheckDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_confirm);
        initView();
        initEvent();
    }

    private void initEvent() {
        mCanacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmCheckDialogAction != null) {
                    confirmCheckDialogAction.onCancelClick();
                    dismiss();
                }
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmCheckDialogAction != null) {
                    confirmCheckDialogAction.onConfirClick(mCheckBox.isChecked());
                    dismiss();
                }
            }
        });
    }

    private void initView() {
        mCanacel = this.findViewById(R.id.dialog_check_box_cancel);
        mConfirm = this.findViewById(R.id.dialog_check_box_confirm);
        mCheckBox = this.findViewById(R.id.dialog_check_box);
    }

    public interface ConfirmCheckDialogAction{
        void onCancelClick();

        void onConfirClick(boolean isCheck);
    }

    public void setConfirmCheckDialogAction(ConfirmCheckDialogAction action){
        confirmCheckDialogAction = action;
    }
}
