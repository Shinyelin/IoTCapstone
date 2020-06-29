package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class Game3OverActivity extends AppCompatActivity {
    TextView tv_result_g3score, tv_result_g3level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game3_over);
        Intent resultintent = getIntent();
        int result_g3_score = resultintent.getIntExtra("G3Score", 0);
        int result_g3_level = resultintent.getIntExtra("G3Level", 0);
        tv_result_g3score = (TextView) findViewById(R.id.tv_result_G3Score);
        tv_result_g3level = (TextView) findViewById(R.id.tv_result_G3Level);


        tv_result_g3score.setText(Integer.toString(result_g3_score));
        tv_result_g3level.setText(Integer.toString(result_g3_level));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getApplicationContext(), Game3Activity.class);
                startActivity(intent1);
                finish();

            }
        }, 7000);
    }

}
