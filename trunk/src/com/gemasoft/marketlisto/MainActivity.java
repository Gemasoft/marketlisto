package com.gemasoft.marketlisto;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
//import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	 	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
	 	ArrayList<String> listTODO = PrepareList();
	    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
	    ArrayAdapter<String> adapter;
	public final static String EXTRA_MESSAGE = "com.gemasoft.MarketListo.MESSAGE";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     		// Get a handle to the list view
     		ListView lv = (ListView) findViewById(R.id.mainlistView);

     		// Bind the data with the list     		
     		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listTODO);
     		lv.setAdapter(adapter);			
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void sendMessage(View view){
    	//Intent intent = new Intent(this, DisplayMessageActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.edit_message);
    	//String message = editText.getText().toString();
    	//intent.putExtra(EXTRA_MESSAGE,  message);
    	//startActivity(intent);
    }
    
 // The main ArrayList .
 	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList<String> PrepareList() {
 		ArrayList todoItems = new ArrayList();
 		todoItems.add("Market list");
 		todoItems.add("Homework");
 		todoItems.add("Pending work tasks");
 		todoItems.add("Taxes");
 		return todoItems;

 	}
 	
 	//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItem(View v) {
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	listTODO.add(message);
        adapter.notifyDataSetChanged();
        editText.setText("");
    }
}
