package com.example.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.minesweeper.Fragments.RecordTable;

/**
 * Created by: lucia heredia inga
 * runs on: Nexus 5X
 */

public class MainScreen extends AppCompatActivity {
    private RadioGroup radioLevelsGroup;
    private RadioButton radioLevelButton;
    private Button startGame;
    private int savedRadioIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.start);
        Button buttonFrag = findViewById(R.id.frag);

        buttonFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame.setEnabled(false);

                RecordTable fragment = new RecordTable();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                transaction.commit();
            }
         });


        // getting saved level from previous game
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        boolean answer1 = sharedPreferences.getBoolean("EASY", false);
        boolean answer2 = sharedPreferences.getBoolean("MEDIUM", false);
        boolean answer3 = sharedPreferences.getBoolean("HARD", false);
        if (answer2)
            radioLevelButton = findViewById(R.id.radioMedium);
        else if (answer3)
            radioLevelButton = findViewById(R.id.radioHard);
        else{ // EASY ALSO AS DEFAULT
            radioLevelButton = findViewById(R.id.radioEasy);
        }
        radioLevelButton.setChecked(true); // set level checked(from before)

        addListenerOnButton();

    }

    public void onBackPressed(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            startGame.setEnabled(true);
        } else {
            super.onBackPressed();
        }
    }

    private void addListenerOnButton() {
        Button btnStartGame = findViewById(R.id.start);
        radioLevelsGroup = findViewById(R.id.radioLevels);

        btnStartGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // for new selection of level: get selected radio button from radioGroup
                int selectedId = radioLevelsGroup.getCheckedRadioButtonId();
                radioLevelButton = findViewById(selectedId); // find radiobutton by id
                SavePreferences(); // saving level for next game

                Intent intent = new Intent(MainScreen.this, GameScreen.class);
                intent.putExtra("level_selected",radioLevelButton.getText());
                startActivity(intent);
            }

        });
    }

    private void SavePreferences(){ // saving level of game
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("EASY", ((RadioButton)findViewById(R.id.radioEasy)).isChecked());
        editor.putBoolean("MEDIUM", ((RadioButton)findViewById(R.id.radioMedium)).isChecked());
        editor.putBoolean("HARD", ((RadioButton)findViewById(R.id.radioHard)).isChecked());
        editor.apply();
    }

}
