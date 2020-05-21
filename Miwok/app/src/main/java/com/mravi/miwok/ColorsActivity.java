package com.mravi.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Create a list of numbers
        ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("me", "ek"));
        words.add(new Word("you", "do"));
        words.add(new Word("three", "teen"));
        words.add(new Word("four", "char"));
        words.add(new Word("five", "paanch"));
        words.add(new Word("six", "chhah"));
        words.add(new Word("seven", "saat"));
        words.add(new Word("eight", "aath"));
        words.add(new Word("nine", "nau"));
        words.add(new Word("ten", "das"));

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
    }
}
