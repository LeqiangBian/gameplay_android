package com.bl.testgameplay.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bl.testgameplay.R;
import com.bl.testgameplay.utils.TranslatePeople;

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
 * 作者: 卞乐强 on 2017/7/14 15:01.
 * 邮箱: 921321110@qq.com
 * github: https://github.com/LeqiangBian
 * csdn: http://blog.csdn.net/qq_34365898
 */
public class SplashActivity extends BaseActivity {

    private VideoView vv;
    private ImageView iv_test1;
    private ImageView iv_test2;
    private Button bt_start;
    private float dimensiony;
    private float mSpeed = 0.64f;
    private boolean iswhtherLoopFlag = false;
    private TranslatePeople translate;
    private Thread thread;
    private Button bt_gamestart;
    private RelativeLayout rl_splash;
    private ImageView iv_finger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    monkeyAnimal();
                    break;
                case 2:
                    changeTargetAnimal();
                    buttonAlphaVis();
                    break;
                case 3:
                    fingerAnimal();
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
    }

    @Override
    protected void initView() {
        vv = (VideoView) findViewById(R.id.vv);
        iv_test1 = (ImageView) findViewById(R.id.iv_test1);
        iv_test2 = (ImageView) findViewById(R.id.iv_test2);
        bt_start = (Button) findViewById(R.id.bt_start_game);
        bt_gamestart = (Button) findViewById(R.id.bt_start_game);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        iv_finger = (ImageView) findViewById(R.id.iv_finger);
    }

    @Override
    protected void initListener() {
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Bofang();

            }
        });
        bt_gamestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(intent);
                bt_gamestart.setEnabled(false);
                finish();
            }
        });
    }

    private void fingerAnimal() {
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(iv_finger, "translationX", -204*dimensiony,1622*dimensiony);
        oa3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (bt_gamestart.isEnabled()){
                    bt_gamestart.callOnClick();
                }
                bt_gamestart.setEnabled(false);
            }
        });

        oa3.setDuration(2000);
        oa3.start();
    }

    @Override
    protected void initData() {
        iv_finger.setX(-204*dimensiony);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa = ObjectAnimator.ofFloat(vv, "scaleX", 1f ,1.26f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(vv, "scaleY", 1f ,1.26f);
        set.playTogether(oa,oa2);
        set.start();
        Bofang();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg3 = Message.obtain();
                msg3.what = 3;
                handler.sendMessage(msg3);
            }
        },8000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGoGoGo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iswhtherLoopFlag = false;
        thread = null;
    }

    /*播放*/
    private void Bofang(){
        Uri parse = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.worldstart);
        vv.setVideoPath(String.valueOf(parse));
        vv.start();
    }

    /*运行吧 小猴子一直往前走后面的路变成透明的，并且香胶旋转消失*/
    public void startGoGoGo(){
        //第二个强制退出
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(700);
                Message msg1 = Message.obtain();
                msg1.what = 1;
                handler.sendMessage(msg1);
                iswhtherLoopFlag = true;

                while (iswhtherLoopFlag) {//第二个强制退出
                    SystemClock.sleep(10);
                    iswhtherLoopFlag = translate.whetherIntersection(
                            rl_splash.getX(),
                            rl_splash.getX() + 100 * dimensiony,
                            rl_splash.getY(),
                            rl_splash.getY() + 100 * dimensiony,
                            iv_test2.getX(),
                            iv_test2.getX() + 100 * dimensiony,
                            iv_test2.getY(),
                            iv_test2.getY() + 100 * dimensiony
                    );

                }
                Message msg2 = Message.obtain();
                msg2.what = 2;
                handler.sendMessage(msg2);
            }
        });
        thread.start();
    }

    /*猴子移动*/
    private void monkeyAnimal(){
        AnimationDrawable anim1 = (AnimationDrawable) iv_test1.getBackground();
        anim1.start();
        AnimationDrawable anim2 = (AnimationDrawable) iv_test2.getBackground();
        anim2.start();
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_test1, "rotation", 0f , 90f);
        oa1.setDuration((long) (90/mSpeed));
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(rl_splash, "translationX", 0f,1920*dimensiony);
        oa2.setDuration((long) (1920*dimensiony/mSpeed));
        set.playSequentially(oa1,oa2);
        set.start();
    }

    /*目标撞击动画*/
    private void changeTargetAnimal(){

        Log.e("fawefasd","fawefsdfsd");
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_test2, "rotation", 0f,360f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(iv_test2, "scaleX", 1f,0);
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(iv_test2, "scaleY", 1f,0);
        set.playTogether(oa1,oa2,oa3);
        set.setDuration(300);
        set.start();
    }

    /*button显示出来*/
    private void buttonAlphaVis(){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(bt_gamestart, "scaleX", 0f , 1f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(bt_gamestart, "scaleY", 0f , 1f);
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(bt_gamestart, "alpha", 0f,1.0f);
        set.setDuration(700);
        set.playTogether(oa1,oa2,oa3);
        set.start();
    }
}
