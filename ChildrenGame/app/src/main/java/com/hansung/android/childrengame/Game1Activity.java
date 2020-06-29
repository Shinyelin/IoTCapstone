package com.hansung.android.childrengame;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import static com.hansung.android.childrengame.BluetoothActivity.BT_CONNECTING_STATUS;

public class Game1Activity extends AppCompatActivity {

    final static int BT_MESSAGE_READ = 2;
    private static String TAG = "game1";
    LinearLayout layout_left, layout_right;
    ProgressBar pg_right, pg_left;
    Button btn_start;
    TextView tv_right, tv_left, tv_countdown, tv_goalwalk;
    int cnt_right = 0;
    int cnt_left = 0;
    int final_playerL_walk = 0;
    int final_playerR_walk = 0;
    int final_playerL_score = 0;
    int final_playerR_score = 0;
    int limit_walk = 0;
    int GAME1_STATUS = 0;
    int GAME1_RUN = 1;
    int GAME1_STOP = 2;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    Handler mBluetoothHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game1);
        layout_left = (LinearLayout) findViewById(R.id.layout_left);
        layout_right = (LinearLayout) findViewById(R.id.layout_right);

        layout_left.setBackgroundResource(R.drawable.b1_1);
        layout_right.setBackgroundResource(R.drawable.b1_1);

        switch (Data.aLevel) {    //학년별 난이도 조절을 위함
            case "하":
                limit_walk = 20;
                break;
            case "중":
                limit_walk = 30;
                break;
            case "상":
                limit_walk = 40;
                break;
        }


        tv_countdown = findViewById(R.id.tv_countdown);
        tv_countdown.setVisibility(View.INVISIBLE);
        btn_start = findViewById(R.id.btn_start_game1);
        tv_goalwalk = findViewById(R.id.tv_goal_walk);


        pg_left = findViewById(R.id.pb_left);
        pg_left.setMax(limit_walk);
        tv_left = findViewById(R.id.tv_run_left);
        pg_right = findViewById(R.id.pb_right);
        pg_right.setMax(limit_walk);
        tv_right = findViewById(R.id.tv_run_right);
        tv_goalwalk.setText(Integer.toString(limit_walk));

        mThreadConnectedBluetooth = new ConnectedBluetoothThread(BluetoothActivity.mBluetoothSocket);
        mThreadConnectedBluetooth.start();
        mBluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("COUNT", Character.toString(readMessage.charAt(0)));
                    //tv_right.setText(readMessage);
                    if (GAME1_STATUS == GAME1_RUN) {
                        if (readMessage.charAt(0) == '2') {
                            pg_right.setProgress(cnt_right);
                            cnt_right++;
                            tv_right.setText(Integer.toString(cnt_right));
                            change_background_R(cnt_right);
                            if (cnt_right == limit_walk) {
                                Toast toast_gameover_R = Toast.makeText(getApplicationContext(), "2P\n 초과달성", Toast.LENGTH_LONG);
                                toast_gameover_R.show();
                            }
                        } else if (readMessage.charAt(0) == '1') {

                            pg_left.setProgress(cnt_left);
                            cnt_left++;
                            tv_left.setText(Integer.toString(cnt_left));
                            change_background_L(cnt_left);
                            if (cnt_left == limit_walk) {
                                Toast toast_gameover_L = Toast.makeText(getApplicationContext(), "1P\n 초과달성", Toast.LENGTH_LONG);
                                toast_gameover_L.show();
                            }
                        } else {
                            pg_right.setProgress(cnt_right);
                            cnt_right++;
                            tv_right.setText(Integer.toString(cnt_right));
                            change_background_R(cnt_right);
                            pg_left.setProgress(cnt_left);
                            cnt_left++;
                            tv_left.setText(Integer.toString(cnt_left));
                            change_background_L(cnt_left);
                        }
                    } else {
                        if (readMessage.charAt(0) == 'O') { //아두이노에서 게임시작 문자열 전송


                            CountDownTimer countDownTimer_ready = new CountDownTimer(3000, 1000) { //3초간 준비 카운트다운 시작 후 게임 시작
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    btn_start.setText(String.format(Locale.getDefault(), "%d초", millisUntilFinished / 1000L + 1));

                                }

                                @Override
                                public void onFinish() {
                                    cnt_right = 0;
                                    cnt_left = 0;
                                    btn_start.setVisibility(View.INVISIBLE);
                                    tv_countdown.setVisibility(View.VISIBLE);

                                    GAME1_STATUS = GAME1_RUN;
                                    CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {   //제한시간 시작
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            tv_countdown.setText(String.format(Locale.getDefault(), "%d초", millisUntilFinished / 1000L));

                                        }

                                        @Override
                                        public void onFinish() {    //제한시간 종료
                                            GAME1_STATUS = GAME1_STOP;
                                            tv_countdown.setText("시간 종료");


                                            tv_countdown.setVisibility(View.INVISIBLE);
                                            btn_start.setText("Ready");
                                            btn_start.setVisibility(View.VISIBLE);
                                            pg_left.setProgress(0);
                                            pg_right.setProgress(0);

                                            final_playerL_walk = cnt_left;
                                            final_playerR_walk = cnt_right;

                                            if (final_playerL_walk >= limit_walk) {  //1P의 걸음 수가 목표 걸음 수보다 클 때
                                                final_playerL_score = final_playerL_walk * 300;
                                                InsertData_g1 task = new InsertData_g1();
                                                task.execute("http://" + Data.IP_ADDRESS + "/insert_gametest.php", Data.aGrade, Data.aClass,
                                                        Integer.toString(final_playerL_walk), Integer.toString(final_playerL_score), Data.aSchool);  //게임 오버에 따른 데이터 저장

                                            } else {
                                                final_playerL_score = 0;
                                            }

                                            if (final_playerR_walk >= limit_walk) {  //2P의 걸음 수가 목표 걸음 수보다 클 때
                                                final_playerR_score = final_playerR_walk * 300;
                                                InsertData_g1 task = new InsertData_g1();
                                                task.execute("http://" + Data.IP_ADDRESS + "/insert_gametest.php", Data.aGrade, Data.aClass,
                                                        Integer.toString(final_playerR_walk), Integer.toString(final_playerR_score), Data.aSchool);  //게임 오버에 따른 데이터 저장

                                            } else {
                                                final_playerR_score = 0;
                                            }


                                            Intent intent = new Intent(getApplicationContext(), Game1OverActivity.class);   //게임 오버 액티비티로 데이터와 함께 이동
                                            intent.putExtra("PLscore", final_playerL_score);
                                            intent.putExtra("PRscore", final_playerR_score);
                                            intent.putExtra("PLwalk", final_playerL_walk);
                                            intent.putExtra("PRwalk", final_playerR_walk);
                                            startActivity(intent);

                                            tv_right.setText("0");
                                            tv_left.setText("0");


                                        }
                                    }.start();


                                }
                            }.start();


                        }
                    }

                }
            }
        };
        mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();





    }

    public void change_background_L(int walkcnt_L) {    //1P의 배경 교체 효과

        switch (walkcnt_L % 10) {
            case 0:
                layout_left.setBackgroundResource(R.drawable.b1_1);
                break;
            case 1:
                layout_left.setBackgroundResource(R.drawable.b1_2);
                break;
            case 2:
                layout_left.setBackgroundResource(R.drawable.b1_3);
                break;
            case 3:
                layout_left.setBackgroundResource(R.drawable.b1_4);
                break;
            case 4:
                layout_left.setBackgroundResource(R.drawable.b1_5);
                break;
            case 5:
                layout_left.setBackgroundResource(R.drawable.b1_6);
                break;
            case 6:
                layout_left.setBackgroundResource(R.drawable.b1_7);
                break;
            case 7:
                layout_left.setBackgroundResource(R.drawable.b1_8);
                break;
            case 8:
                layout_left.setBackgroundResource(R.drawable.b1_9);
                break;
            case 9:
                layout_left.setBackgroundResource(R.drawable.b1_10);
                break;

        }

    }

    public void change_background_R(int walkcnt_R) {    //2P의 배경 교체 효과

        switch (walkcnt_R % 10) {
            case 0:
                layout_right.setBackgroundResource(R.drawable.b1_1);
                break;
            case 1:
                layout_right.setBackgroundResource(R.drawable.b1_2);
                break;
            case 2:
                layout_right.setBackgroundResource(R.drawable.b1_3);
                break;
            case 3:
                layout_right.setBackgroundResource(R.drawable.b1_4);
                break;
            case 4:
                layout_right.setBackgroundResource(R.drawable.b1_5);
                break;
            case 5:
                layout_right.setBackgroundResource(R.drawable.b1_6);
                break;
            case 6:
                layout_right.setBackgroundResource(R.drawable.b1_7);
                break;
            case 7:
                layout_right.setBackgroundResource(R.drawable.b1_8);
                break;
            case 8:
                layout_right.setBackgroundResource(R.drawable.b1_9);
                break;
            case 9:
                layout_right.setBackgroundResource(R.drawable.b1_10);
                break;

        }

    }

    public void bt_back_game1(View view) {
        Intent intent1 = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent1);

    }

    class InsertData_g1 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Game1Activity.this, "Wait...", null, true, true);
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
            String g1Grade = (String) params[1];
            String g1Class = (String) params[2];
            String walkCount = (String) params[3];
            String g1Score = (String) params[4];
            String g1School = (String) params[5];

            String serverURL = (String) params[0];
            String postParameters = "g1Grade=" + g1Grade + "&g1Class=" + g1Class + "&walkCount=" + walkCount + "&g1Score=" + g1Score + "&g1School=" + g1School;

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

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            //byte[] buffer = new byte[1024];

            while (true) {
                try {
                    int bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        byte[] buffer = new byte[1024];
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }


}
