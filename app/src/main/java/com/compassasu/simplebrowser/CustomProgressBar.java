package com.compassasu.simplebrowser;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Kagune on 5/23/2017.
 */
public class CustomProgressBar extends ProgressDialog {
    private Context mContext;
    private ImageView sonicImage;

    public CustomProgressBar(Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        sonicImage=(ImageView)findViewById(R.id.progress_sonic);
        sonicImage.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable anim=(AnimationDrawable)sonicImage.getBackground();
                anim.setOneShot(false);
                anim.start();
            }
        });
    }
}
