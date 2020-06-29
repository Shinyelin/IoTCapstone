package com.hansung.android.childrengame;

public class GameDataArr {

    private String g_score;
    private String g_school;
    private int g_index;

    public String getG_score() {
        return g_score;
    }

    public String getG_school() {
        return g_school;
    }

    public String getG_index() {
        return Integer.toString(g_index);
    }


    public void setGame_gScore(String member_score) {
        this.g_score = member_score;
    }

    public void setGame_gSchool(String member_school) {
        this.g_school = member_school;
    }

    public void setGame_index(int index) {
        this.g_index = index;
    }
}