package com.example.minesweeper;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.minesweeper.Fragments.InsertName;

public class EndScreen extends AppCompatActivity {

    AnimationDrawable rocketAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        Bundle extras = getIntent().getExtras();
        // extras is never null because as default "EASY" level is selected
        assert extras != null;
        String[] strs = extras.getString("situation_at_end").split(",");
        String situation = strs[0]; // "You Won/Lost!"
        String moves = strs[1]; // moves
        show_text(situation);

        ImageView endImage = findViewById(R.id.imageMove); // placing animation
        if(situation.equals("You Won!")) {
            endImage.setBackgroundResource(R.drawable.win); // animation

            Bundle args = new Bundle();
            args.putString("value",moves);

            InsertName fragment = new InsertName(); // fragment
            fragment.putArguments(args); // sending number of moves to fragment method
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_save, fragment).addToBackStack(null);
            transaction.commit();

        }
        else
            endImage.setBackgroundResource(R.drawable.lost); // animation

        rocketAnimation = (AnimationDrawable) endImage.getBackground();
    }

    public void onBackPressed(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        rocketAnimation.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rocketAnimation=null; // After activity close's->animation will close
    }

    public void show_text(String str) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(str); //set text for text view
    }

    public void playAgain(View view) {
        Intent newGameAgain = new Intent(EndScreen.this, MainScreen.class);
        startActivity(newGameAgain);
    }

    public void exitApp(View view) {
        finishAffinity();
        }
    }
