package com.example.vocabtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class StartTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_test);
		
		Button start_test = (Button) findViewById(R.id.start_test);
		start_test.setTextSize(20);
		
		Button view_results = (Button) findViewById(R.id.view_results);
		view_results.setTextSize(20);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_test, menu);
		return true;
	}
	
	public void onClickStart(View view) {
		Intent intent = new Intent(this, TestVocab.class);
		startActivity(intent);
	}
	
	public void onClickResults(View view) {
		Intent intent = new Intent(this, ResultsView.class);
		startActivity(intent);
	}

}
