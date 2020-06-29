package com.hansung.android.childrengame;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
// 불빛터치 놀이시설 학교 대항전
public class SchoolScoreGame3Fragment extends Fragment {

    String TAG = "학교대항전 게임3";

    //private TextView mTextViewResult;
    private ArrayList<GameDataArr> mArrayList;
    private UsersAdapter mUsersadapter;
    private GridView GridView_carinfo;
    private String mJsonString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vs_game3, null);
        GridView_carinfo = (GridView) view.findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();
        mUsersadapter = new UsersAdapter(getActivity(), mArrayList);
        GridView_carinfo.setAdapter(mUsersadapter);
        mArrayList.clear();

        SchoolScoreGame3Fragment.GetData task = new SchoolScoreGame3Fragment.GetData();
        mUsersadapter.notifyDataSetChanged();
        task.execute("http://" + Data.IP_ADDRESS + "/getjson_game3score.php", ""); //불빛터치게임의 학교별 순위를 가져옴

        return view;
    }

    private void showResult() {

        String TAG_JSON = "webnautes";
        String TAG_G3SCHOOL = "g3school";
        String TAG_G3SCORE = "sumg3";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                String g3Score = item.getString(TAG_G3SCORE);
                String g3School = item.getString(TAG_G3SCHOOL);

                GameDataArr gameDataArr = new GameDataArr();
                gameDataArr.setGame_gScore(g3Score);
                gameDataArr.setGame_gSchool(g3School);
                gameDataArr.setGame_index(i + 1);
                mArrayList.add(gameDataArr);
                mUsersadapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result == null) {

            } else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                errorString = e.toString();

                return null;
            }

        }
    }
}