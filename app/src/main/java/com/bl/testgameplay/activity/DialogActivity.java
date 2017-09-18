package com.bl.testgameplay.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bl.testgameplay.R;

import java.util.Random;

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
 * 作者: 卞乐强 on 2017/7/12 02:40.
 * 邮箱: 921321110@qq.com
 * github: https://github.com/LeqiangBian
 * csdn: http://blog.csdn.net/qq_34365898
 */
public class DialogActivity extends BaseActivity {

    private Button bt_test;
    private TextView tv_content;
    private boolean booleanExtra;
    private RatingBar rabar;

    private SoundPool soundPool;
    private int soundID1;
    private int soundID2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_dialog);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initAlization() {
        Intent intent = getIntent();
        booleanExtra = intent.getBooleanExtra("areyouok", false);

        /*初始化几个声音*/
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (booleanExtra){
                    soundPool.play(soundID1, 1.0f, 1.0f, 0, 0, 1.0f);//最后一个参数是速率0.5-2.0
                }else{
                    soundPool.play(soundID2, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });
        soundID1 = soundPool.load(DialogActivity.this, R.raw.succes, 1);//第三个是优先级,默认是1
        soundID2 = soundPool.load(DialogActivity.this, R.raw.defait, 1);
    }

    @Override
    protected void initView() {
        bt_test = (Button) findViewById(R.id.bt_testre);
        tv_content = (TextView) findViewById(R.id.tv_test_content);
        rabar = (RatingBar) findViewById(R.id.ratingBar);
    }

    @Override
    protected void initListener() {
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initData() {
        rabar.setEnabled(false);
        if (booleanExtra){
            float v = (float) (Math.random() * 1.5+3.5);
            rabar.setRating(v);
            tv_content.setText("成功");
        }else{
            float v = (float) (Math.random() * 2);
            rabar.setRating(v);
            tv_content.setText("失败");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
