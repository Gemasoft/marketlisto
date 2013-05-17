package com.gemasoft.marketlisto;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "Items";
	private static final String TABLE_ITEMS = "Items";
	private static final int DATABASE_VERSION = 5;
	
	public MySQLHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Items("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, checked INTEGER, quantity INTEGER, price FLOAT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);
	}	
	
	public void insertItem(String title, Integer checked){	
		ContentValues values = new ContentValues();		
		values.put("title", title);
		values.put("checked", checked);	
		values.put("quantity", 0);	
		values.put("price", 0);	
		this.getWritableDatabase().insert(TABLE_ITEMS, null, values);
		this.close();
	}

	public ArrayList<ListItem> getItemList(){
		
		ArrayList<ListItem> itemsList = new ArrayList<ListItem>();	
		
		String columns[] = {_ID, "title", "checked", "quantity", "price"};
		
		Cursor c = this.getReadableDatabase().query(TABLE_ITEMS, columns, null, null, null, null,  null);
		
		int id, tt, ck, qt, pr;
		
		id = c.getColumnIndex(_ID);
		tt = c.getColumnIndex("title");
		ck = c.getColumnIndex("checked");
		qt = c.getColumnIndex("quantity");
		pr = c.getColumnIndex("price");
		
		int counter = 0;
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			itemsList.add(counter, new ListItem(c.getInt(id),c.getInt(ck) != 0,c.getString(tt),c.getInt(qt),c.getFloat(pr)));
			counter++;
		}
		
		this.close();
		return itemsList;		
	}

	public int getItemsTotal(){
		String columns[] = {_ID};
		Cursor c = this.getReadableDatabase().query(TABLE_ITEMS, columns, null, null, null, null,  null);	
		int count = c.getCount();
		this.close();
		return 	count;
	}
	
	public int getCheckedItems(){
				Cursor c = this.getReadableDatabase().rawQuery("select _ID from " + TABLE_ITEMS + " where checked = 1",null);
				int count = c.getCount();
				this.close();
				return 	count;
	}
	
	public float getTotalPrice(){
		Cursor c = this.getReadableDatabase().rawQuery("select SUM(quantity * price) from " + TABLE_ITEMS + " where checked = 1",null);
			if(c.moveToFirst()) {
			    return c.getFloat(0);
			}else{			
				return 0;
			}
}
	
	public void clearSelections() {
		this.getWritableDatabase().execSQL("UPDATE Items SET checked = 0, quantity = 0, price=0");
		this.close();
	}

	public void checkItem(int _ID, int checked, CharSequence quantity, CharSequence price){	
		this.getWritableDatabase().execSQL("UPDATE " + TABLE_ITEMS + " SET checked = " + Integer.toString(checked)  + ", quantity = " + quantity + ", price = " + price + " WHERE _ID = " + Integer.toString(_ID).trim());
		this.close();
	}

	public void deleteItem(long id) {
		this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE _ID = " + id);
		this.close();
	}
	
	public void deleteAll() {
		this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_ITEMS);
		this.close();
	}
	
	public void deleteChecked() {
		// DELETE CHECKED ITEMS
		this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE checked = 1");
		this.close();
	}
		

}

