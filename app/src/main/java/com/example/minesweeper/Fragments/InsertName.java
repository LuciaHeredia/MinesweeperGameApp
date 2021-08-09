package com.example.minesweeper.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.minesweeper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsertName extends Fragment {
    private static final int MODE_PRIVATE = 0;
    View view;
    private EditText editText;
    private Button saveButton;
    public static final String EMPTY = "-";
    public static final String TEXT= "";
    public String level;
    public String score = "-";
    private String text;
    public String tmpRec = EMPTY+";"+score;
    public List<String> recs = new ArrayList<>();

    public InsertName(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        view = inflater.inflate(R.layout.fragment_insert_name, container, false);

        editText = view.findViewById(R.id.editText);
        saveButton = view.findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> allRecs1 = loadData();
                String[] recs1 = (allRecs1.get(0)).split(";");
                String[] recs2 = (allRecs1.get(1)).split(";");
                String[] recs3 = (allRecs1.get(2)).split(";");
                String[] recs4 = (allRecs1.get(3)).split(";");
                String[] recs5 = (allRecs1.get(4)).split(";");
                String[] recs6 = (allRecs1.get(5)).split(";");
                String[] recs7 = (allRecs1.get(6)).split(";");
                String[] recs8 = (allRecs1.get(7)).split(";");
                String[] recs9 = (allRecs1.get(8)).split(";");


                level = loadLevel(); // level of record
                String newRec = editText.getText().toString()+";"+score;

                switch (level) {
                    case "EASY":
                        if(recs1[1].equals("-") || Integer.parseInt(score)<=Integer.parseInt(recs1[1])) {
                            recs.add(newRec);
                            recs.add(allRecs1.get(0));
                            recs.add(allRecs1.get(1));
                        }
                        else if(recs2[1].equals("-") || Integer.parseInt(score)<=Integer.parseInt(recs2[1])){
                            recs.add(allRecs1.get(0));
                            recs.add(newRec);
                            recs.add(allRecs1.get(1));
                        }
                        else {
                            recs.add(allRecs1.get(0));
                            recs.add(allRecs1.get(1));
                            recs.add(newRec);
                        }

                        for (int i = 3; i < 9; i++)
                            recs.add(allRecs1.get(i));
                        break;

                    case "MEDIUM":
                        for (int i = 0; i < 3; i++)
                            recs.add(allRecs1.get(i));

                        if(recs4[1].equals("-") || Integer.parseInt(score)<=Integer.parseInt(recs4[1])) {
                            recs.add(newRec);
                            recs.add(allRecs1.get(3));
                            recs.add(allRecs1.get(4));
                        }
                        else if(recs5[1].equals("-") || Integer.parseInt(score)<=Integer.parseInt(recs5[1])){
                            recs.add(allRecs1.get(3));
                            recs.add(newRec);
                            recs.add(allRecs1.get(4));
                        }
                        else {
                            recs.add(allRecs1.get(3));
                            recs.add(allRecs1.get(4));
                            recs.add(newRec);
                        }

                        for (int i = 6; i < 9; i++)
                            recs.add(allRecs1.get(i));
                        break;

                    case "HARD":
                        for (int i = 0; i < 6; i++)
                            recs.add(allRecs1.get(i));

                        if(recs7[1].equals("-") || Integer.parseInt(score)<=Integer.parseInt(recs7[1])) {
                            recs.add(newRec);
                            recs.add(allRecs1.get(6));
                            recs.add(allRecs1.get(7));
                        }
                        else if(recs8[1].equals("-") || Integer.parseInt(score)<=Integer.parseInt(recs8[1])){
                            recs.add(allRecs1.get(6));
                            recs.add(newRec);
                            recs.add(allRecs1.get(7));
                        }
                        else {
                            recs.add(allRecs1.get(6));
                            recs.add(allRecs1.get(7));
                            recs.add(newRec);
                        }
                        break;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for(String s : recs){
                    stringBuilder.append(s);
                    stringBuilder.append(",");
                }
                saveData(stringBuilder.toString());

                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public void saveData(String rec){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RECORDS_SHARED", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recs",rec);
        editor.apply();
    }

    public List<String> loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RECORDS_SHARED", MODE_PRIVATE);
        text = sharedPreferences.getString("recs", "");

        String[] allRecs = text.split(",");
        List<String> allRecs1 = new ArrayList<>();

        Collections.addAll(allRecs1, allRecs);

        return allRecs1;
    }

    public String loadLevel(){
        String levelSaved;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        boolean answer1 = sharedPreferences.getBoolean("EASY", false);
        boolean answer2 = sharedPreferences.getBoolean("MEDIUM", false);
        boolean answer3 = sharedPreferences.getBoolean("HARD", false);
        if (answer2)
            levelSaved = "MEDIUM";
        else if (answer3)
            levelSaved = "HARD";
        else{
            levelSaved = "EASY";
        }
        return levelSaved;
    }

    public void putArguments(Bundle args){
        score = (String) args.get("value");
    }

}
