package com.a78.com.fmlearn.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.a78.com.fmlearn.R;

/**
 * Created by home on 2020/2/17.
 */

public class ConfirmDialog extends Dialog {

    private ConfirmDialogLisenter confirmDialogLisenter;

    public ConfirmDialog(@NonNull Context context) {
        this(context,0);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        this(context, true,null
        );
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        initView();
    }

    private void initView() {
        TextView positiveButton = findViewById(R.id.confirm_dialog_positive_text);
        TextView negativeButton = findViewById(R.id.confirm_dialog_negative_text);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogLisenter.onRethinkClick();
                dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogLisenter.onGiveUpClick();

                dismiss();
            }
        });

    }

    public interface ConfirmDialogLisenter{
        void onRethinkClick();

        void onGiveUpClick();
    }

    public void setConfirmDialogLisenter(ConfirmDialogLisenter lisenter){
        confirmDialogLisenter = lisenter;
    }
}
