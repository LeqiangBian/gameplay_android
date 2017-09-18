package com.bl.testgameplay.utils;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.bl.testgameplay.activity.MainActivity;

import bsh.EvalError;
import bsh.Interpreter;

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
 * 作者: 卞乐强 on 2017/7/10 14:30.
 * 邮箱: 921321110@qq.com
 * github: https://github.com/LeqiangBian
 * csdn: http://blog.csdn.net/qq_34365898
 */
public class EvalUtils {

    private final Interpreter in;
    private Thread thread;
    private final Toast test;
    private final Handler handler;
    private int errorFlag = 0;

    public EvalUtils(Context context)  {
        MainActivity con = (MainActivity) context;
        test = Toast.makeText(con, "", Toast.LENGTH_LONG);
        handler = new Handler();
        in = new Interpreter();
        try {
            in.set("thisObject", context);//抛出异常
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
    }

    /*用来开启一个线程执行eval*/
    public void StartEval(final String str, final EvalUtils.OnTestListener l){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    errorFlag = 0;
                    in.eval(str);
                }catch (Exception evalError){
                    evalError.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            errorFlag = 1;
                            test.setText("请检查程序语法");
                            test.show();
                        }
                    });
                }finally {
                    if(errorFlag == 0){
                        l.okEval();
                    }
                }
            }
        });
        thread.start();
    }

    public interface OnTestListener {
        void okEval();
    }
}
