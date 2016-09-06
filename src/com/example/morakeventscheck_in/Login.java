package com.example.morakeventscheck_in;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

public class Login extends Activity{
	
	private EditText userName;
	private EditText password;
	private Button login;
	final Activity activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);
		
		userName = (EditText)findViewById(R.id.login);
		password = (EditText)findViewById(R.id.password);
		login = (Button)findViewById(R.id.button1);
		
		login.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				//dismiss the keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
				
				//if password matches and username is not left blank
				if(password.getText().toString().equals("guest") && !userName.getText().toString().equals("")){
					
					Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + userName.getText().toString() , Toast.LENGTH_SHORT);
					
					toast.show();
					//intent to MainActivity
					((BaseApp) activity.getApplication()).setUser(userName.getText().toString());
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT);
					
					toast.show();
				}
			}
			
		});
	}
	

}
