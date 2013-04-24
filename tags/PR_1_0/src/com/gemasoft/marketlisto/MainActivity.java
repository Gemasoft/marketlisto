package com.gemasoft.marketlisto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	 int selectedItemId = 0;
	 ListView mainListView = null;
	 ListItemAdapter customTODOAdapter = null;
	 MySQLHelper helper = new MySQLHelper(this);
	 ArrayList<ListItem> listTODO = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				helper.clearSelections();
				refreshView();
				Toast.makeText(getApplicationContext(), "The checkboxes have been cleared!", Toast.LENGTH_SHORT).show();			
			}
		});
		
        Button btnAdd = (Button) findViewById(R.id.btnSend);
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		        Intent intent = new Intent(MainActivity.this, AddItem.class);
		        MainActivity.this.startActivity(intent);
			}
		});
			
		//int counter = helper.getItemsTotal();			
		//Toast.makeText(getApplicationContext(), Integer.toString(counter) + " Items found.", Toast.LENGTH_SHORT).show();
			
		listTODO = helper.getItemList();
		
		this.mainListView = (ListView) findViewById(R.id.mainlistView);
		mainListView.setCacheColorHint(1);

		// Bind the data with the list
		this.customTODOAdapter = new ListItemAdapter(MainActivity.this,	R.layout.listitem_custom_row, listTODO);
		mainListView.setAdapter(this.customTODOAdapter);
		mainListView.setItemsCanFocus(false);
		mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
		registerForContextMenu(mainListView);
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.mainlistView) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		menu.setHeaderTitle(listTODO.get(info.position).title);
    		String[] menuItems = {"Delete","Cancel"};
    		for (int i = 0; i<menuItems.length; i++) {
    			menu.add(Menu.NONE, i, i, menuItems[i]);
			}
    		selectedItemId = listTODO.get(info.position)._ID;
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    int menuItemIndex = item.getItemId();
		String[] menuItems = {"Delete","Cancel"};
		String menuItemName = menuItems[menuItemIndex];	
		if(menuItemName == "Delete"){
			helper.deleteItem(selectedItemId);
			refreshView();
			Toast.makeText(getApplicationContext(), "Item deleted!", Toast.LENGTH_SHORT).show();
		}
    	return true;
    }
   
    public void changeSelection(View v)
    {  	
    	CheckBox cBox = (CheckBox) v;
		Integer _ID = (Integer) cBox.getTag();
		int checked = cBox.isChecked() ? 1 : 0;
		helper.checkItem(_ID, checked);
     }
    
    public void refreshView(){
    	customTODOAdapter.notifyDataSetChanged();
		mainListView.refreshDrawableState();	
    }
    
}
