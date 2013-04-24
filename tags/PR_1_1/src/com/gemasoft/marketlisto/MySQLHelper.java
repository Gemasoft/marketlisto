package com.gemasoft.marketlisto;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper {
	
	public String sql;
	
	public MySQLHelper(Context context){
		super(context, "Items", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		sql = "CREATE TABLE Items("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, checked INTEGER)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w("SqlHelper", "Upgrading database from version " + oldVersion	+ " to " + newVersion + ", which will destroy all old data");
		sql = "DROP TABLE IF EXISTS Items";
		db.execSQL(sql);
		onCreate(db);
	}	
	
	public void insertItem(String title, Integer checked){			
		ContentValues values = new ContentValues();		
		values.put("title", title);
		values.put("checked", checked);	
		this.getWritableDatabase().insert("Items", null, values);
	}

	public void openDatabase() {
		// TODO Auto-generated method stub
		this.getWritableDatabase();
	}

	public void closeDatabase() {
		// TODO Auto-generated method stub
		this.close();
	}
	
	public ArrayList<ListItem> getItemList(){
		// TODO Auto-generated method stub
		ArrayList<ListItem> itemsList = new ArrayList<ListItem>();	
		String columns[] = {_ID, "title", "checked"};
		Cursor c = this.getReadableDatabase().query("Items", columns, null, null, null, null,  null);
		
		int id, tt, ck;
		
		id = c.getColumnIndex(_ID);
		tt = c.getColumnIndex("title");
		ck = c.getColumnIndex("checked");
		
		int counter = 0;
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			ListItem listItem = new ListItem();
			listItem._ID = c.getInt(id);
			
			if (c.getInt(ck) == 0){
				listItem.checked = false;
			}else{
				listItem.checked = true;
			}
			listItem.title = c.getString(tt);			
			itemsList.add(counter, listItem);
			counter++;
		}
		this.close();
		return itemsList;		
	}

	public int getItemsTotal(){
		String columns[] = {_ID, "title", "checked"};
		Cursor c = this.getReadableDatabase().query("Items", columns, null, null, null, null,  null);	
		int count = c.getCount();
		this.close();
		return 	count;
	}

	public void update(String string, ContentValues values, String string2,	String[] strings) {
		// TODO Auto-generated method stub
		this.getWritableDatabase().update("Items",values,"_ID=?",null);
		this.close();
	}

	public void clearSelections() {
		sql = "UPDATE Items SET checked = 0";
		this.getWritableDatabase().execSQL(sql);
		this.close();
	}
	
	public void checkItem(int _ID, int checked){	
		sql = "UPDATE Items SET checked = " + Integer.toString(checked)  + " WHERE _ID = " + Integer.toString(_ID);
		this.getWritableDatabase().execSQL(sql);
		this.close();
	}

	public void deleteItem(long id) {
		// TODO Auto-generated method stub
		sql = "DELETE FROM Items WHERE _ID = " + id;
		this.getWritableDatabase().execSQL(sql);
		this.close();
	}
	
	public void deleteAll() {
		// TODO Auto-generated method stub
		sql = "DELETE FROM Items ";
		this.getWritableDatabase().execSQL(sql);
		this.close();
	}
	
	
}

