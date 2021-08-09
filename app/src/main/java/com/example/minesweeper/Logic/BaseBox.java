package com.example.minesweeper.Logic;

import android.content.Context;
import android.view.View;

public abstract class BaseBox extends View {

    private int value;
    private boolean isBomb;
    private boolean isRevealed;
    private boolean isClicked;
    private boolean isFlagged;
    private boolean isFalseFlagged;
    private int x, y;
    private int position;

    public BaseBox(Context context) {
        super(context);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        isBomb = false;
        isRevealed = false;
        isClicked = false;
        isFlagged = false;
        isFalseFlagged = false;

        if( value == -1 ){
            isBomb = true;
        }

        this.value = value;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed() {
        isRevealed = true;
        invalidate();
    }

    public void setUnRevealed() {
        isRevealed = false;
        isClicked = false;
        invalidate();
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked() {
        this.isClicked = true;
        this.isRevealed = true;

        invalidate();
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isFalseFlagged() {
        return isFalseFlagged;
    }

    public void setFalseFlagged(boolean falseFlagged) {
        this.isFalseFlagged = falseFlagged;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition( int x , int y){
        this.x = x;
        this.y = y;
        this.position = y * Game.getInstance().SIZE + x;
        invalidate();
    }

}
