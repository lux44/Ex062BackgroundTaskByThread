package com.lux.ex062backgroundtaskbythread;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(view -> startTask());
        findViewById(R.id.btn_stop).setOnClickListener(view -> stopTask());
    }
    //서비스 컴포넌트를 사용하지 않고 백그라운드 작업을 했을때의 문제점 알아보기
    //android 12(api 31)버전 부터는 디바이스의 뒤로가기 버튼을 클릭했을때
    //액티비티가 finish 되지 않고 그냥 홈화면에 가려만 짐.
    //그래서 이전버전들처럼 뒤로가기 버튼 눌렀을때 액티비티가 완전 finish되도록 조정
    //뒤로가기 버튼 클릭시에 자동으로 발동하는 콜백메소드
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    //별도 스레드로 백그라운드 작업을 수행해보기
    MyThread myThread;
    void startTask(){
        if (myThread!=null) return;

        myThread=new MyThread();
        myThread.start(); //자동으로 스레드의 run()메소드가 발동함.
    }
    void stopTask(){
        if (myThread!=null){
            // myThread.stop(); 절대로 스레드를 직접 stop명령으로 멈추면 안됨.
            //스레드의 종료는 run()메소드가 끝나면 자동 stop 됨.
            //while문 때문에 run()메소드가 끝나지 않기 떄문에 종료하려면 while문을 빠져나오면 됨
            //즉, while문의 조건값을 false로 설정하면 됨
            myThread.isRun=false;
            myThread=null;
        }else {
            Toast.makeText(this, "Toast를 참조하고 있지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    //inner class
    class MyThread extends Thread{
        boolean isRun=true;
        @Override
        public void run() {
            //5초마다 Toast를 보여주는 동작을 수행
            while (isRun){
                //별도의 스레드는 화면 변경작업이 불가능
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Thread", Toast.LENGTH_SHORT).show();
                    }
                });
                //5초 동안 sleep 잠시 대기
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }//thread
}