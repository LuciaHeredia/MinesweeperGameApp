package com.example.minesweeper.Logic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class Board extends GridView{

    public Board(Context context , AttributeSet attrs){
        super(context,attrs);

        Game.getInstance().createBoard(context);
        setAdapter(new BoardAdapter());
    }

    public static class BoardAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Game.getInstance().SIZE * Game.getInstance().SIZE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return Game.getInstance().getBoxAt(position);
        }
    }

}
