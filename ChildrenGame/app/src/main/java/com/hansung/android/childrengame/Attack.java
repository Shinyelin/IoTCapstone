package com.hansung.android.childrengame;
/*우주선에서 나오는 공격*/
public class Attack {
    public float attack_x, attack_y;
    private int dx, dy;

    /*생성자*/
    public Attack(float _x, float _y) {
        attack_x = _x;   // 우주선에서 나오는 공격의 위치
        attack_y = _y;
        dy = 30;
    }

    /*우주선에서 나오는 공격 이동*/
    public boolean Move() {
        attack_y -= dy;

        if (attack_x < 0 || attack_x > MyGameView.Width || attack_y < 0 || attack_y > MyGameView.Height)
            return true; // 화면을 벗어나면 true
        else
            return false;
    }
} // ATTACK 끝