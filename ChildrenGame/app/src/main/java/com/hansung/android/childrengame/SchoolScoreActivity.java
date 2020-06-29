package com.hansung.android.childrengame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
//학교 대항전 액티비티
public class SchoolScoreActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private SchoolScoreGame1Fragment fragment1;
    private SchoolScoreGame2Fragment fragment2;
    private SchoolScoreGame3Fragment fragment3;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_school);

        fragmentManager = getSupportFragmentManager();

        fragment1 = new SchoolScoreGame1Fragment();  //제자리달리기 학교 대항전
        fragment2 = new SchoolScoreGame2Fragment();  //우주선 학교 대항전
        fragment3 = new SchoolScoreGame3Fragment();  //불꽃터치게 학교 대항전

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.VSContent, fragment1).commitAllowingStateLoss();
    }

    public void clickHandler(View view) {
        transaction = fragmentManager.beginTransaction();

        switch (view.getId()) {
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
