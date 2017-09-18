package com.example.emy.wichacks;

import java.util.ArrayList;

public class BasicMove extends Move {

    boolean Y = false;

    private ArrayList<Direction> directions;

    public BasicMove(Direction d){
        this.directions = new ArrayList<Direction>();
        this.directions.add(d);
        type = BASIC;
    }

    public int apply(int pos, boolean var) {
        // TODO Auto-generated method stub
        if(var == Y){
            switch(directions.get(0)){
                case LEFT:
                    return pos-1;
                case RIGHT:
                    return pos+1;
                default:
                    return pos;
            }
        }else{
            switch(directions.get(0)){
                case UP:
                    return pos+1;
                case DOWN:
                    return pos-1;
                default:
                    return pos;
            }
        }
    }
//
//    @Override
//    public JLabel toLabel() throws IOException {
//        // TODO Auto-generated method stub
//        Image img;
//        switch(directions.get(0)){
//            case UP:
//                img = ImageIO.read(new File("UP.jpg"));
//                break;
//            case DOWN:
//                img = ImageIO.read(new File("DOWN.jpg"));
//                break;
//            case LEFT:
//                img = ImageIO.read(new File("LEFT.jpg"));
//                break;
//            case RIGHT:
//                img = ImageIO.read(new File("RIGHT.jpg"));
//                break;
//            default:
//                img = null;
//        }
//        JLabel toret = new JLabel();
//        toret.setIcon(new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
//
//        return toret;
//    }

    public ArrayList<Direction> directions() {
        // TODO Auto-generated method stub
        return directions;
    }

}
