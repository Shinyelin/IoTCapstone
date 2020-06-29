package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class Game2OverActivity extends AppCompatActivity {
    TextView tv_result_g2score, tv_g2result, tv_g2level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over2);
        Intent resultintent = getIntent();
        int result_g2_score = resultintent.getIntExtra("G2Score", 0);
        int result_g2_level = resultintent.getIntExtra("G2Level", 0);
        tv_result_g2score = (TextView) findViewById(R.id.tv_result_G2Score);
        tv_g2result = (TextView) findViewById(R.id.tv_g2result);
        tv_g2level = (TextView) findViewById(R.id.tv_g2level);
        if (result_g2_score == 0) {
            tv_g2result.setText("LOSE");
        } else {
            tv_g2result.setText("WIN");

        }
        tv_g2level.setText(Integer.toString(result_g2_level));


        tv_result_g2score.setText(Integer.toString(result_g2_score));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getApplicationContext(), Game2Activity.class);
                startActivity(intent1);
                finish();

            }
        }, 4000);
    }

}
