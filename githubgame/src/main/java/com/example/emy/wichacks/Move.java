package com.example.emy.wichacks;

import java.util.ArrayList;



public abstract class Move {

    public static final int BASIC = -1;
    public static final int FOR = 0;
    public static final int IF = 1;

    int type;

    public abstract ArrayList<Direction> directions();

    public abstract int apply(int pos, boolean var);

    //pos is a number, var is X or Y
//    public abstract JLabel toLabel() throws IOException;

}

