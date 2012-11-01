package com.gemasoft.marketlisto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.*;
import android.content.res.XmlResourceParser;

public class ListSelector extends Activity {
 	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
 	ArrayList<String> listTODO;
    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;
    
    private ListView mainListView = null;

    final String SETTING_TODOLIST = "todolist";

	public final static String EXTRA_MESSAGE = "com.gemasoft.MarketListo.MESSAGE";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Toast.makeText(getApplicationContext(),
						" You clicked Save button", Toast.LENGTH_SHORT).show();

				SaveSelections();
			}
		});

		Button btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Toast.makeText(getApplicationContext(),
						" You clicked Clear button", Toast.LENGTH_SHORT).show();

				ClearSelections();
			}
		});
        
		//GET THE ITEMS FROM THE XML FILE
		listTODO = PrepareListFromXml();

		//GET A HANDLE TO THE VIEW LIST
		mainListView = (ListView) findViewById(R.id.mainlistView);			
		mainListView.setCacheColorHint(0);
		
		//BIND THE DATA WITH THE LIST  		
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listTODO.toArray(new String[0]));
		mainListView.setAdapter(adapter);			
		mainListView.setItemsCanFocus(false);
		mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		LoadSelections();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
	protected void onPause() {
		// always handle the onPause to make sure selections are saved if user clicks back button
		SaveSelections();

		super.onPause();
	}
    
    public void openList(View view){
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE,  message);
    	startActivity(intent);
    }
    
    //THE MAIN ARRAY LIST
 	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
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
    
    //THE XML MAIN ARRAY LIST
    public ArrayList<String> PrepareListFromXml() {

    	
    	Log.i("MainActivity", "PrepareListFromXml");
    	
    	XmlResourceParser todolistXml = getResources().getXml(R.xml.todolist);
    	
		ArrayList<String> todoItems = new ArrayList<String>();
		
		int eventType = -1;
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_TAG) {

				String strNode = todolistXml.getName();
				if (strNode.equals("item")) {
					todoItems.add(todolistXml.getAttributeValue(null, "title"));
				}
			}

			try {
				eventType = todolistXml.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return todoItems;
	}
    
    private void SaveSelections() {

		// save the selections in the shared preference in private mode for the user

		SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = settingsActivity.edit();

		String savedItems = getSavedItems();

		prefEditor.putString(SETTING_TODOLIST, savedItems);

		prefEditor.commit();
	}
    
    private String getSavedItems() {
		String savedItems = "";

		int count = this.mainListView.getAdapter().getCount();

		for (int i = 0; i < count; i++) {

			if (this.mainListView.isItemChecked(i)) {
				if (savedItems.length() > 0) {
					savedItems += "," + this.mainListView.getItemAtPosition(i);
				} else {
					savedItems += this.mainListView.getItemAtPosition(i);
				}
			}

		}
		return savedItems;
	}
    
    private void ClearSelections() {

		// user has clicked clear button so uncheck all the items

		int count = this.mainListView.getAdapter().getCount();

		for (int i = 0; i < count; i++) {
			this.mainListView.setItemChecked(i, false);
		}

		// also clear the saved selections
		SaveSelections();

	}
    
	private void LoadSelections() {
		// if the selections were previously saved load them

		SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE);

		if (settingsActivity.contains(SETTING_TODOLIST)) {
			String savedItems = settingsActivity.getString(SETTING_TODOLIST, "");

			this.listTODO.addAll(Arrays.asList(savedItems.split(",")));
			int count = this.mainListView.getAdapter().getCount();

			for (int i = 0; i < count; i++) {
				String currentItem = (String) this.mainListView.getAdapter()
						.getItem(i);
				if (this.listTODO.contains(currentItem)) {
					this.mainListView.setItemChecked(i, true);
				}

			}

		}
	}
}

