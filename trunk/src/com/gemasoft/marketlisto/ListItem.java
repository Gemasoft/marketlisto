package com.gemasoft.marketlisto;

public class ListItem {
	public int _ID;
	public boolean checked;
	public String title;
	public int quantity;
	public float price;
	
	public ListItem(int _ID,boolean checked, String title, int quantity, float price){
		super();
		this._ID = _ID;
		this.checked = checked;
		this.title = title;
		this.quantity = quantity;
		this.price=price;
	}
}
