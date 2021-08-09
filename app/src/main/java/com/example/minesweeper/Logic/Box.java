package com.example.minesweeper.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.minesweeper.R;

@SuppressLint("ViewConstructor")
public class Box  extends BaseBox implements View.OnTouchListener {

    Canvas canvas;
    long start;
    long end;
    Timer timer = new Timer(Game.getInstance().timer_value);

    public Box (Context context, int x, int y, TextView timer_value) {
        super(context);
        setPosition(x,y);
        setOnTouchListener(this);
  }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { // placing flag

       switch ( event.getActionMasked() ) {
            case MotionEvent.ACTION_DOWN: // mouse click down
                this.start = 0L;
                this.start = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
               break;

            case MotionEvent.ACTION_UP: // mouse click up
                end = System.currentTimeMillis() - start;
                if(end > 400) { // if long click
                    stateButton(v,true);
                    end = 0;
                }
                else { // if click
                    stateButton(v,false);
                    end = 0;
                }
                break;

            default: // for fixing problem
                long end_tmp = System.currentTimeMillis() - start;
                if(end_tmp > 400)  // if long click
                    stateButton(v,true);
                else  // if click
                    stateButton(v,false);
                break;
        }

        if(Game.getInstance().isFirstTime) // if it is the first touch
            isFisrt();

        return true;
    }

    public void stateButton(View v, boolean state) {
        if(state)
            onLongClick(v);
        else
            onClick(v);
    }

    public void isFisrt() { // board first click/long click --> start timer
        timer.onStart();
        Game.getInstance().isFirstTime = false;
    }

    public void onClick(View v) { // placing box
        Game.getInstance().click( getXPos(), getYPos(), true);
    }

    public boolean onLongClick(View v) { // placing flag
        Game.getInstance().flag( getXPos(), getYPos() );
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        super.onDraw(canvas);

        drawButton(canvas);

        if(isFlagged()) {
            drawFlag(canvas);
            if (isFalseFlagged() && isRevealed())
                drawFlagWrong(canvas);
        }else if( isRevealed() && isBomb() && !isClicked() )
            drawNormalBomb(canvas);
        else {
            if( isClicked() ){
                if( getValue() == -1 )
                    drawBombExploded(canvas);
                else
                    drawNumber(canvas);
            }else
                drawButton(canvas);
        }
    }

    private void drawBombExploded(Canvas canvas ){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bomb_red);
        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

    private void drawFlag(Canvas canvas ){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.flag);
        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

    private void drawFlagWrong(Canvas canvas ){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.flag_x);
        assert drawable != null;
        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

    private void drawButton(Canvas canvas ){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.button);
        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

    private void drawNormalBomb(Canvas canvas ){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bomb_grey);
        assert drawable != null;
        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

    private void drawNumber( Canvas canvas ){
        Drawable drawable = null;

        switch (getValue() ){
            case 0:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_pressed);
                break;
            case 1:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.number_1);
                break;
            case 2:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.number_2);
                break;
            case 3:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.number_3);
                break;
            case 4:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.number_4);
                break;
            case 5:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.number_5);
                break;
        }

        assert drawable != null;
        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

}
