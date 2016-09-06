package com.example.morakeventscheck_in;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.HashMap;
import org.json.JSONArray;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DisplayTicketsList extends ListActivity {

	private ProgressDialog pDialog;
	
	//JSON Node names
	private static final String TAG_NAME = "FullName";
	private static final String TAG_EVENT_NAME = "EventName";
	private static final String TAG_QUANTITY = "Quantity";
	private static final String TAG_ORDER_NUMBER ="OrderNumber";
	private static final String TAG_TRANSACTION_ID = "Transaction";
	private static final String TAG_CHECKED_IN = "CheckedIn";
	
	TextView blah = null;
	
	//URL String
	private static String url_display_tickets = "http://67.183.219.137:8081/get_tickets.php";
	
	//ticketInfo JSONArray
	JSONArray ticketInfo = null;
	
	//Hashmap for ListView?
	ArrayList<HashMap<String, String>> ticketList;
	String ticketSearch;
	String user;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_list_view);
		
		//Hashmap for listview
		ticketList = new ArrayList<HashMap<String, String>>();
		
		Intent i = getIntent();
		ticketSearch = i.getStringExtra("SearchCode");
		//user = i.getStringExtra("User");
		System.out.println(ticketSearch);
		//doInBackground call
		loadTickets t = new loadTickets(DisplayTicketsList.this, ticketSearch);
		t.execute("");
		
		blah = (TextView) findViewById(R.id.display);
		ListView lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				try{
					JSONObject json = ticketInfo.getJSONObject(position);
					String name = json.getString(TAG_NAME);
					String event = json.getString(TAG_EVENT_NAME);
					String quantity = json.getString(TAG_QUANTITY);
					String order = json.getString(TAG_ORDER_NUMBER);
					String transaction = json.getString(TAG_TRANSACTION_ID);
					String checkedIn = json.getString(TAG_CHECKED_IN);
					
					
					Intent i = new Intent(getApplicationContext(), CheckInTicket.class);
					i.putExtra("Name", name);
					i.putExtra("EventName", event);
					i.putExtra("Quantity", quantity);
					i.putExtra("OrderNumber", order);
					i.putExtra("Transaction", transaction);
					i.putExtra("CheckedIn", checkedIn);
					i.putExtra("User", user);
					startActivity(i);
				}
				catch(Exception e){e.printStackTrace();}
			}
			
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		pDialog.dismiss();
		loadTickets t = new loadTickets(DisplayTicketsList.this, ticketSearch);
		t.execute("");
		
		
	}
	
	class loadTickets extends AsyncTask<String, String, String> {
		Context mContext = null;
		String codeToSearch = "";
		
		loadTickets(Context context, String codeToSearch){
			mContext = context;
			this.codeToSearch = codeToSearch;
		}
		
		//Show progress dialog
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(DisplayTicketsList.this);
			pDialog.setMessage("Loading tickets. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(5);
			//pDialog.setCancelable(true);
			pDialog.show();
		}
		
		protected String doInBackground(String... args){
			try{
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("Transaction", codeToSearch));
				
				//Create the HTTP Request
				HttpParams httpParameters = new BasicHttpParams();
				
				//Setup Timeouts
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				HttpConnectionParams.setSoTimeout(httpParameters, 10000);
				
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(url_display_tickets);
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				//try the connection
				try{
				HttpResponse response = httpclient.execute(httppost);
				
				HttpEntity entity = response.getEntity();
	
				String result = EntityUtils.toString(entity);
								
				
				JSONObject json = new JSONObject(result);
				
				
				Log.d("TEST JSON TICKETS: ", json.toString());
				
				ticketInfo = json.getJSONArray("tickets");
				System.out.println(ticketInfo.length());
				
				
				}
				catch(Exception e)
				{
					e.printStackTrace();
					cancel(true);
				}
				
			
				
				
				
			}
			catch(Exception e)
			{
				
			}
			
			
			return "";
		}
		
		protected void onCancelled(){
			pDialog.dismiss();
			blah.setText("Unable to connect to database. Please check internet connection");
			setListAdapter(null);
		}
		
		protected void onPostExecute(String result){
			pDialog.dismiss();
			try{
				if(ticketInfo.length()==0){blah.setText("Invalid Transaction Number. No Results Found.");}
				else{
				JSONObject json = ticketInfo.getJSONObject(0);
			
			
			
			blah.setText(json.getString(TAG_NAME) + " - " + json.getString(TAG_TRANSACTION_ID) + "\nOrder # " + json.getString(TAG_ORDER_NUMBER));
			}
			}
			catch(Exception e){e.printStackTrace();}
			
			runOnUiThread(new Runnable(){
				public void run(){
					setListAdapter(new JSONAdapter(DisplayTicketsList.this, ticketInfo));
				}
			});
			
		}
		
		
		
	}
	
}
