package com.bl.testgameplay.game;

import com.bl.testgameplay.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * 作者: 卞乐强 on 2017/7/19 11:08.
 * 邮箱: 921321110@qq.com
 * github: https://github.com/LeqiangBian
 * csdn: http://blog.csdn.net/qq_34365898
 */
/*关卡类*/
public class Checkpoint {

    /*障碍物集合*/
    private List targetList;
    /*目标集合*/
    private List orderList;

    public Checkpoint(float dimens){
        targetList = new LinkedList();
        orderList = new LinkedList();
        alizetionChekPoint(dimens);
    }

    /*初始化关卡*/
    private void alizetionChekPoint(float dimens){
        //这个是主角的坐标
        float[] floatsZ = {600*dimens,700*dimens,800*dimens,900*dimens};
        //各个关卡obstruction坐标
        float[] floatsOB1_0 = {0*dimens,100*dimens,0*dimens,100*dimens};
        float[] floatsOB2_0 = {600*dimens,700*dimens,200*dimens,300*dimens};
        float[] floatsOB3_0 = {100*dimens,200*dimens,500*dimens,600*dimens};
        float[] floatsOB3_1 = {1100*dimens,1200*dimens,500*dimens,600*dimens};
        //各个关卡target坐标
        float[] floatsT1_0 = {0*dimens,500*dimens,500*dimens,400*dimens};
        float[] floatsT2_0 = {300*dimens,1000*dimens,300*dimens,600*dimens};
        float[] floatsT3_0 = {600*dimens,800*dimens,600*dimens,800*dimens};
        float[] floatsT3_1 = {800*dimens,1000*dimens,400*dimens,900*dimens};
    }

    /*检测是否是当前关卡的目标上,用于触摸事件上做判断*/
    public float[] backAfloatarrays(int whatCheckpoint){
        float[] floats = {};

        return floats;
    }
}
