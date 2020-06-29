package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class VSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs);


    }

    public void bt_school_vs(View view) {   //학교대항전 액티비티로 이동
        Intent intent1 = new Intent(this, SchoolScoreActivity.class);
        startActivity(intent1);
    }

    public void bt_class_vs(View view) {   //반대항전 액티비티로 이동
        Intent intent2 = new Intent(this, ClassScoreActivity.class);
        startActivity(intent2);
    }
}
