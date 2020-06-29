package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class Game1OverActivity extends AppCompatActivity {
    TextView tv_result_plwalk, tv_result_prwalk, tv_result_plscore, tv_result_prscore, tv_Lresult, tv_Rresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);
        Intent resultintent = getIntent();
        int result_pL_score = resultintent.getIntExtra("PLscore", 0);
        int result_pR_score = resultintent.getIntExtra("PRscore", 0);
        int result_pL_walk = resultintent.getIntExtra("PLwalk", 0);
        int result_pR_walk = resultintent.getIntExtra("PRwalk", 0);
        tv_result_plscore = (TextView) findViewById(R.id.tv_result_Lscore);
        tv_result_prscore = (TextView) findViewById(R.id.tv_result_Rscore);
        tv_result_plwalk = (TextView) findViewById(R.id.tv_result_Lwalk);
        tv_result_prwalk = (TextView) findViewById(R.id.tv_result_Rwalk);
        tv_Lresult = (TextView) findViewById(R.id.tv_Lresult);
        tv_Rresult = (TextView) findViewById(R.id.tv_Rresult);

        if (result_pL_score == 0) {
            tv_Lresult.setText("LOSE");
        } else {
            tv_Lresult.setText("WIN");

        }

        if (result_pR_score == 0) {
            tv_Rresult.setText("LOSE");
        } else {
            tv_Rresult.setText("WIN");

        }

        tv_result_plscore.setText(Integer.toString(result_pL_score));
        tv_result_prscore.setText(Integer.toString(result_pR_score));
        tv_result_prwalk.setText(Integer.toString(result_pR_walk));
        tv_result_plwalk.setText(Integer.toString(result_pL_walk));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4000);
    }

}
