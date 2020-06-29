package com.hansung.android.childrengame;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersAdapter extends BaseAdapter {

    private ArrayList<GameDataArr> mInfoList = new ArrayList<>();
    private Activity context = null;

    public UsersAdapter(Activity context, ArrayList<GameDataArr> list) {
        this.context = context;
        this.mInfoList = list;
    }

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public GameDataArr getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_d' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        /* 'listview_d'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_gamescore = (TextView) convertView.findViewById(R.id.tv_list_gamescore);
        TextView tv_gameschool = (TextView) convertView.findViewById(R.id.tv_list_gameschool);
        TextView tv_gameindex = (TextView) convertView.findViewById(R.id.tv_list_gameindex);


        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        GameDataArr myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        tv_gamescore.setText(myItem.getG_score());
        tv_gameschool.setText(myItem.getG_school());
        tv_gameindex.setText(myItem.getG_index());



        return convertView;
    }

}