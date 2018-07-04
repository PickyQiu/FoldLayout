package com.picky.lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ListView mListView = findViewById(R.id.lv);
        GridView mGridView = findViewById(R.id.gv);
        List<String> mMenuList = new ArrayList<>();
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mMenuList.add("Item" + i);
            mList.add("item" + i);
        }
        mListView.setAdapter(new ArrayAdapter<>(this, R.layout.item_lv, mList));
        mGridView.setAdapter(new ArrayAdapter<>(this, R.layout.item_gv, mMenuList));
    }
}
