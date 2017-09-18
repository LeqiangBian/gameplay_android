package com.bl.testgameplay.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bl.testgameplay.R;
import com.bl.testgameplay.utils.NoFastClickUtils;
import com.bl.testgameplay.utils.TranslatePeople;

import java.util.ArrayList;

/**
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 * <p>
 * 作者: 卞乐强 on 2017/7/13 14:49.
 * 邮箱: 921321110@qq.com
 * github: https://github.com/LeqiangBian
 * csdn: http://blog.csdn.net/qq_34365898
 */

public class HomeActivity extends BaseActivity {

    private RelativeLayout ll_tesdt;
    private TranslatePeople translate;
    private float dimensiony;
    private ArrayList<int[]> list;
    private Toast toast;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int checkpoint = (int) msg.obj-1;
                    Log.e("afsdasdfasd","checkpoint :"+checkpoint);
                    ll_tesdt.setEnabled(false);
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    intent.putExtra("checkpoint",checkpoint);
                    startActivityForResult(intent,0);
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };
    @Override
    protected void initAlization() {
        translate = new TranslatePeople();
        dimensiony = getResources().getDimension(R.dimen.y1);
        toast = Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT);
        /*初始化11个坐标信息*/
        list = new ArrayList();
        int[] ints1 = {45,235,850,1041};
        int[] ints2 = {425,621,803,993};
        int[] ints3 = {681,873,645,831};
        int[] ints4 = {487,683,437,625};
        int[] ints5 = {101,291,247,439};
        int[] ints6 = {433,623,87,277};
        int[] ints7 = {861,1051,161,351};
        int[] ints8 = {1033,1215,367,557};
        int[] ints9 = {1181,1371,69,859};
        int[] ints10 = {1470,1657,811,1005};
        int[] ints11 = {1719,1920,711,913,};
        list.add(ints1);
        list.add(ints2);
        list.add(ints3);
        list.add(ints4);
        list.add(ints5);
        list.add(ints6);
        list.add(ints7);
        list.add(ints8);
        list.add(ints9);
        list.add(ints10);
        list.add(ints11);
    }

    @Override
    protected void initView() {
        ll_tesdt = (RelativeLayout) findViewById(R.id.ll_test_bg);
        ImageView iv_02= (ImageView) findViewById(R.id.iv_test2);
        AnimationDrawable anim2 = (AnimationDrawable) iv_02.getBackground();
        anim2.start();
    }

    @Override
    protected void initListener() {
        ll_tesdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (NoFastClickUtils.isFastClick()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast.setText("傻吊你那么快戳我干什么?");
                                    toast.show();
                                }
                            });
                        }else{
                            clickWhereSb(motionEvent.getX(),motionEvent.getY());
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        startBG_music();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ll_tesdt.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            //释放资源
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            switch (resultCode) {
                case RESULT_OK:
                    startBG_music();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startBG_music(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();
                    //设置播放的文件
                    AssetFileDescriptor fileDescriptor;
                    int random = (int)(Math.random()*10)%2;
                    if (random == 0){
                        fileDescriptor = getAssets().openFd("delailianmeng.mp3");
                    }else {
                        fileDescriptor = getAssets().openFd("homebg.mp3");
                    }
                    mediaPlayer.setDataSource(fileDescriptor);
                    //mediaPlayer.prepare();//同步的准备,就是运行在主线程里的,要是没有准备好就不会播放音乐,阻塞主线程
                    mediaPlayer.prepareAsync();//异步的准备,开启子线程去准备
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            return false;
                        }
                    });

                    //如果要用异步准备就要注册setOnPreparedListener
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {//回调方法
                            if (!mediaPlayer.isPlaying()){
                                mediaPlayer.start();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*判断点哪里了*/
    public void clickWhereSb(final float x1, final float y1){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean bb = false;
                int i = 0;
                for (int[] ints:list){
                    i = i+1;
                    bb = bb||translate.whetherInRange(x1,y1,ints[0],ints[1],ints[2],ints[3]);
                    if (bb){
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = i;
                        handler.sendMessage(msg);
                        break;
                    }
                }
            }
        }).start();
    }
}
