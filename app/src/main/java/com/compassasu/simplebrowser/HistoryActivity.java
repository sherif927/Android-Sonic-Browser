package com.compassasu.simplebrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView mRecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mRecyclerview=(RecyclerView)findViewById(R.id.recyclerview);
    }
}
