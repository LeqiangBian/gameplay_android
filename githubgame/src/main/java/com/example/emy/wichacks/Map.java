package com.example.emy.wichacks;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Map extends AppCompatActivity {

    private static final boolean X = true;
    private static final boolean Y = false;
    public static final int coordLimit = 4;
    public static final int commandLimit = 5;
    public static final int maxIterations = 4;
    public static final int maxMoves = 2;
    private int xdim, ydim;
    private int startx, starty;
    private int endx, endy;
    private int commands;
    //Direction[] directions;
    Move [] moves;
    private boolean choiceMade;
    boolean correct = false;

    LinearLayout layoutVertical;
    TableLayout table;
    Button[][] buttonArray;

    Animal[][] animals;

    private ArrayList<Direction> dogmoves;
    private ArrayList<Direction> catmoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //initialize start loc, commands, end loc variables
        randomInitialize();

        layoutVertical = (LinearLayout) findViewById(R.id.linearlayout_map);
        buttonArray = new Button[xdim][ydim];
        table = new TableLayout(this);
        for (int row = 0; row < xdim; row++) {
            TableRow currentRow = new TableRow(this);
            for (int button = 0; button < ydim; button++) {
                final Button currentButton = new Button(this);
                int randpic = animal_to_image(animals[xdim-row-1][button]);
                currentButton.setBackgroundResource(randpic);
                currentButton.setWidth(40);
                currentButton.setHeight(50);
                if(row==xdim-startx - 1 && button==starty) {
                    currentButton.setText("START");
                    currentButton.setTextColor(Color.BLUE);
                    currentButton.setTextSize(13);
                }
                if(row != xdim-endx-1 || button != endy){
                    currentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!choiceMade) {
                                choiceMade = true;
                                currentButton.setBackgroundColor(Color.RED);
                                disableAllButtons(buttonArray);
                            }
                        }
                    });
                }else{
                    currentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!choiceMade) {
                                choiceMade = true;
                                currentButton.setBackgroundColor(Color.GREEN);
                                correct = true;
                                disableAllButtons(buttonArray);
                            }
                        }
                    });
                }

                // you can store them
                buttonArray[row][button] = currentButton;
                // and you have to add them to the TableRow
                currentRow.addView(currentButton);
            }
            // a new row has been constructed -> add to table
            table.addView(currentRow);
        }
        // and finally takes that new table and add it to your layout.
        layoutVertical.addView(table);

        Button button_new = (Button)findViewById(R.id.button_new);
        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Map.class);
                startActivity(intent);
            }
        });
    } // onCreate

    private void randomImage() {
        Random r = new Random();
        ImageView choice;

    } //randomImage

    public void disableAllButtons(Button[][] buttons) {
        for(int i = 0; i < buttons.length; i++){
            for(int j = 0; j < buttons[i].length; j++){
                buttons[i][j].setEnabled(false);
            }
        }
    } //disableAllButtons

    private void randomInitialize() {
        xdim = randDim(coordLimit);
        ydim = randDim(coordLimit);
        initializeAnimals();
        startx = randCoord(xdim);
        starty = randCoord(ydim);
        commands = randDim(commandLimit);
        //directions = randomDirections(commands);
        moves = randomMoves(commands);
        initializeEnd();
    } //randomInit

    private Move [] randomMoves(int number){
        Move [] toRet = new Move[number];
        int currentx = startx; int currenty = starty;
        for(int i = 0; i < number; i++){
            Random r = new Random();
            int choice = r.nextInt(3);
            if(choice == 0){
                toRet[i] = randomBasicMove(currentx, currenty);
                Log.i("TAG","basic");
            }else if(choice == 1){
                toRet[i] = randomForLoopMove(currentx, currenty);
                Log.i("TAG","for");
            }else{
                toRet[i] = randomIfStatement(currentx, currenty);
                Log.i("TAG","if");
            }
            currentx = toRet[i].apply(currentx, X);
            currenty = toRet[i].apply(currenty, Y);
        }
        return toRet;
    }

    private IfStatement randomIfStatement(int currentx, int currenty){
        Random r = new Random();
        Animal condition = animals[currentx][currenty];

        int dogx = currentx; int dogy = currenty;
        int num_dog_moves = 1 + r.nextInt(maxMoves);
        ArrayList<Direction> dogMoves = new ArrayList<Direction>();
        for(int i = 0; i < num_dog_moves; i++){
            Direction d = randMove(dogx, dogy);
            dogMoves.add(d);
            dogx = (new BasicMove(d)).apply(dogx, X);
            dogy = (new BasicMove(d)).apply(dogy, Y);
        }

        int catx = currentx; int caty = currenty;
        int num_cat_moves = 1 + r.nextInt(maxMoves);
        ArrayList<Direction> catMoves = new ArrayList<Direction>();
        for(int i = 0; i < num_cat_moves; i++){
            Direction d = randMove(catx, caty);
            catMoves.add(d);
            catx = (new BasicMove(d)).apply(catx, X);
            caty = (new BasicMove(d)).apply(caty, Y);
        }

        return new IfStatement(condition, dogMoves, catMoves);
    }

    private ForLoop randomForLoopMove(int currentx, int currenty){
        Random r = new Random();
        int num_moves = 1 + r.nextInt(maxMoves);
        int iterations = 1;
        ArrayList<Direction> moves = new ArrayList<Direction>();
        for(int i = 0; i < num_moves; i++){
            Direction d = randMove(currentx, currenty);
            moves.add(d);
            currentx = (new BasicMove(d)).apply(currentx, X);
            currenty = (new BasicMove(d)).apply(currenty, Y);
        }
        boolean stillGoing = true;
        for(int i = 1; i < maxIterations && stillGoing; i++){
            for(Direction d : moves){
                if(legal(currentx, currenty, d)){
                    currentx = (new BasicMove(d)).apply(currentx, X);
                    currenty = (new BasicMove(d)).apply(currenty, Y);
                }else{
                    stillGoing = false;
                    break;
                }
            }
            if(stillGoing){
                iterations++;
            }
        }
        return new ForLoop(iterations, moves);
    }

    private BasicMove randomBasicMove(int currentx, int currenty){
        return new BasicMove(randMove(currentx, currenty));
    }

    private void initializeAnimals(){
        animals = new Animal[xdim][ydim];
        for(int i = 0; i < animals.length; i++){
            for(int j = 0; j < animals[i].length; j++){
                animals[i][j] = randomAnimal();
            }
        }
    }

    private Animal randomAnimal(){
        Random r = new Random();
        Animal [] values = Animal.values();
        int choice = r.nextInt(values.length);
        return values[choice];
    }

    private int animal_to_image(Animal a) {
        switch(a){
            case DOG:
                return R.drawable.dog;
            case CAT:
                return R.drawable.octocat;
            default:
                return 0;
        }
    }

    private Direction[] randomDirections(int number){
        Direction[] toRet = new Direction[number];
        int currentx = startx; int currenty = starty;
        for(int i = 0; i < number; i++){
            toRet[i] = randMove(currentx, currenty);
            currentx = apply(currentx, toRet[i], X);
            currenty = apply(currenty, toRet[i], Y);
        }
        return toRet;
    } //randomDirections

    private void initializeEnd(){
        int x = startx;
        int y = starty;
        for(Move m : moves){
            for(Direction d : m.directions()){
                switch(d){
                    case UP:
                        x+=1;
                        break;
                    case DOWN:
                        x-=1;
                        break;
                    case LEFT:
                        y-=1;
                        break;
                    case RIGHT:
                        y+=1;
                        break;
                }
            }
        }
        endx = x;
        endy = y;

        LinearLayout command_layout = (LinearLayout)findViewById(R.id.linearlayout_commands);
        command_layout.setOrientation(LinearLayout.VERTICAL);
        ArrayList<LinearLayout> command_ll_list = new ArrayList<LinearLayout>();
        for(Move m : moves) {
            switch(m.type){
                case Move.BASIC:
                    Log.i("TAG","BASIC");
                    LinearLayout llb = new LinearLayout(command_layout.getContext());
                    llb.setOrientation(LinearLayout.HORIZONTAL);
                    for (Direction d : m.directions()) {
                        ImageView imageview = new ImageView(this);
                        if (d.equals(Direction.UP)) {
                            imageview.setImageResource(R.drawable.up);
                        } else if (d.equals(Direction.DOWN)) {
                            imageview.setImageResource(R.drawable.down);
                        } else if (d.equals(Direction.LEFT)) {
                            imageview.setImageResource(R.drawable.left);
                        } else if (d.equals(Direction.RIGHT)) {
                            imageview.setImageResource(R.drawable.right);
                        }
                        llb.addView(imageview);
                    }
                    command_ll_list.add(llb);
                    break;
                case Move.IF:
                    Log.i("TAG","IF");
                    IfStatement i = (IfStatement) m;
                    //make container holding multiple imageviews
                    LinearLayout ll = new LinearLayout(command_layout.getContext());
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv1 = new TextView(ll.getContext());
                    tv1.setText("IF");
                    ll.addView(tv1);
                    ImageView iv = new ImageView(this);
                    iv.setImageResource(R.drawable.dog);
                    ll.addView(iv);
                    Log.i("TAG", "dog" + R.drawable.dog);
                    for (Direction d : i.dogmoves) {
                        ImageView imageview = new ImageView(this);
                        if (d.equals(Direction.UP)) {
                            imageview.setImageResource(R.drawable.up);
                            Log.i("TAG", "up" + R.drawable.up);
                        } else if (d.equals(Direction.DOWN)) {
                            imageview.setImageResource(R.drawable.down);
                            Log.i("TAG", "down" + R.drawable.down);
                        } else if (d.equals(Direction.LEFT)) {
                            imageview.setImageResource(R.drawable.left);
                            Log.i("TAG", "left" + R.drawable.left);
                        } else if (d.equals(Direction.RIGHT)) {
                            imageview.setImageResource(R.drawable.right);
                            Log.i("TAG", "right" + R.drawable.right);
                        }
                        ll.addView(imageview);
                    }
                    TextView tve = new TextView(ll.getContext());
                    tve.setText("  ELSEIF");
                    ll.addView(tve);
                    ImageView ivc = new ImageView(ll.getContext());
                    ivc.setImageResource(R.drawable.octocat);
                    ll.addView(ivc);
                    Log.i("TAG", "cat" + R.drawable.octocat);
                    for (Direction d : i.catmoves) {
                        ImageView imageview = new ImageView(this);
                        if (d.equals(Direction.UP)) {
                            imageview.setImageResource(R.drawable.up);
                            Log.i("TAG", "up" + R.drawable.up);
                        } else if (d.equals(Direction.DOWN)) {
                            imageview.setImageResource(R.drawable.down);
                            Log.i("TAG", "down" + R.drawable.down);
                        } else if (d.equals(Direction.LEFT)) {
                            imageview.setImageResource(R.drawable.left);
                            Log.i("TAG", "left" + R.drawable.left);
                        } else if (d.equals(Direction.RIGHT)) {
                            imageview.setImageResource(R.drawable.right);
                            Log.i("TAG", "right" + R.drawable.right);
                        }
                        ll.addView(imageview);
                    }
                    command_ll_list.add(ll);
                    break;
                case Move.FOR:
                    Log.i("TAG", "FOR");
                    LinearLayout llf = new LinearLayout(command_layout.getContext());
                    llf.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv = new TextView(llf.getContext());
                    tv.setText("Do " + Integer.toString(((ForLoop) m).iterations)+ " times: ");
                    llf.addView(tv);
                    for (Direction d : ((ForLoop)m).moves) {
                        ImageView imageview = new ImageView(this);
                        if (d.equals(Direction.UP)) {
                            imageview.setImageResource(R.drawable.up);
                        } else if (d.equals(Direction.DOWN)) {
                            imageview.setImageResource(R.drawable.down);
                        } else if (d.equals(Direction.LEFT)) {
                            imageview.setImageResource(R.drawable.left);
                        } else if (d.equals(Direction.RIGHT)) {
                            imageview.setImageResource(R.drawable.right);
                        }
                        llf.addView(imageview);
                    }
                    command_ll_list.add(llf);
                    break;
            }

        }
        for(LinearLayout ll : command_ll_list) {
            if((ll.getParent()) != null) {
                ((ViewGroup)ll.getParent()).removeView(ll);
            }
            command_layout.addView(ll);
        }
    } //initializeEnd

    private Direction randMove(int x, int y){
        ArrayList<Direction> options = new ArrayList<Direction>();
        for(Direction d : Direction.values()){
            if(legal(x, y, d)){
                options.add(d);
            }
        }
        Random r = new Random();
        int choice = r.nextInt(options.size());
        Direction toret = options.get(choice);
        return toret;
    }

    private boolean legal(int x, int y, Direction d){
        switch(d){
            case UP:
                return x<xdim-1; //OG: x<xdim-1
            case DOWN:
                return x>0;
            case LEFT:
                return y>0;
            case RIGHT:
                return y<ydim-1; //OG: y<ydim-1
            default:
                return false;
        }
    } //legal

    private int apply(int pos, Direction d, boolean coord){
        if(coord == Y){
            switch(d){
                case LEFT:
                    return pos-1; //OG: pos-1
                case RIGHT:
                    return pos+1; //OG: pos+1
                default:
                    return pos;
            }
        }else{
            switch(d){
                case UP:
                    return pos+1; //OG: pos+1
                case DOWN:
                    return pos-1; //OG: pos-1
                default:
                    return pos;
            }
        }
    } //apply

    private int randDim(int limit){
        return 2 + randCoord(limit-1); // 2 <= dim <= limit
    }

    private int randCoord(int limit){
        Random r = new Random();
        return r.nextInt(limit); // 0 <= coord < limit
    }

    private void reset() {
        xdim = 0;
        ydim = 0;
        startx = 0;
        starty = 0;
        endx = 0;
        endy = 0;
        commands = 0;

//        for(int i = 0; i < directions.length; i++) {
//            directions[i] = null;
//        }
        for(int i = 0; i < buttonArray.length; i++) {
            buttonArray[i] = null;
        }

        choiceMade = false;
        correct = false;

        layoutVertical.removeAllViews();
        table.removeAllViews();
    } //reset

} //Map
