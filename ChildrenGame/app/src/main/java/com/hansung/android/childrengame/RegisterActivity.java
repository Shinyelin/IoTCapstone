package com.hansung.android.childrengame;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/*회원가입 액티비티*/
public class RegisterActivity extends AppCompatActivity {
    private static String TAG = "admin";
    String aGrade = "";
    String aClass = "";
    EditText et_aSchool, et_aGrade, et_aClass, et_aPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_aSchool = (EditText) findViewById(R.id.et_aSchool);
        et_aPwd = (EditText) findViewById(R.id.et_aPwd);

        final Button Gradebtn = (Button) findViewById(R.id.btn_aGrade);
        Gradebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] GradeSelect = new String[]{"1", "2", "3", "4", "5", "6"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder maindialog = new AlertDialog.Builder(RegisterActivity.this);
                maindialog.setTitle("학년을 선택하세요")
                        .setSingleChoiceItems(GradeSelect
                                , 0
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedIndex[0] = which;
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(RegisterActivity.this, GradeSelect[selectedIndex[0]],
                                        Toast.LENGTH_LONG).show();
                                Gradebtn.setText(GradeSelect[selectedIndex[0]]);
                                aGrade = GradeSelect[selectedIndex[0]];

                            }
                        }).create().show();

            }
        });

        final Button Classbtn = (Button) findViewById(R.id.btn_aClass);
        Classbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] ClassSelect = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder main2dialog = new AlertDialog.Builder(RegisterActivity.this);
                main2dialog.setTitle("반을 선택하세요")
                        .setSingleChoiceItems(ClassSelect
                                , 0
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedIndex[0] = which;
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(RegisterActivity.this, ClassSelect[selectedIndex[0]],
                                        Toast.LENGTH_LONG).show();
                                Classbtn.setText(ClassSelect[selectedIndex[0]]);
                                aClass = ClassSelect[selectedIndex[0]];

                            }
                        }).create().show();

            }
        });
    }

    public void register_submitClick(View view) {


        RegisterActivity.InsertData_admin task = new RegisterActivity.InsertData_admin();
        task.execute("http://" + Data.IP_ADDRESS + "/insert_schoolinfo.php", et_aSchool.getText().toString(), aGrade, aClass, et_aPwd.getText().toString());    //회원가입
        Intent intent1 = new Intent(this, LoginActivity.class);
        startActivity(intent1);
    }

    class InsertData_admin extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RegisterActivity.this, "Wait...", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "Post response -" + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String aSchool = (String) params[1];
            String aGrade = (String) params[2];
            String aClass = (String) params[3];
            String aPwd = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "aSchool=" + aSchool + "&aGrade=" + aGrade + "&aClass=" + aClass + "&aPwd=" + aPwd;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {
                Log.d(TAG, "InsertData : Error", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

}
