package com.example.morakeventscheck_in;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class CheckInTicket extends Activity {

	
	Button checkIn;
	Button undoCheckIn;
	Button goHome;
	TextView title;
	TextView descriptor;
	
	JSONObject json;
	
	final Context context = this;
	final Activity activity = this;
	
	private static final String TAG_NAME = "FullName";
	private static final String TAG_EVENT_NAME = "EventName";
	private static final String TAG_QUANTITY = "quantity";
	private static final String TAG_ORDER_NUMBER ="OrderNumber";
	private static final String TAG_TRANSACTION_ID = "Transaction";
	private static final String TAG_CHECKED_IN = "checkedIn";
	private static final String TAG_TYPE = "type";
	private static final String TAG_TIME = "timestamp";
	private static final String TAG_NOTES = "notes";
	private static final String TAG_USER = "user";
	
	String name;
	String eventName; 
	String quantity; 
	String orderNumber; 
	String transaction;
	int checkedIn;
	String buttonType;
	String time;
	String notes;
	String user;
	
	
	ProgressDialog pDialog;
	
	//URL String
	private static String url_update_tickets = "http://67.183.219.137:8081/update_tickets.php";
	private static String url_update_log = "http://67.183.219.137:8081/update_log.php";
	
	public String getDate(long time) {
	    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    cal.setTimeInMillis(time);
	    String date = DateFormat.format("dd-MM-yyyy", cal).toString();
	    return date;
	}
	
	
	
	@Override
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.single_ticket);
		
		Intent i = getIntent();
		name = i.getStringExtra("Name");
		eventName = i.getStringExtra("EventName");
		quantity = i.getStringExtra("Quantity");
		orderNumber = i.getStringExtra("OrderNumber");
		transaction = i.getStringExtra("Transaction");
		checkedIn = Integer.parseInt(i.getStringExtra("CheckedIn"));
		buttonType = "";
		time = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
		notes = "";
		user = ((BaseApp) activity.getApplication()).getUser();
		
		checkIn = (Button)findViewById(R.id.checkIn);
		goHome = (Button) findViewById(R.id.Home);
		
		undoCheckIn = (Button)findViewById(R.id.undoCheckIn);
		title = (TextView) findViewById(R.id.Title);
		descriptor = (TextView) findViewById(R.id.ticketDescriptor);
		title.setText(name + " - " + transaction + "\nOrder # " + orderNumber);
		
		descriptor.setText("Ticket Type: " + eventName + "\nQuantity: " + quantity + "\nChecked in: " + checkedIn + " of " + quantity);
		
		
		
		checkButtons();
		
		
		
		
		goHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				
			}
		});
	
		
		
		//Handling for checkIn
		checkIn.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v){
				
				
				
			buttonType = "check in";
			checkedIn++;
			
			
			//update tickets DB
			checkInTicket t = new checkInTicket(CheckInTicket.this, checkedIn);
			t.execute("");
			
			time = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
			notes = "";
			
			//update history log DB
			updateLog l = new updateLog(CheckInTicket.this);
			l.execute("");
			
			checkButtons();
			
			}
			
			
		});
		
		
		
		undoCheckIn.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v){
				buttonType = "undo check in";
				
				
				
				
				//Pop up dialog for notes
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("Reason for undo:");
				final EditText input = new EditText(context);
				alert.setView(input);
				
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						notes = input.getText().toString();
						checkedIn--;
						checkButtons();
						checkInTicket t = new checkInTicket(CheckInTicket.this, checkedIn);
						t.execute("");
						time = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
						updateLog l = new updateLog(CheckInTicket.this);
						l.execute("");
						
					}
					
					
				});
				
				alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Canceled
						
					}
				});
				
				
				final AlertDialog d = alert.create();
				d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				
				//don't allow user to click "ok" to undo check in without providing a reason
				input.addTextChangedListener(new TextWatcher(){

					private void handleText(){
						Button okButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
						if(input.getText().length()==0){
							okButton.setEnabled(false);
						}
						else{
							okButton.setEnabled(true);
						}
					}
					
					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						handleText();
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// Nothing to do
						
					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// Nothing to do
						
					}
					
				});
				
				
				d.show();
				d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						
				
			}
		});
		
		
	}//end of onCreate()
	
	/**
	$checkedIn = $_POST['checkedIn'];
	$orderNumber = $_POST['orderNumber'];
	$fullName = $_POST['fullName'];
	$EventName = $_POST['EventName'];
	$quantity = $_POST['quantity'];
	$transaction = $_POST['transaction'];
	$type = $_POST['type'];
	$timestamp = $_POST['timestamp'];
	$notes = $_POST['notes'];
	$user = $_POST['user'];
	**/
	
	//updates DB with history of check in/undo check in
		class updateLog extends AsyncTask<String, String, String>{
			Context mContext = null;
			
			
			updateLog(Context context){
				mContext = context;
			}
			
			

			@Override
			protected String doInBackground(String... args) {
				try{
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair(TAG_TRANSACTION_ID, transaction));
					params.add(new BasicNameValuePair(TAG_EVENT_NAME, eventName));
					params.add(new BasicNameValuePair(TAG_QUANTITY, quantity));
					params.add(new BasicNameValuePair(TAG_CHECKED_IN, Integer.toString(checkedIn)));
					params.add(new BasicNameValuePair(TAG_NAME, name));
					params.add(new BasicNameValuePair(TAG_ORDER_NUMBER, orderNumber));
					params.add(new BasicNameValuePair(TAG_TYPE, buttonType));
					params.add(new BasicNameValuePair(TAG_TIME, time));
					params.add(new BasicNameValuePair(TAG_NOTES, notes));
					params.add(new BasicNameValuePair(TAG_USER, user));
					
					//System.out.println("Log Update: " +  params.toString());
					
					HttpParams httpParameters = new BasicHttpParams();
					
					//timeouts
					
					HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
					HttpConnectionParams.setSoTimeout(httpParameters, 10000);
					
					HttpClient httpclient = new DefaultHttpClient(httpParameters);
					HttpPost httppost = new HttpPost(url_update_log);
					httppost.setEntity(new UrlEncodedFormEntity(params));
					
					try{
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						String result = EntityUtils.toString(entity);
						
						json = new JSONObject(result);
						Log.d("Test Update Log: ", json.toString());
					}
					catch(Exception e){e.printStackTrace();}
				}
				catch(Exception e){}
				return null;
			}
		}
	
	
		
		//AsyncTask checkInTicket updates DB and displayed # of tickets checked in.
		class checkInTicket extends AsyncTask<String, String, String> {
			Context mContext = null;
			int quan;
			
			checkInTicket(Context context, int quantity){
				mContext = context;
				this.quan = quantity;
			}
			
			@Override
			protected void onPreExecute(){
				super.onPreExecute();
				pDialog = new ProgressDialog(CheckInTicket.this);
				pDialog.setMessage("Updating ticket...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... args) {
				try{
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair(TAG_TRANSACTION_ID, transaction));
					params.add(new BasicNameValuePair(TAG_EVENT_NAME, eventName));
					params.add(new BasicNameValuePair(TAG_QUANTITY, quantity));
					params.add(new BasicNameValuePair(TAG_CHECKED_IN, Integer.toString(checkedIn)));
					//System.out.println("Display Updated: " + params.toString());
					HttpParams httpParameters = new BasicHttpParams();
					
					//timeouts
					
					HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
					HttpConnectionParams.setSoTimeout(httpParameters, 10000);
					
					HttpClient httpclient = new DefaultHttpClient(httpParameters);
					HttpPost httppost = new HttpPost(url_update_tickets);
					httppost.setEntity(new UrlEncodedFormEntity(params));
					
					try{
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						String result = EntityUtils.toString(entity);
						
						json = new JSONObject(result);
						Log.d("Test Update tickets: ", json.toString());
					}
					catch(Exception e){
						e.printStackTrace();
						cancel(true);}
					
					
				}
				catch(Exception e){}
				return null;
			}
			
			protected void onCancelled(){
				pDialog.dismiss();
				title.setText("Unable to connect to database. Please check internet connection.");
				descriptor.setText("");
				checkIn.setEnabled(false);
				checkIn.setTextColor(Color.GRAY);
				undoCheckIn.setEnabled(false);
				undoCheckIn.setTextColor(Color.GRAY);
			}
			
			protected void onPostExecute(String result){
				pDialog.dismiss();
				
				try{
					
					title.setText(name + " - " + transaction + "\nOrder # " + orderNumber);
					descriptor.setText("Ticket Type: " + eventName + "\nQuantity: " + quantity + "\nChecked in: " + checkedIn + " of " + quantity);
					}
					catch(Exception e){e.printStackTrace();}
					
					
			}
		}
		
		public void checkButtons()
		{
			//Button clickable logic
					if(checkedIn> 0){
						//One or more tickets have been checked in
						System.out.println("(UNDO VALID) At least one ticket is checked in");
						undoCheckIn.setEnabled(true);
					}
					else{
						System.out.println("(UNDO INVALID) No tickets have been checked in");
						undoCheckIn.setEnabled(false);
					}
					
					if(Integer.parseInt(quantity) > checkedIn){
						//Not all tickets have been checked in
						System.out.println("(CHECK IN VALID) not all tickets checked in: " + checkedIn + " of " + quantity);
						checkIn.setEnabled(true);
					}
					else{
						//all tickets have been checked in
						System.out.println("(CHECK IN INVALID) all tickets checked in");
						checkIn.setEnabled(false);
					} 
		}
	}

