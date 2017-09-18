package com.bl.testgameplay.utils;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.bl.testgameplay.game.Checkpoint;

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
 * 作者: 卞乐强 on 2017/7/11 13:47.
 * 邮箱: 921321110@qq.com
 * github: https://github.com/LeqiangBian
 * csdn: http://blog.csdn.net/qq_34365898
 */
public class TranslatePeople {

    private String[] split;
    private String s;
    private Checkpoint checkpoint;

    public TranslatePeople(){

    }

    public TranslatePeople(float dimens){
        checkpoint = new Checkpoint(dimens);
    }

    /*把拖拽的代码翻译成我想显示的符号*/
    public CharSequence convertDragCode(CharSequence text, int selectedItem) {
        Spanned spanned = Html.fromHtml(conVertZ_Y(text.toString()) + "(" +selectedItem + ");" + " ");
        return spanned;
    }

    public CharSequence convertDragTest(CharSequence text1, CharSequence text2) {
        s = text1.toString() + text2.toString();
        Log.e("asdasdsad","s ::"+s);
        split = s.split(" ");
        Spanned spanned = Html.fromHtml(s);
        return spanned;
    }

    public void UpDateS_split(String str){
        this.s = str;
        split = s.split(" ");
    }

    /*上下左右转英文*/
    private String conVertZ_Y(String s){
        if (s.contains("后")){
            s = "turn(180); step";
        }else if (s.contains("左")){
            s = "left(-90); step";
        }else if (s.contains("右")){
            s = "right(90); step";
        }else{
            s = "step";
        }
        return s;
    }

    public String[] getSplit() {
        return split;
    }

    public String getS() {
        return s;
    }

    /*用于判断8个参数的四个集合是否有交集*/
    public boolean whetherIntersection(float x1,float x2,float y1,float y2,float x3,float x4,float y3,float y4){
        boolean b1 = x1 < x3 && x3 < x2;
        boolean b2 = x1 < x4 && x4 < x2;
        boolean b3 = y1 < y3 && y3 < y2;
        boolean b4 = y1 < y4 && y4 < y2;
        boolean b = (b1 || b2) && (b3 || b4);

        /*特殊情况正对着*/
        if (!b){
            b1 = x1 <= x3 && x3 <= x2;
            b2 = x1 <= x4 && x4 <= x2;
            b = (b1 || b2) && (b3 || b4);
            if (!b){
                b3 = y1 <= y3 && y3 <= y2;
                b4 = y1 <= y4 && y4 <= y2;
                b = (b1 || b2) && (b3 || b4);
            }
        }
        if (b){
            Log.e("afesdfasdfsadfs",
                            "x1:"+x1+
                            "x2:"+x2+
                            "y1:"+y1+
                            "y2:"+y2+
                            "x3:"+x3+
                            "x4:"+x4+
                            "y3:"+y3+
                            "y4:"+y4

            );
        }
        return !b;
    }

    /*检查手指是不是在这个区域*/
    public boolean whetherInRange(float x1,float y1,float x2,float x3,float y2,float y3){
        boolean b1 = x2 < x1 && x1 < x3;
        boolean b2 = y2 < y1 && y1 < y3;
        return  b1&&b2;
    }
}
