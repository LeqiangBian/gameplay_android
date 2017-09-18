package com.bl.testgameplay.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bl.testgameplay.R;
import com.bl.testgameplay.utils.EvalUtils;
import com.bl.testgameplay.utils.TranslatePeople;
import com.bl.testgameplay.view.MultiLineNoteEditText;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private LinearLayout ll_parent;
    private RelativeLayout rl_bg;
    private RelativeLayout rl_code_gram;
    private LinearLayout rl_test_re;
    private LoopView loopView;
    private ArrayList<String> list;
    //标记一下位置
    private int positonFlag = 0;
    private TextView tv_test01;
    private MultiLineNoteEditText et_test;
    private TranslatePeople translate;
    private ClipboardManager clipboardManager;

    private Bitmap alterBitmap;
    /**
     * 画板
     */
    private Canvas canvas;
    private Paint paint;
    private float dimensiony;
    private ImageView iv_03;
    private TextView tv_05;
    private Button bt_run;
    /*用于标记图片头的方向,0123  左上右下*/
    private int headDirectionFlag = 1;
    private EvalUtils evalUtils;
    private ImageView iv_test_people;
    private List<Animator> items;

    /*平移的像素单位*/
    private int unitPX = 61;
    private Toast toast;

    /*用于标记translation*/
    private float translationX = 0.0f;
    private float translationY = 0.0f;
    /*用于标记角度*/
    private float mRotation = 0.0f;
    private boolean whtherLoopFlag = true;
    private boolean iswhtherLoopFlag = true;

    private ImageView iv_target;

    /*各种播放*/
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private boolean whatSpoolOK = false;
    private int soundID1;
    private int soundID2;
    /*界面是否可见用于处理横竖屏切换的问题*/
    private boolean iSsurfaceVisite = true;
    /*目标和主角的宽高*/
    private float mDiySize = 100;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    et_test.setText((Spanned) msg.obj);
                    break;
                case 2:
                    et_test.setText((String)msg.obj);
                    break;
                case 3:
                    changeTargetAnimal(1);
                    break;
                case 4:
                    changeTargetAnimal(2);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private AnimationDrawable anim1;
    private AnimationDrawable anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initAlization() {
        dimensiony = getResources().getDimension(R.dimen.y1);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        list = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            list.add(i+" m");
        }
        /*初始化翻译员*/
        translate = new TranslatePeople(dimensiony);

        // 创建一个空白的图片
        alterBitmap = Bitmap.createBitmap((int) (1300*dimensiony + 0.5), (int) (900*dimensiony + 0.5), Bitmap.Config.ARGB_8888);//一个像素4个byte
        canvas = new Canvas(alterBitmap);
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        // 设置画笔的颜色
        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);//因为创建一个bitmap他的默认是黑色背景,我们要给他设置一个白色背景
        // 设置画笔的宽度
        paint.setStrokeWidth(2);
        evalUtils = new EvalUtils(MainActivity.this);
        //动画数组
        items = new LinkedList<>();

        toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);

        /*初始化几个声音*/
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                whatSpoolOK = true;
            }
        });
        soundID1 = soundPool.load(MainActivity.this, R.raw.kill_01, 1);//第三个是优先级,默认是1
        soundID2 = soundPool.load(MainActivity.this, R.raw.teemo, 1);//第三个是优先级,默认是1
    }

    @Override
    protected void initView() {
        ll_parent = (LinearLayout) findViewById(R.id.ll_test_parent);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_test_bg);
        rl_code_gram = (RelativeLayout) findViewById(R.id.rl_code_egion);
        rl_test_re = (LinearLayout) findViewById(R.id.rl_code_edit);

        loopView = (LoopView) findViewById(R.id.loopView);
        tv_test01 = (TextView) findViewById(R.id.tv_test01);
        et_test = (MultiLineNoteEditText) findViewById(R.id.et_test03);
        iv_03 = (ImageView) findViewById(R.id.iv_test3);

        tv_05 = (TextView) findViewById(R.id.tv_test04);
        bt_run = (Button) findViewById(R.id.bt_run);
        iv_test_people = (ImageView) findViewById(R.id.iv_test1);

        iv_target = (ImageView) findViewById(R.id.iv_test2);

    }

    @Override
    protected void initListener() {
        for (int i = 0;i<ll_parent.getChildCount();i++){
            ll_parent.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button view1 = (Button) view;
                    CharSequence text = view1.getText();

                    if (text.equals("前")){
                        tv_test01.setText(Html.fromHtml("(向上拖)<br/><br/><font color='#FFFF00'>直行→m</font>"));
                    }else{
                        tv_test01.setText(Html.fromHtml("(向上拖)<br/><br/><font color='#FFFF00'>" + text + "转</font><br/><font color='#FFFF00'>直行→m</font>"));
                    }
                }
            });
        }

        /*用于拖入代码*/
        et_test.setOnDragListener(new MyDragListener());
        rl_test_re.setOnTouchListener(new MyTouchListener());

        iv_03.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            // 当imageview被触摸的时候调用的方法.
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        boolean bb = translate.whetherInRange(
                                event.getX(),
                                event.getY(),
                                iv_test_people.getX(),
                                iv_test_people.getX()+mDiySize*dimensiony,
                                iv_test_people.getY(),
                                iv_test_people.getY()+mDiySize*dimensiony
                        );
                        if (bb&&whatSpoolOK){
                            soundPool.play(soundID2, 1.0f, 1.0f, 0, 0, 1.0f);//最后一个参数是速率0.5-2.0
                            startX = (int) (iv_test_people.getX()+50*dimensiony);
                            startY = (int) (iv_test_people.getY()+50*dimensiony);
                            Log.e("asdfasdf",""+startX +"||"+startY);
                        }
                        Log.e("asdfasdf","摸到");
                        break;
                    case MotionEvent.ACTION_MOVE:// 移动
                        Log.e("asdfasdf","移动");
                        int newX = (int) event.getX();
                        int newY = (int) event.getY();
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        canvas.drawLine(startX, startY, newX, newY, paint);
                        Log.e("asdfasdf",""+startX +"||"+startY);
                        showRulerLength(Math.abs(newX - startX),Math.abs(newY - startY));
                        iv_03.setImageBitmap(alterBitmap);
                        // 记得重新初始化手指在屏幕上的坐标
//                        startX = (int) event.getX();
//                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:// 离开
                        Log.e("asdfasdf","放手");
                        showRulerLength(0,0);
                        iv_03.setImageBitmap(null);
                        break;
                }
                return true;// false代表的是事件没有处理完毕,等待事件处理完毕, true代表事件已经处理完毕了.
            }
        });

        bt_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        changEvalTextColor();
                        /*开启元素动画*/
                        anim1.start();

                        whtherLoopFlag = true;
                        if (!bt_run.isEnabled()){
                            toast.setText("程序正在运行请稍等");
                            toast.show();
                            return;
                        }
                        bt_run.setEnabled(false);
                        final AnimatorSet set = new AnimatorSet();

                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iswhtherLoopFlag = false;
                                anim1.stop();
                                Intent intent = new Intent(MainActivity.this,DialogActivity.class);
                                intent.putExtra("areyouok",!whtherLoopFlag);
                                startActivityForResult(intent,0);
                            }

//                            @Override
//                            public void onAnimationPause(Animator animation) {
//                                super.onAnimationPause(animation);
//                                iswhtherLoopFlag = false;
//                                Intent intent = new Intent(MainActivity.this,DialogActivity.class);
//                                intent.putExtra("areyouok",!whtherLoopFlag);
//                                startActivityForResult(intent,0);
//                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                iswhtherLoopFlag = true;
                                while (whtherLoopFlag&&iswhtherLoopFlag){//第二个强制退出
                                    SystemClock.sleep(10);
                                    whtherLoopFlag = translate.whetherIntersection(
                                            iv_target.getX(),
                                            iv_target.getX()+mDiySize*dimensiony,
                                            iv_target.getY(),
                                            iv_target.getY()+mDiySize*dimensiony,
                                            iv_test_people.getX(),
                                            iv_test_people.getX()+mDiySize*dimensiony,
                                            iv_test_people.getY(),
                                            iv_test_people.getY()+mDiySize*dimensiony
                                    );
                                }
                                if (!whtherLoopFlag){
                                    Message msg = Message.obtain();
                                    msg.what = 3;
                                    handler.sendMessage(msg);
                                }else if(!iswhtherLoopFlag){
                                    Message msg = Message.obtain();
                                    msg.what = 4;
                                    handler.sendMessage(msg);
                                }
                            }
                        }).start();

                        if (items.size()>0){
                            items.clear();
                        }

                        String objStr = conVertStr_thisobjStr(et_test.getText().toString());
                        evalUtils.StartEval(objStr, new EvalUtils.OnTestListener() {
                            @Override
                            public void okEval() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        set.setDuration(700);
                                        set.playSequentially(items);
                                        set.start();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        et_test.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_test.setText("");
                return true;
            }
        });

        et_test.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                translate.UpDateS_split(et_test.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            switch (resultCode) {
                case RESULT_OK:
                    headDirectionFlag = 1;
                    iv_test_people.setTranslationX(0);
                    iv_test_people.setTranslationY(0);
                    iv_test_people.setX(600f*dimensiony);
                    iv_test_people.setY(800f*dimensiony);
                    iv_test_people.setRotation(0);
                    iv_test_people.invalidate();

                    /*换个地方*/
                    iv_target.setTranslationX((float) (Math.random()*1200*dimensiony));
                    iv_target.setTranslationY((float) (Math.random()*800*dimensiony));
                    iv_target.setScaleX(1f);
                    iv_target.setScaleY(1f);
                    iv_target.invalidate();

                    translationX = 0.0f;
                    translationY = 0.0f;
                    mRotation = 0.0f;
                    bt_run.setEnabled(true);
                    startBG_music();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String conVertStr_thisobjStr(String s) {
        String newStr = "";
        String[] split = s.trim().split(" ");
        for (String str : split){
            newStr = newStr + "thisObject." + str;
        }
        return newStr;
    }

    /*目标撞击动画*/
    private void changeTargetAnimal(final int i){

        Log.e("fawefasd","fawefsdfsd");
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (i == 1){
                    soundPool.play(soundID1, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_target, "rotation", 0f,360f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(iv_target, "scaleX", 1f,0);
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(iv_target, "scaleY", 1f,0);
        if (i == 1){
            set.playTogether(oa1,oa2,oa3);
        }else if (i == 2){
            set.play(oa1);
        }
        set.setDuration(300);
        set.start();
    }

    //显示尺子长度
    private void showRulerLength(final int abs, final int i) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                double sqrt = Math.sqrt(Math.pow(abs, 2) + Math.pow(i, 2));
                int v = (int)sqrt / unitPX;  // 一个单位代表61px
                tv_05.setText("当前尺子长度 :"+v +" m");
            }
        });
    }

    @Override
    protected void initData() {
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                MainActivity.this.positonFlag = index;

            }
        });
        //设置原始数据
        loopView.setItems(list);

        //设置初始位置
        loopView.setInitPosition(10);

        tv_test01.setText(Html.fromHtml("(向上拖)<br/><br/><font color='#FFFF00'>直行→m</font>"));

        //播放背景音乐
        startBG_music();

        /*初始化元素动画*/
        anim1 = (AnimationDrawable) iv_test_people.getBackground();
        anim2 = (AnimationDrawable) iv_target.getBackground();
        anim2.start();
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.e("asdasdadasd",""+iv_test_people.getTranslationX());
                    Log.e("asdasdadasd",""+iv_test_people.getTranslationY());
                    Log.e("asdasdadasd",""+iv_test_people.getX());
                    Log.e("asdasdadasd",""+iv_test_people.getY());
                    Log.e("asdasdadasd", "" + iv_test_people.getRotation());
                    Log.e("asdasdadasd","-----------------------------------------");
                    Log.e("asdasdadasd",""+iv_target.getTranslationX());
                    Log.e("asdasdadasd",""+iv_target.getTranslationY());
                    Log.e("asdasdadasd",""+iv_target.getX());
                    Log.e("asdasdadasd",""+iv_target.getY());
                    Log.e("asdasdadasd", "" + iv_target.getRotation());

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DROP:
                    ClipData clipData = event.getClipData();
                    ClipData.Item item = clipData.getItemAt(0);
                    CharSequence charSequence = translate.convertDragTest(et_test.getText(), item.getText());
                    Log.e("tweafsdf","asdfasdf");
                    et_test.setText(charSequence);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                int selectedItem = loopView.getSelectedItem();
                CharSequence text = tv_test01.getText();
                CharSequence charSequence = translate.convertDragCode(text, selectedItem);
                ClipData data = ClipData.newPlainText("code", charSequence);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        alterBitmap = null;
        canvas = null;
        paint = null;
        super.onDestroy();
    }

    /*image的移动动画代码*/
    //相对图片方向前进
    public void step(final int ruler){

        handler.post(new Runnable() {
            @Override
            public void run() {
                translateStep(ruler);
            }
        });
    }

    //相对图片方向后退
    public void turn(int i){
        if (i != 180){
            i = 10/0;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                headDirectionFlag = headDirectionFlag + 2;
                if (headDirectionFlag > 3){
                    headDirectionFlag = headDirectionFlag - 4;
                }
                ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "rotation", mRotation , mRotation + 180f);
                mRotation = mRotation + 180f;
                oa.setDuration(700);
                items.add(oa);
            }
        });
    }

    //相对图片方向左转
    public void left(int i){
        if (i != -90){
            i = 10/0;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                headDirectionFlag = headDirectionFlag - 1;
                if (headDirectionFlag < 0){
                    headDirectionFlag = 3;
                }
                ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "rotation", mRotation , mRotation - 90f);
                mRotation = mRotation - 90f;
                oa.setDuration(700);
                items.add(oa);
            }
        });
    }

    //相对图片方向右转
    public void right(int i){
        if (i != 90){
            i = 10/0;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                headDirectionFlag = headDirectionFlag + 1;
                if (headDirectionFlag > 3){
                    headDirectionFlag = 0;
                }
                ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "rotation", mRotation , mRotation + 90f);
                mRotation = mRotation + 90f;
                oa.setDuration(700);
                items.add(oa);
            }
        });
    }

    //平移动画
    private void translationImage(final String str,int ruler){
        ValueAnimator valueAnimator = null;

        if (str.equals("X")){
            if (headDirectionFlag == 0){
                valueAnimator = ValueAnimator.ofFloat(translationX, translationX - ruler*unitPX);
                translationX = translationX - ruler*unitPX;
            }else if (headDirectionFlag == 2){
                valueAnimator = ValueAnimator.ofFloat(translationX, translationX + ruler*unitPX);
                translationX = translationX + ruler*unitPX;
            }
        }else if(str.equals("Y")){
            if (headDirectionFlag == 1){
                valueAnimator = ValueAnimator.ofFloat(translationY, translationY - ruler*unitPX);
                translationY = translationY - ruler*unitPX;
            }else if (headDirectionFlag == 3){
                valueAnimator = ValueAnimator.ofFloat(translationY, translationY + ruler*unitPX);
                translationY = translationY + ruler*unitPX;
            }
        }
        valueAnimator.setDuration(700);
        items.add(valueAnimator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                Log.e("asdfasdf","当前方向 :"+headDirectionFlag);
                float delta = (float)animation.getAnimatedValue();
                if (str.equals("X")){
                    iv_test_people.setTranslationX(delta);
                }else if(str.equals("Y")){
                    iv_test_people.setTranslationY(delta);
                }
            }
        });
    }


//    //平移动画
//    private void translationImage(final String str,int ruler){
//
//        if (str.equals("X")){
//            if (headDirectionFlag == 0){
//                ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "translationX", translationX , translationX - ruler*unitPX);
//                translationX = translationX - ruler*unitPX;
//                items.add(oa);
//            }else if (headDirectionFlag == 2){
//                for (int i = 1;i<(ruler+1);i++){
//                    ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "translationX", translationX , translationX + i*unitPX);
//                    translationX = translationX + i*unitPX;
//                    items.add(oa);
//                }
//            }
//        }else if(str.equals("Y")){
//            if (headDirectionFlag == 1){
//                ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "translationY", translationX , translationX - ruler*unitPX);
//                translationY = translationY - ruler*unitPX;
//                items.add(oa);
//            }else if (headDirectionFlag == 3){
//                for (int i = 1;i<(ruler+1);i++){
//                    ObjectAnimator oa = ObjectAnimator.ofFloat(iv_test_people, "translationY", translationX , translationX + i*unitPX);
//                    translationY = translationY + i*unitPX;
//                    items.add(oa);
//                }
//            }
//        }
//
//    }

    /*直行*/
    private void translateStep(int ruler){
        if (headDirectionFlag == 1||headDirectionFlag == 3){//x
            translationImage("Y",ruler);
        }else {
            translationImage("X",ruler);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (iSsurfaceVisite){
            startBG_music();
            anim2.start();
        }
        super.onConfigurationChanged(newConfig);
    }

    private void startBG_music(){
        Log.e("asdfasdfs","startBG_music");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer == null){
                        mediaPlayer = new MediaPlayer();
                    }

                    mediaPlayer.reset();
                    //设置播放的文件
                    AssetFileDescriptor fileDescriptor;
                    int random = (int)(Math.random()*10)%2;
                    if (random == 0){
                        fileDescriptor = getAssets().openFd("bg_source_aaa.mp3");
                    }else {
                        fileDescriptor = getAssets().openFd("bg_source_bbb.mp3");
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

    /*用于显示分步执行的效果*/
    private void changEvalTextColor(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                String[] split = translate.getSplit();
                if (split==null){
                    return;
                }
                for (int i = 0;i<split.length;i++){
                    String str = "";
                    for (int j = 0;j<split.length;j++){
                        if (i == j){
                            str = str + "<font color='#12edf0'>"+split[j]+" "+"</font>";
                        }else{
                            str = str + split[j]+" ";
                        }
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = Html.fromHtml(str);
                    handler.sendMessage(msg);
                    SystemClock.sleep(705);
                }
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = translate.getS();
                handler.sendMessage(msg);
            }
        }).start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        iSsurfaceVisite = false;
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iSsurfaceVisite = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        anim2.stop();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            //释放资源
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }
}
