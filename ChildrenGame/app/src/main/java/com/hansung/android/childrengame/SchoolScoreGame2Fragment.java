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
// 우주선 놀이시설 학교 대항전
public class SchoolScoreGame2Fragment extends Fragment {

    String TAG = "학교대항전 게임2";
    private EditText mEditTextCnum;
    private EditText mEditTextCowner;
    private EditText mEditTextCphone;
    private ArrayList<GameDataArr> mArrayList;
    private UsersAdapter mUsersadapter;
    private GridView GridView_carinfo;
    private EditText mEditTextSearchKeyword;
    private String mJsonString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vs_game2, null);
        GridView_carinfo = (GridView) view.findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();
        mUsersadapter = new UsersAdapter(getActivity(), mArrayList);
        GridView_carinfo.setAdapter(mUsersadapter);
        mArrayList.clear();

        SchoolScoreGame2Fragment.GetData task = new SchoolScoreGame2Fragment.GetData();
        mUsersadapter.notifyDataSetChanged();
        task.execute("http://" + Data.IP_ADDRESS + "/getjson_game2score.php", ""); //우주서게임의 학교별 순위를 가져옴

        return view;
    }

    private void showResult() {

        String TAG_JSON = "webnautes";
        String TAG_G2SCHOOL = "g2school";
        String TAG_G2SCORE = "sumg2";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                String g2Score = item.getString(TAG_G2SCORE);
                String g2School = item.getString(TAG_G2SCHOOL);

                GameDataArr gameDataArr = new GameDataArr();
                gameDataArr.setGame_gScore(g2Score);
                gameDataArr.setGame_gSchool(g2School);
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

            //progressDialog.dismiss();
            // mTextViewResult.setText(result);
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

                //Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }
}