package com.gemasoft.marketlisto;

import java.util.ArrayList;

import android.R.string;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class StartActivity extends Activity {
	int selectedItemId = 0;
	ListView mainListView = null;
	ListItemAdapter customTODOAdapter = null;
	MySQLHelper helper = new MySQLHelper(this);
	ArrayList<ListItem> listTODO = null;
	Typeface ThinPencilHandwriting;
	private Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);
		
		setupActionBar();
		
		this.mainListView = (ListView) findViewById(R.id.mainlistView);

		mainListView.setAdapter(this.customTODOAdapter);
		mainListView.setItemsCanFocus(false);

		registerForContextMenu(mainListView);
		
		//Toast.makeText(this,"$" + String.valueOf(helper.getTotalPrice()), Toast.LENGTH_LONG).show();

		refreshView();
	}

	//BUTTONS ACTIONS
	public void addItem(View view){
		Intent intent = new Intent(StartActivity.this, AddItem.class);
		StartActivity.this.startActivityForResult (intent, 0);		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		refreshView();
		CheckedCounter();
	}

	public void clearMarked(View view){
		helper.clearSelections();
		Toast.makeText(getApplicationContext(),	getResources().getString(R.string.button_clear_alert), Toast.LENGTH_SHORT).show();		
		refreshView();
		CheckedCounter();
	}

	public void deleteItem(View view){
		new AlertDialog.Builder(StartActivity.this)
		.setMessage(
				getResources()
				.getString(
						R.string.button_delete_checked_confirmation))
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) {
								helper.deleteChecked();
								Toast.makeText(getApplicationContext(),	getResources().getString(R.string.button_delete_checked_alert),	Toast.LENGTH_SHORT).show();
							}
						}).setNegativeButton("No", null).show();	
		refreshView();
		CheckedCounter();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshView();	
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int menuItemIndex = item.getItemId();
		String[] menuItems = { "Delete", "Cancel" };
		String menuItemName = menuItems[menuItemIndex];
		if (menuItemName == "Delete") {
			helper.deleteItem(selectedItemId);
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.button_delete_checked_performed),
							Toast.LENGTH_SHORT).show();
			refreshView();
			CheckedCounter();
		}
		return true;
	}

	
	//ON ITEM SELECTION CHANGED
	public void changeSelection(View v) {
		CheckBox cBox = (CheckBox) v;
		if (cBox.isChecked()) {
			cBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);	
			Intent intent = new Intent(StartActivity.this, CaptureActivity.class);
			intent.putExtra("_ID", (Integer)cBox.getTag());				
			StartActivity.this.startActivityForResult(intent,0);	
		} else {
			cBox.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
			//ThinPencilHandwriting = Typeface.createFromAsset(this.getAssets(),"fonts/ThinPencilHandwriting.ttf");
			cBox.setTypeface(ThinPencilHandwriting, Typeface.BOLD);	
			helper.checkItem((Integer)cBox.getTag(), 0, "0", "0");
			refreshView();
			CheckedCounter();
		}		
	
		return;
	}

	//REFRESHES THE LIST
	public void refreshView() {
		listTODO = helper.getItemList();
		// Bind the data with the list
		this.customTODOAdapter = new ListItemAdapter(StartActivity.this,R.layout.listitem_custom_row, listTODO);
		mainListView.setAdapter(this.customTODOAdapter);
		customTODOAdapter.notifyDataSetChanged();
		mainListView.refreshDrawableState();
	}

	//ON PRESS BACK BUTTON IN THE MAIN SCREEN
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setMessage(getResources().getString(R.string.exit_confirmation))
		.setCancelable(false)
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				StartActivity.this.finish();
			}
		}).setNegativeButton("No", null).show();
	}

	// CONTEXT MENU BUILDER
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.mainlistView) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(listTODO.get(info.position).title);
			String[] menuItems = {
					getResources().getString(R.string.action_delete),
					getResources().getString(R.string.action_cancel) };
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
			selectedItemId = listTODO.get(info.position)._ID;
		}
	}

	// MENU BUILDER
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		this.menu = menu;
		//Update the counter
		CheckedCounter();
		return true;
	}

	public void SendNotification(String msg){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_notification)
		.setContentTitle("MarketListo")
		.setContentText(msg);
		NotificationCompat.InboxStyle inboxStyle =
				new NotificationCompat.InboxStyle();
		String[] events = new String[6];
		// Sets a title for the Inbox style big view
		inboxStyle.setBigContentTitle("Event tracker details:");

		// Moves events into the big view
		for (int i=0; i < events.length; i++) {

			inboxStyle.addLine(events[i]);
		}
		// Moves the big view style object into the notification object.
		mBuilder.setStyle(inboxStyle);

		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());

	}

	//ITEMS COUNTER
	public void CheckedCounter(){
		int counter = 0;

		ListView mainListView = (ListView) findViewById(R.id.mainlistView);

		int total = mainListView.getCount();

		for (int i = 0; i < total; i++) 
		{
			if (mainListView.getChildAt(i)!= null){
				ViewGroup row = (ViewGroup) mainListView.getChildAt(i);
				CheckBox cbx = (CheckBox) row.findViewById(R.id.cbxChecked);
				//  Get your controls from this ViewGroup and perform your task on them =)	  
				if (cbx.isChecked()){
					counter++;
				}
			}
		}

		MenuItem counterMenu = (MenuItem) menu.findItem(R.id.menu_counter);		
		//counterMenu.setTitle(String.valueOf(counter) + "/" + String.valueOf(total));
		counterMenu.setTitle(String.valueOf("Items: "  + helper.getCheckedItems()) + "/" + String.valueOf(helper.getItemsTotal()) + " - " + "Total: $" + String.valueOf(helper.getTotalPrice()));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

}
