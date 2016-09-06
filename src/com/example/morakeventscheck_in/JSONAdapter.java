package com.example.morakeventscheck_in;

import android.widget.*;
import android.app.Activity;
import org.json.*;
import android.view.View;
import android.view.ViewGroup;

public class JSONAdapter extends BaseAdapter implements ListAdapter {

	private final Activity activity;
	private final JSONArray jsonArray;
	
	public JSONAdapter(Activity activity, JSONArray jsonArray){
		this.jsonArray = jsonArray;
		this.activity = activity;
	}
	
	@Override
	public int getCount(){
		return jsonArray.length();
	}
	
	@Override
	public JSONObject getItem(int position){
		return jsonArray.optJSONObject(position);
	}
	
	@Override
	public long getItemId(int position){
		JSONObject jsonObject = getItem(position);
		return jsonObject.optLong("id");
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null)
		{
			convertView = activity.getLayoutInflater().inflate(R.layout.row, null);
		}
		//JSONObject jsonObject = getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.RowItems);
		try{
			name.setText("Ticket Type: " + this.getItem(position).getString("EventName") + "\nQuantity: " + this.getItem(position).getString("Quantity") 
					+ "\nChecked in: " + this.getItem(position).getString("CheckedIn") + " of " + this.getItem(position).getString("Quantity"));
		}
		catch(Exception e){}
		return convertView;
	}
}
