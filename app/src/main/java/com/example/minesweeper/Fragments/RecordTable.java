package com.example.minesweeper.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.minesweeper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordTable extends Fragment {
    private static final int MODE_PRIVATE = 0;
    public static final String TEXT = "text";
    View view;
    private String text;
    private int moves;
    private TextView easyName1;
    private TextView easyRecord1;
    private TextView easyName2;
    private TextView easyRecord2;
    private TextView easyName3;
    private TextView easyRecord3;
    private TextView mediumName1;
    private TextView mediumRecord1;
    private TextView mediumName2;
    private TextView mediumRecord2;
    private TextView mediumName3;
    private TextView mediumRecord3;
    private TextView hardName1;
    private TextView hardRecord1;
    private TextView hardName2;
    private TextView hardRecord2;
    private TextView hardName3;
    private TextView hardRecord3;

    public RecordTable(){
   }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        view = inflater.inflate(R.layout.fragment_record_table, container, false);

       loadData();

        return view;
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RECORDS_SHARED", MODE_PRIVATE);
        text = sharedPreferences.getString("recs","");

        if(text!=null) {
            String[] allRecs = text.split(",");
            List<String> allRecs1 = new ArrayList<>();

            Collections.addAll(allRecs1, allRecs);

            String[] recs1 = (allRecs1.get(0)).split(";");
            String[] recs2 = (allRecs1.get(1)).split(";");
            String[] recs3 = (allRecs1.get(2)).split(";");
            String[] recs4 = (allRecs1.get(3)).split(";");
            String[] recs5 = (allRecs1.get(4)).split(";");
            String[] recs6 = (allRecs1.get(5)).split(";");
            String[] recs7 = (allRecs1.get(6)).split(";");
            String[] recs8 = (allRecs1.get(7)).split(";");
            String[] recs9 = (allRecs1.get(8)).split(";");

            easyName1 = view.findViewById(R.id.easyName1);
            easyName1.setText(recs1[0]);
            easyRecord1 = view.findViewById(R.id.easyRecord1);
            easyRecord1.setText(recs1[1]);

            easyName2 = view.findViewById(R.id.easyName2);
            easyName2.setText(recs2[0]);
            easyRecord2 = view.findViewById(R.id.easyRecord2);
            easyRecord2.setText(recs2[1]);

            easyName3 = view.findViewById(R.id.easyName3);
            easyName3.setText(recs3[0]);
            easyRecord3 = view.findViewById(R.id.easyRecord3);
            easyRecord3.setText(recs3[1]);

            mediumName1 = view.findViewById(R.id.mediumName1);
            mediumName1.setText(recs4[0]);
            mediumRecord1 = view.findViewById(R.id.mediumRecord1);
            mediumRecord1.setText(recs4[1]);

            mediumName2 = view.findViewById(R.id.mediumName2);
            mediumName2.setText(recs5[0]);
            mediumRecord2 = view.findViewById(R.id.mediumRecord2);
            mediumRecord2.setText(recs5[1]);

            mediumName3 = view.findViewById(R.id.mediumName3);
            mediumName3.setText(recs6[0]);
            mediumRecord3 = view.findViewById(R.id.mediumRecord3);
            mediumRecord3.setText(recs6[1]);

            hardName1 = view.findViewById(R.id.hardName1);
            hardName1.setText(recs7[0]);
            hardRecord1 = view.findViewById(R.id.hardRecord1);
            hardRecord1.setText(recs7[1]);

            hardName2 = view.findViewById(R.id.hardName2);
            hardName2.setText(recs8[0]);
            hardRecord2 = view.findViewById(R.id.hardRecord2);
            hardRecord2.setText(recs8[1]);

            hardName3 = view.findViewById(R.id.hardName3);
            hardName3.setText(recs9[0]);
            hardRecord3 = view.findViewById(R.id.hardRecord3);
            hardRecord3.setText(recs9[1]);
        }
    }

}
