package com.example.emy.wichacks;


import java.util.ArrayList;

public class IfStatement extends Move{

    Animal condition;
    public ArrayList<Direction> dogmoves;
    public ArrayList<Direction> catmoves;

    public IfStatement(Animal condition, ArrayList<Direction> dogmoves, ArrayList<Direction> catmoves){
        this.condition = condition;
        this.dogmoves = dogmoves;
        this.catmoves = catmoves;
        type = IF;
    }

    @Override
    public ArrayList<Direction> directions() {
        switch(condition){
            case DOG:
                return dogmoves;
            case CAT:
                return catmoves;
            default:
                return new ArrayList<Direction>();
        }
    }

    @Override
    public int apply(int pos, boolean var) {
        ArrayList<Direction> moves = directions();
        for(Direction d : moves){
            BasicMove bm = new BasicMove(d);
            pos = bm.apply(pos, var);
        }
        return pos;
    }

//    private ImageIcon animal_to_image(Animal a) throws IOException{
//        Image choice;
//        switch(a){
//            case DOG:
//                choice = ImageIO.read(getClass().getResource("dog.png"));
//                break;
//            case CAT:
//                choice = ImageIO.read(getClass().getResource("cat.png"));
//                break;
//            default:
//                choice = null;
//        }
//        Image scaled = choice.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
//        return new ImageIcon(scaled);
//    }

//    public JLabel toLabel() throws IOException {
//        JLabel toret = new JLabel();
//        toret.setLayout(new GridLayout(0, dogmoves.size() + catmoves.size() + 2*2));
//
//        toret.add(new JLabel("IF"));
//
//        JLabel dog = new JLabel();
//        dog.setIcon(animal_to_image(Animal.DOG));
//        toret.add(dog);
//
//        for(Direction d : dogmoves){
//            toret.add((new BasicMove(d)).toLabel());
//        }
//
//        toret.add(new JLabel("ELSE IF"));
//
//        JLabel cat = new JLabel();
//        cat.setIcon(animal_to_image(Animal.CAT));
//        toret.add(cat);
//
//        for(Direction d : catmoves){
//            toret.add((new BasicMove(d)).toLabel());
//        }
//        return toret;
//    }

}
