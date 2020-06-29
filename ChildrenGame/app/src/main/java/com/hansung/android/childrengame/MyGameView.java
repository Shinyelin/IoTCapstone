package com.hansung.android.childrengame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import static com.hansung.android.childrengame.Game2Activity.GAME2SCORE;


public class MyGameView extends SurfaceView implements Callback {
    public static int Width, Height, cx, cy;                          // 화면의 전체 폭과 중심점
    public static int Vec[][] = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    static GameThread mThread;
    static int ATTCK = 0;
    static int surFinish = 0;
    private static String TAG = "Game2score";
    int level_limit = 10;

    Random r = new Random();
    private Activity activity;
    private int x1, y1, x2, y2;                                  // Viewport 좌표
    private int sx1, sy1, ship_x1, ship_y1;                // Viewport가 1회에 이동할 거리
    private Bitmap imgBack1, imgBack2;                    // 배경 이미지
    private Bitmap spaceShip;                                  // 우주선 이미지
    private Bitmap attack, space_attack, energy;
    private int aw, ah, saw, sah, sw, sh;                                                // 우주선의 폭과 높이
    private long counter = 0;                                    // 루프의 전체 반복 횟수
    private boolean canRun = true;                            // 스레드 실행용 플래그
    private int dx, dy;
    private ArrayList<Missle> MissleList;
    private ArrayList<Attack> AttackList;
    private int LIFE = 3;


    //--------------------------------------
    //         생 성 자
    //--------------------------------------
    public MyGameView(Activity paramAct, Context context) {
        super(context);
        activity = paramAct;

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        MissleList = new ArrayList<Missle>();
        AttackList = new ArrayList<Attack>();

        // 화면 해상도 구하기
        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        Width = display.getWidth();
        Height = display.getHeight();

        cx = Width / 2;                   // 화면의 중심점
        cy = Height / 2;


        dx = 0;
        dy = 0;

        Resources res = context.getResources();          // 리소스 읽기
        // 배경화면 읽고 배경화면의 크기를 화면의 크기로 조정
        imgBack1 = BitmapFactory.decodeResource(res, R.drawable.make_uni);
        imgBack1 = Bitmap.createScaledBitmap(imgBack1, Width, Height, true);
        // 우주선 읽고 폭과 높이 계산
        spaceShip = BitmapFactory.decodeResource(res, R.drawable.spaceship);
        sw = spaceShip.getWidth() / 2;
        sh = spaceShip.getHeight() / 2;
        space_attack = BitmapFactory.decodeResource(res, R.drawable.space_attack);
        saw = space_attack.getWidth() / 2;
        sah = space_attack.getHeight() / 2;
        energy = BitmapFactory.decodeResource(res, R.drawable.energy);

        attack = BitmapFactory.decodeResource(res, R.drawable.attack_line);
        aw = attack.getWidth() / 2;
        ah = attack.getHeight() / 2;
        x1 = cx;          // Viewport의 시작 위치는 이미지의 한가운데
        y1 = cy;
        ship_x1 = Width / 2;
        ship_y1 = Height;
        sx1 = -1;         // Viewport를 1회에 이동시킬 거리
        sy1 = -1;

        mThread = new GameThread(context, holder);
    } // 생성자 끝

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }

    //--------------------------------------
    //    Surface가 생성될 때 호출됨
    //--------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {    //학년별 난이도 조절을 위함
        GAME2SCORE = 0;
        switch (Data.aLevel) {
            case "하":
                level_limit = 150;
                break;
            case "중":
                level_limit = 125;
                break;
            case "상":
                level_limit = 100;
                break;
        }
        //mThread.start();

    }

    //--------------------------------------
    //    Surface가 바뀔 때 호출됨
    //--------------------------------------
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    //--------------------------------------
    //    Surface가 삭제될 때 호출됨
    //--------------------------------------
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean done = true;
        canRun = false;

        while (done) {
            try {
                mThread.join();                            // 스레드가 현재 step을 끝낼 때 까지 대기
                done = false;
                InsertData_g2 task = new InsertData_g2();
                task.execute("http://" + Data.IP_ADDRESS + "/insert_gametest2.php", Data.aSchool, Data.aGrade, Data.aClass,
                        Integer.toString(GAME2SCORE));

            } catch (InterruptedException e) {         // 인터럽트 신호가 오면?
                // 그 신호 무시 - 아무것도 않음
            }
            surFinish++;
        } // while
    } // surfaceDestroyed

    // --------------- 스레드 영역-----------------------
    class GameThread extends Thread {
        SurfaceHolder mHolder;

        //--------------------------------------
        //    Thread Constructor
        //--------------------------------------
        public GameThread(Context context, SurfaceHolder holder) {
            mHolder = holder;
        }

        //--------------------------------------
        //    Thread run
        //--------------------------------------
        @Override

        public void run() {
            Rect src = new Rect();                     // Viewport의 좌표
            Rect dst = new Rect();                     // View(화면)의 좌표
            dst.set(0, 0, Width, Height);              // View는 화면 전체 크기
            while (canRun) {
                Canvas canvas = null;
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        if (ATTCK == 1) {
                            AttackList.add(new Attack(Game2Activity.ship.getX() + sw - aw, Height - sh * 2 + 10));//공격

                            ATTCK = 0;
                        }
                        ScrollImage();                                      // Viewport 이동
                        src.set(x1, y1, x1 + cx, y1 + cy);           // 이동한 Viewport 좌표
                        canvas.drawBitmap(imgBack1, src, dst, null);
                        // canvas.drawBitmap(spaceShip, cx - aw, cy - ah, null);
                        MoveAndDrawMissle(canvas);
                        if (AttackList.isEmpty() == false) {
                            MoveAndDrawAttack(canvas);
                            RemoveAttackMissle();
                        }
                        DrawLife(canvas);
                        //canvas.drawBitmap(space_attack, 100, 100, null);


                    }
                } catch (NullPointerException e) {

                } finally {
                    if (canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);

                } // try
            }
            Game2Activity.GAME2_STATUS = Game2Activity.GAME2_STOP;
            Intent in = new Intent(activity, Game2OverActivity.class);
            in.putExtra("G2Score", Game2Activity.GAME2SCORE);
            in.putExtra("G2Level", Game2Activity.GAME2LEVEL);


            activity.startActivity(in);
            activity.finish();
        } // run 끝


        //-------------------------------------ship-
        //    배경 Scroll
        //--------------------------------------
        private void ScrollImage() {
            counter++;
            if (counter % 2 == 0) {                  // 루프의 2회에 1번씩 스크롤
                x1 = sx1;                            // Viewport를 위로 이동 (sx는 음수(-)임)
                y1 += sy1;
                if (x1 < 0) x1 = cx;                 // 이미지를 벗어나면 이미지의 중심으로 이동
                if (y1 < 0) y1 = cy;
                y2 = y2 - 10;
            }
            if (counter % 750 == 0) {                    //약 15초
                Game2Activity.myHandler.sendEmptyMessage(1);
            }
            if (counter % level_limit == 0) {
                MissleList.add(new Missle(cx - saw + randomRange(-2, 2) * 300, 0));
                Log.d("MissleGET", Float.toString(Game2Activity.ship.getX()));
                //level_count++;

                if (level_limit > 50) {
                    level_limit = level_limit - 5;
                } else {
                    for (int i = 0; i < MissleList.size(); i++) {
                        MissleList.get(i).setDy();
                    }
                }


            }


        } // Scroll 끝


        // ---------------------------------
        //      미사일 이동하고 그리기
        // ---------------------------------
        private void MoveAndDrawMissle(Canvas canvas) {

            for (int i = MissleList.size() - 1; i >= 0; i--) {      // ArrayList를 역순으로 검사
                if (MissleList.get(i).Move() == true)              // 미사일 이동 후 화면을 벗어났는지 판단
                {
                    MissleList.remove(i);                            // 미사일 삭제
                    LIFE--;
                    if (LIFE == 0) {
                        canRun = false;
                    }
                }
            }

            try {
                for (Missle tMissle : MissleList) {                      // 모든 미사일을 canvas에 그린다
                    canvas.drawBitmap(space_attack, tMissle.x, tMissle.y, null);
                }
            } catch (ConcurrentModificationException e) {
                for (Missle tMissle : MissleList) {                      // 모든 미사일을 canvas에 그린다
                    canvas.drawBitmap(space_attack, tMissle.x, tMissle.y, null);
                }
            }

        } // MoveAndDrawMissle

        private void MoveAndDrawAttack(Canvas canvas) {

            for (int i = AttackList.size() - 1; i >= 0; i--) {      // ArrayList를 역순으로 검사
                if (AttackList.get(i).Move() == true)              // 공격 이동 후 화면을 벗어났는지 판단
                    AttackList.remove(i);                            // 공격 삭제
            }


            for (Attack tAttack : AttackList) {                      // 모든 공격을 canvas에 그린다
                canvas.drawBitmap(attack, tAttack.attack_x, tAttack.attack_y, null);
            }
        } // MoveAndDrawAttack

        private void DrawLife(Canvas canvas) {
            for (int l = 0; l < LIFE; l++) {
                canvas.drawBitmap(energy, Width - 150 - l * 150, 130, null);
            }
        } // DrawLife

        private void RemoveAttackMissle() { //우주선에서 나온 공격과 외계에서 날아온 공격의 좌표값이 일치하면 삭제

            loop:
            for (int j = 0; j < AttackList.size(); j++) {
                for (int i = 0; i < MissleList.size(); i++) {

                    if ((MissleList.get(i).x + saw == AttackList.get(j).attack_x + aw)
                            && (MissleList.get(i).y <= AttackList.get(j).attack_y) &&
                            (MissleList.get(i).y + sah * 2 >= AttackList.get(j).attack_y)) {
                        MissleList.remove(i);
                        AttackList.remove(j);
                        Log.d("REMOVE", "OK");
                        Game2Activity.myHandler.sendEmptyMessage(0);
                        break loop;
                    }
                }

            }


        }


    } // Thread 끝

    class InsertData_g2 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // progressDialog = ProgressDialog.show(Game2Activity.this,"Wait...",null,true,true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "Post response -" + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String g2School = (String) params[1];
            String g2Grade = (String) params[2];
            String g2Class = (String) params[3];
            String g2Score = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "g2School=" + g2School + "&g2Grade=" + g2Grade + "&g2Class=" + g2Class + "&g2Score=" + g2Score;

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


} // SurfaceView 끝

