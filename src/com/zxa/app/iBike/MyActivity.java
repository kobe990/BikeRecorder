package com.zxa.app.iBike;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.content.Intent;

public class MyActivity extends ActivityGroup {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.setup(this.getLocalActivityManager());

        TabHost.TabSpec spec;
        Intent intent;
        intent = new Intent().setClass(this, RecordActivity.class);
        spec = tabHost.newTabSpec("记录").setIndicator("First").setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, GoalsActivity.class);
        spec = tabHost.newTabSpec("目标").setIndicator("second").setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.main_radiogroup);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId) {
                    case R.id.record_radio:
                        tabHost.setCurrentTab(0);
                        break;
                    case R.id.goals_radio:
                        tabHost.setCurrentTab(1);
                        break;
                }
            }
        });
    }
}
