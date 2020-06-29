package com.hansung.android.childrengame;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static com.hansung.android.childrengame.BluetoothActivity.BT_CONNECTING_STATUS;

public class Game2Activity extends AppCompatActivity {
    final static int BT_MESSAGE_READ = 2;
    private final static int DO_UPDATE_SCORE = 0;
    private final static int DO_UPDATE_TEXT = 1;
    static public Handler myHandler;
    static int diswidth, disheight;
    static float ship_x, ship_y, one_diswidth;
    static ImageView ship;
    static TextView TV_g2score, TV_g2level;
    static int GAME2SCORE = 0;
    static int GAME2LEVEL = 1;
    static int GAME2_STATUS = 0;
    static int GAME2_RUN = 1;
    static int GAME2_STOP = 2;
    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    Button g2start;
    private MyGameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game2);
        g2start = (Button) findViewById(R.id.btn_start_game2);
        ship = (ImageView) findViewById(R.id.ship);
        TV_g2score = (TextView) findViewById(R.id.tv_g2score);
        TV_g2level = (TextView) findViewById(R.id.tv_g2level);
        mGameView = new MyGameView(Game2Activity.this, this);   // 변수 생성
        TV_g2score.setText("0");
        TV_g2level.setText("0");
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        diswidth = dm.widthPixels;

        disheight = dm.heightPixels;

        one_diswidth = 300;

        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                final int what = msg.what;
                switch (what) {
                    case DO_UPDATE_SCORE:
                        doScoreUpdate();
                        break;
                    case DO_UPDATE_TEXT:
                        doLevelUpdate();
                        break;
                }
            }
        };

        mThreadConnectedBluetooth = new Game2Activity.ConnectedBluetoothThread(BluetoothActivity.mBluetoothSocket);
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
                    if (GAME2_STATUS == GAME2_RUN) {
                        char Control = readMessage.charAt(0);
                        switch (Control) {
                            case '0':
                                ship_x = ship.getX();
                                ship.setX(350);
                                Log.d("LOCATION", Float.toString(ship.getX()));
                                break;

                            case '1':
                                ship_x = ship.getX();
                                ship.setX(650);
                                Log.d("LOCATION", Float.toString(ship.getX()));
                                break;

                            case '2':
                                ship_x = ship.getX();
                                ship.setX(950);
                                Log.d("LOCATION", Float.toString(ship.getX()));
                                break;

                            case '3':
                                ship_x = ship.getX();
                                ship.setX(1250);
                                Log.d("LOCATION", Float.toString(ship.getX()));
                                break;

                            case '4':
                                ship_x = ship.getX();
                                ship.setX(1550);
                                Log.d("LOCATION", Float.toString(ship.getX()));
                                break;

                            case 'A':
                                MyGameView.ATTCK = 1;
                                break;

                        }
                    } else {
                        if (readMessage.charAt(0) == 'O') { //아두이노에서 게임시작 문자열 전송
                            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) { //3초간 준비 카운트다운 시작 후 게임 시작
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    g2start.setText(String.format(Locale.getDefault(), "%d초", millisUntilFinished / 1000L));

                                }

                                @Override
                                public void onFinish() {
                                    GAME2_STATUS = GAME2_RUN;
                                    MyGameView.mThread.start();
                                    g2start.setVisibility(View.INVISIBLE);
                                }
                            }.start();

                        }
                    }

                }
            }
        };
        mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();


        RelativeLayout layout = (RelativeLayout) findViewById(R.id.g2layout);
        layout.addView(mGameView, 0);


    }


    private void doScoreUpdate() {
        GAME2SCORE = GAME2SCORE + 50;
        TV_g2score.setText(Integer.toString(GAME2SCORE));
    }

    private void doLevelUpdate() {
        GAME2LEVEL = GAME2LEVEL + 1;
        TV_g2level.setText(Integer.toString(GAME2LEVEL));
    }

    public void g2_btn_goRight(View view) {
        ship_x = ship.getX();
        ship.setX(ship_x + one_diswidth);
        Log.d("LOCATION", Float.toString(ship.getX()));


    }

    public void g2_btn_goLeft(View view) {
        ship_x = ship.getX();
        ship.setX(ship_x - one_diswidth);
        Log.d("LOCATION", Float.toString(ship.getX()));

    }

    public void g2_btn_attck(View view) {
        MyGameView.ATTCK = 1;

    }

    public void bt_back_game2(View view) {
        Intent intent1 = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent1);

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
