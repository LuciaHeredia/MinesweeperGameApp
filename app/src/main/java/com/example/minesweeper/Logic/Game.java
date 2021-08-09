package com.example.minesweeper.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minesweeper.EndScreen;
import com.example.minesweeper.R;

public class Game {

    private static final String TAG = "Game";
    int SIZE;
    public int NUMBER_BOMBS;
    private TextView bombs_flags;
    public  TextView timer_value;
    @SuppressLint("StaticFieldLeak")
    private static Game instance;
    private Context context;
    private Box[][] MinesweeperBoard;
    private int[][] GenerateBoard;
    boolean isFirstTime;
    Intent end_of_game;
    Toast toast;
    public int moves;
    boolean boxClickedCountMove;

    public static Game getInstance() {
        if(instance == null)
            instance = new Game();
        return instance;
    }

    private Game()  {
        Log.i("my_tag", "NEW GAME");
    }

    public void settingBoard(Context context, int size, int number_bombs, TextView bf, TextView timer_value) {
        this.bombs_flags = bf;
        this.SIZE = size;
        this.NUMBER_BOMBS = number_bombs;
        this.timer_value = timer_value;
        this.isFirstTime = true;
        moves = 0;
        boxClickedCountMove = true;
        MinesweeperBoard = new Box[SIZE][SIZE];
        createBoard(context);
    }

    void createBoard(Context context) {
        this.context = context;
        GenerateBoard = Generator.generate(NUMBER_BOMBS,SIZE,SIZE);
        setGrid(context,GenerateBoard);
    }

    private void setGrid( final Context context, final int[][] grid ){
        for( int x = 0 ; x < SIZE ; x++ ){
            for( int y = 0 ; y < SIZE ; y++ ){
                if( MinesweeperBoard[x][y] == null ){
                    MinesweeperBoard[x][y] = new Box( context , x,y, timer_value);
                }
                MinesweeperBoard[x][y].setValue(grid[x][y]);
                MinesweeperBoard[x][y].invalidate();
            }
        }
    }

    Box getBoxAt(int position) {
        int x = position % SIZE;
        int y = position / SIZE;

        return MinesweeperBoard[x][y];
    }

    private Box getBoxAt(int x, int y){
        return MinesweeperBoard[x][y];
    }

    void click(int x, int y, boolean boxClickedCountMove){

        if( x >= 0 && y >= 0 && x < SIZE && y < SIZE && !getBoxAt(x,y).isClicked() && !getBoxAt(x,y).isFlagged()) { // if box not clicked yet
            getBoxAt(x, y).setClicked(); // set now as clicked

            if(boxClickedCountMove) { // count the box clicked
                moves++;
                Log.i("On Click", "move No. " + moves);
            }

            if (getBoxAt(x, y).getValue() == 0) { // if no bomb

                for (int xt = -1; xt <= 1; xt++) {
                    for (int yt = -1; yt <= 1; yt++) {
                        if (xt != yt) {
                            click(x + xt, y + yt, false);
                        }
                    }
                }
            }

            if (getBoxAt(x, y).isBomb())  // if box clicked is a mine/bomb
                onGameLost(); // game lost
        }

        checkAllBoxes(); // check all boxes
    }

    private boolean checkAllBoxes(){
        int bombNotFound = NUMBER_BOMBS;
        int notRevealed = SIZE * SIZE; // board size
        for ( int x = 0 ; x < SIZE ; x++ ){
            for( int y = 0 ; y < SIZE ; y++ ){

                if( getBoxAt(x,y).isRevealed() || getBoxAt(x,y).isFlagged() ) { // box revealed or box flagged
                    notRevealed--;
                }

                if( getBoxAt(x,y).isFlagged() && getBoxAt(x,y).isBomb() ) // box is flagged and is a bomb
                    bombNotFound--;
            }
        }

        // if number of boxes no revealed equals the number of bombs left to find
        if((notRevealed == bombNotFound)){
            change_bombs_flags(bombs_flags, 0); // nullify
            show_state("You Won!");
        }
        return false;
    }

    private void change_bombs_flags(TextView mTextView, int bombs_flags) { // change number bombs/flags showed
        mTextView.findViewById(R.id.count);
        mTextView.setText(String.valueOf(bombs_flags));
    }

    void flag(int x, int y){
        boolean isFlagged = getBoxAt(x,y).isFlagged();
        boolean isRevealed = getBoxAt(x,y).isRevealed();
        if (!isRevealed && isFlagged && Integer.parseInt((String) bombs_flags.getText()) != NUMBER_BOMBS) {
            moves++;
            Log.i("in longClick","move No. " + moves);

            change_bombs_flags(bombs_flags, Integer.parseInt((String) bombs_flags.getText()) + 1);
            getBoxAt(x, y).setFlagged(!isFlagged);
            getBoxAt(x, y).invalidate();
        }

        if (!isRevealed && !isFlagged && Integer.parseInt((String) bombs_flags.getText()) != 0) {
            moves++;
            Log.i("On longClick","move No. " + moves);

            change_bombs_flags(bombs_flags, Integer.parseInt((String) bombs_flags.getText()) - 1);
            getBoxAt(x, y).setFlagged(!isFlagged);
            getBoxAt(x, y).invalidate();
        }
    }


    private void onGameLost() { // handle lost game
        for ( int x = 0 ; x < SIZE ; x++ ) {
            for (int y = 0; y < SIZE; y++) {

                // if box flagged and it is not a bomb
                if (getBoxAt(x, y).isFlagged() && !getBoxAt(x, y).isBomb())
                    getBoxAt(x, y).setFalseFlagged(true);

                getBoxAt(x,y).setRevealed();
            }
        }
        show_state("You Lost!");
    }

    private void show_state(String str) { // WIN/LOST
        this.end_of_game = new Intent(this.context, EndScreen.class);
        end_of_game.putExtra("situation_at_end",str+","+ moves);
        context.startActivity(end_of_game);
    }

    public void sensorAddBomb(){ // LOCKED: Every 8sec - a bomb gets added to the board.
        int bombNotFound = NUMBER_BOMBS;
        int notRevealed = SIZE * SIZE; // board size
        Box boxToCover = returnBoxRevealed(false);

        if(bombNotFound==notRevealed){
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            change_bombs_flags(bombs_flags, 0); // nullify
            onGameLost();
        }else{
            Toast.makeText(context, "FINE: A Bomb was added.", Toast.LENGTH_SHORT).show();
            Log.i("FAIN:", "add bomb");
            boxToCover.setValue(-1);
            NUMBER_BOMBS++;
            change_bombs_flags(bombs_flags, Integer.parseInt((String) bombs_flags.getText()) + 1);
        }
    }

    public void sensorAddBox(){ // LOCKED: Every 4sec - a box gets covered.
        Box boxToCover = returnBoxRevealed(true);

        if(boxToCover!=null){
            Toast.makeText(context, "FINE: A Box was covered.", Toast.LENGTH_SHORT).show();
            Log.i("FAIN:", "cover box");
            boxToCover.setUnRevealed();
        }
    }

    public Box returnBoxRevealed(boolean s){

        for ( int x = 0 ; x < SIZE ; x++ ){
            for( int y = 0 ; y < SIZE ; y++ ){
                if( getBoxAt(x,y).isRevealed() && s) { // box revealed
                    return  getBoxAt(x,y);
                }

                else if(!getBoxAt(x,y).isRevealed() && !getBoxAt(x,y).isBomb() && !s){ //box not revealed add bomb
                    return  getBoxAt(x,y);
                }
            }
        }
        return null;
    }

}
