package com.hansung.android.childrengame;
/*외부에서 날라오는 공격*/

public class Missle {
    public float x, y;
    private int dx, dy;

    /*생성자*/
    public Missle(float _x, float _y) {
        x = _x;   // 외부 공격 생성 위치
        y = _y;
        //dy = 5;
        switch (Data.aLevel) {
            case "하":
                dy = 1;
                break;
            case "중":
                dy = 5;
                break;
            case "상":
                dy = 10;
                break;
        }
    }

    /*외부 공격 이동*/
    public boolean Move() {
        y += dy;

        if (x < 0 || x > MyGameView.Width || y < 0 || y > MyGameView.Height)
            return true; // 화면을 벗어나면 true
        else
            return false;
    }

    public void setDy() {
        if (this.dy < 10) {
            this.dy++;
        }
    }
} // Missle 끝