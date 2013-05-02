package com.gemasoft.marketlisto;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class StartActivity extends Activity {
	 int selectedItemId = 0;
	 ListView mainListView = null;
	 ListItemAdapter customTODOAdapter = null;
	 MySQLHelper helper = new MySQLHelper(this);
	 ArrayList<ListItem> listTODO = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);

		findViewById(R.id.btnSend).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO ADD ITEM BUTTON CLICK
				   Intent intent = new Intent(StartActivity.this, AddItem.class);
			        StartActivity.this.startActivity(intent);
			}
		});
		
		findViewById(R.id.btnClear).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO CLEAR BUTTON CLICK
				helper.clearSelections();
				refreshView();
				
				
				
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.button_clear_alert), Toast.LENGTH_SHORT).show();	
			}
		});
		
		findViewById(R.id.btnDeleteChecked).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO DELETE BUTTON CLICK
				new AlertDialog.Builder(StartActivity.this)	 
	               .setMessage(getResources().getString(R.string.button_delete_checked_confirmation))
	               .setCancelable(false)
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	       				helper.deleteChecked();
	    				refreshView();
	    				Toast.makeText(getApplicationContext(), getResources().getString(R.string.button_delete_checked_alert), Toast.LENGTH_SHORT).show();	
	                   }
	               })
	               .setNegativeButton("No", null)
	               .show();
			}		
		});
		
		
		this.mainListView = (ListView) findViewById(R.id.mainlistView);
		
		mainListView.setAdapter(this.customTODOAdapter);
		mainListView.setItemsCanFocus(false);
 
		registerForContextMenu(mainListView);
		
		refreshView();

	}

	@Override	
	public void onResume(){
		super.onResume();		
        refreshView();	
	}
		
	  @Override
	    public boolean onContextItemSelected(MenuItem item) {
		    int menuItemIndex = item.getItemId();
			String[] menuItems = {"Delete","Cancel"};
			String menuItemName = menuItems[menuItemIndex];	
			if(menuItemName == "Delete"){
				helper.deleteItem(selectedItemId);
				refreshView();
				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.button_delete_checked_performed), Toast.LENGTH_SHORT).show();
			}
	    	return true;
	    }
	   
	    //changes the item state
	    public void changeSelection(View v)
	    {  	
	    	CheckBox cBox = (CheckBox) v;
			helper.checkItem((Integer)cBox.getTag(), cBox.isChecked() ? 1 : 0);
			if(cBox.isChecked())
				cBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG );	
			else
				cBox.setPaintFlags(Paint.LINEAR_TEXT_FLAG );
	     }
	    
	    //Refreshes the list
	    public void refreshView(){
	    	listTODO = helper.getItemList();
	    	// Bind the data with the list
	        this.customTODOAdapter = new ListItemAdapter(StartActivity.this,	R.layout.listitem_custom_row, listTODO);
	        mainListView.setAdapter(this.customTODOAdapter);	
	    	customTODOAdapter.notifyDataSetChanged();
			mainListView.refreshDrawableState();	
	    }
	    
	    @Override
	    public void onBackPressed() {
	        new AlertDialog.Builder(this)
	               .setMessage(getResources().getString(R.string.exit_confirmation))
	               .setCancelable(false)
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                        StartActivity.this.finish();
	                   }
	               })
	               .setNegativeButton("No", null)
	               .show();
	    } 

	//CONTEXT MENU BUILDER
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.mainlistView) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		menu.setHeaderTitle(listTODO.get(info.position).title);
    		String[] menuItems = {getResources().getString(R.string.action_delete),getResources().getString(R.string.action_cancel)};
    		for (int i = 0; i<menuItems.length; i++) {
    			menu.add(Menu.NONE, i, i, menuItems[i]);
			}
    		selectedItemId = listTODO.get(info.position)._ID;
    	}
    }
	
	//MENU BUILDER
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_add_item:
	        	findViewById(R.id.btnSend).performClick();
	            return true;
	        case R.id.menu_delete_all:
	        	findViewById(R.id.btnDeleteChecked).performClick();
	            return true;
	        case R.id.menu_unmark:
	        	findViewById(R.id.btnClear).performClick();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
