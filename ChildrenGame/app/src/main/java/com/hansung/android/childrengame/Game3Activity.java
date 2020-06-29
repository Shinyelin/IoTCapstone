package com.hansung.android.childrengame;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import static android.content.ContentValues.TAG;
import static com.hansung.android.childrengame.BluetoothActivity.BT_CONNECTING_STATUS;
import static com.hansung.android.childrengame.BluetoothActivity.mBluetoothSocket;

public class Game3Activity extends Activity {
    final static int BT_MESSAGE_READ = 2;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    Handler mBluetoothHandler;
    TextView TV_status, TV_g3Level, TV_text_g3Level;
    ImageView IV_status;
    int GAME3_STATUS = 0;
    int GAME3_RUN = 1;
    int GAME3_STOP = 2;
    int GAME3LEVEL = 1;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);
        TV_status = (TextView) findViewById(R.id.tv_status);
        TV_g3Level = (TextView) findViewById(R.id.tv_g3level);
        TV_text_g3Level = (TextView) findViewById(R.id.tv_text_g3level);
        IV_status = (ImageView) findViewById(R.id.iv_status);
        TV_g3Level.setVisibility(View.INVISIBLE);
        TV_text_g3Level.setVisibility(View.INVISIBLE);
        IV_status.setVisibility(View.INVISIBLE);
        mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
        mThreadConnectedBluetooth.start();
        mBluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    char bt_read = '0';
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                        bt_read = readMessage.charAt(0);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Log.d("Bluetooth/RUN ", Character.toString(bt_read));

                    char Control = bt_read;//readMessage.charAt(0);
                    switch (Control) {  //아두이노에서 받은 문자열
                        case 'L': //눌러야 하는 버튼을 생성했다는 것을 뜻하는 문자열을 수신
                            IV_status.setImageResource(R.drawable.press_btn);
                            break;
                        case 'O':   //게임 시작 준비가 됨
                            if (GAME3_STATUS != GAME3_RUN) {
                                TV_status.setVisibility(View.VISIBLE);
                                Log.d("Bluetooth/press ", Character.toString(bt_read));
                                startTimer();
                            }
                            break;

                        case '1':   //카운트다운
                            IV_status.setImageResource(R.drawable.count_5);
                            break;

                        case '2':   //카운트다운
                            IV_status.setImageResource(R.drawable.count_4);
                            break;

                        case '3':   //카운트다운
                            IV_status.setImageResource(R.drawable.count_3);
                            break;

                        case '4':   //카운트다운
                            IV_status.setImageResource(R.drawable.count_2);
                            break;

                        case '5':   //카운트다운
                            IV_status.setImageResource(R.drawable.count_1);
                            break;

                        case 'T':   //아두이노에서 미션 성공 시
                            if (GAME3_STATUS == GAME3_RUN) {
                                mThreadConnectedBluetooth.write("1");   //아두이노에 전송, 게임 카운트 속도가 빨라짐
                                IV_status.setImageResource(R.drawable.smilingface);
                                GAME3LEVEL++;
                                TV_g3Level.setText(Integer.toString(GAME3LEVEL));
                            }
                            break;

                        case 'F':
                            if (GAME3_STATUS == GAME3_RUN) {
                                mThreadConnectedBluetooth.write("X");   //게임 실패 시
                                IV_status.setImageResource(R.drawable.cryingface);
                                GAME3_STATUS = GAME3_STOP;

                                InsertData_g3 task = new Game3Activity.InsertData_g3();
                                task.execute("http://" + Data.IP_ADDRESS + "/insert_gametest3.php", Data.aGrade, Data.aClass,
                                        Integer.toString(GAME3LEVEL * 100), Data.aSchool);  //게임 오버에 따른 데이터 저장
                                Intent intent = new Intent(getApplicationContext(), Game3OverActivity.class);   //게임 오버 액티비티로 데이터와 함께 이동
                                intent.putExtra("G3Level", GAME3LEVEL);
                                intent.putExtra("G3Score", GAME3LEVEL * 100);
                                startActivity(intent);
                            }

                            break;


                    }

                }
            }
        };
        mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();


    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
        finish();

    }

    void startTimer() { //3초간 준비 카운트다운 시작 후 게임 시작
        cTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TV_status.setText(String.format(Locale.getDefault(), "%d초", millisUntilFinished / 1000L));

            }

            @Override
            public void onFinish() {
                mThreadConnectedBluetooth.write("K");

                TV_status.setVisibility(View.INVISIBLE);
                IV_status.setVisibility(View.VISIBLE);
                TV_g3Level.setVisibility(View.VISIBLE);
                TV_text_g3Level.setVisibility(View.VISIBLE);
                GAME3_STATUS = GAME3_RUN;
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }


    public void bt_back_game3(View view) {
        Intent intent1 = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent1);


    }

    class InsertData_g3 extends AsyncTask<String, Void, String> {
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressDialog = ProgressDialog.show(Game3Activity.this,"Wait...",null,true,true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "Post response -" + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String g3Grade = (String) params[1];
            String g3Class = (String) params[2];
            String g3Score = (String) params[3];
            String g3School = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "g3Grade=" + g3Grade + "&g3Class=" + g3Class + "&g3Score=" + g3Score + "&g3School=" + g3School;

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
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
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