package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
/*메인 액티비티*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        TextView tv_school = findViewById(R.id.tv_main_school);
        TextView tv_grade = findViewById(R.id.tv_main_grade);
        TextView tv_class = findViewById(R.id.tv_main_class);
        TextView tv_level = findViewById(R.id.tv_main_level);

        tv_school.setText(Data.aSchool);
        tv_grade.setText(Data.aGrade);
        tv_class.setText(Data.aClass);
        tv_level.setText(Data.aLevel);


    }

    public void bt_game_1(View view) {  //제자리 달리기 게임으로 이동
        Intent intent1 = new Intent(this, Game1Activity.class);
        startActivity(intent1);
    }

    public void bt_game_2(View view) {  //우주선 게임으로 이동
        Intent intent1 = new Intent(this, Game2Activity.class);
        startActivity(intent1);
    }

    public void bt_game_3(View view) {  //불빛 터치 게임으로 이동
        Intent intent1 = new Intent(this, Game3Activity.class);
        startActivity(intent1);
    }

    public void bt_vs(View view) {  //대항전 페이지로 이동
        Intent intent1 = new Intent(this, VSActivity.class);
        startActivity(intent1);
    }

    public void bt_bluetooth(View view) {  //블루투스 연동 페이지로 이동
        Intent intent1 = new Intent(this, BluetoothActivity.class);
        startActivity(intent1);
    }
}
