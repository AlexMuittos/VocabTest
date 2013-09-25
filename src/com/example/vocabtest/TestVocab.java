package com.example.vocabtest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabtest.VocabContent.node;

public class TestVocab extends Activity {
	//Used to update view with new meanings and words
	ListView meanings_view;
	String[] meanings_rownames;
	ArrayAdapter<String> meanings_adapter;
	
	TextView word, score;
	Button next_button;
	
	//Test progress and performance tracker variables
	int counter = 0, correct_counter = 0;
	
	//vocabcontent is the parser object that populates
	//dict with whole word set and populates entry with 
	//candidates for populating one test entry
	ArrayList<node> dict;	
	String[] entry;
	VocabContent vc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		vc = new VocabContent();
		Context context = getApplicationContext();	
		try {
			InputStream inp_words = context.getAssets().open("barron_words.txt");
			dict = vc.makeList(inp_words);
			entry = vc.getTestEntry(dict);
			Log.d("ENTRY : " + Integer.toString(counter), Arrays.toString(entry));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test_vocab);
		
		//Set the current word for current test entry
		word = (TextView) findViewById(R.id.word);
		word.setText(entry[0]);
		
		//Set the current meaning choices for current test entry
		meanings_rownames = Arrays.copyOfRange(entry, 1, 6);
        meanings_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,meanings_rownames);
		meanings_view = (ListView) findViewById(R.id.meanings);
		meanings_view.setAdapter(meanings_adapter);
		
		score = (TextView) findViewById(R.id.score);
		score.setText("Current Score : 0/" + counter);
		next_button = (Button) findViewById(R.id.next_button);
		next_button.setTextSize(30);
	}
	
	public void onClickNext(View view) {
		//If answer is correct increment correct counter
		int answer_index = Integer.parseInt(entry[6]);
		int answer_entered = meanings_view.getCheckedItemPosition() + 1;
		if (answer_index == answer_entered) correct_counter++;
		counter++;
		score.setText("Current Score : " + correct_counter + "/" + counter);
		
		//Display the word with all meanings
		Toast.makeText(this, entry[7], Toast.LENGTH_SHORT).show();

		//Clear out selection 
		meanings_view.clearChoices();
		
		//TODO : Either give option to restart test or show a results page
		if (counter == 10) {
			next_button.setText("Finish");
			next_button.setEnabled(false);
			meanings_view.clearChoices();	
		}
		else {
			
			entry = vc.getTestEntry(dict);
			Log.d("ENTRY : " + Integer.toString(counter), Arrays.toString(entry));
			
			//Update new meanings 
			//The actual bound object needs to change, not a copy
		    for(int j=0;j<5;j++) {
			  meanings_rownames[j] = entry[j+1];
		    }
			
		    //Reset with new word and notify adapter of data change to 
		    //trigger redraw of view
			word.setText(entry[0]);	
			meanings_adapter.notifyDataSetChanged();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_vocab, menu);
		return true;
	}

}
