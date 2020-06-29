package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
//반 대항전 액티비티
public class ClassScoreActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ClassScoreGame1Fragment fragment1;
    private ClassScoreGame2Fragment fragment2;
    private ClassScoreGame3Fragment fragment3;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_score);

        fragmentManager = getSupportFragmentManager();

        fragment1 = new ClassScoreGame1Fragment();  //제자리달리기 반 대항전
        fragment2 = new ClassScoreGame2Fragment();  //우주선 반 대항전
        fragment3 = new ClassScoreGame3Fragment();  //불빛터치 반 대항전

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.VSContent, fragment1).commitAllowingStateLoss();
    }

    public void clickHandler(View view) {
        transaction = fragmentManager.beginTransaction();

        switch (view.getId()) { //프레그먼트 교체
            case R.id.btn_fragmentA:
                transaction.replace(R.id.VSContent, fragment1).commitAllowingStateLoss();
                break;
            case R.id.btn_fragmentB:
                transaction.replace(R.id.VSContent, fragment2).commitAllowingStateLoss();
                break;
            case R.id.btn_fragmentC:
                transaction.replace(R.id.VSContent, fragment3).commitAllowingStateLoss();
                break;
        }
    }

}
