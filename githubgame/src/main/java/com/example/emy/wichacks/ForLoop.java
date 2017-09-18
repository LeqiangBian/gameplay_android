package com.example.emy.wichacks;

import java.util.ArrayList;

public class ForLoop extends Move{

    public int iterations;
    public ArrayList<Direction> moves;

    public ForLoop(int iterations, ArrayList<Direction> moves){
        this.iterations = iterations;
        this.moves = moves;
        type = FOR;
    }

    @Override
    public ArrayList<Direction> directions() {
        ArrayList<Direction> toRet = new ArrayList<Direction>();
        for(int i = 0; i < iterations; i++){
            for(Direction d : moves){
                toRet.add(d);
            }
        }
        return toRet;
    }

    @Override
    public int apply(int pos, boolean var) {
        ArrayList<Direction> directions = directions();
        for(Direction d : directions){
            BasicMove bm = new BasicMove(d);
            pos = bm.apply(pos, var);
        }
        return pos;
    }

//    public JLabel toLabel() throws IOException {
//        JLabel toret = new JLabel();
//        toret.setLayout(new GridLayout(1, 1+moves.size()));
//        toret.add(new JLabel("REPEAT x"+Integer.toString(iterations)+":"));
//        for(Direction d : moves){
//            BasicMove bm = new BasicMove(d);
//            toret.add(bm.toLabel());
//        }
//        return toret;
//    }

}
