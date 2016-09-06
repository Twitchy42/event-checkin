package com.example.morakeventscheck_in;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.content.Intent;

public class MainActivity extends Activity {

	
	EditText searchText;
	Button searchButton;
	Button clearButton;
	String user;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Intent i = getIntent();
		//user = i.getStringExtra("User");
		
		//EditText
		searchText = (EditText) findViewById(R.id.searchByCode);
		
		
		//Button
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
		
			
		
			@Override
			public void onClick(View v) {
				// Launching DisplayTicket Activity
				if(!searchText.getText().toString().equals("")){
				Intent i = new Intent(getApplicationContext(), DisplayTicketsList.class);
				
				i.putExtra("SearchCode", searchText.getText().toString());
				i.putExtra("User", user);
				startActivity(i);
				}
			}
		});
		
		clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchText.setText("");
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
