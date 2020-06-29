package com.hansung.android.childrengame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "login";
    EditText et_School, et_grade, et_class, et_pwd;
    String mJsonString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        et_School = (EditText) findViewById(R.id.ET_school);
        et_grade = (EditText) findViewById(R.id.ET_grade);
        et_class = (EditText) findViewById(R.id.ET_class);
        et_pwd = (EditText) findViewById(R.id.ET_pass);

    }

    public void onClick_login(View view) {

        Data.aSchool = et_School.getText().toString();
        Data.aGrade = et_grade.getText().toString();
        Data.aClass = et_class.getText().toString();
        switch ((Integer.parseInt(Data.aGrade) + 1) / 2) {    //학년별 난이도 조절을 위함
            case 1:
                Data.aLevel = "하";
                break;
            case 2:
                Data.aLevel = "중";
                break;
            case 3:
                Data.aLevel = "상";
                break;
        }


        GetData task = new GetData();
        task.execute(Data.aSchool, Data.aGrade, Data.aClass, et_pwd.getText().toString());  //로그인
    }

    public void onClick_register(View view) {
        Intent intent1 = new Intent(this, RegisterActivity.class);
        startActivity(intent1);
    }


    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();


            Log.d(TAG, "response - " + result);

            if (result == null) {

                Toast.makeText(LoginActivity.this, errorString,
                        Toast.LENGTH_SHORT).show();
            } else {

                mJsonString = result;


                Toast.makeText(LoginActivity.this, mJsonString,
                        Toast.LENGTH_LONG).show();

                if (mJsonString.equals("환영합니다")) {
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);

                }


            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String searchKeyword4 = params[3];

            String serverURL = "http://" + Data.IP_ADDRESS + "/query_login.php";
            String postParameters = "aSchool=" + searchKeyword1 + "&aGrade=" + searchKeyword2 + "&aClass=" + searchKeyword3 + "&aPwd=" + searchKeyword4;


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

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

}